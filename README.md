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

    Body:
`{
"password":"Opeoluwa1",
"email":"opeoluwaagnes@gmail.com"
}
`

    Response 1:
    
    This an unsuccessful request due to existing details.
    
    status: 404 Bad Request
    
    message:
`{
"message": {
"message": "User Already Exist"
},
"successfull": false
}`

    Response 2:
    
    This is an unsuccessful request due to Wrong password or email fomart.
    
    status : 404 Bad Request
    
    Message:
`{
"message": {
"message": "Weak password"
},
"successfull": false
}`

    Response 3:
    
    This is a successful request.
    
    status:200 Accepted
    
    Message:
`"message": {
"message": "Registration completed"
},
"successfull": true
}`

    **Search for available logistic company request**

    DESCRIPTION
    
    This end-points allow user search for logistic company that are available for booking.This end-point don't need the user exist before they can search.

    Request:
    
    Method: Get
    
    Header: content-type:application/json
    
    URL:localhost:2020/api/v1/customer/availableCompany
    
    Response 1:
    
    This response is receved if a logistic compny is available.

{`
"message": {
"message": [
{
"id": 1,
"companyName": "Vision five company",
"phoneNumber": null,
"address": "Lagos ibadan express way mowe",
"email": "ogungbeniopemipo1@gmail.com",
"cacNumber": "B4561234"
}
]
},
"successfull": true
}`


    **Customer login Request**

    This end point authenticate user before they are given access to the Logistic application.it takes in email and password.
    
    Request:
    
    Method: Post
    
    URL:localhost:2020/api/v1/customer/login
    
    Header: content-type:application/json

    Body:
`{
"email":"opeoluwaagnes@gmail.com",
"password":"Opeoluwa1"
}`
    
    Response 1:
    
    This response is recieved if the user logins with wrong details.
`JSon:
{
"message": {
"message": "invalid login details"
},
"successfull": true
}`

    Response 2:
    
    Successful request.
`JSon:
{
"message": {
"message": "Login successful"
},
"successfull": true
}`

    Book delivery Request
    
    This endpoint allows users to book a delivery. it returns a generated booking-Id after the request has been sent successfully.
    
    Request:
    
    Method: Post
    
    Header: content-type:application/json
    
    URL:localhost:2020/api/v1/customer/order
    
    Body:
`JSon:
{
"receiverName":"ope",

    "receiverStreet": "13 emily akinola",
   
    "receiverState":"lagos",
    
    "receiverArea":"Bariga",
    
    "receiverPhoneNumber":"07066221008",

    "pickUpState":"oyo",

    "pickUpArea":"Iganna",

    "pickUpStreet":"Baptist high school",

    "pickUpName":"Mr Shola",

    "pickUpPhoneNumber":"09027104166",

    "typeOfPackage":"Files",

    "customerEmail":"opeoluwaagnes@gmail.com",
    
    "packageWeight":2,
    
    "typeOfVechicle":"Bike",

    "logisticCompanyName":"Vision five company"
}
`
    
    Response 1:
    
    successful Response.

`JSon:
{
"message": {
"message": "Your tracking id is Bk-7GP"
},
"successfull": true
}
`

## **Track order Request**


#### Description

    This end point allow user track their delivery. it takes the booking id and return the status of their delivery.User don't need to register or login before they can call this end-point.

#### Request:

    Method: Get
    
    Header: content-type:application/json
    
    URL:localhost:2020/api/v1/customer/trackOrder

`Body
raw (json)
{
"bookingId": "Bk-rJ"
}
   ` 
#### Response 1:
    
    unsuccessful request due to either wrong booking id number or unexisting booking id.
    
    Status code : 400 Bad request

`JSon:
{
"message": {
"message": "Order doesn't exist"
},
"successfull": false
}`

#### Response 2 :

    Successful response.
    
    Status code:200 ok

`json
{
"message": {
"message": "You order is IN_TRANSIT"
},
"successfull": true
}`


### Cancel delivery Request

      This end-point enable customer to get their booking history.This end-point only takes the customer email and return list of all booking history related to the customer.

## Request:

    Method: Get
    
    Header: content-type:application/json
    
    URL:localhost:2020/api/v1/customer/cancel


`Body
raw (json)
json
{
"bookingId":"Bk-ng5",
"customerEmail":"shayo@gmail.com",
"reasonOnWhyBookingWasCancelled":"slow delivery",
"companyName":"shola company"
}`

### Find Deliveries by delivery status Request


      This endpoint allows users to retrieve their delivery history based on the delivery status.
#### Request:

    Method: Get
    
    Header: content-type:application/json
    
    URL:llocalhost:2020/api/v1/customer/bookingHistoryByStatus


`Body
raw (json)
json
{
"email":"opeoluwaagnes@gmail.com",
"deliveryStatus":"Pending"
}`

### Successful Response

`Body
json
{
"message": {
"message": [
{
"id": 4,
"recieverName": "",
"recieverEmail": null,
"recieverAddress": "13 emily akinola Bariga lagos",
"recieverPhoneNumber": "07066221008",
"pickUpAddress": "Baptist high school Iganna oyo",
"pickUpUserName": null,
"pickUpPhoneNumber": "09027104166",
"typeOfPackage": "Files",
"nameOfVechicle": "Bike",
"customerEmail": "opeoluwaagnes@gmail.com",
"logisticCompany": "Vision five company",
"deliveryPrice": 70640,
"packageWeight": 2,
"bookingId": "Bk-75l",
"deliveryStatus": "PENDING"
},
{
"id": 6,
"recieverName": "",
"recieverEmail": null,
"recieverAddress": "13 emily akinola Bariga lagos",
"recieverPhoneNumber": "07066221008",
"pickUpAddress": "Baptist high school Iganna oyo",
"pickUpUserName": null,
"pickUpPhoneNumber": "09027104166",
"typeOfPackage": "Files",
"nameOfVechicle": "Bike",
"customerEmail": "opeoluwaagnes@gmail.com",
"logisticCompany": "Vision five company",
"deliveryPrice": 70640,
"packageWeight": 2,
"bookingId": "Bk-YXx",
"deliveryStatus": "PENDING"
},
{
"id": 7,
"recieverName": "",
"recieverEmail": null,
"recieverAddress": "13 emily akinola Bariga lagos",
"recieverPhoneNumber": "07066221008",
"pickUpAddress": "Baptist high school Iganna oyo",
"pickUpUserName": null,
"pickUpPhoneNumber": "09027104166",
"typeOfPackage": "Files",
"nameOfVechicle": "Bike",
"customerEmail": "opeoluwaagnes@gmail.com",
"logisticCompany": "Vision five company",
"deliveryPrice": 70640,
"packageWeight": 2,
"bookingId": "Bk-kAH",
"deliveryStatus": "PENDING"
},
{
"id": 8,
"recieverName": "",
"recieverEmail": null,
"recieverAddress": "13 emily akinola Bariga lagos",
"recieverPhoneNumber": "07066221008",
"pickUpAddress": "Baptist high school Iganna oyo",
"pickUpUserName": null,
"pickUpPhoneNumber": "09027104166",
"typeOfPackage": "Files",
"nameOfVechicle": "Bike",
"customerEmail": "opeoluwaagnes@gmail.com",
"logisticCompany": "Vision five company",
"deliveryPrice": 70640,
"packageWeight": 2,
"bookingId": "Bk-CD3",
"deliveryStatus": "PENDING"
},
{
"id": 9,
"recieverName": "",
"recieverEmail": null,
"recieverAddress": "13 emily akinola Bariga lagos",
"recieverPhoneNumber": "07066221008",
"pickUpAddress": "Baptist high school Iganna oyo",
"pickUpUserName": null,
"pickUpPhoneNumber": "09027104166",
"typeOfPackage": "Files",
"nameOfVechicle": "Bike",
"customerEmail": "opeoluwaagnes@gmail.com",
"logisticCompany": "Vision five company",
"deliveryPrice": 70640,
"packageWeight": 2,
"bookingId": "Bk-gUU",
"deliveryStatus": "PENDING"
}
]
},
"successfull": true
}`




### Logistic Controller

      This Api enables logistic companies to register, update, and accept deliveries, allowing them to set daily  
      delivery preferences for streamlined operations.

#### Features

      Register
      Login
      Register Vechicles
      Set daily Delivery limit
      Accept/Reject Delivery request
      Update Delivery Request



### Logistics Register Request


      This end point create a new user.it takes Company name, CAC,address,email,phone number and the password.The phone number,email and password must meet the following criterias.
      
      Password Validation:
      
      Valid Password: P@ssword123
      
      Starts with an uppercase letter.
      
      Followed by more than four letters.
      
      Ends with at least one digit or special character.
      
      Email Validation:
      
      Valid Email: user@gmail.com
      
      Username: user
      
      Domain: gmail
      
      Top-level domain: com
      
      Phone Number Validation
      
      Valid International Number: +123456789012
      
      Starts with a plus sign.
      
      Followed by a valid sequence of digits (6 to 12).
      
      Valid Local Number: 07801234567
      
      Starts with 0.
      
      Followed by a valid local number sequence.
      
      Request:
      
      Method: Post
      
      Header: content-type:application/json
      
      URI : localhost:2020/api/v1/logistic/register


      Body
``raw (json)
json
{
"phoneNumber":"07066221008",
"password":"Opemip@1",
"companyName":"Vision five company",
"address":"Lagos ibadan express way mowe",
"email":"ogungbeniopemipo1@gmail.com",
"cacNumber":"B4561234"
}``


`Example
New Request
Request
cURL
curl --location 'localhost:2020/api/v1/logistic/register' \
--data-raw '{
"phoneNumber":"07066221008",
"password":"Opemip@1",
"companyName":"Vision five company",
"address":"Lagos ibadan express way mowe",
"email":"ogungbeniopemipo1@gmail.com",
"cacNumber":"B4561234"
}'`


#### Response 1

`Body
json
{
"message": {
"message": "User Exist"
},
"successfull": false
}`


### Logistic login Request


      This end point verify user before the accessing the logistic app,it takes in the company name and their password.it verify if the details exist in the database and also if the given details are valid.
      
      Request
      
      Method: Post
      
      Header: Content-type:application/json
      
      URI: localhost:2020/api/v1/logistic/login

      Body
`raw (json)
json
{
"email":"Vision five company",
"password":"Opemip@1"
}`


#### Response 1

`Body
Headers (4)
json
{
"message": {
"message": "You are in already"
},
"successfull": false
}`


### Accept order Request


      This end-point allows the logistic company to either accept or reject delivery request.it takes in the company name,booking-id and the response (Note: either accepted or reject).
      
      Request:
      
      Method: Post
      
      Header: Content-type:application/json
      
      URI:localhost:2020/api/v1/logistic/acceptOrder
      
      Body
`raw (json)
json
{
"companyName":"Vision five company",
"bookingId":"Bk-NNb",
"response":"Accepted"
}`


`Example
cURL
curl --location 'localhost:2020/api/v1/logistic/acceptOrder' \
--data '{
"companyName":"Vision five company",
"bookingId": "Bk-rJ9",
"response":"Accepted"
`}'

