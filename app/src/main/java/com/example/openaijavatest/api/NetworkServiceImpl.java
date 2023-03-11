package com.example.openaijavatest.api;

import com.example.openaijavatest.custom.application.OpenAIApplication;
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

public class NetworkServiceImpl implements NetworkService {

    @Override
    public Call<ChatGPTResponse> callChatToGPTTurbo(String token, ChatGPTRequest request) {
        return OpenAIApplication.getInstance().getOpenAiApiManager().getApi().chatToGPTTurbo("Bearer " + token, request);
    }

    @Override
    public Call<ChatDavinciResponse> callChatToDavinci(String token, ChatDavinciRequest request) {
        return OpenAIApplication.getInstance().getOpenAiApiManager().getApi().chatToGPTDavinci("Bearer " + token, request);
    }

    @Override
    public Call<CreateImageByOpenAIResponse> callCreateImageByOpenAI(String token, CreateImageByOpenAIRequest request) {
        return OpenAIApplication.getInstance().getOpenAiApiManager().getApi().createImageByOpenAI("Bearer " + token, request);
    }

    @Override
    public Call<ResponseBody> callGenerateImageByHotpot(String token, RequestBody inputText, MultipartBody.Part seedImage, RequestBody promptStrength, RequestBody styleId) {
        return OpenAIApplication.getInstance().getHotpotApiManager().getApi().generateImageByHotpot(token, inputText, seedImage, promptStrength, styleId);
    }

    @Override
    public Call<StableDiffusionImg2ImgResponse> callTextToImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest) {
        return OpenAIApplication.getInstance().getStableDiffusionApiManager().getApi().textToImageByStableDiffusion(stableDiffusionImg2ImgRequest);
    }

    @Override
    public Call<StableDiffusionImg2ImgResponse> callImageToImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest) {
        return OpenAIApplication.getInstance().getStableDiffusionApiManager().getApi().imageToImageByStableDiffusion(stableDiffusionImg2ImgRequest);
    }

    @Override
    public Call<StableDiffusionImg2ImgResponse> callControlNetImageByStableDiffusion(StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest) {
        return OpenAIApplication.getInstance().getStableDiffusionApiManager().getApi().controlNetImageByStableDiffusion(stableDiffusionImg2ImgRequest);
    }
}
