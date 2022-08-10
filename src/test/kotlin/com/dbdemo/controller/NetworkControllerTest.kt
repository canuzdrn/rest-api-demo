package com.dbdemo.controller

import com.dbdemo.dto.CustomerRequest
import com.dbdemo.dto.NetworkRequest
import com.dbdemo.dto.TransactionRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class NetworkControllerTest(
    @Autowired
    val mockMvc: MockMvc, // only used in tests --> allow you to make request to your rest api without issuing any HTTP request
    @Autowired
    val objectMapper: ObjectMapper // with the library called jackson --> ObjectMapper serializes the object in json format
) {

    val baseUrl = "/api/chain"

    @Nested
    @DisplayName("POST network")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNetwork {
        @Test
        fun `should create network`() {
            //
            val network = NetworkRequest("ETH")
            mockMvc.post("$baseUrl/networks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(network)
            }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.name") { value(network.name) }
                    }
                }
        }
    }

    @Nested
    @DisplayName("POST transaction")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostTransaction {
        @Test
        fun `should create transaction`() {
            //
            val sampleUser = CustomerRequest("sample_user", "sample_user@gmail.com", "Istanbul")
            mockMvc.post("/api/techoffice") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(sampleUser)
            }
            //
            //
            val sampleTransaction = TransactionRequest(100, "ETH", "sample_user")
            mockMvc.post("$baseUrl/transactions") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(sampleTransaction)
            }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.amount") { value(sampleTransaction.amount) }
                        jsonPath("$.network.name") { value(sampleTransaction.networkName) }
                        jsonPath("$.username") { value(sampleTransaction.username) }
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET all transactions")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllTransactions {
        @Test
        fun `should return all transactions`() {
            //
            mockMvc.get("$baseUrl/transactions")
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$[0].amount") { value(100) } // since we manually initiate the db , the only transaction data it has is what we created at line  68
                        jsonPath("$[0].network.name") { value("ETH") }
                        jsonPath("$[0].username") { value("sample_user") }
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET all networks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllNetworks {
        @Test
        fun `should return all networks`() {
            //
            mockMvc.get("$baseUrl/networks")
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$[0].name") { value("ETH") }
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET the transactions in a network")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetNetworkTransactions {
        @Test
        fun `should return the transactions belongs to the given network`() {
            //
            val networkName = "ETH"

            mockMvc.get("$baseUrl/networks/$networkName/transactions")
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$[0].network.name") { value("ETH") }
                        jsonPath("$[0].customer.username") { value("sample_user") }
                        jsonPath("$[0].amount") { value(100) }
                        jsonPath("$[0].username") { value("sample_user") }
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET the transaction by its id")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetTransactionById {
        @Test
        fun `should return the requested transaction`() {
            //
            val transactionId = 3   // determined by jpa entities' auto generated id
            mockMvc.get("$baseUrl/transactions/$transactionId")
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.network.name") { value("ETH") }
                        jsonPath("$.customer.username") { value("sample_user") }
                        jsonPath("$.amount") { value(100) }
                        jsonPath("$.username") { value("sample_user") }
                    }
                }
        }
    }

    @Nested
    @DisplayName("DELETE the transaction by id")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteTransaction {
        @Test
        fun `should delete the transaciton`() {
            //
            val transactionId = 3
            mockMvc.delete("$baseUrl/transactions/$transactionId")
                .andExpect {
                    status { isNoContent() }
                }

            // we shouldn't be able to retrieve the transaction after deletion
            mockMvc.get("$baseUrl/transactions/$transactionId")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}