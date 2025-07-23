# Banking Account Management System

## Overview
The Banking Account Management System is a Web Application developed using Spring Boot and React, designed to facilitate banking operations.
This API enables users to manage their bank accounts with functionalities such as transferring funds, making deposits and withdrawing
money. The system employs MySQL for data storage.

# Key Features:

* Account Management: Users can create and manage their bank accounts, ensuring secure access and operations.
* Fund Transfers: Users can transfer funds to other accounts efficiently and securely.
* Deposits: Users can deposit money into their accounts, updating their balance in real-time.
* Withdrawals: Users can withdraw funds from their accounts, ensuring transaction integrity and security.

# Technologies Used:

* Java 21
* Spring Boot(3.3.0)
* React(18.2.0)
* MySQL
* JUnit
* Rest-Assured Framework
* MapStruct

# Testing and Quality Assurance:
The Unit Tests provide over 85% of code coverage

# Security Features:
* JWT Authentication
* Password hashing

# API Description
## Account Controller
* GET /accounts: Returns all Accounts
* GET /accounts/{id}: Returns account with specific Id
* POST /accounts/new: Creates new Account
* PUT /accounts/update/{id}: Updates details of specific Account
* PUT /accounts/transfer/{id}: Transfers money from one Account to another
* DELETE /accounts/delete/{id}: Deletes Account with specific Id
## Card Controller
* GET /cards: Returns all Cards
* GET /cards/{id}: Returns Card with specific Id
* POST /cards/new: Creates new Card
* PUT /cards/update/{id}: Updates details of specific Card
* DELETE /cards/delete/{id}: Deletes Card with specific Id
## Deposit Controller
* GET /deposits: Returns all Deposits
* GET /deposits/{id}: Returns Deposit with specific Id
* GET /deposits/account/{accountNumber}: Returns Deposits of specific account
* POST /deposits: Creates new Deposit
* PUT /deposits/update/{id}: Updates Deposit with specific Id
* DELETE /deposits/delete/{id}: Deletes Deposit with specific Id

## Withdrawal Controller
* GET /withdrawals: Returns all Withdrawals
* GET /withdrawals/{id}: Returns Withdrawal with specific Id
* GET /withdrawals/account/{accountNumber}: Returns Withdrawals of specific account
* POST /withdrawals/new: Creates new Withdrawal
* PUT /withdrawals/update/{id}: Updates Withdrawal with specific Id
* DELETE /withdrawals/delete/{id}: Deletes Withdrawal with specific Id
## User Controller
* GET /users: Returns all Users
* GET /users/{id}: Returns User with specific Id
* GET /users/name/{username}: Returns User with specific username
* POST /users/new: Creates new User
* PUT /users/update/{id}: Updates User with specific Id
* PUT /users/changePassword: Updates current user's password
* DELETE /users/delete/{id}: Deletes User with specific Id
## Authentication Controller
* POST /auth/login: Login with username/password credentials 
