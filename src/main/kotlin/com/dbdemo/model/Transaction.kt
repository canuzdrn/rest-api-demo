package com.dbdemo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
data class Transaction(
    @Id
    @GeneratedValue
    var id: Long = -1,
    var amount: Int,
    var username: String,
    var networkName: String,
) {

    @ManyToOne
    open lateinit var network: Network

    @ManyToOne
    open lateinit var customer: Customer

}


