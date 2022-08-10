package com.dbdemo.repository

import com.dbdemo.model.Network
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface NetworkRepository : JpaRepository<Network, Long> {

    fun findByName(@Param("name") name: String): Network?


}