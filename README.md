# LogisticApp
This is a mini logistic application that connect user with a available logistic company.This application is build using java maven and mySql for the data storage.

Steps on how to Install and use :

1. From your terminal/ command prompt clone the repository using git clone <https://github.com/OgungbeniOpeoluwa/ContactManagement.git>.
2. Ensure Maven is properly set up.
3. Navigate to the project directory using the terminal and run mvn clean install to download dependencies and build the project. Alternatively, open the project in your preferred IDE,the IDE automatically handles the dependencies.
4. Set up MySQL database.
5. Configure database connection settings in <application.properties>. Update the database connection settings in this file to match your MySQL database credentials.
6. Run the application from your IDE by running the main class.Alternatively, you can use the command mvn spring-boot:run to start the application from the command line.
7. Test the application's endpoints using Postman by importing the provided collection file and sending HTTP requests


    Customer End-Point

    This Api range from registering user to enabling them search for available logistic companies,to 
    booking deliveries and tracking their deliveries .

    Features
    1. Register
    2. Login
    3. Search For Available Logistic companies
    4. Track Delivery
    5. Book Delivery

Customer Register
Description
This end point create a new user.it takes just a email and password.The email and password are expected to meet the following criterias.

Password Validation:
->Valid Password: P@ssword123

1. Starts with an uppercase letter.
2. Followed by more than four letters.
3. Ends with at least one digit or special character.

Email Validation:

->Valid Email: user@gmail.com

1. Username: user
2. Domain: gmail
3. Top-level domain: com

Request:
Url: localhost:2020/api/v1/customer/register

Method: Post

Header: content-type:application/json

curl --location 'localhost:2020/api/v1/customer/register' \
--data-raw '{
"password":"Opeoluwa1",
"email":"opeoluwaagnes2@gmail.com"
}'

Response 1:

This an unsuccessful request due to existing details.

status: 404 Bad Request

message: