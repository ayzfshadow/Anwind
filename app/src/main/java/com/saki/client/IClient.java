package com.saki.client;

public interface IClient {
    public int getRequstionID();

    public void send(byte[] bin);
}
