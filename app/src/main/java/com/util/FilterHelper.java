package com.util;

import java.util.ArrayList;
import java.util.HashMap;

import iCode.utility.PrefHelper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FilterHelper {

    private static FilterHelper filterHelper = null;


    private static final String KEY_INCIDENT_FILTER_STATUS = "KEY_INCIDENT_FILTER_STATUS";
    private static final String KEY_INCIDENT_FILTER_ASSIGNED_TO = "KEY_INCIDENT_FILTER_ASSIGNED_TO";
    private static final String KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID = "KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID";
    private static final String KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID = "KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID";
    private static final String KEY_INCIDENT_FILTER_DATE = "KEY_INCIDENT_FILTER_DATE";

    private static final String KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID = "KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID";
    private static final String KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID = "KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID";
    private static final String KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID = "KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID";


    private static final String KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID = "KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID";
    private static final String KEY_GUEST_CARD_FILTER_SOURCE = "KEY_GUEST_CARD_FILTER_SOURCE";

    private static final String KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID = "KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID";
    private static final String KEY_RENTAL_APPLICATION_FILTER_STATUS = "KEY_RENTAL_APPLICATION_FILTER_STATUS";

    private static final String KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID = "KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID";

    public static FilterHelper getInstance() {
        if(filterHelper == null)
            filterHelper = new FilterHelper();

        return filterHelper;
    }

    public void saveIncidentFilter(String status, String assigned_to,
                                   String property_group_id,
                                   String property_type_id, String date) {


        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_STATUS,status);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_ASSIGNED_TO,assigned_to);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID,property_group_id);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID,property_type_id);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_DATE,date);

    }

    public String getIncidentStatusFilter() {
        return PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_STATUS, "");
    }

    public String getIncidentPropertyTypeFilter() {
        return PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID, "");
    }

    public String getIncidentAssignedToFilter() {
        return PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_ASSIGNED_TO, "");
    }

    public String getIncidentDateFilter() {
        return PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_DATE, "");
    }

    public ArrayList<String> getIncidentPropertyGroupIdFilter() {

        String propertyGroupIds = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID, "");
        ArrayList<String> arrayOfPropertyGroupIds = new ArrayList<>();
        if(propertyGroupIds!=null && !propertyGroupIds.isEmpty()) {
            String[] splitPropertyGroupIds = propertyGroupIds.split(",");

            if(splitPropertyGroupIds.length>=1) {

                for(int index=0;index<splitPropertyGroupIds.length;index++) {
                    arrayOfPropertyGroupIds.add(splitPropertyGroupIds[index]);
                }

            }
        }

        return arrayOfPropertyGroupIds;
    }

    public HashMap<String, RequestBody> getIncidentFilterForOnlySummary() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        return filterMap;

    }

    public HashMap<String, RequestBody> getIncidentFilter() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentStatus = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_STATUS, null);
        if(incidentStatus!=null && !incidentStatus.isEmpty()) {
            filterMap.put("status",getRequestBody(incidentStatus));
        }

        String incidentAssignTo = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_ASSIGNED_TO, null);
        if(incidentAssignTo!=null && !incidentAssignTo.isEmpty()) {
            filterMap.put("assigned_to",getRequestBody(incidentAssignTo));
        }

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        String incidentPropertyTypeId = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID, null);
        if(incidentPropertyTypeId!=null && !incidentPropertyTypeId.isEmpty()) {
            filterMap.put("property_type_id",getRequestBody(incidentPropertyTypeId));
        }

        String incidentDate = PrefHelper.getInstance().getString(KEY_INCIDENT_FILTER_DATE, null);
        if(incidentDate!=null && !incidentDate.isEmpty()) {
            filterMap.put("date",getRequestBody(incidentDate));
        }

        return filterMap;

    }


    public void savePropertyFilter(
                                   String property_group_id,
                                   String property_type_id,
                                   String property_status_id) {


        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID,property_group_id);
        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID,property_type_id);
        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID,property_status_id);


    }


    public String getPropertyFilterPropertyTypeId() {
        return PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID, "");
    }

    public String getPropertyFilterPropertyStatusId() {
        return PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID, "");
    }

    public ArrayList<String> getPropertyFilterPropertyGroupId() {

        String propertyGroupIds = PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID, "");
        ArrayList<String> arrayOfPropertyGroupIds = new ArrayList<>();
        if(propertyGroupIds!=null && !propertyGroupIds.isEmpty()) {
            String[] splitPropertyGroupIds = propertyGroupIds.split(",");

            if(splitPropertyGroupIds.length>=1) {

                for(int index=0;index<splitPropertyGroupIds.length;index++) {
                    arrayOfPropertyGroupIds.add(splitPropertyGroupIds[index]);
                }

            }
        }

        return arrayOfPropertyGroupIds;
    }

    public HashMap<String, RequestBody> getPropertyFilter() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        String incidentPropertyTypeId = PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID, null);
        if(incidentPropertyTypeId!=null && !incidentPropertyTypeId.isEmpty()) {
            filterMap.put("property_type_id",getRequestBody(incidentPropertyTypeId));
        }

        String incidentPropertyStatusId = PrefHelper.getInstance().getString(KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID, null);
        if(incidentPropertyStatusId!=null && !incidentPropertyStatusId.isEmpty()) {
            filterMap.put("unit_status",getRequestBody(incidentPropertyStatusId));
        }

        return filterMap;

    }

    public void saveGuestCardFilter(
            String property_group_id,
            String source) {


        PrefHelper.getInstance().putString(KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID,property_group_id);
        PrefHelper.getInstance().putString(KEY_GUEST_CARD_FILTER_SOURCE,source);


    }

    public String getGuestCardSourceFilter() {
        return PrefHelper.getInstance().getString(KEY_GUEST_CARD_FILTER_SOURCE, "");
    }

    public ArrayList<String> getGuestCardFilterPropertyGroupId() {

        String propertyGroupIds = PrefHelper.getInstance().getString(KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID, "");
        ArrayList<String> arrayOfPropertyGroupIds = new ArrayList<>();
        if(propertyGroupIds!=null && !propertyGroupIds.isEmpty()) {
            String[] splitPropertyGroupIds = propertyGroupIds.split(",");

            if(splitPropertyGroupIds.length>=1) {

                for(int index=0;index<splitPropertyGroupIds.length;index++) {
                    arrayOfPropertyGroupIds.add(splitPropertyGroupIds[index]);
                }

            }
        }

        return arrayOfPropertyGroupIds;
    }

    public HashMap<String, RequestBody> getGuestCardFilter() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        String incidentPropertyTypeId = PrefHelper.getInstance().getString(KEY_GUEST_CARD_FILTER_SOURCE, null);
        if(incidentPropertyTypeId!=null && !incidentPropertyTypeId.isEmpty()) {
            filterMap.put("source",getRequestBody(incidentPropertyTypeId));
        }

        return filterMap;

    }

    public void saveRentalApplicationFilter(
            String property_group_id,
            String status) {


        PrefHelper.getInstance().putString(KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID,property_group_id);
        PrefHelper.getInstance().putString(KEY_RENTAL_APPLICATION_FILTER_STATUS,status);


    }

    public String getRentalApplicationStatusFilter() {
        return PrefHelper.getInstance().getString(KEY_RENTAL_APPLICATION_FILTER_STATUS, "");
    }

    public ArrayList<String> getRentalApplicationFilterPropertyGroupId() {

        String propertyGroupIds = PrefHelper.getInstance().getString(KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID, "");
        ArrayList<String> arrayOfPropertyGroupIds = new ArrayList<>();
        if(propertyGroupIds!=null && !propertyGroupIds.isEmpty()) {
            String[] splitPropertyGroupIds = propertyGroupIds.split(",");

            if(splitPropertyGroupIds.length>=1) {

                for(int index=0;index<splitPropertyGroupIds.length;index++) {
                    arrayOfPropertyGroupIds.add(splitPropertyGroupIds[index]);
                }

            }
        }

        return arrayOfPropertyGroupIds;
    }

    public HashMap<String, RequestBody> getRentalApplicationFilter() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        String incidentPropertyTypeId = PrefHelper.getInstance().getString(KEY_RENTAL_APPLICATION_FILTER_STATUS, null);
        if(incidentPropertyTypeId!=null && !incidentPropertyTypeId.isEmpty()) {
            filterMap.put("unit_status",getRequestBody(incidentPropertyTypeId));
        }

        return filterMap;

    }

    public void saveDashboardFilter(String property_group_id) {
        PrefHelper.getInstance().putString(KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID,property_group_id);
    }



    public ArrayList<String> getDashboardFilterPropertyGroupId() {

        String propertyGroupIds = PrefHelper.getInstance().getString(KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID, "");
        ArrayList<String> arrayOfPropertyGroupIds = new ArrayList<>();
        if(propertyGroupIds!=null && !propertyGroupIds.isEmpty()) {
            String[] splitPropertyGroupIds = propertyGroupIds.split(",");

            if(splitPropertyGroupIds.length>=1) {

                for(int index=0;index<splitPropertyGroupIds.length;index++) {
                    arrayOfPropertyGroupIds.add(splitPropertyGroupIds[index]);
                }

            }
        }

        return arrayOfPropertyGroupIds;
    }

    public HashMap<String, RequestBody> getDashboardFilter() {
        HashMap<String, RequestBody> filterMap = new HashMap<>();

        String incidentPropertyGroupId = PrefHelper.getInstance().getString(KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID, null);
        if(incidentPropertyGroupId!=null && !incidentPropertyGroupId.isEmpty()) {
            filterMap.put("property_group_id",getRequestBody(incidentPropertyGroupId));
        }

        return filterMap;

    }
    private RequestBody getRequestBody(String value) {
        RequestBody description =
                RequestBody.create(
                        MultipartBody.FORM, value);
        return description;
    }

    public void clearData() {

        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_STATUS,null);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_ASSIGNED_TO,null);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_PROPERTY_GROUP_ID,null);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_PROPERTY_TYPE_ID,null);
        PrefHelper.getInstance().putString(KEY_INCIDENT_FILTER_DATE,null);
        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_GROUP_ID,null);
        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_TYPE_ID,null);
        PrefHelper.getInstance().putString(KEY_PROPERTY_FILTER_PROPERTY_STATUS_ID,null);
        PrefHelper.getInstance().putString(KEY_GUEST_CARD_FILTER_PROPERTY_GROUP_ID,null);
        PrefHelper.getInstance().putString(KEY_GUEST_CARD_FILTER_SOURCE,null);
        PrefHelper.getInstance().putString(KEY_RENTAL_APPLICATION_FILTER_PROPERTY_GROUP_ID,null);
        PrefHelper.getInstance().putString(KEY_RENTAL_APPLICATION_FILTER_STATUS,null);
        PrefHelper.getInstance().putString(KEY_DASHBOARD_FILTER_PROPERTY_GROUP_ID,null);

    }
}
