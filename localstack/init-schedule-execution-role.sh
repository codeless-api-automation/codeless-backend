echo $(aws iam create-policy --policy-name schedule-execution-role-policy --policy-document file://schedule/policy.json)
echo $(aws iam create-role --role-name schedule-execution-role --assume-role-policy-document file://schedule/trust-policy.json)
echo $(aws iam attach-role-policy --policy-arn arn:aws:iam:*:*:policy/schedule-execution-role-policy --role-name schedule-execution-role)