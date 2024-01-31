## Local environment setup

1. Install docker
2. Run the command
```
docker-compose up -d
```
3. Get lambda logs
```
aws --endpoint-url=http://localhost:4566 logs tail /aws/lambda/codeless-test-execution --follow
```

4. Get table data during testing
```
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name execution
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name test
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name schedule
```

4. Activate local profile
```
spring.profiles.active=local
```