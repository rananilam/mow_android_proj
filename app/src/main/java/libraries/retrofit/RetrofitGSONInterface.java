package libraries.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public abstract class RetrofitGSONInterface
{
    private final Gson gson;

    public RetrofitGSONInterface()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        registerTypes(gsonBuilder);
        gsonBuilder.setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
        gson = gsonBuilder.create();
    }

    public abstract void registerTypes(GsonBuilder gsonBuilder);

    public Gson getGson()
    {
        return gson;
    }

    public boolean hasProperty(JsonObject jsonObject, String propertyName)
    {
        return jsonObject.has(propertyName) && !jsonObject.get(propertyName).isJsonNull();
    }
}
