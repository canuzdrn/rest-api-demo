package com.dbdemo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Entity
data class Customer(
    @Id
    @GeneratedValue
    val id: Long = 0,
    @field:Size(min = 3, message = "Username must contain at least 3 characters")
    val username: String,
    @field:Email(
        message = "Entered email is not valid",
        regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    )
    val email: String,
    @field:NotBlank(message = "City name can not be blank")
    val city: String,
) {

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private val transactions: MutableSet<Transaction> = mutableSetOf()

    @JsonIgnore
    fun getTransactions(): MutableSet<Transaction> {
        return transactions
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "customersNetworks")
    private val networks: MutableSet<String> = mutableSetOf()

    fun getNetworks(): MutableSet<String> {
        return networks
    }
}


