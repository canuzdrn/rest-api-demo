package com.dbdemo.dto

data class TransactionRequest(
    var amount: Int,
    var networkName: String,
    var username: String
)