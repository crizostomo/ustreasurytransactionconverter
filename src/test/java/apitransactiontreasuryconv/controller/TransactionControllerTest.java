package apitransactiontreasuryconv.controller;

import apitransactiontreasuryconv.api.v1.controller.TransactionController;
import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.exception.NotFoundException;
import apitransactiontreasuryconv.infrastructure.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService service;

    @Test
    void testListAllTransactions() throws Exception {
        List<Transaction> inDatabase = List.of(
                new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("10.00")),
                new Transaction(2L, "Transaction2", LocalDate.now(), new BigDecimal("10.00")),
                new Transaction(3L, "Transaction3", LocalDate.now(), new BigDecimal("10.00"))
        );

        given(service.findAll()).willReturn(inDatabase);

        String responseJson = mockMvc.perform(get("/api/v1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseNode = objectMapper.readTree(responseJson);
        assertEquals(inDatabase.size(), responseNode.size());
    }

    @Test
    void testGetByIdIsPresent() throws Exception {
        Long desiredId = 1L;
        Transaction foundTransaction = new Transaction(desiredId, "Transaction1", LocalDate.of(2024, 10, 02), new BigDecimal("10.00"));

        given(service.findById(any(Long.class))).willReturn(foundTransaction);

        String responseJson = mockMvc.perform(get("/api/v1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseNode = objectMapper.readTree(responseJson);
        assertEquals(desiredId.intValue(), responseNode.get("id").asInt());
        assertEquals(foundTransaction.getDescription(), responseNode.get("description").asText());
        assertEquals("2024-10-02", responseNode.get("date").asText());
        assertEquals(10.00, responseNode.get("amountInUSDollars").asDouble());
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        given(service.findById(any(Long.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostNewValidTransaction() throws Exception {
        Transaction newTransaction = new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("10.00"));

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post("/api/v1/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isCreated());
    }

    @Test
    void testPostInvalidTransactionWithMoreThan50Characters() throws Exception {
        Transaction newTransaction = new Transaction(
                1L,
                "TransactionThatHasMoreThan50CharactersSoItIsGoingToFail",
                LocalDate.now(),
                new BigDecimal("10.00")
        );

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post("/api/v1/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostInvalidTransactionWithNegativeAmount() throws Exception {
        Transaction newTransaction = new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("-10.00"));

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post("/api/v1/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isBadRequest());
    }
}