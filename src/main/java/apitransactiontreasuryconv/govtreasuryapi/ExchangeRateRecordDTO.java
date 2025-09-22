package apitransactiontreasuryconv.govtreasuryapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Java 21 record is used here instead of a classic class.
 *
 * Benefit:
 * - Immutable by default.
 * - Auto-generates constructor, getters, equals, hashCode, and toString.
 *
 * Java 11 equivalent:
 * - You'd need to manually define fields, constructor, getters, equals, hashCode, and toString.
 */
public record ExchangeRateRecordDTO(

        @JsonProperty("exchange_rate")
        BigDecimal exchangeRate,
        
        @JsonProperty("record_date")
        LocalDate recordDate
) {
}
