package com.dbdemo.controller

import com.dbdemo.model.Transaction
import com.dbdemo.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chain/transactions")
class TransactionController(private val service: TransactionService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @PostMapping
    fun postToNetwork(@RequestBody transaction: Transaction): Transaction {
        return service.postToNetwork(transaction)
    }

    @GetMapping
    fun getAllTransactions(): List<Transaction> {
        return service.getAllTransactions()
    }

    @GetMapping("/{transactionId}")
    fun getTransaction(@PathVariable transactionId: Long): Transaction {
        return service.getTransaction(transactionId)
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTransactions(@PathVariable transactionId: Long): Unit {
        service.deleteTransaction(transactionId)
    }
}