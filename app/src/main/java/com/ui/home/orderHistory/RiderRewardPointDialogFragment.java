package com.ui.home.orderHistory;

import android.os.Bundle;
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

public class RiderRewardPointDialogFragment extends DialogFragment {

    private static final String KEY_RIDER_REWARD_POINT = "KEY_RIDER_REWARD_POINT";

    private ImageView imageViewClose;
    private TextView textViewRiderRewardPoints;

    public static RiderRewardPointDialogFragment newInstance(float riderRewardPoint) {
        RiderRewardPointDialogFragment incidentCommentDialogFragment = new RiderRewardPointDialogFragment();
        Bundle args = new Bundle();
        args.putFloat(KEY_RIDER_REWARD_POINT, riderRewardPoint);
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
        View v = inflater.inflate(R.layout.dialog_rider_reward_point, container, false);
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
        textViewRiderRewardPoints = Objects.requireNonNull(getView()).findViewById(R.id.textViewRiderRewardPoints);


        assert getArguments() != null;
        float riderRewardPoint = getArguments().getFloat(KEY_RIDER_REWARD_POINT,0.0F);
        textViewRiderRewardPoints.setText(String.valueOf(riderRewardPoint));


        imageViewClose.setOnClickListener(view -> {
            dismiss();
        });
    }

}