Module 20 - API Automation Test 

How to run test, enter command in terminal: .\gradlew clean test

Positive Test Cases
1. Get User List
2. Create User
3. Edit User (Put Request)
4. Edit User (Patch Request)
5. Delete User
6. Validate JSON Schema

Negative Test Cases
1. Get non-existing user
2. Edit non-existing user (PUT)
3. Edit non-existing user (PATCH)
4. Delete non-existing user
5. Create user without header

Edge Test Cases

1. Create user with name > 200 chars
2. Create user with name include special characters
3. Create user with job > 200 chars
4. Create user with job include special characters
