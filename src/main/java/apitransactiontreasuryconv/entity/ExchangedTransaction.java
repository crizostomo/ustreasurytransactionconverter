package apitransactiontreasuryconv.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/***
 * Java 21 Feature: record class for immutable data containers.
 * - Eliminates boilerplate code (getters, constructors, equals, hashCode, toString).
 * - All fields are implicitly final and private.
 *
 * Java 11 equivalent: Create a full class with private fields, constructor, getters, setters, equals, hashCode, and toString manually.
 */
@Schema(name = "Entity for Exchanged Transaction")
public record ExchangedTransaction(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Smart Watch Generation II")
        String description,

        @Schema(example = "2024-10-02")
        LocalDate date,

        @Schema(example = "100.00")
        BigDecimal amountInUSDollars,

        @Schema(example = "Real")
        String targetCurrency,

        @Schema(example = "5.85")
        BigDecimal exchangeRate,

        @Schema(example = "585.00")
        BigDecimal convertedAmount

) {

    /***
     * Java 21 Feature: Compact constructor in record to apply logic like calculations.
     *
     * Java 11 equivalent: You'd create a full class and do this logic in the constructor manually.
     */
    public ExchangedTransaction(Transaction transaction, String targetCurrency, BigDecimal exchangeRate) {
        this(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAmountInUSDollars(),
                targetCurrency,
                exchangeRate,
                transaction.getAmountInUSDollars()
                        .multiply(new BigDecimal(String.valueOf(exchangeRate)))
                        .setScale(2, RoundingMode.HALF_EVEN)
        );
    }

    /***
     * Java 21 Record already implements:
     * - equals()
     * - hashCode()
     * - toString()
     *
     * Java 11 equivalent: Manually override each method using IDE-generated code or Lombok.
     */
}