#!/bin/bash

# AWS configuration
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"

# Point aws-cli to LocalStack instead of AWS
ENDPOINT=http://localhost:4566/

TABLE_USER=$(cat <<EOF
{
  "TableName": "user",
  "KeySchema": [
    {
      "AttributeName": "username",
      "KeyType": "HASH"
    }
  ],
  "AttributeDefinitions": [
    {
      "AttributeName": "username",
      "AttributeType": "S"
    }
  ],
  "BillingMode": "PAY_PER_REQUEST"
}
EOF
)
echo $(aws --endpoint-url ${ENDPOINT} dynamodb create-table --cli-input-json "$TABLE_USER")

TABLE_TEST=$(cat <<EOF
{
  "TableName": "test",
  "KeySchema": [
    {
      "AttributeName": "id",
      "KeyType": "HASH"
    }
  ],
  "AttributeDefinitions": [
    {
      "AttributeName": "id",
      "AttributeType": "S"
    },
    {
      "AttributeName": "customerId",
      "AttributeType": "S"
    }
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "GIS_TESTS_BY_CUSTOMER_ID",
      "KeySchema": [
        {
          "AttributeName": "customerId",
          "KeyType": "HASH"
        }
      ],
      "Projection": {
        "ProjectionType": "INCLUDE",
        "NonKeyAttributes": [
          "id",
          "name",
          "json"
        ]
      }
    }
  ],
  "BillingMode": "PAY_PER_REQUEST"
}
EOF
)
echo $(aws --endpoint-url ${ENDPOINT} dynamodb create-table --cli-input-json "$TABLE_TEST")

echo $(aws --endpoint-url ${ENDPOINT} dynamodb list-tables)