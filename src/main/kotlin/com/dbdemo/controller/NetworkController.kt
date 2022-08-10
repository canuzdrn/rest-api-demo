package com.dbdemo.controller

import com.dbdemo.model.Network
import com.dbdemo.model.Transaction
import com.dbdemo.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chain/networks")
class NetworkController(private val service: TransactionService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @GetMapping
    fun getAllNetworks(): List<Network> {
        return service.getAllNetworks()
    }

    @GetMapping("/{networkName}/transactions")
    fun getNetworkTransactions(@PathVariable networkName: String): MutableSet<Transaction> {
        return service.getNetworkTransactions(networkName)
    }

    @PostMapping
    fun addNetwork(@RequestBody network: Network): Network {
        return service.postNetwork(network)
    }
}