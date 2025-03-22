package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.order.Order
import java.lang.reflect.Type

class OrderDs : JsonDeserializer<Order> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Order? {


        val order = Order()

        json?.let {
            val jsonObject = it.asJsonObject



            if (jsonObject.has("PrimaryOrderID") && !jsonObject["PrimaryOrderID"].isJsonNull)
                order.primaryOrderID = jsonObject["PrimaryOrderID"].asInt

            if (jsonObject.has("AccessoryType") && !jsonObject["AccessoryType"].isJsonNull)
                order.accessoryType = jsonObject["AccessoryType"].asString

            if (jsonObject.has("AccessoryTypeID") && !jsonObject["AccessoryTypeID"].isJsonNull)
                order.accessoryTypeID = jsonObject["AccessoryTypeID"].asInt

            if (jsonObject.has("AccountName") && !jsonObject["AccountName"].isJsonNull)
                order.accountName = jsonObject["AccountName"].asString

            if (jsonObject.has("BillingProfileID") && !jsonObject["BillingProfileID"].isJsonNull)
                order.billingProfileID = jsonObject["BillingProfileID"].asInt

            if (jsonObject.has("ChairPad") && !jsonObject["ChairPad"].isJsonNull)
                order.chairPad = jsonObject["ChairPad"].asString

            if (jsonObject.has("ChairPadPrice") && !jsonObject["ChairPadPrice"].isJsonNull)
                order.chairPadPrice = jsonObject["ChairPadPrice"].asFloat

            if (jsonObject.has("CompanyID") && !jsonObject["CompanyID"].isJsonNull)
                order.companyID = jsonObject["CompanyID"].asInt

            if (jsonObject.has("Compor") && !jsonObject["Compor"].isJsonNull)
                order.compor = jsonObject["Compor"].asString

            if (jsonObject.has("CustomerID") && !jsonObject["CustomerID"].isJsonNull)
                order.customerID = jsonObject["CustomerID"].asInt

            if (jsonObject.has("CustomerPickupLocation") && !jsonObject["CustomerPickupLocation"].isJsonNull)
                order.customerPickupLocation = jsonObject["CustomerPickupLocation"].asString

            if (jsonObject.has("CustomerPickupLocationID") && !jsonObject["CustomerPickupLocationID"].isJsonNull)
                order.customerPickupLocationID = jsonObject["CustomerPickupLocationID"].asString

            if (jsonObject.has("DefaultAccessoryType") && !jsonObject["DefaultAccessoryType"].isJsonNull)
                order.defaultAccessoryType = jsonObject["DefaultAccessoryType"].asInt

            if (jsonObject.has("DefaultStateType") && !jsonObject["DefaultStateType"].isJsonNull)
                order.defaultStateType = jsonObject["DefaultStateType"].asInt

            if (jsonObject.has("DevicePropertyIDs") && !jsonObject["DevicePropertyIDs"].isJsonNull)
                order.devicePropertyIDs = jsonObject["DevicePropertyIDs"].asString

            if (jsonObject.has("DevicePropertyOption") && !jsonObject["DevicePropertyOption"].isJsonNull)
                order.devicePropertyOption = jsonObject["DevicePropertyOption"].asString

            if (jsonObject.has("DevicePropertyOptionID") && !jsonObject["DevicePropertyOptionID"].isJsonNull)
                order.devicePropertyOptionID = jsonObject["DevicePropertyOptionID"].asInt

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                order.deviceTypeID = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("DeviceTypeName") && !jsonObject["DeviceTypeName"].isJsonNull)
                order.deviceTypeName = jsonObject["DeviceTypeName"].asString

            if (jsonObject.has("FirstName") && !jsonObject["FirstName"].isJsonNull)
                order.firstName = jsonObject["FirstName"].asString

            if (jsonObject.has("HandController") && !jsonObject["HandController"].isJsonNull)
                order.handController = jsonObject["HandController"].asString

