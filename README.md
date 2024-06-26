# **LogisticApp**

_This is a mini logistic application that connect user with an available logistic company.This application is build using java maven and mySql for the data storage._

## Table of Content
1. [SetUp](#Getting-Started)
2. [CustomerApi](#CustomerAPI)
3. [LogisticApi](#LogisticAPI)


# Getting-Started
1. Create an account with git 
2. From your terminal/ command prompt clone the repository using this git command 
    * git clone <https://github.com/OgungbeniOpeoluwa/ContactManagement.git>.
3. Ensure all dependencies in the project are well injected in your pom.xml.
    * To download and build the project you can run this command on the terminal: _mvn clean install_
4. setup mysql database to configure database connection.
5. To start the application from your IDE run the application main class.Alternatively you can run this command on the terminal
    * mvn spring-boot:run
6. install postman to test the application end-points by providing the necessary url and body requests if necessary.

# CustomerAPI

_This Api range from registering user to enabling them search for available logistic companies,to 
booking deliveries and tracking their deliveries ._

### **Features**
1. Register
2. Login
3. Search For Available Logistic companies
4. Track Delivery
5. Book Delivery

##    Register Request
    
####    **Description**

   _This end point create a new user.it takes the user email and password.The email and password are expected to meet the following criteria._

#####    _Password Validation:_
         Valid Password: P@ssword123
   1. Starts with an uppercase letter.
   2. Followed by more than four letters.
   3. Ends with at least one digit or special character.

#####    _Email Validation:_

         Valid Email: user@gmail.com
   1. Username: user
   2. Domain: gmail
   3. Top-level domain: com

####    **Request:**

   * Url: localhost:2020/api/v1/customer/register

   * Method: Post

   * Header: content-type:application/json

   * Body:

```
{
"password":"Opeoluwa1",
"email":"opeoluwaagnes@gmail.com"
}
```

####    **Response 1:**
    
   _unsuccessful request due to existing details._
    
      status: 404 Bad Request
   * Response Body:
```
{
"message": {
"message": "User Already Exist"
},
"successfull": false
}
```

#### **Response 2:**
    
   _unsuccessful request due to Wrong password or email format._
    
      status : 404 Bad Request
   * Response Body:
```
{
"message": {
"message": "Weak password"
},
"successfull": false
}
```
####  **Response 3:**
_successful request._
    
    status:200 Accepted
    
   * Response Body:
```
{
"message": {
"message": "Registration completed"
},
"successfull": true
}
```
##  **Search for available logistic company request**

###    **DESCRIPTION**
    
   _This end-points allow user search for logistic company that are available for booking.This end-point don't need the user exist before they can search._

####    **Request:**
    
   * Method: Get
    
   * Header: content-type:application/json
    
   * URL:localhost:2020/api/v1/customer/availableCompany
    
####    **Response 1:**
    
   _This response is received if a logistic company is available._
   
   * Response Body:
```{
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
}
```


## ****Customer login Request****

_This end point authenticate user before they are given access to the Logistic application.it takes in email and password._
    
#### **Request:**
    
* Method: Post

* URL:localhost:2020/api/v1/customer/login
    
* Header: content-type:application/json

    Body:
```
{
"email":"opeoluwaagnes@gmail.com",
"password":"Opeoluwa1"
}
```
    
#### **Response 1:**
    
_This response is received if the user logins with wrong details._
   * Response Body:
```
{
"message": {
"message": "invalid login details"
},
"successfull": true
}
```

#### **Response 2:**

_Successful request._
 * Response Body:
```{
"message": {
"message": "Login successful"
},
"successfull": true
}
```

## **Book delivery Request**
    
_This endpoint allows users to book a delivery. it returns a generated booking-Id after the request has been sent successfully._
    
#### **Request:**
    
* Method: Post
    
* Header: content-type:application/json
    
* URL:localhost:2020/api/v1/customer/order

* Body:

```{
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
```
    
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

* Method: Get
    
* Header: content-type:application/json
    
* URL:localhost:2020/api/v1/customer/trackOrder

* Body
```
raw (json)
{
"bookingId": "Bk-rJ"
}
 ``` 
#### Fields:
* BookingId(require,String):The user bookingId.

#### Response 1:
_unsuccessful request due to either wrong booking id number or un-exiting booking id._
    
    Status code : 400 Bad request
* Response Body:

```
{
"message": {
"message": "Order doesn't exist"
},
"successfull": false
}
```

#### Response 2 :
_Successful response._
    
    Status code:200 ok

```
{
"message": {
"message": "You order is IN_TRANSIT"
},
"successfull": true
}
```


### Cancel delivery Request

_This end-point enable customer to get their booking history.This end-point only takes the customer email and 
return list of all booking history related to the customer._

#### Request:

* Method: Get
    
* Header: content-type:application/json

* URL:localhost:2020/api/v1/customer/cancel

* Body
```
raw (json)
{
"bookingId":"Bk-ng5",
"customerEmail":"shayo@gmail.com",
"reasonOnWhyBookingWasCancelled":"slow delivery",
"companyName":"shola company"
}
```

## Find Deliveries by delivery status Request

_This endpoint allows users to retrieve their delivery history based on the delivery status._
#### Request:

* Method: Get
    
* Header: content-type:application/json
    
* URL:localhost:2020/api/v1/customer/bookingHistoryByStatus

* Body
```
raw (json)

{
"email":"opeoluwaagnes@gmail.com",
"deliveryStatus":"Pending"
}
```
#### **Fields:**
* Email(Require, String):_The user Email_
* DeliveryStatus(require,String):_Status of The delivery intended to find_

### Response 1
_Successful Response_
      
    Status Code:201

* Response Body
```
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
}
```




## **LogisticAPI**

_This Api enables logistic companies to register, update, and accept deliveries, allowing them to set daily  
delivery preferences for streamlined operations._

### **Features**
* Register
* Login
* Register Vehicles
* Set daily Delivery limit
* Accept/Reject Delivery request
* Update Delivery Request



## Logistics Register Request

_This end point create a new user.it takes Company name, CAC document,address,email,phone number and the password.The phone number,email and password must meet the following criterias._
      
##### Password Validation:
      Valid Password: P@ssword123
      
* Starts with an uppercase letter.
      
* Followed by more than four letters.
      
* Ends with at least one digit or special character.
      
##### **Email Validation:**
      
* Valid Email: user@gmail.com
      
* Username: user
      
* Domain: gmail
      
* Top-level domain: com
      
##### **Phone Number Validation:**
      
* Valid International Number: +123456789012

* Starts with a plus sign.

* Followed by a valid sequence of digits (6 to 12).
      
### **Request:**
* Method: Post

* Header: content-type:application/json

* URL : localhost:2020/api/v1/logistic/register

* Body:

```raw (json)
json
{
"phoneNumber":"07066221008",
"password":"Opemip@1",
"companyName":"Vision five company",
"address":"Lagos ibadan express way mowe",
"email":"ogungbeniopemipo1@gmail.com",
"cacNumber":"B4561234"
}
```

#### **Fields:**

* phoneNumber(required,String):The user phone number.
* CompanyName:(required,String):The user Company name.
* Address:(Required, String):The user address.
* Email(Required, String):The user  Email address.
* CacNumber(Required,String):The Company CaC Number



#### Response 1
_Unsuccessful request._

    status code:Bad_Request
* Response  Body:

```
{
"message": {
"message": "User Exist"
},
"successfull": false
}
```

Response 2:
_Successful Request_

    status code:created
```
{
    "message": {
        "message": "Registration Completed"
    },
    "successfull": true
    }
```


## Logistic login Request

_This end point verify user before the accessing the logistic app,it takes in the company name and their password.it verify if the details exist 
in the database and also if the given details are valid._
      
#### **Request**

* Method: Post
      
* Header: Content-type:application/json

* URL: localhost:2020/api/v1/logistic/login

* Body

`raw (json)
{
"companyName":"Vision five company",
"password":"Opemip@1"
}`

#### **Fields**
* CompanyName:(required,String):The company name
* Password(required,String)


#### **Response 1**
_unsuccessful request_

 Example 1:
    
* Response Body:
```
{
"message": {
"message": "You are in already"
},
"successfull": false
}
```
Example 2:
 * Response Body:
    ```
   {
    "message": {
        "message": "Invalid login details"
    },
    "successfull": false
    }
   ```

#### **Successful Response**
* Response Body:
```
  {
  "message": {
  "message": "You have Successfully login"
  },
  "successfull": true
  }
  ```


## Accept order Request

_This end-point allows the logistic company to either accept or reject delivery request.it takes in the company name,booking-id and the response (Note: either accepted or reject)._
      
#### **Request:**
      
* Method: Post

* Header: Content-type:application/json

* URL:localhost:2020/api/v1/logistic/acceptOrder
       
* Request Body
```
raw (json)

{
"companyName":"Vision five company",
"bookingId":"Bk-NNb",
"response":"Accepted"
}
```


#### Successful Response
* Response Body
```
{
"message": {
"message": "Booking being process"
},
"successfull": true
}
```

###  Failed Response
  Example 1:
* Response Body:
```
{
    "message": {
        "message": "Vision fie company company doesn't exist"
    },
    "successfull": false
}
```
  Example 2:
  * Response Body
```
    {
    "message": {
    "message": "Booking id doesn't exist"
    },
    "successfully": false
    }
   ```

## Register Vehicle Request

_The end-point allows the company to register the types of vehicles they possess._
      
### **Request:**
      
* Method: Post
      
* Header: Content-type:application/json
      
* URL:localhost:2020/api/v1/logistic/vehicle

* Body

```
raw (json)
json
{
"vehicleType":"Bike",
"companyName":"Vision five company"
}
```
#### **Fields**:
1. VehicleType(require,String):The Vehicle type of the user
2. CompanyName(Require,String):The company name of the user

### Response 1
_Unsuccessful response_

    Status Code: 400
* Response Body

```
{
"message": {
"message": "Vehicle already exist"
},
"successfull": false
}
```

#### **Response 2:**
_Successful Response_
* Response Body:
```
{
    "message": {
        "message": "You have successfully register your vechicle"
    },
    "successfull": true
}
```




## **Set Day availability Request**

_This end-point allows user set numbers of delivery they can 
deliver in a day.This end-point takes the vehicle type,the 
company name and number of deliveries intended for the day._
      
### **Request:**
      
* Method: Post

* Header: content-type:application/json

* Url: localhost:2020/api/v1/logistic/limit

* Body
```
raw (json)
json
{
"companyName":"Vision five company",
"vechicleType":"Bike",
"number":2
}
```




#### Response 1
_Successful Response_

* Response Body

```
{
"message": {
"message": "Vechilce Day Limit as been set Successfully"
},
"successfull": true
}
```
### Failed Response
* Response Body
```
{
    "message": {
        "message": "Vechicle doesn't exist"
    },
    "successfull": false
}
```

## **update booking Request**

_This end-point allow logistic company update a delivery which 
takes the company name, the booking-id and the updated message 
of the delivery which is either in_Transit or delivered._
      
#### **Request:**
      
* Method: Put

* Header: content-type:application/json.
      
* URL:localhost:2020/api/v1/logistic/update

* Body

```
raw (json)

{
"companyName":"Vision five company",
"bookingId":"Bk-rJ9",
"update":"in_transit"
}
```

### Successful Response 
* Body
```
{
"message": {
"message": "Delivery Has Been updated"
},
"successfull": true
}
```
### Failed Response
  
* Response Body
```
{
    "message": {
        "message": "Logistic company doesn't exist"
    },
    "successfull": false
}
```
