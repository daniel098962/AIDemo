package com.example.openaijavatest.data.api.response;

import com.example.openaijavatest.data.api.Meta;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StableDiffusionImg2ImgResponse {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("generationTime")
    private Number mGenerationTime;

    @SerializedName("id")
    private Number mID;

    @SerializedName("output")
    private Object mOutputList = new Object();

    @SerializedName("meta")
    private Meta mMeta;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public Number getGenerationTime() {
        return mGenerationTime;
    }

    public void setGenerationTime(Number generationTime) {
        mGenerationTime = generationTime;
    }

    public Number getID() {
        return mID;
    }

    public void setID(Number ID) {
        mID = ID;
    }

    public Object getOutputList() {
        return mOutputList;
    }

    public void setOutputList(Object outputList) {
        mOutputList = outputList;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }
}
