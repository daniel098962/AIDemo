package com.example.openaijavatest.data.api;

import com.google.gson.annotations.SerializedName;

public class Choice {

    @SerializedName("index")
    private Integer mIndex;

    @SerializedName("message")
    private Message mMessage;

    @SerializedName("finish_reason")
    private String mFinishReason;

    @SerializedName("text")
    private String mText;

    public Integer getIndex() {
        return mIndex;
    }

    public void setIndex(Integer index) {
        mIndex = index;
    }

    public Message getMessage() {
        return mMessage;
    }

    public void setMessage(Message message) {
        mMessage = message;
    }

    public String getFinishReason() {
        return mFinishReason;
    }

    public void setFinishReason(String finishReason) {
        mFinishReason = finishReason;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
