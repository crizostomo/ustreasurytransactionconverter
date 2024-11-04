package apitransactiontreasuryconv.controller;

import apitransactiontreasuryconv.api.v1.controller.ExchangeRateController;
import apitransactiontreasuryconv.entity.ExchangedTransaction;
import apitransactiontreasuryconv.entity.Transaction;
import apitransactiontreasuryconv.infrastructure.service.ExchangeRateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ExchangeRateService service;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long transactionId;
    private Transaction transaction;
    private BigDecimal exchangeRate;
    private ExchangedTransaction exchangedTransaction;

    @BeforeEach
    void setUp() {
        transactionId = 1L;
        transaction = new Transaction(transactionId, "Transaction1", LocalDate.of(2024, 10, 02), new BigDecimal("10.00"));
        exchangeRate = new BigDecimal("6.0");
    }

    @Test
    void testConvertCountryCurrencyResponse() throws Exception {
        String targetCountryCurrency = "Brazil-Real";
        exchangedTransaction = new ExchangedTransaction(transaction, targetCountryCurrency, exchangeRate);

        given(service.convertForCountryCurrency(any(), any(), any())).willReturn(exchangedTransaction);

        String responseJson = mockMvc.perform(get("/api/v1/" + transactionId + "/Brazil/Real")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseNode = objectMapper.readTree(responseJson);

        assertEquals(transactionId.intValue(), responseNode.get("id").asInt());
        assertEquals(exchangedTransaction.getDescription(), responseNode.get("description").asText());
        assertEquals("2024-10-02", responseNode.get("date").asText());
        assertEquals(10.00, responseNode.get("amountInUSDollars").asDouble());
        assertEquals("Brazil-Real", responseNode.get("targetCurrency").asText());
        assertEquals(6.0, responseNode.get("exchangeRate").asDouble());
        assertEquals(60.00, responseNode.get("convertedAmount").asDouble());
    }

    @Test
    void testConvertCurrencyResponse() throws Exception {
        String targetCurrency = "Real";
        exchangedTransaction = new ExchangedTransaction(transaction, targetCurrency, exchangeRate);

        given(service.convertForBaseCurrency(any(), any())).willReturn(exchangedTransaction);

        String responseJson = mockMvc.perform(get("/api/v1/" + transactionId + "/" + targetCurrency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseNode = objectMapper.readTree(responseJson);

        assertEquals(transactionId.intValue(), responseNode.get("id").asInt());
        assertEquals(exchangedTransaction.getDescription(), responseNode.get("description").asText());
        assertEquals("2024-10-02", responseNode.get("date").asText());
        assertEquals(10.00, responseNode.get("amountInUSDollars").asDouble());
        assertEquals(targetCurrency, responseNode.get("targetCurrency").asText());
        assertEquals(6.0, responseNode.get("exchangeRate").asDouble());
        assertEquals(60.00, responseNode.get("convertedAmount").asDouble());
    }
}