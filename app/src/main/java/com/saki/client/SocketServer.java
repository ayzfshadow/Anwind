package com.saki.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public abstract class SocketServer {
    private class ReadThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    onReceive(read());
                } catch (IOException e) {
                    onReadException(e);
                    return;
                }
            }
        }
    }

    private DataInputStream in;
    private OutputStream out;
    private Socket socket;
    private String ip;
    private int port;

    public SocketServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean connection() {
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            new ReadThread().start();
            out = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onReadException(IOException e) {
        shutDown();
        e.printStackTrace();
    }

    public abstract void onReceive(byte[] bin);

    public abstract byte[] read() throws IOException;

    protected byte[] readBlock(int len) throws IOException {
        byte[] dst = new byte[len];
        in.readFully(dst);
        return dst;
    }

    protected byte readByte() throws IOException {
        return in.readByte();
    }

    protected int readInt() throws IOException {
        return in.readInt();
    }

    public void send(byte[] bin) throws IOException {
        out.write(bin);
        out.flush();
    }

    public void shutDown() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
