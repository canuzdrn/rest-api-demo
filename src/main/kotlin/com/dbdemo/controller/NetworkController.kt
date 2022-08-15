package com.dbdemo.controller

import com.dbdemo.model.Network
import com.dbdemo.model.Transaction
import com.dbdemo.service.TransactionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api/chain/networks")
class NetworkController(private val service: TransactionService) {

    @GetMapping
    fun getAllNetworks(): List<Network> {
        return service.getAllNetworks()
    }

    @GetMapping("/{networkName}/transactions")
    fun getNetworkTransactions(@PathVariable networkName: String): MutableSet<Transaction> {
        return service.getNetworkTransactions(networkName)
    }

    @PostMapping
    fun addNetwork(@Valid @RequestBody network: Network): Network {
        return service.postNetwork(network)
    }
}