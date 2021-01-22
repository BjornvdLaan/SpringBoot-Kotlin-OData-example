# Spring Boot OData v4 service in Kotlin

## Steps to run the Project
```
./gradlew bootRun (on Mac)
gradlew.bat bootRun (on Windows)
```

## Create Executable Jar
```
./gradlew build (on Mac)
gradlew.bat build (on Windows)
```

Run the Executable
```
java -jar build/libs/reporting-0.0.1-SNAPSHOT.jar
```

## See the results

Service document (see the Entity Collections that are available)
```
http://localhost:8080/odata
```

Metadata document (see the structure of the results)
```
http://localhost:8080/odata/$metadata
```

Entity Collections
```
http://localhost:8080/odata/Products
http://localhost:8080/odata/Counterparties
```
