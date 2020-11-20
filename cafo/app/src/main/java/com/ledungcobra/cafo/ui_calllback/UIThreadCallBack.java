package com.ledungcobra.cafo.ui_calllback;

public interface UIThreadCallBack<T,E> extends ResultCallBack<T,E> {

    void stopProgressIndicator();
    void startProgressIndicator();

}
