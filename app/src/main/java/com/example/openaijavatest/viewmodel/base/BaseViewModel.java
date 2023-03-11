package com.example.openaijavatest.viewmodel.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable;

    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        boolean success = false;
        if (disposable != null) {
            success = compositeDisposable.remove(disposable);
        }
    }

    @Override
    protected void onCleared() {

        if (compositeDisposable != null) {

            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        super.onCleared();
    }
}
