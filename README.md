# Springboot project for my internship
#### RESTful API that includes customers, transactions and networks. Kotlin and Spring technologies are used. I used PostgreSQL database with JPA functionality as my database. Also included JUnit5 unit tests using Mockk library. You can clone this project in IntelliJ IDEA and the application will be running at port 8080 as default. (which can be altered by editing application.properties file)
##### server.port= REQUESTED_PORT* 
##### *: 2-byte signed integer
---
#### It is simple RESTful API that has elements which are conceptually similar to blockchain and blockchain networks such as users (customers), transactions, networks (ETH, SOL, etc.) In order to increase the readablity of methods, models and properties of the project on a web environment I also configured a SwaggerUI (Springfox) which can be visited via http://localhost:8080/swagger-ui/
### The purpose of this project is to learn and practice concepts related to:
> - Kotlin
> - Springboot
> - Building a REST API
> - JPA Relations
### In more detail, I used the following:
> - REST API guidelines
> - HTTP (GET, POST, PUT, DELETE, responses, status codes)
> - Repository design pattern
> - Dependency injection
> - Data Transfer Objects (DTOs) & ObjectMapper (mostly in tests)
> - Testing API Endpoints (SwaggerUI & Postman)
> - PostgreSQL
> - Docker (Setting up a Docker PostgreSQL environment)
