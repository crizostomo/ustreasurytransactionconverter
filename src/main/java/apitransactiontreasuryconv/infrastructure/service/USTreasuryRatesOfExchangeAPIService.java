package apitransactiontreasuryconv.infrastructure.service;

import apitransactiontreasuryconv.entity.ExchangedTransaction;
import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.govtreasuryapi.ExchangeRatesClient;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class USTreasuryRatesOfExchangeAPIService implements ExchangeRateService {

    private final TransactionService transactionService;

    private final ExchangeRatesClient apiClient;

    public USTreasuryRatesOfExchangeAPIService(TransactionService transactionService, ExchangeRatesClient apiClient) {
        this.transactionService = transactionService;
        this.apiClient = apiClient;
    }

    @Override
    public ExchangedTransaction convertForCountryCurrency(Long transactionId, String country, String currency) {
        Transaction transaction = transactionService.findById(transactionId);
        String countryCurrency = WordUtils.capitalizeFully(country) + "-" + currency;
        BigDecimal conversionRate = apiClient.getConversationRateForCountryCurrency(transaction, countryCurrency);

        return new ExchangedTransaction(transaction, countryCurrency, conversionRate);
    }

    @Override
    public ExchangedTransaction convertForBaseCurrency(Long transactionId, String baseCurrency) {
        Transaction transaction = transactionService.findById(transactionId);
        BigDecimal conversionRate = apiClient.getConversationRateForBaseCurrency(transaction, baseCurrency);

        return new ExchangedTransaction(transaction, baseCurrency, conversionRate);
    }

}
