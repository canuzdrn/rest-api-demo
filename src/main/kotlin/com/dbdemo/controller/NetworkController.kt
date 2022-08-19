package com.dbdemo.controller

import com.dbdemo.model.Network
import com.dbdemo.model.Transaction
import com.dbdemo.service.TransactionService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api/chain/networks")
@Api(value = "Network controller documentation")
class NetworkController(private val service: TransactionService) {

    @GetMapping
    @ApiOperation(value = "Retrieves all networks")
    fun getAllNetworks(): List<Network> {
        return service.getAllNetworks()
    }

    @GetMapping("/{networkName}/transactions")
    @ApiOperation(value = "Retrieves all transactions on the requested network")
    fun getNetworkTransactions(@PathVariable networkName: String): MutableSet<Transaction> {
        return service.getNetworkTransactions(networkName)
    }

    @PostMapping
    @ApiOperation(value = "Adds new network")
    fun addNetwork(@Valid @RequestBody network: Network): Network {
        return service.postNetwork(network)
    }
}