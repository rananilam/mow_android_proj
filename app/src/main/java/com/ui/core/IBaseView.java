package com.ui.core;


import android.content.Context;

import libraries.retrofit.RestError;

public interface IBaseView {

    void showToastMessage(String message);

    void setProgressBar(boolean show);

    void processErrorWithToast(RestError restError);

    void processErrorWithDialog(RestError restError);

    Context getContextt();
}
