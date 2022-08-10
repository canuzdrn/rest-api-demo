package com.dbdemo.service

import com.dbdemo.model.Customer
import com.dbdemo.repository.CustomerRepository
import com.dbdemo.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
internal class CustomerServiceTest {

    private val customerRepository = mockk<CustomerRepository>(relaxed = true)
    private val transactionRepository = mockk<TransactionRepository>()
    private val customerService = CustomerService(customerRepository, transactionRepository)

    @Test
    fun `when getAllCustomers is called it should correctly call findAll from it's repository `() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "sample_city")
        every { customerRepository.findAll() } returns listOf(sampleCustomer)

        // when
        customerService.getCustomers()

        // then
        verify(exactly = 1) { customerRepository.findAll() }
    }

    @Test
    fun `when getCustomer is called it should correctly return the requested customer`() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "sample_city")
        every { customerRepository.findByUsername("sample_customer") } returns sampleCustomer

        // when
        customerService.getCustomer(sampleCustomer.username)

        // then
        verify(exactly = 1) { customerRepository.findByUsername(sampleCustomer.username) }
        assertEquals(customerService.getCustomer(sampleCustomer.username), customerRepository.findByUsername(sampleCustomer.username))
    }

    @Test
    fun `when getCustomer is called with invalid username it should throw NoSuchElementException error`() {
        // given
        val invalidCustomer = Customer(1, "invalid_customer", "invalid_email@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(invalidCustomer.username) } returns null

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            customerService.getCustomer(invalidCustomer.username)
        }
    }

    @Test
    fun `when saveCustomer is called it should correctly call save from its repository`() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(sampleCustomer.username) } returns null
        every { customerRepository.save(any()) } returns sampleCustomer

        // when
        customerService.saveCustomer(sampleCustomer)

        // then
        verify(exactly = 1) { customerRepository.save(ofType()) }
    }

    @Test
    fun `when saveCustomer is called with an already existing username IllegalArgumentException should be thrown`() {
        // given
        val existingCustomer = Customer(1, "existing_customer", "existingemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(existingCustomer.username) } returns existingCustomer

        // when / then
        assertThrows(IllegalArgumentException::class.java) {
            customerService.saveCustomer(existingCustomer)
        }
    }

    @Test
    fun `when updateCustomer is called with a valid customer with no active transaction delete and save should be called from customer repository`() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(sampleCustomer.username) } returns sampleCustomer
        every { transactionRepository.isUserExists(sampleCustomer.username) } returns false
        every { customerRepository.save(any()) } returns sampleCustomer

        // when
        customerService.updateCustomer(sampleCustomer)

        // then
        verify(exactly = 1) { customerRepository.delete(ofType()) }
        verify(exactly = 1) { customerRepository.save(ofType()) }
    }

    @Test
    fun `when updateCustomer is called with an invalid customer NoSuchElementException should be thrown`() {
        // given
        val invalidCustomer = Customer(1, "invalid_customer", "samplemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(invalidCustomer.username) } returns null
        every { transactionRepository.isUserExists(invalidCustomer.username) } returns false

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            customerService.updateCustomer(invalidCustomer)
        }
    }

    @Test
    fun `when updateCustomer is called with a valid customer that has an active transaction IllegalArgumentException should be thrown`() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(sampleCustomer.username) } returns sampleCustomer
        every { transactionRepository.isUserExists(sampleCustomer.username) } returns true

        // when / then
        assertThrows(IllegalArgumentException::class.java) {
            customerService.updateCustomer(sampleCustomer)
        }
    }

    @Test
    fun `when deleteCustomer is called it should call delete method from its repository if no active transaction exists`() {
        // given
        val sampleCustomer = Customer(1, "sample_customer", "samplemail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(sampleCustomer.username) } returns sampleCustomer
        every { transactionRepository.isUserExists(sampleCustomer.username) } returns false
        every { customerRepository.delete(sampleCustomer) } returns Unit

        // when
        customerService.deleteCustomer(sampleCustomer.username)

        // then
        verify(exactly = 1) { customerRepository.delete(ofType()) }
    }

    @Test
    fun `when deleteCustomer is called it should throw NoSuchElementException if no customer exists with given username`() {
        // given
        val invalidCustomer = Customer(1, "invalid_customer", "invalidmail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(invalidCustomer.username) } returns null
        every { transactionRepository.isUserExists(invalidCustomer.username) } returns false
        every { customerRepository.delete(invalidCustomer) } returns Unit

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            customerService.deleteCustomer(invalidCustomer.username)
        }
    }

    @Test
    fun `when deleteCustomer is called it should throw IllegalArgumentException if active transaction exists`() {
        // given
        val invalidCustomer = Customer(1, "invalid_customer", "invalidmail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(invalidCustomer.username) } returns invalidCustomer
        every { transactionRepository.isUserExists(invalidCustomer.username) } returns true
        every { customerRepository.delete(invalidCustomer) } returns Unit

        // when / then
        assertThrows(IllegalArgumentException::class.java) {
            customerService.deleteCustomer(invalidCustomer.username)
        }
    }

    @Test
    fun `when showTransactions is called with an invalid user NoSuchElementException should be thrown`() {
        // given
        val invalidCustomer = Customer(1, "invalid_customer", "invalidmail@gmail.com", "SampleCity")
        every { customerRepository.findByUsername(invalidCustomer.username) } returns null

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            customerService.showTransactions(invalidCustomer.username)
        }
    }

}




