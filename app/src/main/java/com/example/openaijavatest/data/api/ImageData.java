package com.example.openaijavatest.data.api;

import com.google.gson.annotations.SerializedName;

public class ImageData {

    @SerializedName("url")
    private String mUrl;

    @SerializedName("b64_json")
    private String mBase64Json;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getBase64Json() {
        return mBase64Json;
    }

    public void setBase64Json(String base64Json) {
        mBase64Json = base64Json;
    }
}
