package com.example.openaijavatest.data.api;

import com.google.gson.annotations.SerializedName;

public class Message {


    @SerializedName("role")
    private String mRole;

    @SerializedName("content")
    private String mContent;

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
