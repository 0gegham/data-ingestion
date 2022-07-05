## Data ingestion application

## Stack
* Java
* Spring (Boot/Data JPA/Batch/Validation)
* H2 Database
* Zip4j
* Lombok
* MapStruct
***
## Description

Given the ZIP file (data.zip in application resources) which can contain any number of CSV files. All files have similar structure (firstName, lastName, date).
#### Simple data ingestion application. 
Read static zip file (from application resources directory), process it and write to database.