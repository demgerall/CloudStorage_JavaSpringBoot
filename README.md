# Project name “Cloud Storage”

## Project description

A REST application has been developed that provides an interface for uploading files and displaying a list of the user's already uploaded files. 

A ready-made web application (**FRONT**) integrates with this service without the need for improvements.

The service implements the following functions:

   1. Displaying a list of files.
   2. Uploading files.
   3. Deleting files.
   4. Renaming files.
   5. User authorization.
   6. When you enter your username and password, the user's presence in the database and access rights are checked. Passwords are stored in encrypted form. In case of successful authorization, a JWT token is generated, which provides the user with access to file management during its validity period.

After authorization, all requests from the **FRONT** are sent with the "auth-token" header containing the current token. When logging out (logout), the token is revoked and the user is redirected to the login page.

## Server and DB (database) start:

- **FRONT** is running on port **8081** (http://localhost:8081).
- The **PostgreSQL** DB is used. It works on port **5432**.
- To launch it, use the "**docker compose up**" command.

## Description and start FRONT

1. Install node.js (version no lower than 14.15.0) on your computer following the instructions: https://nodejs.org/ru/download/
2. Download [**FRONT**](https://github.com/netology-code/jd-homeworks/blob/master/diploma/netology-diplom-frontend) (JavaScript)
3. Go to the **FRONT** folder of the application and run all startup commands from it.
4. Following the description README.md **FRONT** of the project to launch node.js application (npm install...).
5. You can set the url for calling your **BACKEND** service:
    1. In the `.env` file of the **FRONT** (located in the root of the project) of the application, you need to change the url to the **BACKEND**, for example: `VUE_APP_BASE_URL=http://localhost:8080`.
    2. Rebuild and run the **FRONT** again: **`npm run build`**.
    3. The changed `url` will be saved for the next launches.



