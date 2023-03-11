package com.example.openaijavatest.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.openaijavatest.R;
import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.custom.application.OpenAIApplication;
import com.example.openaijavatest.data.api.request.CreateImageByOpenAIRequest;
import com.example.openaijavatest.data.api.response.CreateImageByOpenAIResponse;
import com.example.openaijavatest.view.base.BaseFragment;
import com.example.openaijavatest.viewmodel.MainViewModel;
import com.example.openaijavatest.viewmodel.WordToImageViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordToImageFragment extends BaseFragment {

    private final String TAG = "///";

    private MainViewModel mMainViewModel;
    private WordToImageViewModel mWordToImageViewModel;

    private TextView mQuestionTextView;
    private AppCompatImageView mResultImageView;
    private TextInputEditText mQueryEditText;
    private SwitchCompat mAISwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.word_to_image_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mWordToImageViewModel = new ViewModelProvider(requireActivity()).get(WordToImageViewModel.class);

        //region initView
        {
            mQuestionTextView = view.findViewById(R.id.question_text_view);
            mResultImageView = view.findViewById(R.id.result_image_view);
            mQueryEditText = view.findViewById(R.id.idEdtQuery);
            mAISwitch = view.findViewById(R.id.ai_switch);
        }
        //endregion

        //region subscribe
        {
            mWordToImageViewModel.input.mTextViewEditorActionEventObservable = RxTextView.editorActionEvents(mQueryEditText);
            mWordToImageViewModel.input.mAiSwitchObservable = RxCompoundButton.checkedChanges(mAISwitch);
            if (mWordToImageViewModel.isSetUp()) {

                //region HideInputMethod
                {
                    Disposable disposable = mWordToImageViewModel.output.mCallHideInputMethodSubject.subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                mMainViewModel.input.mHideSoftKeyboardSubject.onNext(true);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mCallHideInputMethod accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region QuestionTextView setText
                {
                    Disposable disposable = mWordToImageViewModel.output.mQuestionTextViewSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mQuestionTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mQuestionTextViewSetTextSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region Result ImageView
                {
                    Disposable disposable = mWordToImageViewModel.output.mResultImageSetImageSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String urlString) throws Exception {
                            try {
                                Glide.with(mResultImageView)
                                        .load(urlString)
                                        .fitCenter()
                                        .into(mResultImageView);
                            } catch (Exception e) {
                                Log.e(TAG, "mResultImageSetImageSubject accept try catch error: ", e);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mResultImageSetImageSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region Receive Show Toast
                {
                    Disposable disposable = mWordToImageViewModel.output.mShowToastSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mMainViewModel.input.mReceiveToastStringSubject.onNext(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mShowToastSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion
            }
        }
        //endregion
    }
}
