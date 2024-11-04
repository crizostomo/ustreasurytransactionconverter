package apitransactiontreasuryconv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No valid exchange rate available for transaction conversion.")
public class ExchangedRateNotFoundException extends RuntimeException{

    public ExchangedRateNotFoundException() {
    }

    public ExchangedRateNotFoundException(String message) {
        super(message);
    }

    public ExchangedRateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangedRateNotFoundException(Throwable cause) {
        super(cause);
    }

    public ExchangedRateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
