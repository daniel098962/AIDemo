package com.example.openaijavatest.data.api.request;

import com.google.gson.annotations.SerializedName;

public class StableDiffusionImg2ImgRequest {

    @SerializedName("key")
    private String mToken;

    @SerializedName("model_id")
    private String mModelID;

    @SerializedName("controlnet_model")
    private String mControlNetModel;

    @SerializedName("prompt")
    private String mPrompt;

    @SerializedName("negative_prompt")
    private String mNegativePrompt;

    @SerializedName("init_image")
    private String mSeedImageUrlString;

    @SerializedName("width")
    private Number mWidth = 512;

    @SerializedName("height")
    private Number mHeight = 512;

    @SerializedName("samples")
    private Number mSamples = 1;

    @SerializedName("num_inference_steps")
    private Number mNumInferenceSteps = 30;

    @SerializedName("safety_checker")
    private String mSafetyChecker = "no";

    @SerializedName("enhance_prompt")
    private String mEnhancePrompt = "no";

    @SerializedName("scheduler")
    private String mScheduler = "UniPCMultistepScheduler";

    @SerializedName("guidance_scale")
    private Number mGuidanceScale = 7.5;

    @SerializedName("strength")
    private Number mStrength = 0.7;

    @SerializedName("seed")
    private Number mSeed;

    @SerializedName("webhook")
    private String mWenHook;

    @SerializedName("track_id")
    private String mTrackID;

    @SerializedName("auto_hint")
    private String mAutoHint = "yes";

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getModelID() {
        return mModelID;
    }

    public void setModelID(String modelID) {
        mModelID = modelID;
    }

    public String getControlNetModel() {
        return mControlNetModel;
    }

    public void setControlNetModel(String controlNetModel) {
        mControlNetModel = controlNetModel;
    }

    public String getPrompt() {
        return mPrompt;
    }

    public void setPrompt(String prompt) {
        mPrompt = prompt;
    }

    public String getNegativePrompt() {
        return mNegativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        mNegativePrompt = negativePrompt;
    }

    public String getSeedImageUrlString() {
        return mSeedImageUrlString;
    }

    public void setSeedImageUrlString(String seedImageUrlString) {
        mSeedImageUrlString = seedImageUrlString;
    }

    public Number getWidth() {
        return mWidth;
    }

    public void setWidth(Number width) {
        mWidth = width;
    }

    public Number getHeight() {
        return mHeight;
    }

    public void setHeight(Number height) {
        mHeight = height;
    }

    public Number getSamples() {
        return mSamples;
    }

    public void setSamples(Number samples) {
        mSamples = samples;
    }

    public Number getNumInferenceSteps() {
        return mNumInferenceSteps;
    }

    public void setNumInferenceSteps(Number numInferenceSteps) {
        mNumInferenceSteps = numInferenceSteps;
    }

    public String getSafetyChecker() {
        return mSafetyChecker;
    }

    public void setSafetyChecker(String safetyChecker) {
        mSafetyChecker = safetyChecker;
    }

    public String getEnhancePrompt() {
        return mEnhancePrompt;
    }

    public void setEnhancePrompt(String enhancePrompt) {
        mEnhancePrompt = enhancePrompt;
    }

    public String getScheduler() {
        return mScheduler;
    }

    public void setScheduler(String scheduler) {
        mScheduler = scheduler;
    }

    public Number getGuidanceScale() {
        return mGuidanceScale;
    }

    public void setGuidanceScale(Number guidanceScale) {
        mGuidanceScale = guidanceScale;
    }

    public Number getStrength() {
        return mStrength;
    }

    public void setStrength(Number strength) {
        mStrength = strength;
    }

    public Number getSeed() {
        return mSeed;
    }

    public void setSeed(Number seed) {
        mSeed = seed;
    }

    public String getWenHook() {
        return mWenHook;
    }

    public void setWenHook(String wenHook) {
        mWenHook = wenHook;
    }

    public String getTrackID() {
        return mTrackID;
    }

    public void setTrackID(String trackID) {
        mTrackID = trackID;
    }

    public String getAutoHint() {
        return mAutoHint;
    }

    public void setAutoHint(String autoHint) {
        mAutoHint = autoHint;
    }
}
