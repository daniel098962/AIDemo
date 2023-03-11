package com.example.openaijavatest.data.api.request;

import com.example.openaijavatest.data.api.Message;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTRequest {


    @SerializedName("model")
    private String mModel;

    @SerializedName("messages")
    private List<Message> mMessageList = new ArrayList<>();

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public List<Message> getMessageList() {
        return mMessageList;
    }

    public void setMessageList(List<Message> messageList) {
        mMessageList = messageList;
    }
}
