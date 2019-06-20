## Simple Search Engine Server implementation

Search Engine Server implementation that exposes following Rest API
```
POST /document/<document_key> -- stores document in engine under specified document key
GET /document/<document_key>  -- retrieves document by key
GET /document/search?token=<token1>&token=<token2>... -- search documents with specified tokens and retrieves their document keys
```

### Build and Installation
You can use maven to build jar file
```
mvn clean install
```
Then you can run server using
```
java -jar target/searchengine-server-1.0-SNAPSHOT.jar
```
