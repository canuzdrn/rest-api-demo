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
import org.springframework.test.web.servlet.put

//-------------------------- SINCE NO MOCKING OBJECT IS USED THERE MUST BE AT LEAST ONE DUMMY DATA TO TEST IN DATABASE --> SO I CREATED CUSTOMER,NETWORK,TRANSACTION TO TEST

@SpringBootTest
@AutoConfigureMockMvc
internal class CustomerControllerTest(
    @Autowired
    val mockMvc: MockMvc, // only used in tests --> allow you to make request to your rest api without issuing any HTTP request
    @Autowired
    val objectMapper: ObjectMapper // with the library called jackson --> ObjectMapper serializes the object in json format
) {

    val baseUrl = "/api/techoffice"

    @Nested
    @DisplayName("GET /api/techoffice")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetCustomers {

        @Test
        fun `should return all customers`() {
            //
            // need to create(POST) a dummy customer to test the response of request (database is empty initially)
            val requestedUser = CustomerRequest("newuser", "newuser@gmail.com", "Istanbul")
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestedUser)
            }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.username") { value(requestedUser.username) }
                        jsonPath("$.email") { value(requestedUser.email) }
                        jsonPath("$.city") { value(requestedUser.city) }
                    }
                }
            //
            mockMvc.get(baseUrl)
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].username") { value(requestedUser.username) }
                    jsonPath("$[0].email") { value(requestedUser.email) }
                    jsonPath("$[0].city") { value(requestedUser.city) }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/techoffice/{userName}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetCustomerByUsername {
        @Test
        fun `should return the customer with the given name`() {
            //
            val requestedUser = CustomerRequest("newuser", "newuser@gmail.com", "Istanbul")

            mockMvc.get("$baseUrl/${requestedUser.username}")
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.username") { value(requestedUser.username) }
                    jsonPath("$.email") { value(requestedUser.email) }
                    jsonPath("$.city") { value(requestedUser.city) }
                }

        }

        @Test
        fun `should return NOT FOUND if the customer does not exist`() {

            val requestedUser = "invalid_user"

            mockMvc.get("$baseUrl/$requestedUser")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("GET transactions of a customer by username")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUserTransactions {
        @Test
        fun `should return the transactions of the given user`() {
            //
            val requestedUser = CustomerRequest("newuser", "newuser@gmail.com", "Istanbul")
            // need to create network to order transactions on it
            val requestedNetwork = NetworkRequest("ETH")
            mockMvc.post("/api/chain/networks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestedNetwork)
            }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.name") { value(requestedNetwork.name) }
                    }
                }
            //
            //
            // need to create(POST) a dummy transaction to test the response of request (database is empty initially)
            val requestedTransaction = TransactionRequest(100, "ETH", requestedUser.username)
            mockMvc.post("/api/chain/transactions") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestedTransaction)
            }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.amount") { value(requestedTransaction.amount) }
                        jsonPath("$.network.name") { value(requestedTransaction.networkName) }
                        jsonPath("$.username") { value(requestedTransaction.username) }
                    }
                }
            //

            mockMvc.get("$baseUrl/${requestedUser.username}/transactions")
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].amount") { value(requestedTransaction.amount) }
                    jsonPath("$[0].username") { value(requestedTransaction.username) }
                    jsonPath("$[0].network.name") { value(requestedTransaction.networkName) }
                }
        }

        @Test
        fun `should return NOT FOUND if the customer does not exist`() {

            val requestedUser = "invalid_user"

            mockMvc.get("$baseUrl/$requestedUser")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST customer")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostCustomer {
        @Test
        fun `should add non existing user`() {
            val requestedUser = CustomerRequest("newuser", "newuser@gmail.com", "Istanbul")

            // if we try to get a GET request then
            // we should successfully get the added customer
            mockMvc.get("$baseUrl/${requestedUser.username}")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.username") { value("newuser") }
                    jsonPath("$.email") { value("newuser@gmail.com") }
                    jsonPath("$.city") { value("Istanbul") }
                }

        }

        @Test
        fun `should return BAD REQUEST if customer with the given name already exists`() {

            val existingCustomer = CustomerRequest("newuser", "newuser@gmail.com", "Istanbul")

            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(existingCustomer)
            }
                .andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("Put (update) existing customer")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PutCustomer {
        @Test
        fun `should update existing customer`() {
            val createdCustomer = CustomerRequest("example_user", "example@gmail.com", "Istanbul")
            // need to post a dummy customer to test PUT
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(createdCustomer)
            }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.username") { value(createdCustomer.username) }
                        jsonPath("$.email") { value(createdCustomer.email) }
                        jsonPath("$.city") { value(createdCustomer.city) }
                    }
                }
            ////////////////////////////////////////////////////////////////////
            // want to update existing customer (createdUser) --> so we need to create a PUT request which has the body of a customer who has the same username as createdUser
            val updatedCustomer = CustomerRequest("example_user", "newmail@gmail.com", "newcity")

            mockMvc.put(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedCustomer)
            }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.username") { value(updatedCustomer.username) }
                        jsonPath("$.email") { value(updatedCustomer.email) }
                        jsonPath("$.city") { value(updatedCustomer.city) }
                    }
                }
            // we must be able to get the updated customer at the end
            mockMvc.get("$baseUrl/${createdCustomer.username}")
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.username") { value(updatedCustomer.username) }
                        jsonPath("$.email") { value(updatedCustomer.email) }
                        jsonPath("$.city") { value(updatedCustomer.city) }
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if given customer does not exist`() {
            val requestedUser = "invalid_user"

            mockMvc.get("$baseUrl/$requestedUser")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE customer by username")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteCustomer {
        @Test
        fun `should delete existing customer`() {
            //
            // need to create(POST) a dummy customer to test the response of request (database is empty initially)
            val sampleUser = CustomerRequest("sampleuser", "sampleuser@gmail.com", "Istanbul")
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(sampleUser)
            }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.username") { value(sampleUser.username) }
                        jsonPath("$.email") { value(sampleUser.email) }
                        jsonPath("$.city") { value(sampleUser.city) }
                    }
                }
            //

            mockMvc.delete("$baseUrl/${sampleUser.username}")
                .andExpect {
                    status { isNoContent() }
                }
            // after removing the customer --> if we want to get the customer --> request will result in NOT FOUND
            mockMvc.get("$baseUrl/${sampleUser.username}")
                .andExpect {
                    status { isNotFound() }
                }

        }

        @Test
        fun `should return not found if no customer exists with the given username`() {
            val invalidName = "invalid_name"

            // when / then
            mockMvc.delete("$baseUrl/$invalidName")
                .andDo { println() }
                .andExpect { status { isNotFound() } }
        }
    }
}
