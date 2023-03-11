package com.example.openaijavatest.api;

import com.example.openaijavatest.data.api.request.ChatGPTRequest;
import com.example.openaijavatest.data.api.request.CreateImageByOpenAIRequest;
import com.example.openaijavatest.data.api.request.StableDiffusionImg2ImgRequest;
import com.example.openaijavatest.data.api.response.ChatGPTResponse;
import com.example.openaijavatest.data.api.request.ChatDavinciRequest;
import com.example.openaijavatest.data.api.response.ChatDavinciResponse;
import com.example.openaijavatest.data.api.response.CreateImageByOpenAIResponse;
import com.example.openaijavatest.data.api.response.StableDiffusionImg2ImgResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    Call<ChatGPTResponse> chatToGPTTurbo(@Header("Authorization") String token, @Body ChatGPTRequest chatGPTRequest);

    @Headers("Content-Type: application/json")
    @POST("completions")
    Call<ChatDavinciResponse> chatToGPTDavinci(@Header("Authorization") String token, @Body ChatDavinciRequest chatDavinciRequest);

    @Headers("Content-Type: application/json")
    @POST("images/generations")
    Call<CreateImageByOpenAIResponse> createImageByOpenAI(@Header("Authorization") String token, @Body CreateImageByOpenAIRequest createImageByOpenAIRequest);

    @Multipart
    @POST("make-art")
    Call<ResponseBody> generateImageByHotpot(@Header("Authorization") String token,
                                             @Part("inputText") RequestBody inputText,
                                             @Part MultipartBody.Part seedImage,
                                             @Part("promptStrength") RequestBody promptStrength,
                                             @Part("styleId") RequestBody styleId);


    @Headers("Content-Type: application/json")
    @POST(" v3/text2img")
    Call<StableDiffusionImg2ImgResponse> textToImageByStableDiffusion(@Body StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);

    @Headers("Content-Type: application/json")
    @POST("v3/img2img")
    Call<StableDiffusionImg2ImgResponse> imageToImageByStableDiffusion(@Body StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);

    @Headers("Content-Type: application/json")
    @POST("v5/controlnet")
    Call<StableDiffusionImg2ImgResponse> controlNetImageByStableDiffusion(@Body StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);
}
