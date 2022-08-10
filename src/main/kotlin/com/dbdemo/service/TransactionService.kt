package com.dbdemo.service

import com.dbdemo.model.Network
import com.dbdemo.model.Transaction
import com.dbdemo.repository.CustomerRepository
import com.dbdemo.repository.NetworkRepository
import com.dbdemo.repository.TransactionRepository
import org.springframework.stereotype.Service


@Service
class TransactionService(
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository,
    private val networkRepository: NetworkRepository
) {

    fun postToNetwork(transaction: Transaction): Transaction {

        val network = networkRepository.findByName(transaction.networkName)
        val customer = customerRepository.findByUsername(transaction.username)

        if (network != null && customer != null) {
            transaction.network = network
            transaction.customer = customer

            network.getTransactions().add(transaction)

            customer.getTransactions().add(transaction)
            customer.getNetworks().add(transaction.network.name)

            return transactionRepository.save(transaction)
        } else if (network != null && customer == null) {
            throw NoSuchElementException("No such customer with this username \"${transaction.username}\" exists")
        } else if (network == null && customer != null) {
            throw NoSuchElementException("No such network with this network name \"${transaction.networkName}\" exists. You should create this network first !")
        } else {
            throw NoSuchElementException("Neither such customer with username \"${transaction.username}\" nor such network with network name \"${transaction.networkName}\" exist")
        }
    }

    fun getAllTransactions(): List<Transaction> {
        return transactionRepository.findAll()
    }

    fun getAllNetworks(): List<Network> {
        return networkRepository.findAll()
    }

    fun postNetwork(network: Network): Network {
        if (networkRepository.findByName(network.name) == null) {
            return networkRepository.save(network)
        } else {
            throw IllegalArgumentException("There already exists a network with the same name..!")
        }
    }

    fun getNetworkTransactions(networkName: String): MutableSet<Transaction> {
        val requestedNetwork = networkRepository.findByName(networkName)

        if (requestedNetwork != null) {
            return requestedNetwork.getTransactions()
        } else {
            throw NoSuchElementException("No such network with this network name \"$networkName\" exists. You should create this network first !")
        }
    }

    fun deleteTransaction(transactionId: Long): Unit {
        val requestedTransaction = transactionRepository.findById(transactionId)
        if (requestedTransaction.isPresent) transactionRepository.deleteById(transactionId) else throw NoSuchElementException("Transaction is not found !")
    }

    fun getTransaction(transactionId: Long): Transaction {
        val requestedTransaction = transactionRepository.findById(transactionId)
        if (requestedTransaction.isPresent) return transactionRepository.findById(transactionId).get() else throw NoSuchElementException("Transaction is not found !")
    }

}