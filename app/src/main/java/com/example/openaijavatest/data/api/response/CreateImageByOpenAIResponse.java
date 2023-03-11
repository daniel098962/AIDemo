package com.example.openaijavatest.data.api.response;

import com.example.openaijavatest.data.api.ImageData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreateImageByOpenAIResponse {

    @SerializedName("created")
    private Long mCreatedTime;

    @SerializedName("data")
    private List<ImageData> mImageDataList = new ArrayList<>();

    public Long getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(Long createdTime) {
        mCreatedTime = createdTime;
    }

    public List<ImageData> getImageDataList() {
        return mImageDataList;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        mImageDataList = imageDataList;
    }
}
