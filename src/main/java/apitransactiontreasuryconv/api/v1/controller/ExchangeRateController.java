package apitransactiontreasuryconv.api.v1.controller;

import apitransactiontreasuryconv.entity.ExchangedTransaction;
import apitransactiontreasuryconv.infrastructure.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Exchange Rate Controller", description = "Responsible for converting transactions")
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @Operation(summary = "It converts one transaction by Id by passing the currency, e.g. 'Real' ", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversion ok"),
            @ApiResponse(responseCode = "404", description = "Transaction or currency not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Parameters")
    })
    @GetMapping("/{transactionId}/{baseCurrency}")
    public ResponseEntity<ExchangedTransaction> conversionForBaseCurrency(
            @PathVariable("transactionId") Long transactionId,
            @PathVariable("baseCurrency") String baseCurrency) {

        ExchangedTransaction exchangedTransaction = service.convertForBaseCurrency(transactionId, baseCurrency);

        if (exchangedTransaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(exchangedTransaction);
    }

    @Operation(summary = "It converts one transaction by Id by passing the country and currency, e.g. 'Brazil/Real' ", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversion ok"),
            @ApiResponse(responseCode = "404", description = "Transaction or currency not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Parameters")
    })
    @GetMapping("/{transactionId}/{country}/{currency}")
    public ResponseEntity<ExchangedTransaction> conversionForCountryCurrency(
            @PathVariable("transactionId") Long transactionId,
            @PathVariable("country") String country,
            @PathVariable("currency") String currency) {

        ExchangedTransaction exchangedTransaction = service.convertForCountryCurrency(transactionId, country, currency);

        if (exchangedTransaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(exchangedTransaction);
    }
}
