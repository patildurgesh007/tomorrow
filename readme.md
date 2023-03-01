# Queue System Application

## Overview

### This application is developed to execute Jobs Submitted by Multiple clients.

## Source Code Review

### Controller - 
#### Controllers are designed to communicate with application using REST calls.
### persistence - 
#### Package contains all Java Persistence classes
### Services - 
#### Service performs all validations, scheduling, execution, tracking.
### Security - 
#### Package contains all spring security and logging related operations.
### ExecutableJob - 
#### is wrapper class around the JobRequest with Job priority. This is runnable service.
### JobRequest - 
#### class is entity contains all request related information.

##Documentation
### SpringDoc Openapi html page
#### http://localhost:8080/swagger-ui.html

### SpringDoc Openapi REST Api
#### http://localhost:8080/api-docs/

