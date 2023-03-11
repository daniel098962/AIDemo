package com.example.openaijavatest.data.api.request;

import com.google.gson.annotations.SerializedName;

public class ChatDavinciRequest {

    @SerializedName("model")
    private String mModel;

    @SerializedName("prompt")
    private String mContent;

    @SerializedName("temperature")
    private Integer mTemperature;

    @SerializedName("max_tokens")
    private Integer mMaxTokens;

    @SerializedName("top_p")
    private Integer mTopP;

    @SerializedName("frequency_penalty")
    private Number mFrequencyPenalty;

    @SerializedName("presence_penalty")
    private Number mPresencePenalty;

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Integer getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Integer temperature) {
        mTemperature = temperature;
    }

    public Integer getMaxTokens() {
        return mMaxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        mMaxTokens = maxTokens;
    }

    public Integer getTopP() {
        return mTopP;
    }

    public void setTopP(Integer topP) {
        mTopP = topP;
    }

    public Number getFrequencyPenalty() {
        return mFrequencyPenalty;
    }

    public void setFrequencyPenalty(Number frequencyPenalty) {
        mFrequencyPenalty = frequencyPenalty;
    }

    public Number getPresencePenalty() {
        return mPresencePenalty;
    }

    public void setPresencePenalty(Number presencePenalty) {
        mPresencePenalty = presencePenalty;
    }
}