            if (jsonObject.has("InventoryID") && !jsonObject["InventoryID"].isJsonNull)
                order.inventoryID = jsonObject["InventoryID"].asInt

            if (jsonObject.has("IsChairPadOptionAvailable") && !jsonObject["IsChairPadOptionAvailable"].isJsonNull)
                order.isChairPadOptionAvailable = jsonObject["IsChairPadOptionAvailable"].asBoolean

            if (jsonObject.has("IsCustomerPickupLocation") && !jsonObject["IsCustomerPickupLocation"].isJsonNull)
                order.isCustomerPickupLocation = jsonObject["IsCustomerPickupLocation"].asBoolean

            if (jsonObject.has("IsCustomerSecondOrder") && !jsonObject["IsCustomerSecondOrder"].isJsonNull)
                order.isCustomerSecondOrder = jsonObject["IsCustomerSecondOrder"].asBoolean

            if (jsonObject.has("IsExistingOperator") && !jsonObject["IsExistingOperator"].isJsonNull)
                order.isExistingOperator = jsonObject["IsExistingOperator"].asString

            if (jsonObject.has("IsPickupInstructionAvailable") && !jsonObject["IsPickupInstructionAvailable"].isJsonNull)
                order.isPickupInstructionAvailable =
                    jsonObject["IsPickupInstructionAvailable"].asBoolean

            if (jsonObject.has("IsShippingAddress") && !jsonObject["IsShippingAddress"].isJsonNull)
                order.isShippingAddress = jsonObject["IsShippingAddress"].asBoolean

            if (jsonObject.has("ItemFullDescription") && !jsonObject["ItemFullDescription"].isJsonNull)
                order.itemFullDescription = jsonObject["ItemFullDescription"].asString

            if (jsonObject.has("ItemImagePath") && !jsonObject["ItemImagePath"].isJsonNull)
                order.itemImagePath = jsonObject["ItemImagePath"].asString

            if (jsonObject.has("ItemName") && !jsonObject["ItemName"].isJsonNull)
                order.itemName = jsonObject["ItemName"].asString

            if (jsonObject.has("ItemShortDescription") && !jsonObject["ItemShortDescription"].isJsonNull)
                order.itemShortDescription = jsonObject["ItemShortDescription"].asString

            if (jsonObject.has("JoyStickPosition") && !jsonObject["JoyStickPosition"].isJsonNull)
                order.joyStickPosition = jsonObject["JoyStickPosition"].asString

            if (jsonObject.has("LastName") && !jsonObject["LastName"].isJsonNull)
                order.lastName = jsonObject["LastName"].asString

            if (jsonObject.has("LocationID") && !jsonObject["LocationID"].isJsonNull)
                order.locationID = jsonObject["LocationID"].asInt

            if (jsonObject.has("Message") && !jsonObject["Message"].isJsonNull)
                order.message = jsonObject["Message"].asString

            if (jsonObject.has("MiddleName") && !jsonObject["MiddleName"].isJsonNull)
                order.middleName = jsonObject["MiddleName"].asString

            if (jsonObject.has("ModelOfItem") && !jsonObject["ModelOfItem"].isJsonNull)
                order.modelOfItem = jsonObject["ModelOfItem"].asString

            if (jsonObject.has("OccupantID") && !jsonObject["OccupantID"].isJsonNull)
                order.occupantID = jsonObject["OccupantID"].asInt

            if (jsonObject.has("OperatorHeightFeet") && !jsonObject["OperatorHeightFeet"].isJsonNull)
                order.operatorHeightFeet = jsonObject["OperatorHeightFeet"].asInt

            if (jsonObject.has("OperatorHeightInch") && !jsonObject["OperatorHeightInch"].isJsonNull)
                order.operatorHeightInch = jsonObject["OperatorHeightInch"].asInt

            if (jsonObject.has("OperatorID") && !jsonObject["OperatorID"].isJsonNull)
                order.operatorID = jsonObject["OperatorID"].asInt

            if (jsonObject.has("OperatorNote") && !jsonObject["OperatorNote"].isJsonNull)
                order.operatorNote = jsonObject["OperatorNote"].asString

