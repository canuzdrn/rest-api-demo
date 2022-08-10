package com.dbdemo.repository

import com.dbdemo.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface TransactionRepository : JpaRepository<Transaction, Long>, CrudRepository<Transaction, Long> {


    @Query("SELECT CASE WHEN count(e) > 0 THEN true ELSE false END FROM Transaction e where e.username = :username")
    fun isUserExists(@Param("username") username: String): Boolean

}