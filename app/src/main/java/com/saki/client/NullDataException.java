package com.saki.client;

/**
 * Created by Saki on 2018/8/8.
 */

public class NullDataException extends Exception{
    public NullDataException(){
        super("NullDataException:数据内容长度为0");
    }
}
