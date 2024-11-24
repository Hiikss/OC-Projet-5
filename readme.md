# OCP5

## Setup the database
The DBSM used is MySQL. Create a database named `test` with a user named `user` and with password `123456`. 
Play the SQL script located at `ressources/script.sql` to create the tables.
MySQL has to listen to the 3306 port.

## Backend
> cd back
### Setup
Run the application with :
> mvn spring-boot:run

### Tests
To process the tests :
> mvn clean test

### Coverage
The coverage is generated with the previous command. 
You can find the results at :`back/target/site/jacoco/index.html`


## Frontend
> cd front
### Setup
Install the dependencies :
> npm install

Run the application :
> npm run start

### Tests
To run unitary tests :
> npm run test

To run end-to-end test :
> npm run e2e

### Coverage
Run unitary tests coverage :
> npx jest --coverage

Results can be found at `coverage/jest/lcov-report/index.html`

Run e2e tests coverage :
> npm run e2e:coverage

Results can be found at `coverage/lcov-report/index.html`