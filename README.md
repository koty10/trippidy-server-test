# Trippidy Server Integration Tests
This is a project for REST API integration tests and performance tests using Locust for the Trippidy server

## Integration Tests Run Instructions
- Go to the integration_tests folder
- Copy integration_tests\src\main\resources\trippidy.properties.template to integration_tests\src\main\resources\trippidy.properties and fill in values. I can send them by email as I do not want them to be present in a public repository.
- Change the RestAssured.baseURI domain in dev.koten.trippidy.TrippiddyTest to the one where your server is running or just test my server instance. 

## Performance Tests Run Instructions
- Go to the performance_tests folder
- Copy config.json.template toconfig.json and fill in values. I can send them by email as I do not want them to be present in a public repository.
- Run `docker-compose up`
- Open http://localhost:8089 (I had to use Firefox as Chrome did not allow me to access unsecured HTTP)
- Run test
