package com.example.openaijavatest.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;

import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.custom.application.OpenAIApplication;
import com.example.openaijavatest.data.api.request.ChatDavinciRequest;
import com.example.openaijavatest.data.api.request.ChatGPTRequest;
import com.example.openaijavatest.data.api.response.ChatDavinciResponse;
import com.example.openaijavatest.data.api.response.ChatGPTResponse;
import com.example.openaijavatest.data.api.Message;
import com.example.openaijavatest.viewmodel.base.BaseViewModel;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.widget.TextViewEditorActionEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageViewModel extends BaseViewModel {

    private final String TAG = "///";

    public Input input = new Input();
    public Output output = new Output();

    private NumberFormat mDecimalFormat = new DecimalFormat("0.00000");
    private SharedPreferences mSharedPreferences = OpenAIApplication.getInstance().getSharedPreferences(OpenAiProperties.SHARED_NAME, Context.MODE_PRIVATE);

    private boolean mIsUseTurbo = false;

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

                                output.mResponseSetTextSubject.onNext("Please wait..");
                                output.mQuestionTokensSetTextSubject.onNext("");
                                output.mReplyTokensSetTextSubject.onNext("");
                                output.mTotalTokensSetTextSubject.onNext("");
                                output.mPriceSetTextSubject.onNext("");

                                // validating text
                                try {
                                    String promptString = actionEvent.getView().getText().toString();
                                    if (!TextUtils.isEmpty(promptString)) {

                                        output.mQuestionSetTextSubject.onNext(promptString);

                                        //region Call NetworkService Request
                                        {
                                            if (mIsUseTurbo) {
                                                ChatGPTRequest request = new ChatGPTRequest();
                                                request.setModel(OpenAiProperties.CHAT_GPT_MODEL);
                                                List<Message> messageList = new ArrayList<>();
                                                Message message = new Message();
                                                message.setRole("user");
                                                message.setContent(promptString);
                                                messageList.add(message);
                                                request.setMessageList(messageList);
                                                OpenAIApplication.getInstance().getNetworkService().callChatToGPTTurbo(mSharedPreferences.getString(OpenAiProperties.SHARED_OPEN_AI, ""), request).enqueue(new Callback<ChatGPTResponse>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<ChatGPTResponse> call, @NonNull Response<ChatGPTResponse> response) {

                                                        Log.d("///", "callChatToGPTTurbo onResponse isSuccessful: " + response.isSuccessful());
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            ChatGPTResponse gptResponse = response.body();
                                                            if (gptResponse.getChoicesList() != null && gptResponse.getChoicesList().size() > 0) {
                                                                output.mResponseSetTextSubject.onNext(gptResponse.getChoicesList().get(0).getMessage().getContent());
                                                            }

                                                            if (gptResponse.getUsage() != null) {

                                                                output.mQuestionTokensSetTextSubject.onNext(String.valueOf(gptResponse.getUsage().getPromptTokens()));
                                                                output.mReplyTokensSetTextSubject.onNext(String.valueOf(gptResponse.getUsage().getCompletionTokens()));
                                                                output.mTotalTokensSetTextSubject.onNext(String.valueOf(gptResponse.getUsage().getTotalTokens()));


                                                                Number priceNumber = gptResponse.getUsage().getTotalTokens() * 0.002 / 1000;
                                                                String priceString = String.valueOf(priceNumber);
                                                                if (priceString.contains("E")) {
                                                                    priceString = mDecimalFormat.format(priceNumber);
                                                                }
                                                                output.mPriceSetTextSubject.onNext(String.valueOf(priceString));
                                                            }
                                                        }

                                                        if (!response.isSuccessful()) {
                                                            Log.d("///", "callChatToGPTTurbo onResponse fail: " + new Gson().toJson(response.errorBody()));
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<ChatGPTResponse> call, @NonNull Throwable t) {
                                                        Log.e("///", "callChatToGPTTurbo onFailure: ", t);
                                                    }
                                                });
                                            }

                                            if (!mIsUseTurbo) {
                                                ChatDavinciRequest request = new ChatDavinciRequest();
                                                request.setModel(OpenAiProperties.DAVINCI_MODEL);
                                                request.setContent(promptString);
                                                request.setTemperature(0);
                                                request.setMaxTokens(100);
                                                request.setTopP(1);
                                                request.setFrequencyPenalty(0.0);
                                                request.setPresencePenalty(0.0);
                                                OpenAIApplication.getInstance().getNetworkService().callChatToDavinci(mSharedPreferences.getString(OpenAiProperties.SHARED_OPEN_AI, ""), request).enqueue(new Callback<ChatDavinciResponse>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<ChatDavinciResponse> call, @NonNull Response<ChatDavinciResponse> response) {
                                                        Log.d("///", "callChatToDavinci onResponse isSuccessful: " + response.isSuccessful());
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            ChatDavinciResponse gpt3Response = response.body();
                                                            if (gpt3Response.getChoicesList() != null && gpt3Response.getChoicesList().size() > 0) {
                                                                output.mResponseSetTextSubject.onNext(gpt3Response.getChoicesList().get(0).getText());
                                                            }

                                                            if (gpt3Response.getUsage() != null) {

                                                                output.mQuestionTokensSetTextSubject.onNext(String.valueOf(gpt3Response.getUsage().getPromptTokens()));
                                                                output.mReplyTokensSetTextSubject.onNext(String.valueOf(gpt3Response.getUsage().getCompletionTokens()));
                                                                output.mTotalTokensSetTextSubject.onNext(String.valueOf(gpt3Response.getUsage().getTotalTokens()));

                                                                Number priceNumber = gpt3Response.getUsage().getTotalTokens() * 0.02 / 1000;
                                                                String priceString = String.valueOf(priceNumber);
                                                                if (priceString.contains("E")) {
                                                                    priceString = mDecimalFormat.format(priceNumber);
                                                                }
                                                                output.mPriceSetTextSubject.onNext(String.valueOf(priceString));
                                                            }
                                                        }

                                                        if (!response.isSuccessful()) {
                                                            Log.d("///", "callChatToDavinci onResponse fail: " + new Gson().toJson(response.errorBody()));
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<ChatDavinciResponse> call, @NonNull Throwable t) {
                                                        Log.e("///", "callChatToDavinci onFailure: ", t);
                                                    }
                                                });
                                            }
                                        }
                                        //endregion

                                    } else {
                                        output.mShowToastSubject.onNext("Please enter your query..");
                                    }
                                } catch (Exception e) {
                                    Log.e("///", "EditorInfo.IME_ACTION_SEND try catch error: ", e);
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

            //region Switch CheckChange Event
            {
                Disposable disposable = input.mModelSwitchCheckChangeObservable.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        mIsUseTurbo = aBoolean;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mModelSwitchCheckChangeObservable accept error: ", throwable);
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
        public InitialValueObservable<Boolean> mModelSwitchCheckChangeObservable;
    }

    public class Output {

        public PublishSubject<Boolean> mCallHideInputMethodSubject = PublishSubject.create();
        public PublishSubject<String> mQuestionSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mResponseSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mQuestionTokensSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mReplyTokensSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mTotalTokensSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mPriceSetTextSubject = PublishSubject.create();

        public PublishSubject<String> mShowToastSubject = PublishSubject.create();
    }
}
