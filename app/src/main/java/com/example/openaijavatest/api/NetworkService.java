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

public interface NetworkService {

    Call<ChatGPTResponse> callChatToGPTTurbo(String token, ChatGPTRequest request);

    Call<ChatDavinciResponse> callChatToDavinci(String token, ChatDavinciRequest request);

    Call<CreateImageByOpenAIResponse> callCreateImageByOpenAI(String token, CreateImageByOpenAIRequest request);

    Call<ResponseBody> callGenerateImageByHotpot(String token, RequestBody inputText, MultipartBody.Part seedImage, RequestBody promptStrength, RequestBody styleId);

    Call<StableDiffusionImg2ImgResponse> callTextToImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);

    Call<StableDiffusionImg2ImgResponse> callImageToImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);

    Call<StableDiffusionImg2ImgResponse> callControlNetImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest);

}
