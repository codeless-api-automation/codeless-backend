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
      "IndexName": "GSI_TESTS_BY_CUSTOMER_ID",
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


TABLE_EXECUTION=$(cat <<EOF
{
  "TableName": "execution",
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
    },
    {
      "AttributeName": "scheduleId",
      "AttributeType": "S"
    }
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "GSI_EXECUTIONS_BY_CUSTOMER_ID",
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
          "type",
          "executionStatus",
          "regionName"
        ]
      }
    },
    {
      "IndexName": "GSI_EXECUTIONS_BY_SCHEDULE_ID",
      "KeySchema": [
        {
          "AttributeName": "scheduleId",
          "KeyType": "HASH"
        }
      ],
      "Projection": {
        "ProjectionType": "INCLUDE",
        "NonKeyAttributes": [
          "id",
          "name",
          "type",
          "executionStatus",
          "regionName"
        ]
      }
    }
  ],
  "BillingMode": "PAY_PER_REQUEST"
}
EOF
)

echo $(aws --endpoint-url ${ENDPOINT} dynamodb create-table --cli-input-json "$TABLE_EXECUTION")


TABLE_SCHEDULE=$(cat <<EOF
{
  "TableName": "schedule",
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
    },
    {
      "AttributeName": "testId",
      "AttributeType": "S"
    }
  ],
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "GSI_SCHEDULES_BY_CUSTOMER_ID",
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
          "testId",
          "timer",
          "regionName"
        ]
      }
    },
    {
      "IndexName": "GSI_SCHEDULES_BY_TEST_ID",
      "KeySchema": [
        {
          "AttributeName": "testId",
          "KeyType": "HASH"
        }
      ],
      "Projection": {
        "ProjectionType": "KEYS_ONLY"
      }
    }
  ],
  "BillingMode": "PAY_PER_REQUEST"
}
EOF
)

echo $(aws --endpoint-url ${ENDPOINT} dynamodb create-table --cli-input-json "$TABLE_SCHEDULE")

echo $(aws --endpoint-url ${ENDPOINT} dynamodb list-tables)