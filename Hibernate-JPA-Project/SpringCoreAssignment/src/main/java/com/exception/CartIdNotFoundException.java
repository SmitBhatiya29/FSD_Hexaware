package com.exception;

public class CartIdNotFoundException extends Exception{
    String message;
    public CartIdNotFoundException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
