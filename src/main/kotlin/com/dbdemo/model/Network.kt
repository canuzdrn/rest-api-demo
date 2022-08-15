package com.dbdemo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank


@Entity
data class Network(
    @Id
    @GeneratedValue
    val id: Long = -1,
    @field:NotBlank(message = "Name of network can not be blank")
    val name: String,
) {

    @OneToMany(mappedBy = "network", fetch = FetchType.LAZY)
    private val transactions: MutableSet<Transaction> = mutableSetOf()

    @JsonIgnore
    fun getTransactions(): MutableSet<Transaction> {
        return transactions
    }
}