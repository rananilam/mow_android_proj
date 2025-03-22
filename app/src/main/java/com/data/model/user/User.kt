package com.data.model.user

import libraries.image.helper.models.MediaResult
import java.io.Serializable

class User: Serializable {


    /*
    "ID": 0,
  "FirstName": "Test1",
  "MiddleName": "Test",
  "LastName": "123",
  "CellNumber": "(111)111-1111",
  "HomeNumber": "(111)111-1111",
  "Email": "rutvijbhatt.verve@gmail.com",



  "Notes": "Note",
     */
    var id = 0
    var firstName = ""
    var lastName = ""
    var middleName = ""
    var accountName = ""
    var adminID = 0
    var companyEmail = ""
    var companyId = 0
    var token = ""
    var inActive = false

    var accessoryTypeID = 0
    var customerId = 0
    var email = ""
    var fullName = ""
    var isDefaultOccupant = false
    var licenseNo = ""
    var notes = ""
    var occupantID = 0
    var cellNumber = ""
    var homeNumber = ""
    var operatorID = 0
    var operatorIDOccupantID = 0
    var isDefault = false

    var deviceStyle = ""
    var deviceTypeID = 0
    var operatorName = ""

    //Register flow
    var country = ""
    var billAddress1 = ""
    var billAddress2 = ""
    var billCity = ""
    var billStateID = 0
    var otherBillStateName = ""
    var billZip = ""
    var password = ""
    var createdBy = 0
    var fileContent = ""
    var mimeType = ""
    var fileName = ""
    var dateOfBirth = ""
    var expiryDate = ""

    var heightFeet = 0
    var heightInch = 0
    var weight = 0

    var mediaResult: MediaResult? = null


    var alternateName = ""
    var isBlacklisted = false
    var rewardTurn = false
    var rewardsPoint = 0.0F
    var stateName = ""

    var occupantName = ""


}