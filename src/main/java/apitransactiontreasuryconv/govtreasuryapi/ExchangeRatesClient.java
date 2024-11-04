package apitransactiontreasuryconv.govtreasuryapi;

import apitransactiontreasuryconv.entity.ExchangedTransaction;
import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.exception.ExchangedRateNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static apitransactiontreasuryconv.util.ApiEndpoints.API_BASE_URL;

@Component
public class ExchangeRatesClient {

    private final RestTemplate restTemplate;

    private final int lookupPeriodMonths;

    public ExchangeRatesClient(RestTemplate restTemplate,
                               @Value("${exchange.rate.lookup.period.months:6}") int lookupPeriodMonths) {
        this.restTemplate = restTemplate;
        this.lookupPeriodMonths = lookupPeriodMonths;
    }

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRatesClient.class);

    private String buildFiltersParam(LocalDate date, String currencyOption) {
        StringBuilder sb = new StringBuilder();
        LocalDate startDate = date.minus(Period.ofMonths(lookupPeriodMonths));
        sb.append("record_date:lte:"); sb.append(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        sb.append(",record_date:gte:"); sb.append(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        sb.append(",");
        sb.append(currencyOption);

        logger.info("Date filter set to 6-month period from {} to {}", startDate, date);

        return sb.toString();
    }

    private String filterOptionForBaseCurrency(String baseCurrency) {
        return "currency:eq:" + baseCurrency;
    }

    private String filterOptionForCountryCurrency(String countryCurrency) {
        return "country_currency_desc:eq:" + countryCurrency;
    }

    private String buildRequestPath(LocalDate date, String currencyOption) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(API_BASE_URL);
        uriComponentsBuilder.queryParam("fields", "exchange_rate,record_date");
        uriComponentsBuilder.queryParam("filter", buildFiltersParam(date, currencyOption));
        uriComponentsBuilder.queryParam("sort", "-record_date");

        String requestPath = uriComponentsBuilder.toUriString();
        logger.info("Building request path: {}", requestPath);
        return requestPath;
    }

    private BigDecimal getConversionRate(Transaction transaction, String currencyOption) {
        String requestPath = buildRequestPath(transaction.getDate(), currencyOption);

        TreasuryRatesDTO dto = restTemplate.getForObject(
                requestPath, TreasuryRatesDTO.class
        );

        if (dto.getData().isEmpty()) {
            logger.error("No valid exchange rate available for transaction: {} with currency option: {}", transaction, currencyOption);
            throw new ExchangedRateNotFoundException("No valid exchange rate available for transaction conversion.");
        }

        BigDecimal exchangeRate = dto.getData().get(0).getExchangeRate();

        ExchangedTransaction exchangeRateTransaction = new ExchangedTransaction(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAmountInUSDollars(),
                currencyOption,
                exchangeRate,
                transaction.getAmountInUSDollars().multiply(exchangeRate)
        );
        logger.info("Found exchange rate: {} for transaction: {}", exchangeRate, exchangeRateTransaction);
        return exchangeRate;
    }

    public BigDecimal getConversationRateForCountryCurrency(Transaction transaction, String countryCurrency) {
        logger.info("Getting conversion rate for country currency: {} for transaction: {}", countryCurrency, transaction);
        return getConversionRate(transaction, filterOptionForCountryCurrency(countryCurrency));
    }

    public BigDecimal getConversationRateForBaseCurrency(Transaction transaction, String baseCurrency) {
        logger.info("Getting conversion rate for base currency: {} for transaction: {}", baseCurrency, transaction);
        return getConversionRate(transaction, filterOptionForBaseCurrency(baseCurrency));
    }
}
