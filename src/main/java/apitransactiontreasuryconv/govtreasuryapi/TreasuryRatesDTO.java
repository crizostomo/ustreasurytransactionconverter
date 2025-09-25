package apitransactiontreasuryconv.govtreasuryapi;

import java.util.List;
import java.util.Map;

/***
 * Java 21 Feature: `record` for immutable data classes
 *
 * ✅ Automatically generates:
 *   - Constructor
 *   - Getters (with field name)
 *   - `toString()`, `equals()`, `hashCode()`
 *
 * Java 11 equivalent: Full POJO with fields, constructor, getters, setters, toString, etc.
 */
public record TreasuryRatesDTO(
        List<ExchangeRateRecordDTO> data,
        Map<String, Object> meta,
        Map<String, Object> link
) {
}
