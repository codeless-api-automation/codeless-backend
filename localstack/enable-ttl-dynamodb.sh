#!/bin/bash

# AWS configuration
export AWS_ACCESS_KEY_ID="test1"
export AWS_SECRET_ACCESS_KEY="test2"
export AWS_DEFAULT_REGION="us-east-1"

# Point aws-cli to LocalStack instead of AWS
ENDPOINT=http://localhost:4566/
#ENDPOINT=https://dynamodb.us-east-1.amazonaws.com/

ENABLE_TTL_ON_EXECUTION_TABLE=$(cat <<EOF
{
  "TimeToLiveSpecification": {
    "AttributeName": "ttl",
    "Enabled": true
  }
}
EOF
)

echo $(aws --endpoint-url ${ENDPOINT} dynamodb update-time-to-live --table-name execution --cli-input-json "$ENABLE_TTL_ON_EXECUTION_TABLE")