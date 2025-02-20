package com.example.demo.global.apipayLoad.handler;


import com.example.demo.global.apipayLoad.code.BaseCode;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseCode errorCode) { super((ErrorStatus) errorCode); }
}