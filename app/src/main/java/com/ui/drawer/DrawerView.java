package com.ui.drawer;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mow.cash.android.BuildConfig;
import com.mow.cash.android.R;
import com.data.DataRepository;
import com.data.Injection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DrawerView extends RelativeLayout {

    private RecyclerView rvMenu;
    private TextView textViewName;
    private TextView textViewReportBug;
    private TextView textViewSignOut;
    private TextView textViewFirstChar;
    private TextView textViewVersionName;
    // private ImageView imageViewProfilePic;
    private IDrawerOption iDrawerOption;

    private MenuAdapter menuAdapter;


    public interface IDrawerOption {
        void onMenuClicked(Menu menu);

        void onSubMenuClicked(SubMenu subMenu);

        void onSignOutClicked();
        void onReportBugClicked();
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setLayoutTransition(new LayoutTransition());
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_mow_drawer, this, true);
        rvMenu = findViewById(R.id.recycleViewMenuOptions);
        textViewName = findViewById(R.id.textViewName);
        textViewReportBug = findViewById(R.id.textViewReportBug);
        textViewSignOut = findViewById(R.id.textViewSignOut);
        textViewFirstChar = findViewById(R.id.textViewFirstChar);
        textViewVersionName = findViewById(R.id.textViewVersionName);
        //  textViewSignOut.setOnClickListener(this);

        //  imageViewProfilePic = findViewById(R.id.imageViewProfilePic);


        menuAdapter = new MenuAdapter();

        rvMenu.setAdapter(menuAdapter);

        attachEventListeners();

        getMenuList();

        setProfileInfo();
    }

    public void setProfileInfo() {
        String versionName = BuildConfig.VERSION_NAME;


        Log.e("AppVersion",versionName );

        DataRepository dataRepository = Injection.provideDataRepository();
        textViewName.setText(dataRepository.getUser().getFirstName() + " " + dataRepository.getUser().getLastName());

        if (dataRepository.getUser() != null && dataRepository.getUser().getFirstName() != null && !dataRepository.getUser().getFirstName().isEmpty()) {
            char ch1 = dataRepository.getUser().getFirstName().charAt(0);
            textViewFirstChar.setText("" + ch1);
        }

        textViewVersionName.setText(getContext().getString(R.string.menu_version_number)+":" +BuildConfig.VERSION_NAME);
        //  textViewName.setText(dataRepository.getFirstName() + " " + dataRepository.getLastName());

       /* if (dataRepository.getProfilePic() != null && !dataRepository.getProfilePic().isEmpty()) {

            Picasso.get().load(dataRepository.getProfilePic())
                    .placeholder(R.drawable.ic_default_user_profile_pic)
                    .transform(new CropCircleTransformation())
                    .into(imageViewProfilePic);

        } else {
            Picasso.get().load(R.drawable.ic_default_user_profile_pic)
                    .into(imageViewProfilePic);
        }*/
    }

    public void setInterface(IDrawerOption iDrawerOption) {
        this.iDrawerOption = iDrawerOption;
    }

    private void attachEventListeners() {
        menuAdapter.setIOption(new MenuAdapter.IOption() {

            @Override
            public void onSubMenuClicked(@NonNull SubMenu subMenu) {
                iDrawerOption.onSubMenuClicked(subMenu);
            }

            @Override
            public void onMenuClicked(@NonNull Menu menu) {
                iDrawerOption.onMenuClicked(menu);
            }
        });

        textViewSignOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iDrawerOption.onSignOutClicked();
            }
        });

        textViewReportBug.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iDrawerOption.onReportBugClicked();
            }
        });


    }


    private void getMenuList() {

        List<Menu> menus = new ArrayList<>();


        Menu orderMenu = new Menu();
        orderMenu.setName(getContext().getString(R.string.menu_order));
        orderMenu.setResId(R.drawable.ic_mow_menu_order);

        List<SubMenu> orderSubMenus = new ArrayList<>();
        SubMenu placeAnOrderSubMenu = new SubMenu();
        placeAnOrderSubMenu.setSubMenuEnum(SubMenuEnum.PLACE_AN_ORDER);
        placeAnOrderSubMenu.setName(getContext().getString(R.string.menu_place_an_order));

        SubMenu activeOrdersSubMenu = new SubMenu();
        activeOrdersSubMenu.setSubMenuEnum(SubMenuEnum.ACTIVE_ORDERS);
        activeOrdersSubMenu.setName(getContext().getString(R.string.menu_active_orders));

        SubMenu orderHistorySubMenu = new SubMenu();
        orderHistorySubMenu.setSubMenuEnum(SubMenuEnum.ORDER_HISTORY);
        orderHistorySubMenu.setName(getContext().getString(R.string.menu_order_history));


        orderSubMenus.add(placeAnOrderSubMenu);
        orderSubMenus.add(activeOrdersSubMenu);
        orderSubMenus.add(orderHistorySubMenu);

        orderMenu.setSubMenu(orderSubMenus);


        menus.add(orderMenu);


        Menu myAccountMenu = new Menu();
        myAccountMenu.setName(getContext().getString(R.string.menu_my_account));
        myAccountMenu.setResId(R.drawable.ic_mow_menu_order);


        List<SubMenu> myAccountSubMenus = new ArrayList<>();
        SubMenu myProfileSubMenu = new SubMenu();
        myProfileSubMenu.setSubMenuEnum(SubMenuEnum.MY_PROFILE);
        myProfileSubMenu.setName(getContext().getString(R.string.menu_my_profile));

        SubMenu notificationsSubMenu = new SubMenu();
        notificationsSubMenu.setSubMenuEnum(SubMenuEnum.NOTIFICATIONS);
        notificationsSubMenu.setName(getContext().getString(R.string.menu_notifications));

        SubMenu riderRewardsSubMenu = new SubMenu();
        riderRewardsSubMenu.setSubMenuEnum(SubMenuEnum.RIDER_REWARDS);
        riderRewardsSubMenu.setName(getContext().getString(R.string.menu_rider_rewards));

        SubMenu deactivatemyaccountSubMenu = new SubMenu();
        deactivatemyaccountSubMenu.setSubMenuEnum(SubMenuEnum.DEACTIVATE_MY_ACCOUNT);
        deactivatemyaccountSubMenu.setName(getContext().getString(R.string.menu_deactivate_my_account));


        myAccountSubMenus.add(myProfileSubMenu);
        myAccountSubMenus.add(notificationsSubMenu);
        myAccountSubMenus.add(riderRewardsSubMenu);
        myAccountSubMenus.add(deactivatemyaccountSubMenu);

        myAccountMenu.setSubMenu(myAccountSubMenus);


        menus.add(myAccountMenu);


        Menu retailMenu = new Menu();
        retailMenu.setName(getContext().getString(R.string.menu_retail));
        retailMenu.setResId(R.drawable.ic_mow_menu_order);

        List<SubMenu> retailSubMenus = new ArrayList<>();
        SubMenu shopOnlineSubMenu = new SubMenu();
        shopOnlineSubMenu.setSubMenuEnum(SubMenuEnum.SHOP_ONLINE);
        shopOnlineSubMenu.setName(getContext().getString(R.string.menu_shop_online));

        retailSubMenus.add(shopOnlineSubMenu);
        retailMenu.setSubMenu(retailSubMenus);
        menus.add(retailMenu);


        Menu informationMenu = new Menu();
        informationMenu.setName(getContext().getString(R.string.menu_information));
        informationMenu.setResId(R.drawable.ic_mow_menu_order);


        List<SubMenu> informationSubMenus = new ArrayList<>();

        SubMenu faqsSubMenu = new SubMenu();
        faqsSubMenu.setSubMenuEnum(SubMenuEnum.FAQS);
        faqsSubMenu.setName(getContext().getString(R.string.menu_faqs));

        SubMenu giveFeedbackSubMenu = new SubMenu();
        giveFeedbackSubMenu.setSubMenuEnum(SubMenuEnum.GIVE_FEEDBACK);
        giveFeedbackSubMenu.setName(getContext().getString(R.string.menu_give_feedback));

        SubMenu privacyAndSecurityStatementSubMenu = new SubMenu();
        privacyAndSecurityStatementSubMenu.setSubMenuEnum(SubMenuEnum.PRIVACY_AND_SECURITY_STATEMENT);
        privacyAndSecurityStatementSubMenu.setName(getContext().getString(R.string.menu_privacy_and_security_statement));

        SubMenu doNotSellMyPersonalInformationSubMenu = new SubMenu();
        doNotSellMyPersonalInformationSubMenu.setSubMenuEnum(SubMenuEnum.DO_NOT_SELL_MY_PERSONAL_INFORMATION);
        doNotSellMyPersonalInformationSubMenu.setName(getContext().getString(R.string.menu_do_not_sell_my_personal_information));

        SubMenu termsOfUseSubMenu = new SubMenu();
        termsOfUseSubMenu.setSubMenuEnum(SubMenuEnum.TERMS_OF_USE);
        termsOfUseSubMenu.setName(getContext().getString(R.string.menu_terms_of_use));

        SubMenu aboutSubMenu = new SubMenu();
        aboutSubMenu.setSubMenuEnum(SubMenuEnum.ABOUT);
        aboutSubMenu.setName(getContext().getString(R.string.menu_about));

        SubMenu contactCustomerServiceSubMenu = new SubMenu();
        contactCustomerServiceSubMenu.setSubMenuEnum(SubMenuEnum.CONTACT_CUSTOMER_SERVICE);
        contactCustomerServiceSubMenu.setName(getContext().getString(R.string.menu_contact_customer_service));


        informationSubMenus.add(faqsSubMenu);
        informationSubMenus.add(giveFeedbackSubMenu);
        informationSubMenus.add(privacyAndSecurityStatementSubMenu);
        informationSubMenus.add(doNotSellMyPersonalInformationSubMenu);
        informationSubMenus.add(termsOfUseSubMenu);
        informationSubMenus.add(aboutSubMenu);
        informationSubMenus.add(contactCustomerServiceSubMenu);

        informationMenu.setSubMenu(informationSubMenus);


        menus.add(informationMenu);


        menuAdapter.submitList(menus);


    }


}