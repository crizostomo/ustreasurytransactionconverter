package apitransactiontreasuryconv.api.v1.controller;

import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.infrastructure.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Transaction Controller", description = "Responsible to list one or more transactions and create a transaction")
public class TransactionController {

    private final TransactionService service;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService service) {
        this.service = service;
        logger.info("TransactionController initialized");
    }

    @Operation(summary = "It lists all transactions", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping()
    public ResponseEntity<List<Transaction>> listTransactions() {
        logger.info("Fetching all transactions");
        List<Transaction> transactions = service.findAll();

        if (transactions.isEmpty()) {
            logger.warn("No transactions found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(transactions);
        }

        logger.info("Found {} transactions", transactions.size());
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "It lists one transaction by Id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> searchById(@PathVariable("id") Long id) {
        logger.info("Searching for transaction with ID: {}", id);
        Transaction transaction = service.findById(id);

        if (transaction == null) {
            logger.warn("Transaction with ID: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("Transaction found: {}", transaction);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "It creates a transaction", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Parameters")
    })
    @PostMapping("/transaction")
    public ResponseEntity<Transaction> handlePost(@Valid @RequestBody Transaction newTransaction) {
        logger.info("Creating new transaction: {}", newTransaction);

        newTransaction.setId(null);
        Transaction saved = service.save(newTransaction);

        logger.info("Transaction created successfully with ID: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        logger.error("Validation errors: {}", errorMessages);
        return ResponseEntity.badRequest().body(errorMessages);
    }
}
