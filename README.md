# IP-FIREWALL API
The IP-Firewall API is a service that checks whether an IP address exists in the IPsum suspicious list.

## Usage
To use the IP-Firewall API, send a request with the IP address you want to check. The API will respond with a confirmation if the IP address exists in the IPsum suspicious list.

### Example request
```
GET /check-ip?ip=192.0.2.1
```
When the IP address is present in the list, it will response `true`, otherwise it will response `false`.

## Architecture

The architecture consists of two main components: a cron job called retrieveData and an API for querying IP addresses.

### retrieveData:
This cron job fetches the list of suspicious IP addresses from IPsum at regular intervals. Upon retrieval, the data is stored in a file for subsequent processing by the API.

### API
The API is responsible for handling incoming requests to check whether an IP address is present in the list of suspicious IPs. It reads frequently the data stored in the file by the retrieveData cron job and loads all IP addresses into memory for efficient querying.

* Data Storage: The API stores the IP addresses in a trie data structure, allowing for fast and efficient retrieval of IP address information.

* Memory Usage: In the worst-case scenario where all IP addresses from the list are loaded into memory, the API's memory usage can be up to 4 GB.

* Performance: Both reading from and writing to the trie data structure have constant time complexity, ensuring efficient operation even with large datasets.

This architecture ensures that the API remains responsive and can handle requests efficiently, even when dealing with a large number of IP addresses. By periodically fetching and updating the list of suspicious IPs, the system stays up-to-date with the latest information from IPsum.

## Requirement
* JDK 17+
* Apache Maven 3.9.6 

## Running the application locally

To compile:
```shell script
./mvn clean install
```
To run the API:
```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

To run the retrieveData JOB:
```shell script
java -jar target/quarkus-app/quarkus-run.jar fetch
```

## Performance
### API Response Time
After conducting a performance test, the API demonstrated robust response times under load. The test involved sending an average of 37 requests per second over a duration of 2 minutes. The results for the response time are as follows:

* Average: 5 ms
* Minimum: 2 ms
* Maximum: 390 ms
* 90th: 4 ms

These response times indicate that the API consistently delivers fast and reliable responses to incoming requests.

### File Reading Performance
The process of reading a file containing ~27k IP addresses (~4.2 MB in size), on average, the file reading operation completes within a range of 200 to 400 ms. This ensures that the API can quickly load the list of IPs into memory for efficient querying.
