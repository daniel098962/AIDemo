package com.example.openaijavatest.data.model;

public class ImageToImageDetail {

    private String mPrompt = "";

    private String mFileUrl = "";

    private Number mStrength = 0.0;

    private String mModelID = "";

    private String mControlNetModelID = "";

    public String getPrompt() {
        return mPrompt;
    }

    public void setPrompt(String prompt) {
        mPrompt = prompt;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        mFileUrl = fileUrl;
    }

    public Number getStrength() {
        return mStrength;
    }

    public void setStrength(Number strength) {
        mStrength = strength;
    }

    public String getModelID() {
        return mModelID;
    }

    public void setModelID(String modelID) {
        mModelID = modelID;
    }

    public String getControlNetModelID() {
        return mControlNetModelID;
    }

    public void setControlNetModelID(String controlNetModelID) {
        mControlNetModelID = controlNetModelID;
    }
}
