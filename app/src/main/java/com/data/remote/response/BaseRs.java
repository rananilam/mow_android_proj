package com.data.remote.response;

import com.data.model.Result;
import com.data.remote.GsonInterface;
import com.google.gson.JsonElement;

/**
 * Created by verve on 25/03/19.
 */

public class BaseRs {
    private Result result;

    public void decodeResult(JsonElement jsonElement) {
        result = GsonInterface.getInstance().getGson().fromJson(jsonElement, Result.class);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
