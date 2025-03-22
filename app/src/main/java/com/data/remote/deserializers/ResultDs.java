package com.data.remote.deserializers;

import com.data.model.Result;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResultDs implements JsonDeserializer<Result>
{
    @Override
    public Result deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Result result = new Result();
        JsonObject jsonObject = json.getAsJsonObject();

        if(jsonObject.has("StatusCode") && !jsonObject.get("StatusCode").isJsonNull())
        {
            String statusCode = jsonObject.get("StatusCode").getAsString();

            if(statusCode.equalsIgnoreCase("OK"))
                result.setStatus(true);
            else
                result.setStatus(false);
        } else if(jsonObject.has("Result") && !jsonObject.get("Result").isJsonNull()){
            result.setStatus(jsonObject.get("Result").getAsBoolean());
        }



        if(jsonObject.has("Message") && !jsonObject.get("Message").isJsonNull())
        {
            String message = jsonObject.get("Message").getAsString();
            result.setMessage(message);
        }

        if(jsonObject.has("ErrorMessage") && !jsonObject.get("ErrorMessage").isJsonNull())
        {
            String errorMessage = jsonObject.get("ErrorMessage").getAsString();
            result.setErrorMessage(errorMessage);
        }
        return result;
    }
}
