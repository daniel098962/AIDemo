package com.example.openaijavatest.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.openaijavatest.R;
import com.example.openaijavatest.custom.OpenAiProperties;
import com.example.openaijavatest.custom.application.OpenAIApplication;
import com.example.openaijavatest.data.api.request.StableDiffusionImg2ImgRequest;
import com.example.openaijavatest.data.api.response.StableDiffusionImg2ImgResponse;
import com.example.openaijavatest.data.model.ImageToImageDetail;
import com.example.openaijavatest.viewmodel.base.BaseViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.InitialValueObservable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageToImageViewModel extends BaseViewModel {

    private final String TAG = "///";

    public Input input = new Input();
    public Output output = new Output();

    private boolean mIsUseDrawLine = false;
    private String mControlNetModelString = "canny";

    private SharedPreferences mSharedPreferences = OpenAIApplication.getInstance().getSharedPreferences(OpenAiProperties.SHARED_NAME, Context.MODE_PRIVATE);
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mFirebaseStorage.getReference();
    private InputStream mInputStream = null;

    public boolean isSetUp() {

        boolean result = false;
        try {

            //region Picture Input Switch
            {
                Disposable disposable = input.mPictureInputSwitchObservable.subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isUseDrawLine) throws Exception {
                        mIsUseDrawLine = isUseDrawLine;
                        //Change Button Text ,  Change ImageView or DrawLineView
                        output.mButtonSetTextSubject.onNext(mIsUseDrawLine ? "Generate" : "Select");
                        output.mSwitchImageOrDrawLineViewSubject.onNext(mIsUseDrawLine);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mPictureInputSwitchObservable accept: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region ImageUploadButton Click
            {
                Disposable disposable = input.mSelectFileOrGenerateDrawClickObservable.subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {

                        if (mIsUseDrawLine) {
                            output.mCallGenerateDrawLineImageSubject.onNext(true);
                            return;
                        }
                        output.mCallSelectFileSubject.onNext(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mSelectFileClickObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region SendRequestButton Click
            {
                Disposable disposable = input.mSendRequestClickObservable.subscribe(new Consumer<ImageToImageDetail>() {
                    @Override
                    public void accept(ImageToImageDetail imageToImageDetail) throws Exception {
                        output.mCallHideInputMethodSubject.onNext(true);

                        String inputPrompt = imageToImageDetail.getPrompt();
                        String fileUrl = imageToImageDetail.getFileUrl();
                        Float promptStrength = imageToImageDetail.getStrength().floatValue();
                        String modelID = imageToImageDetail.getModelID();
                        String controlNetModelID = mControlNetModelString;

                        if (!TextUtils.isEmpty(inputPrompt) && !TextUtils.isEmpty(fileUrl) && !TextUtils.isEmpty(modelID) && !TextUtils.isEmpty(controlNetModelID)) {

                            StableDiffusionImg2ImgRequest request = new StableDiffusionImg2ImgRequest();
                            request.setToken(mSharedPreferences.getString(OpenAiProperties.SHARED_SD_AI, ""));
                            request.setPrompt(inputPrompt);
                            request.setSeedImageUrlString(fileUrl);
                            request.setModelID(modelID);
                            request.setControlNetModel(controlNetModelID);
                            request.setStrength(promptStrength);
                            Log.d(TAG, "SendRequest request: " + new Gson().toJson(request));

                            OpenAIApplication.getInstance().getNetworkService().callControlNetImageByStableDiffusion(request).enqueue(new Callback<StableDiffusionImg2ImgResponse>() {
                                @Override
                                public void onResponse(Call<StableDiffusionImg2ImgResponse> call, Response<StableDiffusionImg2ImgResponse> response) {

                                    if (response.isSuccessful() && response.body() != null && TextUtils.equals(response.body().getStatus(), "success")) {
                                        Log.d(TAG, "onResponse gson: " + new Gson().toJson(response.body()));
                                        StableDiffusionImg2ImgResponse img2ImgResponse = response.body();
                                        if (img2ImgResponse.getOutputList().size() > 0) {
                                            String urlString = img2ImgResponse.getOutputList().get(0).getAsString();
                                            Log.d(TAG, "onResponse original UrlString: " + urlString);
                                            if (!urlString.startsWith("https://")) {
                                                urlString = urlString.substring(urlString.indexOf("https://"));
                                                int lastIndex = urlString.lastIndexOf(".png");
                                                urlString = urlString.substring(0, lastIndex + 4);
                                            }
                                            output.mResultImageUrlSubject.onNext(urlString);
                                            output.mClearDrawLineViewSubject.onNext(true);
                                        }
                                    }

                                    if (!response.isSuccessful()) {
                                        Log.d(TAG, "onResponse fail: " + response.errorBody());
                                    }
                                }

                                @Override
                                public void onFailure(Call<StableDiffusionImg2ImgResponse> call, Throwable t) {
                                    Log.e(TAG, "callControlNetImageByStableDiffusion onFailure: ", t);
                                }
                            });
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mSendRequestClickObservable accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Get Select File Uri and Upload
            {
                Disposable disposable = input.mReceiveSelectFileSubject.subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        try {
                            mInputStream = OpenAIApplication.getInstance().getApplicationContext().getContentResolver().openInputStream(uri);
                            mStorageReference.child(uri.getPath()).putStream(mInputStream).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    return task.getResult().getStorage().getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    try {
                                        mInputStream.close();
                                    } catch (Exception e) {
                                        Log.e(TAG, "InputStream Close error: ", e);
                                    }

                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Log.d(TAG, "mUploadTask onComplete Uri: " + downloadUri);
                                        output.mFileUrlSetTextSubject.onNext(downloadUri.toString());
                                        output.mResultImageUrlSubject.onNext(downloadUri.toString());
                                    }

                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "mUploadTask onComplete fail: ", task.getException());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            Log.e(TAG, "mReceiveSelectFileSubject accept try catch error: ", e);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mReceiveSelectFileSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Generate DrawLineView to Bitmap and Upload
            {
                Disposable disposable = input.mReceiveGenerateDrawLineViewSubject.subscribe(new Consumer<View>() {
                    @Override
                    public void accept(View view) throws Exception {

                        try {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            loadBitmapFromView(view).compress(Bitmap.CompressFormat.JPEG, 80, baos);
                            byte[] data = baos.toByteArray();

                            mStorageReference.child(String.valueOf(System.currentTimeMillis())).putBytes(data).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    return task.getResult().getStorage().getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    try {
                                        baos.close();
                                    } catch (Exception e) {
                                        Log.e(TAG, "Baos Close error: ", e);
                                    }

                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Log.d(TAG, "mUploadTask onComplete Uri: " + downloadUri);
                                        output.mFileUrlSetTextSubject.onNext(downloadUri.toString());
                                    }

                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "mUploadTask onComplete fail: ", task.getException());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            Log.e(TAG, "mReceiveGenerateDrawLineViewSubject accept try catch error: ", e);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mReceiveGenerateDrawLineViewSubject accept error: ", throwable);
                    }
                });
                addDisposable(disposable);
            }
            //endregion

            //region Spinner Select
            {
                Disposable disposable = input.mModelSpinnerObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        mControlNetModelString = OpenAIApplication.getInstance().getApplicationContext().getResources().getStringArray(R.array.models_array)[position];
                        Log.d(TAG, "mModelSpinnerObservable accept String: " + mControlNetModelString);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "mModelSpinnerObservable accept error: ", throwable);
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

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public class Input {

        public InitialValueObservable<Boolean> mPictureInputSwitchObservable;
        public Observable<Unit> mSelectFileOrGenerateDrawClickObservable;
        public Observable<ImageToImageDetail> mSendRequestClickObservable;
        public InitialValueObservable<Integer> mModelSpinnerObservable;

        public PublishSubject<Uri> mReceiveSelectFileSubject = PublishSubject.create();
        public PublishSubject<View> mReceiveGenerateDrawLineViewSubject = PublishSubject.create();
    }

    public class Output {
        public PublishSubject<Boolean> mCallHideInputMethodSubject = PublishSubject.create();
        public PublishSubject<Boolean> mSwitchImageOrDrawLineViewSubject = PublishSubject.create();
        public PublishSubject<Boolean> mCallSelectFileSubject = PublishSubject.create();
        public PublishSubject<Boolean> mCallGenerateDrawLineImageSubject = PublishSubject.create();
        public PublishSubject<String> mResultImageUrlSubject = PublishSubject.create();
        public PublishSubject<String> mButtonSetTextSubject = PublishSubject.create();
        public PublishSubject<String> mFileUrlSetTextSubject = PublishSubject.create();
        public PublishSubject<Boolean> mClearDrawLineViewSubject = PublishSubject.create();

        public PublishSubject<String> mShowToastSubject = PublishSubject.create();
    }
}
