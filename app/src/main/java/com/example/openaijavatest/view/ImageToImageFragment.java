package com.example.openaijavatest.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.openaijavatest.R;
import com.example.openaijavatest.data.model.ImageToImageDetail;
import com.example.openaijavatest.view.base.BaseFragment;
import com.example.openaijavatest.viewmodel.ImageToImageViewModel;
import com.example.openaijavatest.viewmodel.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxAdapter;
import com.jakewharton.rxbinding3.widget.RxAdapterView;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kotlin.Unit;

public class ImageToImageFragment extends BaseFragment {

    private final String TAG = "///";

    private MainViewModel mMainViewModel;
    private ImageToImageViewModel mImageToImageViewModel;

    private AppCompatImageView mResultImageView;
    private DrawLineView mDrawLineView;

    private TextInputEditText mPromptInputEditText;

    private SwitchCompat mPictureInputSwitch;

    private AppCompatButton mSelectFileButton;

    private TextView mFileUrlTextView;

    private TextInputEditText mPromptStrengthEditText;

    private Spinner mModelSpinner;

    private TextInputEditText mModelIDEditText;

    private AppCompatButton mSendRequestButton;

    private ActivityResultLauncher<PickVisualMediaRequest> mPickMedia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_to_image_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mImageToImageViewModel = new ViewModelProvider(requireActivity()).get(ImageToImageViewModel.class);

        mResultImageView = view.findViewById(R.id.result_image_view);
        mDrawLineView = view.findViewById(R.id.draw_line_view);
        mPictureInputSwitch = view.findViewById(R.id.model_switch);
        mPromptInputEditText = view.findViewById(R.id.input_prompt_edit_text);
        mSelectFileButton = view.findViewById(R.id.select_file_button);
        mFileUrlTextView = view.findViewById(R.id.file_path_text_view);
        mPromptStrengthEditText = view.findViewById(R.id.prompt_strength_edit_text);
        mModelSpinner = view.findViewById(R.id.model_spinner);
        mModelIDEditText = view.findViewById(R.id.model_edit_text);
        mSendRequestButton = view.findViewById(R.id.send_request_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.models_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mModelSpinner.setAdapter(adapter);

        mImageToImageViewModel.input.mPictureInputSwitchObservable = RxCompoundButton.checkedChanges(mPictureInputSwitch);
        mImageToImageViewModel.input.mSelectFileOrGenerateDrawClickObservable = RxView.clicks(mSelectFileButton);
        mImageToImageViewModel.input.mSendRequestClickObservable = RxView.clicks(mSendRequestButton).map(new Function<Unit, ImageToImageDetail>() {
            @Override
            public ImageToImageDetail apply(Unit unit) throws Exception {
                ImageToImageDetail imageToImageDetail = new ImageToImageDetail();
                imageToImageDetail.setPrompt(mPromptInputEditText.getText().toString());
                imageToImageDetail.setFileUrl(mFileUrlTextView.getText().toString());
                imageToImageDetail.setModelID(mModelIDEditText.getText().toString());
                imageToImageDetail.setStrength(Float.valueOf(mPromptStrengthEditText.getText().toString()));
                return imageToImageDetail;
            }
        });
        mImageToImageViewModel.input.mModelSpinnerObservable = RxAdapterView.itemSelections(mModelSpinner);
        if (mImageToImageViewModel.isSetUp()) {

            //region HideInputMethod
            {
                Disposable disposable = mImageToImageViewModel.output.mCallHideInputMethodSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            mMainViewModel.input.mHideSoftKeyboardSubject.onNext(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mCallHideInputMethodSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Change ResultImageView or DrawLineView
            {
                Disposable disposable = mImageToImageViewModel.output.mSwitchImageOrDrawLineViewSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isShowDrawLineView) throws Exception {
                        if (!isShowDrawLineView) {
                            mDrawLineView.clearHistoryPath();
                        }
                        mDrawLineView.setVisibility(isShowDrawLineView ? View.VISIBLE : View.GONE);
                        mResultImageView.setVisibility(isShowDrawLineView ? View.GONE : View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mSwitchImageOrDrawLineViewSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region SelectFile or Generate Image Button SetText
            {
                Disposable disposable = mImageToImageViewModel.output.mButtonSetTextSubject.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mSelectFileButton.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mButtonSetTextSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Call SelectFile
            {
                Disposable disposable = mImageToImageViewModel.output.mCallSelectFileSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            intentToImagePicker();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Result ImageView
            {
                Disposable disposable = mImageToImageViewModel.output.mResultImageUrlSubject.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String urlString) throws Exception {
                        try {
                            if (mResultImageView.getVisibility() != View.VISIBLE) {
                                mResultImageView.setVisibility(View.VISIBLE);
                                mDrawLineView.setVisibility(View.GONE);
                            }
                            Log.d(TAG, "mResultImageUrlSubject accept Url: " + urlString);
                            Glide.with(mResultImageView)
                                    .load(urlString)
                                    .fitCenter()
                                    .into(mResultImageView);
                        } catch (Exception e) {
                            Log.e(TAG, "mResultImageUrlSubject accept try catch error: ", e);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mResultImageUrlSubject accept: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region DownloadUrl
            {
                Disposable disposable = mImageToImageViewModel.output.mFileUrlSetTextSubject.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "mFileUrlSetTextSubject accept UrlString: " + s);
                        mFileUrlTextView.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mFileUrlSetTextSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Call Generate View to Bitmap
            {
                Disposable disposable = mImageToImageViewModel.output.mCallGenerateDrawLineImageSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean && mDrawLineView.getVisibility() == View.VISIBLE) {
                            mImageToImageViewModel.input.mReceiveGenerateDrawLineViewSubject.onNext(mDrawLineView);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Clear DrawLine
            {
                Disposable disposable = mImageToImageViewModel.output.mClearDrawLineViewSubject.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            mDrawLineView.clearHistoryPath();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mClearDrawLineViewSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            mPickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    mImageToImageViewModel.input.mReceiveSelectFileSubject.onNext(uri);
                }
            });
        }
    }

    private void intentToImagePicker() {

        ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType =
                (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;

        // Launch the photo picker and allow the user to choose only images.
        mPickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(mediaType)
                .build());
    }
}
