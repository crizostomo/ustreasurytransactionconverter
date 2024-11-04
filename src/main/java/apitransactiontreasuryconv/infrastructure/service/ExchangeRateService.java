package apitransactiontreasuryconv.infrastructure.service;

import apitransactiontreasuryconv.entity.ExchangedTransaction;

public interface ExchangeRateService {

    ExchangedTransaction convertForBaseCurrency(Long transactionId, String baseCurrency);

    ExchangedTransaction convertForCountryCurrency(Long transactionId, String country, String currency);
}
