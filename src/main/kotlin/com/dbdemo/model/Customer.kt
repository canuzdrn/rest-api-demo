package com.dbdemo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
data class Customer(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var username: String,
    var email: String,
    var city: String,
) {

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private val transactions: MutableSet<Transaction> = mutableSetOf()

    @JsonIgnore
    fun getTransactions(): MutableSet<Transaction> {
        return transactions
    }

    @ElementCollection
    private val networks: MutableSet<String> = mutableSetOf()

    fun getNetworks(): MutableSet<String> {
        return networks
    }
}


