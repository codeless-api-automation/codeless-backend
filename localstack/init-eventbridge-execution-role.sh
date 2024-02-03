echo $(aws iam create-policy --policy-name codeless-schedule-role --policy-document file://iam-policy/eventbridge-schedule.json)
echo $(aws iam create-role --role-name codeless-schedule-role --assume-role-policy-document file://iam-policy/trust-eventbridge-schedule.json)
echo $(aws iam attach-role-policy --policy-arn arn:aws:iam:*:*:policy/codeless-schedule-role --role-name codeless-schedule-role)