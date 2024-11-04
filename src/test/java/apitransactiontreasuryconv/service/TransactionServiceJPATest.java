package apitransactiontreasuryconv.service;

import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.exception.NotFoundException;
import apitransactiontreasuryconv.infrastructure.repository.TransactionRepository;
import apitransactiontreasuryconv.infrastructure.service.TransactionServiceJPA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransactionServiceJPATest {

    @InjectMocks
    private TransactionServiceJPA service;

    @Mock
    private TransactionRepository repository;

    @Test
    void testSaveReturnsPersistedEntity() {
        Transaction newTransaction = new Transaction(null, "Transaction1", LocalDate.now(), new BigDecimal("10.00"));
        Transaction persistedTransaction = new Transaction(1L, newTransaction.getDescription(), newTransaction.getDate(), newTransaction.getAmountInUSDollars());

        given(repository.save(any(Transaction.class))).willReturn(persistedTransaction);

        Transaction returned = service.save(newTransaction);

        assertNotNull(returned);
        assertEquals(persistedTransaction, returned);
    }

    @Test
    void testFindAll() {
        List<Transaction> inDatabase = List.of(
                new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("10.00")),
                new Transaction(2L, "Transaction2", LocalDate.now(), new BigDecimal("10.00")),
                new Transaction(3L, "Transaction3", LocalDate.now(), new BigDecimal("10.00"))
        );

        given(repository.findAll()).willReturn(inDatabase);

        List<Transaction> all = service.findAll();

        assertEquals(inDatabase.size(), all.size());
    }

    @Test
    void testFindByIdReturnsEntityFound() {
        Long desiredId = 1L;
        Transaction persistedTransaction = new Transaction(desiredId, "Transaction1", LocalDate.now(), new BigDecimal("10.00"));

        given(repository.findById(desiredId)).willReturn(Optional.of(persistedTransaction));

        Transaction found = service.findById(desiredId);

        assertEquals(persistedTransaction, found);
    }

    @Test
    void testFindByIdThrowsCustomExceptionWhenNotFound() {
        Long desiredId = 1L;

        given(repository.findById(desiredId)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(desiredId));
    }
}