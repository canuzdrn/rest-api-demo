package com.dbdemo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Entity
data class Transaction(
    @Id
    @GeneratedValue
    val id: Long = -1,
    @field:Min(value = 1, message = "Less than 1 is not a valid amount (integer)")
    val amount: Int,
    @field:Size(min = 3, max = 32, message = "Username must have at least 3 characters")
    val username: String,
    @field:NotBlank(message = "networkName cannot be blank")
    val networkName: String,
) {

    @ManyToOne
    lateinit var network: Network

    @ManyToOne
    lateinit var customer: Customer

}


