package com.dbdemo.controller

import com.dbdemo.model.Transaction
import com.dbdemo.service.TransactionService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api/chain/transactions")
@Api(value = "Transaction controller documentation")
class TransactionController(private val service: TransactionService) {

    @PostMapping
    @ApiOperation(value = "Adds a new transaction to the network (network is explicit in request body)")
    fun postToNetwork(@Valid @RequestBody transaction: Transaction): Transaction {
        return service.postToNetwork(transaction)
    }

    @GetMapping
    @ApiOperation(value = "Retrieves all transactions")
    fun getAllTransactions(): List<Transaction> {
        return service.getAllTransactions()
    }

    @GetMapping("/{transactionId}")
    @ApiOperation(value = "Retrieves requested transaction by ID")
    fun getTransaction(@PathVariable transactionId: Long): Transaction {
        return service.getTransaction(transactionId)
    }

    @DeleteMapping("/{transactionId}")
    @ApiOperation(value = "Deletes requested transaction by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTransactions(@PathVariable transactionId: Long): Unit {
        service.deleteTransaction(transactionId)
    }
}