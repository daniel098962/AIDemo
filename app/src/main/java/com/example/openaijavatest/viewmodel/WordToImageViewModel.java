package com.example.openaijavatest.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.custom.application.OpenAIApplication;
import com.example.openaijavatest.data.api.request.CreateImageByOpenAIRequest;
import com.example.openaijavatest.data.api.request.StableDiffusionImg2ImgRequest;
import com.example.openaijavatest.data.api.response.CreateImageByOpenAIResponse;
import com.example.openaijavatest.data.api.response.StableDiffusionImg2ImgResponse;
import com.example.openaijavatest.viewmodel.base.BaseViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.widget.TextViewEditorActionEvent;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordToImageViewModel extends BaseViewModel {

    private final String TAG = "///";

    public Input input = new Input();
    public Output output = new Output();

    private SharedPreferences mSharedPreferences = OpenAIApplication.getInstance().getSharedPreferences(OpenAiProperties.SHARED_NAME, Context.MODE_PRIVATE);

    private boolean mIsUseStableDiffusion = false;

    public boolean isSetUp() {

        boolean result = false;

        try {

            //region EditText ActionEvent
            {
                Disposable disposable = input.mTextViewEditorActionEventObservable.subscribe(new Consumer<TextViewEditorActionEvent>() {
                    @Override
                    public void accept(TextViewEditorActionEvent actionEvent) throws Exception {
                        if (actionEvent.getActionId() == EditorInfo.IME_ACTION_SEND) {
                            output.mCallHideInputMethodSubject.onNext(true);
                            // validating text
                            try {
                                String promptString = actionEvent.getView().getText().toString();
                                if (!TextUtils.isEmpty(promptString)) {
                                    output.mQuestionTextViewSetTextSubject.onNext(promptString);

                                    //region Call NetworkService Request
                                    {
                                        if (mIsUseStableDiffusion) {

                                            StableDiffusionImg2ImgRequest stableDiffusionImg2ImgRequest = new StableDiffusionImg2ImgRequest();
                                            stableDiffusionImg2ImgRequest.setToken(mSharedPreferences.getString(OpenAiProperties.SHARED_SD_AI, ""));
                                            stableDiffusionImg2ImgRequest.setPrompt(promptString);
                                            Log.d(TAG, "StableDiffusionImg2ImgRequest Request: " + new Gson().toJson(stableDiffusionImg2ImgRequest));
                                            OpenAIApplication.getInstance().getNetworkService().callTextToImageByStableDiffusion(stableDiffusionImg2ImgRequest).enqueue(new Callback<StableDiffusionImg2ImgResponse>() {
                                                @Override
                                                public void onResponse(Call<StableDiffusionImg2ImgResponse> call, Response<StableDiffusionImg2ImgResponse> response) {
                                                    try {
                                                        if (response.isSuccessful() && response.body() != null && TextUtils.equals(response.body().getStatus(), "success")) {
                                                            StableDiffusionImg2ImgResponse stableDiffusionImg2ImgResponse = response.body();

                                                            String urlString = "";
                                                            if (stableDiffusionImg2ImgResponse.getOutputList() instanceof JsonPrimitive) {
                                                                urlString = ((JsonPrimitive) stableDiffusionImg2ImgResponse.getOutputList()).getAsString();
                                                            }

                                                            if (stableDiffusionImg2ImgResponse.getOutputList() instanceof JsonArray) {
                                                                if (((JsonArray) stableDiffusionImg2ImgResponse.getOutputList()).size() > 0) {
                                                                    urlString = ((JsonArray) stableDiffusionImg2ImgResponse.getOutputList()).get(0).getAsString();
                                                                }
                                                            }

                                                            if (TextUtils.isEmpty(urlString)) {
                                                                urlString = stableDiffusionImg2ImgResponse.getOutputList().toString();
                                                                Log.d(TAG, "onResponse Object: " + urlString);
                                                            }

                                                            Log.d(TAG, "onResponse original UrlString: " + urlString);
                                                            if (urlString.length() > 10 && !urlString.startsWith("https://")) {
                                                                urlString = urlString.substring(urlString.indexOf("https://"));
                                                                int lastIndex = urlString.lastIndexOf(".png");
                                                                urlString = urlString.substring(0, lastIndex + 4);
                                                            }
                                                            output.mResultImageSetImageSubject.onNext(urlString);
                                                        }

                                                        if (!response.isSuccessful()) {
                                                            Log.d(TAG, "callTextToImageByStableDiffusion onResponse fail: " + new Gson().toJson(response.errorBody()));
                                                        }
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "callTextToImageByStableDiffusion onResponse try catch error: ", e);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<StableDiffusionImg2ImgResponse> call, Throwable t) {
                                                    Log.e(TAG, "callTextToImageByStableDiffusion onFailure: ", t);
                                                }
                                            });
                                            return;
                                        }
                                        CreateImageByOpenAIRequest request = new CreateImageByOpenAIRequest();
                                        request.setContent(promptString);
                                        OpenAIApplication.getInstance().getNetworkService().callCreateImageByOpenAI(mSharedPreferences.getString(OpenAiProperties.SHARED_OPEN_AI, ""), request).enqueue(new Callback<CreateImageByOpenAIResponse>() {
                                            @Override
                                            public void onResponse(Call<CreateImageByOpenAIResponse> call, Response<CreateImageByOpenAIResponse> response) {

                                                try {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        CreateImageByOpenAIResponse createImageByOpenAIResponse = response.body();
                                                        if (createImageByOpenAIResponse.getImageDataList().size() > 0) {
                                                            output.mResultImageSetImageSubject.onNext(createImageByOpenAIResponse.getImageDataList().get(0).getUrl());
                                                        }
                                                    }

                                                    if (!response.isSuccessful()) {
                                                        Log.d(TAG, "callCreateImageByOpenAI onResponse fail: " + new Gson().toJson(response.errorBody()));
                                                    }
                                                } catch (Exception e) {
                                                    Log.e(TAG, "callCreateImageByOpenAI onResponse try catch error: ", e);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<CreateImageByOpenAIResponse> call, Throwable t) {
                                                Log.e(TAG, "callCreateImageByOpenAI onFailure: ", t);
                                            }
                                        });
                                    }
                                    //endregion

                                } else {
                                    output.mShowToastSubject.onNext("Please enter your query..");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "EditorInfo.IME_ACTION_SEND try catch error: ", e);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mTextViewEditorActionEventObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region AI Switch
            {
                Disposable disposable = input.mAiSwitchObservable.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isUseStableDiffusion) throws Exception {
                        mIsUseStableDiffusion = isUseStableDiffusion;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mAiSwitchObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            result = true;
        } catch (Exception e) {
            Log.e(TAG, "isSetUp try catch error: ", e);
        }

        return result;
    }

    public class Input {
        public Observable<TextViewEditorActionEvent> mTextViewEditorActionEventObservable;
        public InitialValueObservable<Boolean> mAiSwitchObservable;
    }

    public class Output {
        public PublishSubject<Boolean> mCallHideInputMethodSubject = PublishSubject.create();
        public PublishSubject<String> mQuestionTextViewSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mResultImageSetImageSubject = PublishSubject.create();

        public PublishSubject<String> mShowToastSubject = PublishSubject.create();
    }
}
