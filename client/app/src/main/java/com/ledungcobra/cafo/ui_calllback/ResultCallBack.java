package com.ledungcobra.cafo.ui_calllback;

public interface ResultCallBack<T,E> {
    void onResult(T result);
    void onFailure(E error);
}
