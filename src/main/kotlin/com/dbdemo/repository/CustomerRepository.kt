package com.dbdemo.repository

import com.dbdemo.dto.CustomerRequest
import com.dbdemo.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {

    fun findByUsername(@Param("username") username: String): Customer?

}