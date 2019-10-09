# Building project
`mvn clean package`

# Running project
`java -jar target/money-transfer-service-1.0-SNAPSHOT-jar-with-dependencies.jar`

# Tech Stack
* Java 8
* Jetty
* Jersey
* HK2

Multi-threading control is is built on top of tools provided by java.util.concurrency.

# End-points
`POST /api/transfer`
`POST /api/account`
`POST /api/account/{accountNumber}/deposit`
`GET /api/account/{accountNumber}/balance`
