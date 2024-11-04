package apitransactiontreasuryconv.repository;

import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.infrastructure.repository.TransactionRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void testSaveNewTransactionWithGeneratedId() {
        Transaction newTransaction = new Transaction(
                null,
                "Saving a new transaction",
                LocalDate.now(),
                new BigDecimal("10.00")
        );

        Transaction persisted = repository.save(newTransaction);
        repository.flush();

        assertNotNull(persisted);
        assertNotNull(persisted.getId());
    }

    @Test
    void testSaveEnforcesDescriptionSize() {
        assertThrows(ConstraintViolationException.class, () -> {
            Transaction newTransaction = new Transaction(
                    null,
                    "A transaction with more than 50 chars of description, yes more 50 chars of description",
                    LocalDate.now(),
                    new BigDecimal("10.00")
            );

            repository.save(newTransaction);
            repository.flush();
        });
    }

    @Test
    void testSaveEnforcesAmountIsNotNegative() {
        assertThrows(ConstraintViolationException.class, () -> {
            Transaction newTransaction = new Transaction(
                    null,
                    "A transaction with a negative amount",
                    LocalDate.now(),
                    new BigDecimal("-10.00")
            );

            repository.save(newTransaction);
            repository.flush();
        });
    }
}