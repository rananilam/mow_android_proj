package com.ui.home.contentDialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.mow.cash.android.R;

import java.util.Objects;

import im.delight.android.webview.AdvancedWebView;

public class ContentDialogFragment extends DialogFragment {

    private static final String KEY_CONTENT = "KEY_CONTENT";

    private ImageView imageViewClose;
    private AdvancedWebView webViewContent;

    public static ContentDialogFragment newInstance(String rewardPolicy) {
        ContentDialogFragment incidentCommentDialogFragment = new ContentDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, rewardPolicy);
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
        View v = inflater.inflate(R.layout.dialog_content, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpUI();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }*/


    @SuppressLint("SetJavaScriptEnabled")
    private void setUpUI()
    {

        imageViewClose = requireView().findViewById(R.id.imageViewClose);
        webViewContent = requireView().findViewById(R.id.webViewContent);
        webViewContent.getSettings().setJavaScriptEnabled(true);

        assert getArguments() != null;

        /*webViewContent.loadData(Base64.encodeToString(("<style>img{display: inline;height: auto;max-width: 100%;}</style>"
                +getArguments().getString(KEY_CONTENT,"")).getBytes(), Base64.NO_PADDING), "text/html", "base64");*/

        webViewContent.loadDataWithBaseURL(null,("<style>img{display: inline;height: auto;max-width: 100%;}</style>"  +getArguments().getString(KEY_CONTENT,"")),"text/html", "utf-8", null);

        imageViewClose.setOnClickListener(view -> {
            dismiss();
        });
    }

}