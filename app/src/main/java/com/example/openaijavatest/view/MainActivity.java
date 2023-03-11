package com.example.openaijavatest.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.openaijavatest.R;
import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.utility.SoftKeyBoardListener;
import com.example.openaijavatest.view.base.BaseActivity;
import com.example.openaijavatest.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jakewharton.rxbinding3.material.RxBottomNavigationView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private final String TAG = "///";
    private MainViewModel mMainViewModel;
    private View mRootLayout;
    private BottomNavigationView mBottomNavigationView;
    private SoftKeyBoardListener mSoftKeyBoardListener;

    private FragmentManager mFragmentManager;
    private LanguageFragment mLanguageFragment = new LanguageFragment();
    private WordToImageFragment mWordToImageFragment = new WordToImageFragment();
    private ImageToImageFragment mImageToImageFragment = new ImageToImageFragment();
    private Fragment mCurrentFragment = null;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region initData
        {
            mFragmentManager = getSupportFragmentManager();
            mSoftKeyBoardListener = new SoftKeyBoardListener(MainActivity.this);
            mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        //endregion

        //region initView
        {
            mRootLayout = findViewById(R.id.root_layout);
            mBottomNavigationView = findViewById(R.id.bottomNavigationView);
            mFragmentManager.beginTransaction().replace(R.id.fragment_layout, mLanguageFragment, OpenAiProperties.LANGUAGE_PAGE_KEY).addToBackStack(null).commit();
            mCurrentFragment = mLanguageFragment;
        }
        //endregion

        //region subscribe
        {
            mMainViewModel.input.mBottomNavigationViewItemSelected = RxBottomNavigationView.itemSelections(mBottomNavigationView);
            if (mMainViewModel.isAllSetUp()) {

                //region Receive FragmentOrder to change Fragment
                {
                    Disposable disposable = mMainViewModel.output.mFragmentOrderSubject.subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {

                            hideInputMethod();

                            Fragment fragment = null;
                            String tag = "";
                            switch (integer) {
                                case OpenAiProperties.LANGUAGE_PAGE: {
                                    fragment = mLanguageFragment;
                                    tag = OpenAiProperties.LANGUAGE_PAGE_KEY;
                                    break;
                                }
                                case OpenAiProperties.WORD_TO_IMAGE: {
                                    fragment = mWordToImageFragment;
                                    tag = OpenAiProperties.WORD_TO_IMAGE_PAGE_KEY;
                                    break;
                                }
                                case OpenAiProperties.IMAGE_TO_IMAGE: {
                                    fragment = mImageToImageFragment;
                                    tag = OpenAiProperties.IMAGE_TO_IMAGE_PAGE_KEY;
                                    break;
                                }
                            }

                            if (fragment != null) {

                                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();


                                if (mFragmentManager.findFragmentByTag(tag) != null) {

                                    if (mFragmentManager.findFragmentByTag(tag) == mCurrentFragment) {
                                        return;
                                    }

                                    fragmentTransaction.show(fragment).addToBackStack(mCurrentFragment.getTag());
                                } else {
                                    fragmentTransaction.add(R.id.fragment_layout, fragment, tag).addToBackStack(mCurrentFragment.getTag());
                                }

                                if (mCurrentFragment != null) {
                                    fragmentTransaction.hide(mCurrentFragment);
                                }

                                fragmentTransaction.commit();
                                mCurrentFragment = fragment;
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mFragmentOrderSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region Receive HideSoftKeyBoard
                {
                    Disposable disposable = mMainViewModel.output.mCallHideInputMethodSubject.subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                hideInputMethod();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mCallHideInputMethod accept: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion

                //region Show Toast
                {
                    Disposable disposable = mMainViewModel.output.mCallShowToastStringSubject.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, "mCallShowToastStringSubject accept error: ", throwable);
                        }
                    });
                    addDisposable(disposable);
                }
                //endregion
            }
        }
        //endregion

        //region Listener
        {
            mSoftKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                @Override
                public void keyBoardShow(int height) {
                }

                @Override
                public void keyBoardHide(int height) {
                    new Handler().post(() -> {
                        hideInputMethod();
                    });
                }
            });
        }
        //endregion
    }

    @Override
    public void onBackPressed() {
        //Nothing to do.
    }

    private void hideInputMethod() {
        mInputMethodManager.hideSoftInputFromWindow(mRootLayout.getWindowToken(), 0);
    }
}