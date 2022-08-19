package com.dbdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DbdemoApplication

fun main(args: Array<String>) {
    runApplication<DbdemoApplication>(*args)
}
