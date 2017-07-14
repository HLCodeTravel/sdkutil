package com.transsion.json;

public interface OutputHandler {

    OutputHandler write(String value);

    int write(String value, int start, int end, String append);

    int write(String value, int start, int end);
}
