package com.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.data.DataRepository
import com.data.Injection
import com.data.model.device.Device
import com.data.model.device.DeviceInfo
import com.data.model.order.Order
import com.data.model.user.Config
import com.data.model.user.User
import com.data.objectforupdate.DialogUtils
import com.data.objectforupdate.UpdateAppContract
import com.data.objectforupdate.updateAppPresenter
import com.data.remote.request.order.SaveOrderRq
import com.data.remote.response.order.SaveOrderListRs
import com.data.remote.response.user.ValidateLicenseRs
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ActMowHomeBinding
import com.ui.drawer.DrawerView
import com.ui.drawer.Menu
import com.ui.drawer.SubMenu
import com.ui.drawer.SubMenuEnum
import com.ui.help.HelpActivity
import com.ui.home.activeOrder.ActiveOrdersFragment
import com.ui.home.addCreditDebitCard.AddCreditDebitCardFragment
import com.ui.home.addCustomerECheck.AddCustomerECheckFragment
import com.ui.home.addToReservation.AddToReservationFragment
import com.ui.home.billingAddress.BillingAddressFragment
import com.ui.home.checkOut.CheckOutFragment
import com.ui.home.checkOut.CreditCardIssuerAgreementDialogFragment
import com.ui.home.contentDialog.ContentDialogFragment
import com.ui.home.destination.DestinationFragment
import com.ui.home.deviceDetail.DeviceDetailFragment
import com.ui.home.deviceList.DeviceListFragment
import com.ui.home.extendOrder.ExtendOrderFragment
import com.ui.home.help.WebFragment
import com.ui.home.manageOccupant.ManageOccupantFragment
import com.ui.home.notification.NotificationFragment
import com.ui.home.orderHistory.OrderHistoryFragment
import com.ui.home.orderHistory.RiderRewardPointDialogFragment
import com.ui.home.orderView.OrderViewFragment
import com.ui.home.profile.MyProfileFragment
import com.ui.home.reportBug.ReportBugActivity
import com.ui.home.reservedItems.ReservedItemFragment
import com.ui.home.returnRentalDevice.ReturnRentalDeviceFragment
import com.ui.home.shopOnline.ShopOnlineFragment
import com.ui.location.SelectDestinationActivity
import com.ui.login.LoginActivity
import com.ui.main.chats.OrderStatusFragment
import com.ui.productDetails.EnterEquipmentDialogFragment
import iCode.android.BaseActivity
import libraries.image.helper.models.MediaResult

class HomeActivity : BaseActivity(), HomeActivityContract.View  , UpdateAppContract.View{


    private lateinit var binding: ActMowHomeBinding

    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: HomeActivityPresenter
    private lateinit var presenter_updateApp: UpdateAppContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AppVersion", "Version Name: on create of home activity ${BuildConfig.VERSION_NAME}")
        binding = bindContentView(R.layout.act_mow_home)

        dataRepository = Injection.provideDataRepository()
        presenter = HomeActivityPresenter(this, dataRepository)
        presenter_updateApp = updateAppPresenter(this,dataRepository)


        if (savedInstanceState == null) {
            presenter.validateLicense()
        }

