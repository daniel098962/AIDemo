package com.example.openaijavatest.data.api;

import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("H")
    private Number mHeight;

    @SerializedName("W")
    private Number mWidth;

    @SerializedName("auto_hint")
    private String mAutoHint;

    @SerializedName("controlnet_model")
    private String mControlNetModel;

    @SerializedName("embeddings")
    private String mEmbeddings;

    @SerializedName("file_prefix")
    private String mFilePrefix;

    @SerializedName("full_url")
    private String mFullUrl;

    @SerializedName("guidance_scale")
    private Number mGuidanceScale;

    @SerializedName("init_image")
    private String mSeedImageUrlString;

    @SerializedName("lora")
    private String mLora;

    @SerializedName("model_id")
    private String mModelID;

    @SerializedName("n_samples")
    private Number mNSamples;

    @SerializedName("negative_prompt")
    private String mNegativePrompt;

    @SerializedName("outdir")
    private String mOutDir;

    @SerializedName("prompt")
    private String mPrompt;

    @SerializedName("safetychecker")
    private String mSafetyChecker;

    @SerializedName("scheduler")
    private String mScheduler;

    @SerializedName("seed")
    private Number mSeed;

    @SerializedName("steps")
    private Number mSteps;
}
