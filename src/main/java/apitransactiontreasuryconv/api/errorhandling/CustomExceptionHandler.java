package apitransactiontreasuryconv.api.errorhandling;

import apitransactiontreasuryconv.exception.ExchangedRateNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExchangedRateNotFoundException.class)
    public ResponseEntity<?> handleNoSuitableExchangeRateException(final ExchangedRateNotFoundException e, final HttpServletRequest request) {
        ErrorDTO dto = new ErrorDTO("No valid exchange rate available for transaction conversion.");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }
}
