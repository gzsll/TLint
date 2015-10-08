package com.gzsll.hupu.api;

import retrofit.mime.TypedString;

/**
 * Created by sll on 2015/7/3.
 */
public class TypedJsonString extends TypedString {
    public TypedJsonString(String body) {
        super(body);
    }

    @Override
    public String mimeType() {
        return "application/json";
    }
}
