package com.dbdemo.controller

import com.dbdemo.model.Customer
import com.dbdemo.model.Transaction
import com.dbdemo.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/techoffice")
class CustomerController(private val service: CustomerService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @GetMapping
    fun getAllCustomers(): Collection<Customer> {
        return service.getCustomers()
    }

    @GetMapping("/{userName}")
    fun getCustomer(@PathVariable userName: String): Customer? {
        return service.getCustomer(userName)
    }

    @GetMapping("/{userName}/transactions")
    fun getCustomerTransactions(@PathVariable userName: String): MutableSet<Transaction> {
        return service.showTransactions(userName)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomer(@RequestBody customer: Customer): Customer {
        return service.saveCustomer(customer)
    }

    @PutMapping
    fun putCustomer(@RequestBody customer: Customer): Customer {
        return service.updateCustomer(customer)
    }

    @DeleteMapping("/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable userName: String): Unit {
        return service.deleteCustomer(userName)
    }
}