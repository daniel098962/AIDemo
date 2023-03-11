package com.example.openaijavatest.viewmodel;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.viewmodel.base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class MainViewModel extends BaseViewModel {

    private final String TAG = "///";

    public Input input = new Input();
    public Output output = new Output();

    private MainViewModel mMainViewModel = this;

    public boolean isAllSetUp() {

        boolean result = false;

        try {
            //region BottomNavigation
            {
                Disposable disposable = input.mBottomNavigationViewItemSelected.subscribe(new Consumer<MenuItem>() {
                    @Override
                    public void accept(MenuItem menuItem) throws Exception {

                        switch (menuItem.getOrder()) {
                            case OpenAiProperties.LANGUAGE_PAGE: {
                                output.mFragmentOrderSubject.onNext(OpenAiProperties.LANGUAGE_PAGE);
                                break;
                            }
                            case OpenAiProperties.WORD_TO_IMAGE: {
                                output.mFragmentOrderSubject.onNext(OpenAiProperties.WORD_TO_IMAGE);
                                break;
                            }
                            case OpenAiProperties.IMAGE_TO_IMAGE: {
                                output.mFragmentOrderSubject.onNext(OpenAiProperties.IMAGE_TO_IMAGE);
                                break;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mBottomNavigationViewItemSelected accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region HideSoftKeyboard
            {
                Disposable disposable = input.mHideSoftKeyboardSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        output.mCallHideInputMethodSubject.onNext(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mHideSoftKeyboard accept: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Receive Show Toast String
            {
                Disposable disposable = input.mReceiveToastStringSubject.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!TextUtils.isEmpty(s)) {
                            output.mCallShowToastStringSubject.onNext(s);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mReceiveToastStringSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            result = true;
        } catch (Exception e) {
            Log.e(TAG, "isLanguageSetUp try catch error: ", e);
        }

        return result;
    }

    public class Input {
        public Observable<MenuItem> mBottomNavigationViewItemSelected;

        public PublishSubject<Boolean> mHideSoftKeyboardSubject = PublishSubject.create();

        public PublishSubject<String> mReceiveToastStringSubject = PublishSubject.create();
    }

    public class Output {

        public PublishSubject<Integer> mFragmentOrderSubject = PublishSubject.create();

        public PublishSubject<Boolean> mCallHideInputMethodSubject = PublishSubject.create();

        public PublishSubject<String> mCallShowToastStringSubject = PublishSubject.create();
    }
}
