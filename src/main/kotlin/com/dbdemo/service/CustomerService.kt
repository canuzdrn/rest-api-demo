package com.dbdemo.service

import com.dbdemo.model.Customer
import com.dbdemo.model.Transaction
import com.dbdemo.repository.CustomerRepository
import com.dbdemo.repository.TransactionRepository
import org.springframework.stereotype.Service


@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val transactionRepository: TransactionRepository
) {


    fun getCustomers(): Collection<Customer> {
        return customerRepository.findAll()
    }

    fun getCustomer(username: String): Customer {
        return customerRepository.findByUsername(username)
            ?: throw NoSuchElementException("No such customer named \"$username\" is registered.")
    }

    fun saveCustomer(customer: Customer): Customer {
        if (customerRepository.findByUsername(customer.username) != null) {
            throw IllegalArgumentException("Customer with the username \"${customer.username}\" is already exists")
        } else {
            return customerRepository.save(customer)
        }
    }

    fun updateCustomer(customer: Customer): Customer {

        val requestedCustomer = customerRepository.findByUsername(customer.username)

        val currentTransaction = transactionRepository.isUserExists(customer.username) // boolean

        if (requestedCustomer != null) {
            if (!currentTransaction) {
                customerRepository.delete(requestedCustomer)
                return customerRepository.save(customer)
            } else {
                throw IllegalArgumentException("There exists an available transaction of the user \"${customer.username}\" , hence no update is possible.")
            }
        } else {
            throw NoSuchElementException("No such customer named \"${customer.username}\" is registered.")
        }

    }

    fun deleteCustomer(username: String) {
        val requestedCustomer = customerRepository.findByUsername(username)
        val currentTransaction = transactionRepository.isUserExists(username)
        if (requestedCustomer != null) {
            if (!currentTransaction) {
                customerRepository.delete(requestedCustomer)
            } else {
                throw IllegalArgumentException("There exists an available transaction of the user \"$username\" , hence no deletion is possible.")
            }
        } else {
            throw NoSuchElementException("No such customer named \"$username\" is registered.")
        }
    }

    fun showTransactions(username: String): MutableSet<Transaction> {
        val requestedCustomer = customerRepository.findByUsername(username)
        if (requestedCustomer != null) {
            return requestedCustomer.getTransactions()
        } else {
            throw NoSuchElementException("No such customer named \"$username\" is registered.")
        }
    }
}



















