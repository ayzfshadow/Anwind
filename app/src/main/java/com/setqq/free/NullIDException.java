package com.setqq.free;

public class NullIDException extends Exception {
    public NullIDException(String path) {
        super("lua空id异常,脚本中未配置识别id:" + path);
    }
}
