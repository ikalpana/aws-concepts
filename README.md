# aws-concepts

**1.Aws-lambda-dynamodb** -> In this example when ever we are sending a request to Api Gateway it will invoke the endpoint and trigger the lambda function and store it in the dynamodb. 

**2.file-apis** -> In this example we are uploading file through apis in s3 bucket.

**3.lambda-trigger-s3-upload** -> In this example when a text file is uploaded to s3 bucket then it will trigger lambda function and generate logs and we can view it in CloudWatch Logs 

**4. ses-mail-service** -> In this example when apis is successfully executed and email in request body of api should be verified emails and then we are sending emails to verified emails in Aws 

**5. springboot-email-notification** -> In this example first we need to trigger request subscribe api and we have to confirm subscription throught email sent by aws and then we can send notification mails to all emails which are subscribed

**6.springboot-aws-rds** -> In this example we are storing data to RDS when apis are executed and we can check data by building connection in MySQLWorkBench with database instance url, username and password has context menu.

**7.springboot-aws-dynamodb** ->In the example we are storing data in dynamodb table.