#### Successful Response

`Body
Headers (5)
json
{
"message": {
"message": "Booking being process"
},
"successfull": true
}
`
### Register Vechicle Request

      The end-point allows the company to register the types of vehicles they possess.
      
      Request:
      
      Method: Post
      
      Header: Content-type:application/json
      
      URI:localhost:2020/api/v1/logistic/vechicle

`Body
raw (json)
json
{
"vehicleType":"Bike",
"companyName":"Vision five company"
}`

`Example
Register Vechicle Request
curl --location 'localhost:2020/api/v1/logistic/vechicle' \
--data '{
"vehicleType":"Bike",
"companyName":"Vision five company"
}'`

### UnSuccessful  Response

`Body
Headers (4)
json
{
"message": {
"message": "Vehicle already exist"
},
"successfull": false
}
`
### set Day availability Request

      This end-point allows user set numbers of delivery they can deliver in a day.This end-point takes the vechicle type,the company name and number of deliveries intended for the day.
      
      Request:
      
      Method: Post
      
      Header: content-type:application/json
      
      Uri: localhost:2020/api/v1/logistic/limit

`Body
raw (json)
json
{
"companyName":"Vision five company",
"vechicleType":"Bike",
"number":2
}`

`Example
set Day availability Request
curl --location 'localhost:2020/api/v1/logistic/limit' \
--data '{
"companyName":"Vision five company",
"vechicleType":"Bike",
"number":2
}'`


#### Successful Response

`Body
Headers (5)
json
{
"message": {
"message": "Vechilce Day Limit as been set Successfully"
},
"successfull": true
`}

#### update booking Request

      This end-point allow logistic company update a delivery which takes the company name, the booking-id and the updated message of the delivery which is either in_Transit or delivered.
      
      Request:
      
      Method: Put
      
      Header: content-type:application/json.
      URI:localhost:2020/api/v1/logistic/update
      
      
      Body
`raw (json)
json
{
"companyName":"Vision five company",
"bookingId":"Bk-rJ9",
"update":"in_transit"
}`

`Example
update booking Request
curl --location --request PUT 'localhost:2020/api/v1/logistic/update' \
--data '{
"companyName":"Vision five company",
"bookingId":"Bk-7GP",
"update":"in_transit"
}'`



### Accepted Response

`Body
Headers (5)
json
{
"message": {
"message": "Delivery Has Been updated"
},
"successfull": true
}`
