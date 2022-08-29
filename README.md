# Customer manager - RestFull Service + Spring Security

### Instalation

* Create data base MongoDB
* Upload data [customer.json](.../blob/main/docs/customers.json
  )
* Upload data [users.json](.../blob/main/docs/users.json)
* User access test ```[username: juan, password:123],[username: diego, password:123],[username: juan, password:123],[username: martin, password:1234] ```
* Update database name in [Application.yml](.../blob/main/src/main/resources/application.yaml)
* ``` git clone https://github.com/wualtervera/Customer-Manager-Spring-boot-MVC-Spring-Security.git```
* run ```mvn install```

Data de prueba
* Impor in postman [customer-manager.postman_collection](.../blob/main/docs/customer-manager.postman_collection.json
  )

Swagger URL
* ```http://localhost:8080/swagger-ui/index.html```

### Proyect content

**RestFull Service: Customer managment.** 

Content App:
* Models
* Repositories
* Services
* Controllers
* Spring security Http basic
* User role management.
* Unit tests
* Swagger Documentation
* Good coding practices

Applied Programming
* OOP
* class
* interfaces
* Generics
* Maps
* Lists
* Collections
* Streams
* reflections
* encryption
* States
* ... etc

### Tecnologis content

* Spring boot - v2.6.2
* Java 11
* Swagger v3.0.0
* Jacoco v0.8.5
* Junit 5
* Gson 2.9.1


###Preview

* DB Mongo Db

![Response /user](.../blob/main/docs/db-customer-manager.png?raw=true)


* EndPoint /user

![Response /user](.../blob/main/docs/response-endpoint-users-ppi-rest-full-2.png?raw=true)

* Swagger Documentation

![Response /user](.../blob/main/docs/swagger-v3-api-docs.png?raw=true)


* Unit test - Mokito, Spring boot test

![Response /user](.../blob/main/docs/unit-test-customer-controller.png?raw=true)