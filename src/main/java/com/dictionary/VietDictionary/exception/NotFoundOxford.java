package com.dictionary.VietDictionary.exception;

public class NotFoundOxford extends RuntimeException{
    public NotFoundOxford(String word, Exception e)
    {
        super(word+" is not found!");
    }
}