        binding.drawerViewMenu.setInterface(object :
            DrawerView.IDrawerOption {

            override fun onMenuClicked(menu: Menu?) {

                toggleDrawer()
            }

            override fun onSubMenuClicked(subMenu: SubMenu?) {
                toggleDrawer()

                val   marketing_site_url = "https://www.mobilityonwheels.com/"
                Log.e("Nilam","onSubMenuClicked ----->");

                when (subMenu?.subMenuEnum) {
                    SubMenuEnum.PLACE_AN_ORDER -> {
                        showHomeFragment(true)
                    }
                    SubMenuEnum.ACTIVE_ORDERS -> {
                        showActiveOrderFragment()
                    }
                    SubMenuEnum.ORDER_HISTORY -> {
                        showOrderHiStoryFragment()
                    }
                    SubMenuEnum.MY_PROFILE -> {
                        showMyProfileFragment()
                    }
                    SubMenuEnum.NOTIFICATIONS -> {
                        showNotificationsFragment()
                    }
                    SubMenuEnum.RIDER_REWARDS -> {
                        presenter.getRiderRewardsPoint()
                    }
                    SubMenuEnum.DEACTIVATE_MY_ACCOUNT -> {
                        showWebFragment(marketing_site_url+"deactivate-my-account/")

                    }
                    SubMenuEnum.SHOP_ONLINE -> {

                        showShopOnlineFragment()

                        //   val humanObj = GsonInterface.getInstance().gson.fromJson<Human>("{name:\"JonathanSmith\",children:[{name:\"Adam\",children:[{name:\"Suzy\",children:[],},{name:\"Clare\",children:[],},{name:\"Aaron\",children:[],},{name:\"Simon\",children:[],}]},{name:\"Timmy\",children:[],},{name:\"Alison\",children:[{name:\"Natasha\",children:[],},{name:\"Zak\",children:[],}]}]}", Human::class.java)
                        //   Log.i("HUMTEST","Obj: "+humanObj.toString())
                        /*val getAppConfigurationRs: GetAppConfigurationRs =
                            GsonInterface.getInstance().gson.fromJson<GetAppConfigurationRs>(
                                response,
                                GetAppConfigurationRs::class.java
                            )*/
                    }
                    SubMenuEnum.FAQS -> {
                        // showWebFragment("https://devwp.mobilityonwheels.com/faq/")
                        showWebFragment(marketing_site_url+"faq/")

                    }
                    SubMenuEnum.GIVE_FEEDBACK -> {
                        // showWebFragment("https://devwp.mobilityonwheels.com/feedback/")
                        showWebFragment(marketing_site_url+"contact-us/#feedback")
                    }
                    SubMenuEnum.PRIVACY_AND_SECURITY_STATEMENT -> {
                        // showWebFragment("https://devwp.mobilityonwheels.com/privacy-and-security-statement/")
                        showWebFragment(marketing_site_url+"privacy-and-security-statement/")

                    }
                    SubMenuEnum.DO_NOT_SELL_MY_PERSONAL_INFORMATION -> {
                        // showWebFragment("https://devwp.mobilityonwheels.com/do-not-sell-my-personal-information/")
                        showWebFragment(marketing_site_url+"do-not-sell-my-personal-information/")
                    }
                    SubMenuEnum.TERMS_OF_USE -> {
                        // showWebFragment("https://devwp.mobilityonwheels.com/terms-of-use/")
                        showWebFragment(marketing_site_url+"terms-of-use/")

                    }
                    SubMenuEnum.ABOUT -> {
                        showWebFragment(marketing_site_url+"about-us/")
                    }
                    SubMenuEnum.CONTACT_CUSTOMER_SERVICE -> {
                        showWebFragment(marketing_site_url+"contact-us/")
                    }
                    else -> {
                    }
                }
            }

            override fun onSignOutClicked() {
                toggleDrawer()
                logoutDialog()
            }
            override fun onReportBugClicked() {
                toggleDrawer()
                showReportBugFragment(this@HomeActivity)
            }

        })