            if (jsonObject.has("OperatorOccupantID") && !jsonObject["OperatorOccupantID"].isJsonNull)
                order.operatorOccupantID = jsonObject["OperatorOccupantID"].asString

            if (jsonObject.has("OperatorWeight") && !jsonObject["OperatorWeight"].isJsonNull)
                order.operatorWeight = jsonObject["OperatorWeight"].asString

            if (jsonObject.has("PickUpDate") && !jsonObject["PickUpDate"].isJsonNull)
                order.pickUpDate = jsonObject["PickUpDate"].asString

            if (jsonObject.has("PickUpTime") && !jsonObject["PickUpTime"].isJsonNull)
                order.pickUpTime = jsonObject["PickUpTime"].asString

            if (jsonObject.has("PickupInstructionContent") && !jsonObject["PickupInstructionContent"].isJsonNull)
                order.pickupInstructionContent = jsonObject["PickupInstructionContent"].asString

            if (jsonObject.has("PreferredWheelchairSize") && !jsonObject["PreferredWheelchairSize"].isJsonNull)
                order.preferredWheelchairSize = jsonObject["PreferredWheelchairSize"].asString

            if (jsonObject.has("Price") && !jsonObject["Price"].isJsonNull)
                order.price = jsonObject["Price"].asFloat

            if (jsonObject.has("RentalPeriod") && !jsonObject["RentalPeriod"].isJsonNull)
                order.rentalPeriod = jsonObject["RentalPeriod"].asString

            if (jsonObject.has("ReturnDate") && !jsonObject["ReturnDate"].isJsonNull)
                order.returnDate = jsonObject["ReturnDate"].asString

            if (jsonObject.has("ReturnTime") && !jsonObject["ReturnTime"].isJsonNull)
                order.returnTime = jsonObject["ReturnTime"].asString

            if (jsonObject.has("RewardPoint") && !jsonObject["RewardPoint"].isJsonNull)
                order.rewardPoint = jsonObject["RewardPoint"].asFloat

            if (jsonObject.has("ShippingAddressLine1") && !jsonObject["ShippingAddressLine1"].isJsonNull)
                order.shippingAddressLine1 = jsonObject["ShippingAddressLine1"].asString

            if (jsonObject.has("ShippingAddressLine2") && !jsonObject["ShippingAddressLine2"].isJsonNull)
                order.shippingAddressLine2 = jsonObject["ShippingAddressLine2"].asString

            if (jsonObject.has("ShippingCity") && !jsonObject["ShippingCity"].isJsonNull)
                order.shippingCity = jsonObject["ShippingCity"].asString

            if (jsonObject.has("ShippingDeliveryNote") && !jsonObject["ShippingDeliveryNote"].isJsonNull)
                order.shippingDeliveryNote = jsonObject["ShippingDeliveryNote"].asString


            if (jsonObject.has("ShippingStateID") && !jsonObject["ShippingStateID"].isJsonNull)
                order.shippingStateID = jsonObject["ShippingStateID"].asInt

            if (jsonObject.has("ShippingStateName") && !jsonObject["ShippingStateName"].isJsonNull)
                order.shippingStateName = jsonObject["ShippingStateName"].asString

            if (jsonObject.has("ShippingZipcode") && !jsonObject["ShippingZipcode"].isJsonNull)
                order.shippingZipcode = jsonObject["ShippingZipcode"].asString

            if (jsonObject.has("SlipNumber") && !jsonObject["SlipNumber"].isJsonNull)
                order.slipNumber = jsonObject["SlipNumber"].asString

            if (jsonObject.has("StateID") && !jsonObject["StateID"].isJsonNull)
                order.stateID = jsonObject["StateID"].asString

            if (jsonObject.has("chkSameAddress") && !jsonObject["chkSameAddress"].isJsonNull)
                order.chkSameAddress = jsonObject["chkSameAddress"].asBoolean

        }

        return order

    }
}