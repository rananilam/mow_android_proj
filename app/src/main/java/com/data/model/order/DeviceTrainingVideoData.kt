package com.data.model.order

import java.io.Serializable

class DeviceTrainingVideoData{

    /*
       [
    {
        "ErrorMessage": null,
        "Message": null,
        "StatusCode": "OK",
        "AttestationID": 0,
        "CheckOutText": "<p>Test Test</p>\r\n<p>&nbsp;</p>",
        "CompanyName": "BALLY'S AC, CAESARS AC, HARRAH'S AC, AND HARRAH'S OPERATING COMPANY, INC.,",
        "DeviceTypeID": 52,
        "IsPayorOperatorSame": false,
        "LocationID": 3,
        "ModifiedTermsAndConditionText": "<p>Test data for terms and condition1</p>",
        "OperatorName": null,
        "PayorName": null,
        "RentalAgreementText": "<p>Test data for rental agreement1</p>",
        "RentalCompanyName": null,
        "RequestPortal": null,
        "TermsAndConditionText": "<p>Test data for terms and condition1</p>",
        "TrainingVideoURL": "https://mow-web.s3.us-east-1.amazonaws.com/devvideos/Small_374kb.mp4",
        "TraningVideoText": "<p>Test Data for video attestation1</p>"
    }
]
     */

    var attestationID = 0
    var checkOutText = ""
    var companyName = ""
    var deviceTypeID = 0
    var isPayorOperatorSame = false
    var locationID = 0
    var modifiedTermsAndConditionText = ""
    var operatorName = ""
    var payorName = ""
    var rentalAgreementText = ""
    var rentalCompanyName = ""
    var requestPortal = ""
    var termsAndConditionText = ""
    var trainingVideoURL = ""
    var traningVideoText = ""
}