        presenter.addUpdateFCMToken()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        Log.e("AppVersion", "HomeActivity onCreateView called")

    }

    fun showHomeFragment(isShowDestinationList: Boolean) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, DeviceListFragment.newInstance(isShowDestinationList))
            .commit()


    }

    fun showActiveOrderFragment() {

        //homeActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, ActiveOrdersFragment.newInstance())
            .commitAllowingStateLoss() // Prevents crash if the transaction is late
//            .commit()
    }

    fun showOrderHiStoryFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, OrderHistoryFragment.newInstance())
            .commit()
    }

    fun showMyProfileFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, MyProfileFragment.newInstance())
            .commit()
    }


    fun showReportBugFragment(context: Context) {
        val intent = Intent(context, ReportBugActivity::class.java)
        context.startActivity(intent)
    }

    fun showNotificationsFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, NotificationFragment.newInstance())
            .commit()
    }

    fun showShopOnlineFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, ShopOnlineFragment.newInstance())
            .commitNow()

    }

    fun showBillingAddressFragment() {

        val fragment = BillingAddressFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack("BillingAddressFragment")
            .replace(R.id.fragment_content, fragment)
            .commit()
    }

    fun showProductDetailsFragment(
        targetFragment: Fragment,
        requestCode: Int,
        deviceId: Int,
        device: Device,
    ) {

        val fragment = DeviceDetailFragment.newInstance(deviceId, device)
        fragment.setTargetFragment(targetFragment, requestCode)
        supportFragmentManager.beginTransaction()
            .addToBackStack("ProductDetailsFragment")
            .replace(R.id.fragment_content, fragment)
            .commit()

    }

    fun showAddToReservation(
        targetFragment: Fragment?,
        requestCode: Int,
        device: Device,
        deviceInfo: DeviceInfo,
    ) {

        val saveOrderRq = SaveOrderRq()

        saveOrderRq.compPriceDescription = device.compPriceDescription
        saveOrderRq.deviceTypeID = device.deviceTypeId
        saveOrderRq.inventoryID = device.inventoryId
        saveOrderRq.itemImagePath = device.itemImagePath
        saveOrderRq.itemName = device.itemName
        saveOrderRq.regularPriceDescription = device.regularPriceDescription

        saveOrderRq.chairPadPrice = deviceInfo.chairPadPrice
        saveOrderRq.description = deviceInfo.description
        saveOrderRq.devicePropertyIDs = deviceInfo.devicePropertyIDs
        saveOrderRq.deviceTypeName = deviceInfo.deviceTypeName
        saveOrderRq.deviceTypeShortName = deviceInfo.deviceTypeShortName
        saveOrderRq.deviceItemShortDescription = deviceInfo.itemShortDescription
        saveOrderRq.operatorOccupantSame = deviceInfo.operatorOccupantSame

        val fragment = AddToReservationFragment.newInstance(saveOrderRq)
        fragment.setTargetFragment(targetFragment, requestCode)

        supportFragmentManager.beginTransaction()
            .addToBackStack("AddToReservationFragment")
            .replace(
                R.id.fragment_content,
                fragment
            )
            .commit()
    }

    fun showAddToReservation(saveOrderRq: SaveOrderRq) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("AddToReservationFragment")
            .replace(
                R.id.fragment_content,
                AddToReservationFragment.newInstance(saveOrderRq)
            )
            .commit()
    }



    fun showReservedItemFragment(isOpenFromCheckOut: Boolean) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("ReservedItemFragment")
            .replace(R.id.fragment_content, ReservedItemFragment.newInstance(isOpenFromCheckOut))
            .commit()
    }

    fun showCheckOutFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("CheckOutFragment")
            .replace(R.id.fragment_content, CheckOutFragment.newInstance())
            .commit()
    }

    fun showAddCreditDebitCardFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("AddCreditDebitCardFragment")
            .replace(R.id.fragment_content, AddCreditDebitCardFragment.newInstance())
            .commit()


    }
    // Nilam open Add Customer E-Check page
    fun showAddCustomerECheckFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("AddCustomerECheckFragment")
            .replace(R.id.fragment_content, AddCustomerECheckFragment.newInstance())
            .commit()
    }

    fun showReturnRentalDeviceFragment(orderId: Int, mediaResult: MediaResult) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("ReturnRentalDeviceFragment")
            .replace(
                R.id.fragment_content, ReturnRentalDeviceFragment.newInstance(
                    orderId,
                    mediaResult
                )
            )
            .commit()
    }

    fun showOrderViewFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("OrderViewFragment")
            .replace(R.id.fragment_content, OrderViewFragment.newInstance())
            .commit()
    }

    fun showCreditCardIssuerAggreementDialogFragment() {

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            CreditCardIssuerAgreementDialogFragment.newInstance(),
            "CreditCardIssuerAgreementDialogFragment"
        )
        transaction.commitAllowingStateLoss()
    }

    fun showRiderRewardPointDialogFragment(riderRewardPoint: Float) {


        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            RiderRewardPointDialogFragment.newInstance(riderRewardPoint),
            "RiderRewardPointDialogFragment"
        )
        transaction.commitAllowingStateLoss()
    }

    fun showEnterEquipmentDialogFragment(orderId: Int, fragment: Fragment, requestCode: Int) {
        val enterEquipmentDialogFragment =
            EnterEquipmentDialogFragment.newInstance(orderId)

        enterEquipmentDialogFragment.setTargetFragment(fragment,requestCode)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            enterEquipmentDialogFragment,
            "EnterEquipmentDialogFragment"
        )
        transaction.commitAllowingStateLoss()
    }

    fun showOrderStatusFragment(saveOrderList: SaveOrderListRs) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("OrderStatusFragment")
            .replace(R.id.fragment_content, OrderStatusFragment.newInstance(saveOrderList))
            .commit()

    }

    fun showExtendOrderFragment(order: Order) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("ExtendOrderFragment")
            .replace(R.id.fragment_content, ExtendOrderFragment.newInstance(order))
            .commit()

    }

    fun showManageOccupantFragment(operator: User) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("ManageOccupantFragment")
            .replace(R.id.fragment_content, ManageOccupantFragment.newInstance(operator))
            .commit()

    }

    fun showHelpActivity(context: Context) {
        val intent = Intent(context, HelpActivity::class.java)
        context.startActivity(intent)
    }

    fun showWebFragment(url: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("WebFragment")
            .replace(R.id.fragment_content, WebFragment.newInstance(url))
            .commit()

    }

    fun showDestinationActivity(context: Context) {
        val intent = Intent(context, SelectDestinationActivity::class.java)
        context.startActivity(intent)


    }

    fun showContentDialogFragment(content: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            ContentDialogFragment.newInstance(content),
            "ContentDialogFragment"
        )
        transaction.commitAllowingStateLoss()
    }


    fun showDestinationListFragment(targetFragment: Fragment, requestCode: Int) {

        val destinationFragment = DestinationFragment.newInstance()
        destinationFragment.setTargetFragment(targetFragment, requestCode)

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            destinationFragment,
            "DestinationFragment"
        )
        transaction.commitAllowingStateLoss()
    }

    fun toggleDrawer() {
        Log.e("Nilam","toggleDrawer ----->"+Gravity.LEFT);
        // nilam add below line for force update app
        presenter_updateApp.updateAppConfig()

        if (!binding.drawer.isDrawerOpen(Gravity.LEFT)) {
            binding.drawer.openDrawer(Gravity.LEFT)
        } else {
            binding.drawer.closeDrawer(Gravity.LEFT)
        }
    }


    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this).setTitle(getString(R.string.alert))
            .setMessage(getString(R.string.alert_are_you_sure_you_want_to_signout))
        builder.apply {
            setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                val dataRepository = Injection.provideDataRepository()
                dataRepository.expiredToken()
                finish()
                LoginActivity.start(this@HomeActivity)
            }
            setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->

            }
        }
        builder.show()
    }



    override fun onBackPressed() {

        when(supportFragmentManager.findFragmentById(R.id.fragment_content)) {
            is OrderStatusFragment -> {
                supportFragmentManager.popBackStackImmediate(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
            is OrderViewFragment -> {
                supportFragmentManager.popBackStackImmediate(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
            is DestinationFragment -> {
                if (Injection.provideDataRepository().locationId != 0)
                    super.onBackPressed()
                else
                    Toast.makeText(
                        this,
                        getString(R.string.validation_please_select_a_destination),
                        Toast.LENGTH_LONG
                    ).show()
            }
            else -> {
                super.onBackPressed()
            }
        }

    }

    override fun showRiderRewardsPoint(riderRewardPoint: Float) {
        showRiderRewardPointDialogFragment(riderRewardPoint)
    }

    override fun getRiderRewardsPointFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showValidateLicense(validateLicenseRs: ValidateLicenseRs) {


        with(validateLicenseRs) {
            if(!isValidLicenseForPayor || notValidLicenseForOperator.isNotEmpty() || !isLicenseAvailableForPayor || lstLicenseNotAvailableForOperator.isNotEmpty()) {
                showMyProfileFragment()
            } else {
                if(isActiveOrderAval) {
                    showActiveOrderFragment()
                } else {
                    showHomeFragment(true)
                }
            }
        }
    }

    override fun getValidateLicenseFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun getAppConfigSuccessfully(config: Config?) {
        config?.let {

            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionNameFirst = (packageInfo.versionName)?.toFloat()
            val versionName = BuildConfig.VERSION_NAME
            Log.d("AppVersion", "Version Name: homeactivity $versionName")

            if(it.minimumAndroidVersion <= versionNameFirst!!) {

            }
            else {
                //    DialogUtils.showAlertDialog(context = requireActivity(),)
                DialogUtils.showAlertDialog(context = this, activity = this)
                if (!binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawer.openDrawer(Gravity.LEFT)
                } else {
                    binding.drawer.closeDrawer(Gravity.LEFT)
                }

            }
        }
    }

    override fun getAppConfigFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }


    companion object {

        fun start(context: Context) =
            context.startActivity(Intent(context, HomeActivity::class.java))
    }
}