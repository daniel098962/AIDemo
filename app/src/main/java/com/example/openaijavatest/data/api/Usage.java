package com.example.openaijavatest.data.api;

import com.google.gson.annotations.SerializedName;

public class Usage {

    @SerializedName("prompt_tokens")
    private Integer mPromptTokens;

    @SerializedName("completion_tokens")
    private Integer mCompletionTokens;

    @SerializedName("total_tokens")
    private Integer mTotalTokens;

    public Integer getPromptTokens() {
        return mPromptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        mPromptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return mCompletionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        mCompletionTokens = completionTokens;
    }

    public Integer getTotalTokens() {
        return mTotalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        mTotalTokens = totalTokens;
    }
}
