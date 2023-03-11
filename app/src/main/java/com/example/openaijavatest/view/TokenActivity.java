package com.example.openaijavatest.view;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.openaijavatest.R;
import com.example.openaijavatest.view.base.BaseActivity;
import com.example.openaijavatest.viewmodel.TokenViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TokenActivity extends BaseActivity {

    private final String TAG = "///";

    private TokenViewModel mTokenViewModel;

    private TextInputEditText mOpenAiTokenEditText;
    private AppCompatButton mGetOpenAiTokenButton;
    private TextInputEditText mStableDiffusionEditText;
    private AppCompatButton mGetStableDiffusionTokenButton;
    private AppCompatButton mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        mTokenViewModel = new ViewModelProvider(this).get(TokenViewModel.class);

        //region initView
        {
            mOpenAiTokenEditText = findViewById(R.id.open_ai_token_edit_text);
            mGetOpenAiTokenButton = findViewById(R.id.get_open_ai_token_button);
            mStableDiffusionEditText = findViewById(R.id.stable_diffusion_token_edit_text);
            mGetStableDiffusionTokenButton = findViewById(R.id.get_stable_diffusion_token_button);
            mStartButton = findViewById(R.id.start_button);
        }
        //endregion

        {
            mTokenViewModel.input.mOpenAiEditTextChangeObservable = RxTextView.textChanges(mOpenAiTokenEditText);
            mTokenViewModel.input.mGetOpenAITokenObservable = RxView.clicks(mGetOpenAiTokenButton);
            mTokenViewModel.input.mSDAiEditTextChangeObservable = RxTextView.textChanges(mStableDiffusionEditText);
            mTokenViewModel.input.mGetSDAITokenObservable = RxView.clicks(mGetStableDiffusionTokenButton);
            mTokenViewModel.input.mStartObservable = RxView.clicks(mStartButton);
            if (mTokenViewModel.isSetUp()) {

                //region Start Intent
                {
                    Disposable disposable = mTokenViewModel.output.mStartIntentSubject.subscribe(new Consumer<Intent>() {
                        @Override
                        public void accept(Intent intent) throws Exception {
                            startActivity(intent);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mStartIntentSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region OpenAI Key
                {
                    Disposable disposable = mTokenViewModel.output.mOpenAIKetSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mOpenAiTokenEditText.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mOpenAIKetSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region SD AI Key
                {
                    Disposable disposable = mTokenViewModel.output.mStableDiffusionKeySubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mStableDiffusionEditText.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mStableDiffusionKeySubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion
            }
        }
    }
}