package com.example.openaijavatest.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.openaijavatest.R;
import com.example.openaijavatest.view.base.BaseFragment;
import com.example.openaijavatest.viewmodel.LanguageViewModel;
import com.example.openaijavatest.viewmodel.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LanguageFragment extends BaseFragment {

    private final String TAG = "///";

    private MainViewModel mMainViewModel;
    private LanguageViewModel mLanguageViewModel;

    private TextView mResponseTextView;
    private TextView mQuestionTextView;
    private TextInputEditText mQueryEditText;
    private TextView mQuestionTokensTextView;
    private TextView mReplyTokensTextView;
    private TextView mTotalTokensTextView;
    private TextView mPriceTextView;
    private SwitchCompat mModelSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.language_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mLanguageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);

        //region initView
        {
            mResponseTextView = view.findViewById(R.id.response_text_view);
            mQuestionTextView = view.findViewById(R.id.question_text_view);
            mQueryEditText = view.findViewById(R.id.idEdtQuery);
            mQuestionTokensTextView = view.findViewById(R.id.question_tokens_text_view);
            mReplyTokensTextView = view.findViewById(R.id.reply_tokens_text_view);
            mTotalTokensTextView = view.findViewById(R.id.total_tokens_text_view);
            mPriceTextView = view.findViewById(R.id.price_text_view);
            mModelSwitch = view.findViewById(R.id.model_switch);
        }
        //endregion

        //region subscribe
        {
            mLanguageViewModel.input.mTextViewEditorActionEventObservable = RxTextView.editorActionEvents(mQueryEditText);
            mLanguageViewModel.input.mModelSwitchCheckChangeObservable = RxCompoundButton.checkedChanges(mModelSwitch);
            if (mLanguageViewModel.isSetUp()) {

                //region HideInputMethod
                {
                    Disposable disposable = mLanguageViewModel.output.mCallHideInputMethodSubject.subscribe(new Consumer<Boolean>() {
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
                    Disposable disposable = mLanguageViewModel.output.mQuestionSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                            mQuestionTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mQuestionSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region ResponseTextView setText
                {
                    Disposable disposable = mLanguageViewModel.output.mResponseSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mResponseTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mResponseSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region QuestionTokensTextView setText
                {
                    Disposable disposable = mLanguageViewModel.output.mQuestionTokensSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mQuestionTokensTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mQuestionTokensSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region ReplyTokensTextView setText
                {
                    Disposable disposable = mLanguageViewModel.output.mReplyTokensSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mReplyTokensTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mReplyTokensSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region TotalTokensTextView setText
                {
                    Disposable disposable = mLanguageViewModel.output.mTotalTokensSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mTotalTokensTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mTotalTokensSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region PriceTextView setText
                {
                    Disposable disposable = mLanguageViewModel.output.mPriceSetTextSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mPriceTextView.setText(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mPriceSetText accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region Show Toast
                {
                    Disposable disposable = mLanguageViewModel.output.mShowToastSubject.subscribe(new Consumer<String>() {
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
