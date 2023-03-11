package com.example.openaijavatest.data.api.request;

import com.google.gson.annotations.SerializedName;

public class CreateImageByOpenAIRequest {

    @SerializedName("prompt")
    private String mContent;

    @SerializedName("n")
    private Integer mCreateNumber = 1;

    @SerializedName("size")
    private String mSize = "256x256";

    @SerializedName("response_format")
    private String mFormat = "url";

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Integer getCreateNumber() {
        return mCreateNumber;
    }

    public void setCreateNumber(Integer createNumber) {
        mCreateNumber = createNumber;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }
}
