package com.example.openaijavatest.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.custom.application.OpenAIApplication;
import com.example.openaijavatest.view.MainActivity;
import com.example.openaijavatest.viewmodel.base.BaseViewModel;
import com.jakewharton.rxbinding3.InitialValueObservable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;

public class TokenViewModel extends BaseViewModel {

    private final String TAG = "///";

    public Input input = new Input();
    public Output output = new Output();

    private SharedPreferences mSharedPreferences = OpenAIApplication.getInstance().getSharedPreferences(OpenAiProperties.SHARED_NAME, Context.MODE_PRIVATE);

    public boolean isSetUp() {

        boolean result = false;
        try {

            output.mOpenAIKetSubject.onNext(mSharedPreferences.getString(OpenAiProperties.SHARED_OPEN_AI, ""));
            output.mStableDiffusionKeySubject.onNext(mSharedPreferences.getString(OpenAiProperties.SHARED_SD_AI, ""));

            //region OpenAI API Key TextChange
            {
                Disposable disposable = input.mOpenAiEditTextChangeObservable.subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        Log.d(TAG, "mOpenAiEditTextChangeObservable accept char: " + charSequence);
                        mSharedPreferences.edit().putString(OpenAiProperties.SHARED_OPEN_AI, String.valueOf(charSequence)).apply();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mOpenAiEditTextChangeObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region OpenAI Get API Key Click
            {
                Disposable disposable = input.mGetOpenAITokenObservable.subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        output.mStartIntentSubject.onNext(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(OpenAiProperties.OPEN_AI_API_KEY_URL)));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mGetOpenAITokenObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region SD API Key TextChange
            {
                Disposable disposable = input.mSDAiEditTextChangeObservable.subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        Log.d(TAG, "mSDAiEditTextChangeObservable accept char: " + charSequence);
                        mSharedPreferences.edit().putString(OpenAiProperties.SHARED_SD_AI, String.valueOf(charSequence)).apply();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mSDAiEditTextChangeObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region StableDiffusion Get API Key Click
            {
                Disposable disposable = input.mGetSDAITokenObservable.subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        output.mStartIntentSubject.onNext(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(OpenAiProperties.STABLE_DIFFUSION_API_KEY_URL)));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mGetSDAITokenObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Start Click
            {
                Disposable disposable = input.mStartObservable.subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        output.mStartIntentSubject.onNext(new Intent().setClass(OpenAIApplication.getInstance().getApplicationContext(), MainActivity.class));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mStartObservable accept error: ", throwable);
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
        public InitialValueObservable<CharSequence> mOpenAiEditTextChangeObservable;
        public Observable<Unit> mGetOpenAITokenObservable;
        public InitialValueObservable<CharSequence> mSDAiEditTextChangeObservable;
        public Observable<Unit> mGetSDAITokenObservable;
        public Observable<Unit> mStartObservable;
    }

    public class Output {
        public PublishSubject<Intent> mStartIntentSubject = PublishSubject.create();
        public BehaviorSubject<String> mOpenAIKetSubject = BehaviorSubject.create();
        public BehaviorSubject<String> mStableDiffusionKeySubject = BehaviorSubject.create();
    }
}
