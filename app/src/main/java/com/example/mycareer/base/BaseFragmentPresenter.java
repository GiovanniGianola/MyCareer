package com.example.mycareer.base;

public interface BaseFragmentPresenter<V extends BaseFragmentView> {
    void attachView(V view);
    void detachView();
}
