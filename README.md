# Basic Calander App

This basic app consists of a Vue.js frontend and a Spring backend.
It was created as a solo project as part of the Programmentwicklung II course.
It allows users to create and look at assignees and appointments. Assignees can be added to appointments upon creation and they will get an email when this happens.
The data is stored in a MariaDB database.

No security feature is implemented.

## Please view the ReadMe files in the API and frontend folder for detailed requirements and starting instructions

### Requirements

- Java 11 or later
- MariaDB
- Node.js

### Quick-start

Assuming all prerequisites are fulfilled, you can follow these instructions to get to know the application:
- Start the API by navigating into its folder and executing `./mvnw spring-boot:run`.
- Now start the frontend by navigating into its folder and executing `npm install` and then `npm run serve`.
- It will host the UI at http://localhost:8000, which you can now also open in your browser.

