package com.ui.home.checkOut;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.mow.cash.android.R;

import java.util.Objects;

public class CreditCardIssuerAgreementDialogFragment extends DialogFragment implements View.OnClickListener {


    private ImageView imageViewClose;
    private TextView textViewAgreement;


    @Override
    public void onClick(View v) {
        if(v == imageViewClose){
            getDialog().dismiss();
        }
    }


    public static CreditCardIssuerAgreementDialogFragment newInstance() {
        CreditCardIssuerAgreementDialogFragment incidentCommentDialogFragment = new CreditCardIssuerAgreementDialogFragment();
        Bundle args = new Bundle();
        incidentCommentDialogFragment.setArguments(args);
        return incidentCommentDialogFragment;
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
        View v = inflater.inflate(R.layout.dialog_mow_credit_card_issuer_agreement, container, false);
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
        textViewAgreement = Objects.requireNonNull(getView()).findViewById(R.id.textViewAgreement);
        textViewAgreement.setMovementMethod(new ScrollingMovementMethod());

        imageViewClose.setOnClickListener(this);
      /*  textViewProfileName = Objects.requireNonNull(getView()).findViewById(R.id.textViewProfileName);
        textViewRole = Objects.requireNonNull(getView()).findViewById(R.id.textViewRole);
        recycleViewSwitchUserList = Objects.requireNonNull(getView()).findViewById(R.id.recycleViewSwitchUserList);
*/
        assert getArguments() != null;

       // userList = (ArrayList<User>) getArguments().getSerializable(KEY_SWITCH_USER_LIST);

      /*  String name = Injection.provideDataRepository().getFirstName()+" "+Injection.provideDataRepository().getLastName();
        String profilePic = Injection.provideDataRepository().getProfilePic();
        String roleName = Injection.provideDataRepository().getUserType().getUserType(requireContext());

        textViewProfileName.setText(name);
        textViewRole.setText(roleName);

        if(profilePic!=null && !profilePic.trim().isEmpty()) {

            Picasso.get().load(profilePic)
                    .transform(new CropCircleTransformation())
                    .into(imageViewProfilePic);

        }


        switchUserAdapter = new SwitchUserAdapter(requireActivity());
        recycleViewSwitchUserList.setAdapter(switchUserAdapter);
        switchUserAdapter.submitList(userList);*/


    }

}