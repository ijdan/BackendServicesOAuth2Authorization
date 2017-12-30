package com.ijdan.backendas.authorization.exception;

public class ExceptionsHandller extends Exception{

    public ExceptionsHandller(String s) {
        super(s);
    }

    public ExceptionsHandller(String s, Throwable e) {
        super(s, e);
    }
}
