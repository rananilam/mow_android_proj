package com.ui.productDetails;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.data.CallbackSubscriber;
import com.data.Injection;
import com.data.remote.request.order.AddDeviceInventoryIDRq;
import com.data.remote.response.user.AddDeviceInventoryIDRs;
import com.mow.cash.android.R;

import java.util.Objects;

import iCode.view.ProgressDialogFragment;
import libraries.retrofit.RestError;

public class EnterEquipmentDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String KEY_SWITCH_USER_LIST = "KEY_SWITCH_USER_LIST";

    private ImageView imageViewClose;
    private TextView textViewSave;
    private EditText editTextId;
    private AppCompatTextView textViewMessage;

    private ProgressDialogFragment progressDialogFragment = null;

    private int orderId;


    @Override
    public void onClick(View v) {
        if(v == imageViewClose){
            getDialog().dismiss();

        }else if(v == textViewSave){

            if(!editTextId.getText().toString().trim().isEmpty())
                callWebservice();

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.IncidentCommentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_mow_enter_equipment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpUI();
    }


    private void setUpUI()
    {

        imageViewClose = Objects.requireNonNull(getView()).findViewById(R.id.imageViewClose);
        textViewSave = Objects.requireNonNull(getView()).findViewById(R.id.textViewSave);
        editTextId = Objects.requireNonNull(getView()).findViewById(R.id.appCompatEditText);
        textViewMessage = Objects.requireNonNull(getView()).findViewById(R.id.textViewMessage);


        imageViewClose.setOnClickListener(this);
        textViewSave.setOnClickListener(this);

        assert getArguments() != null;

        orderId = getArguments().getInt("orderId");

        Log.i("OrderId","ORDERID: "+orderId);


        textViewMessage.setText(HtmlCompat.fromHtml(
                getString(R.string.the_equipment_id_number_for_standard),
                HtmlCompat.FROM_HTML_MODE_LEGACY
        ));

    }

    public static EnterEquipmentDialogFragment newInstance(int orderId) {
        EnterEquipmentDialogFragment incidentCommentDialogFragment = new EnterEquipmentDialogFragment();
        Bundle args = new Bundle();
        args.putInt("orderId",orderId);
        incidentCommentDialogFragment.setArguments(args);
        return incidentCommentDialogFragment;
    }


    private void setProgressBar(boolean show) {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
            progressDialogFragment = null;
        }

        if (show) {
            progressDialogFragment = ProgressDialogFragment.newInstance();

            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

            fragmentTransaction.add(progressDialogFragment, "ProgressDialogFragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    private void callWebservice() {


        setProgressBar(true);
        AddDeviceInventoryIDRq addDeviceInventoryIDRq = new AddDeviceInventoryIDRq(editTextId.getText().toString().trim(),orderId);
        Injection.provideDataRepository().addDeviceInventoryID(addDeviceInventoryIDRq,
                new CallbackSubscriber<AddDeviceInventoryIDRs>() {
            @Override
            public void onSuccess(AddDeviceInventoryIDRs response) {
                setProgressBar(false);
                getDialog().dismiss();

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
            }

            @Override
            public void onFailure(RestError restError) {
                setProgressBar(false);
                getDialog().dismiss();

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
            }
        });
    }
}