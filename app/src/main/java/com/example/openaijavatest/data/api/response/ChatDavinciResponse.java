package com.example.openaijavatest.data.api.response;

import com.example.openaijavatest.data.api.Choice;
import com.example.openaijavatest.data.api.Usage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatDavinciResponse {

    @SerializedName("id")
    private String mID;

    @SerializedName("object")
    private String mObjectModel;

    @SerializedName("created")
    private Long mCreatedTime;

    @SerializedName("model")
    private String mModel;

    @SerializedName("choices")
    private List<Choice> mChoicesList;

    @SerializedName("usage")
    private Usage mUsage;

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getObjectModel() {
        return mObjectModel;
    }

    public void setObjectModel(String objectModel) {
        mObjectModel = objectModel;
    }

    public Long getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(Long createdTime) {
        mCreatedTime = createdTime;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public List<Choice> getChoicesList() {
        return mChoicesList;
    }

    public void setChoicesList(List<Choice> choicesList) {
        mChoicesList = choicesList;
    }

    public Usage getUsage() {
        return mUsage;
    }

    public void setUsage(Usage usage) {
        mUsage = usage;
    }
}
