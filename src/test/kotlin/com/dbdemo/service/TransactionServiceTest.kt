package com.dbdemo.service

import com.dbdemo.model.Customer
import com.dbdemo.model.Network
import com.dbdemo.model.Transaction
import com.dbdemo.repository.CustomerRepository
import com.dbdemo.repository.NetworkRepository
import com.dbdemo.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional


@SpringBootTest
internal class TransactionServiceTest {

    private val customerRepository = mockk<CustomerRepository>(relaxed = true)
    private val transactionRepository = mockk<TransactionRepository>(relaxed = true)
    private val networkRepository = mockk<NetworkRepository>()
    private val transactionService = TransactionService(customerRepository, transactionRepository, networkRepository)

    @Test
    fun `when postToNetwork is called new transaction should be added to transactionRepository`() {
        // given
        val sampleNetwork = Network(1, "ETH")
        val sampleUser = Customer(1, "sample_user", "samplemail@gmail.com", "SampleCity")
        val sampleTransaction = Transaction(1, 100, sampleUser.username, sampleNetwork.name)
        every { networkRepository.findByName(sampleTransaction.networkName) } returns sampleNetwork
        every { customerRepository.findByUsername(sampleTransaction.username) } returns sampleUser
        every { transactionRepository.save(sampleTransaction) } returns sampleTransaction

        // when
        transactionService.postToNetwork(sampleTransaction)

        // then
        verify(exactly = 1) { transactionRepository.save(ofType()) }
    }

    @Test
    fun `when getAllTransactions is called it findAll should be called from transactionRepository `() {
        // given
        val sampleTransaction = Transaction(1, 100, "sample_user", "ETH")
        every { transactionRepository.findAll() } returns listOf(sampleTransaction)

        // when
        transactionService.getAllTransactions()

        // then
        verify(exactly = 1) { transactionRepository.findAll() }
    }

    @Test
    fun `when getAllNetworks is called it findAll should be called from networkRepository `() {
        // given
        val sampleNetwork = Network(1, "ETH")
        every { networkRepository.findAll() } returns listOf(sampleNetwork)

        // when
        transactionService.getAllNetworks()

        // then
        verify(exactly = 1) { networkRepository.findAll() }
    }

    @Test
    fun `when post network is called with a non existing network it should call save from networkRepository`() {
        // given
        val sampleNetwork = Network(1, "ETH")
        every { networkRepository.findByName(sampleNetwork.name) } returns null
        every { networkRepository.save(any()) } returns sampleNetwork

        // when
        transactionService.postNetwork(sampleNetwork)

        // then
        verify(exactly = 1) { networkRepository.save(ofType()) }
    }

    @Test
    fun `when getNetworkTransactions is called set of transactions should be returned`() {
        // given
        val sampleNetwork = Network(1, "ETH")
        every { networkRepository.findByName(sampleNetwork.name) } returns sampleNetwork

        // when
        val returned = transactionService.getNetworkTransactions(sampleNetwork.name)

        // then
        assertEquals(returned, sampleNetwork.getTransactions())

    }

    @Test
    fun `when getNetworkTransactions is called NoSuchElementException should be thrown if network name is invalid `() {
        // given
        val invalidNetwork = Network(1, "NONE")
        every { networkRepository.findByName("NONE") } returns null

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            transactionService.getNetworkTransactions(invalidNetwork.name)
        }
    }

    @Test
    fun `when deleteTransaction is called delete from transactionRepository should be called`() {
        // given
        val transaction = Transaction(1, 100, "sample_user", "ETH")
        every { transactionRepository.findById(transaction.id) } returns Optional.of(transaction)

        // when
        transactionService.deleteTransaction(transaction.id)

        // then
        verify(exactly = 1) { transactionRepository.deleteById(ofType()) }
    }

    @Test
    fun `when deleteTransaction is called with invalid transaction id NoSuchElementException should be thrown`() {
        // given
        val invalidTransaction = Transaction(1, 100, "sample_user", "ETH")
        every { transactionRepository.findById(invalidTransaction.id) } returns Optional.empty()

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            transactionService.deleteTransaction(transactionId = invalidTransaction.id)
        }
    }

    @Test
    fun `when getTransaction is called findById from transactionRepository should be called`() {
        // given
        val transaction = Transaction(1, 100, "sample_user", "ETH")
        every { transactionRepository.findById(transaction.id) } returns Optional.of(transaction)

        // when
        transactionService.getTransaction(transactionId = transaction.id)

        // then
        verify { transactionRepository.findById(ofType()) }
    }

    @Test
    fun `when getTransaction is called with an invalid id NoSuchElementException should be thrown`() {
        // given
        val invalidId: Long = -10
        every { transactionRepository.findById(invalidId) } returns Optional.empty()

        // when / then
        assertThrows(NoSuchElementException::class.java) {
            transactionService.getTransaction(transactionId = invalidId)
        }
    }


}













