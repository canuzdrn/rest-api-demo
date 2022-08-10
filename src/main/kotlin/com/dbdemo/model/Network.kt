package com.dbdemo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
data class Network(
    @Id
    @GeneratedValue
    var id: Long = -1,
    var name: String,
) {

    @OneToMany(mappedBy = "network", fetch = FetchType.LAZY)
    private val transactions: MutableSet<Transaction> = mutableSetOf()

    @JsonIgnore
    fun getTransactions(): MutableSet<Transaction> {
        return transactions
    }
}