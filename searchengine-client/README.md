## Simple Search Engine Client implementation

CLI designed to interact with Search Engine Server. It provides following commands
```
add <key> <file path> -- sends content of file at specified <file path> to server. This content will be associated with <key>
get <key>             -- retrieves document by key
search <token1> <token2>... -- search documents with specified tokens and displays keys of matched documents
```

### Build and Installation
You can use maven to build jar file
```
mvn clean install
```
Then you can run server using
```
java -jar target/searchengine-client-1.0-SNAPSHOT.jar [host] [port]
    [host] and [port] are optional parameters to specify search engine server. If not specified localhost and 8080 will be used
```
