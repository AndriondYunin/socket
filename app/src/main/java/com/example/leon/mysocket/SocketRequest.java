package com.example.leon.mysocket;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class SocketRequest extends Thread {

    private static final String TAG = SocketRequest.class.getSimpleName();

    private String mServerHost;// 服务器地址
    private int mServerPort;// 服务端口号
    private String mSendMessage;// 要发送的消息


    public SocketRequest(String serverHost, int serverPort, String sendMessage) {
        // TODO Auto-generated constructor stub
        this.mServerHost = serverHost;
        this.mServerPort = serverPort;
        this.mSendMessage = sendMessage;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        Socket socket = new Socket();
        try {
            Log.d(TAG, "请求连接到服务器...");
            SocketAddress socketAddress = new InetSocketAddress(mServerHost,
                    mServerPort);
            socket.connect(socketAddress, 5 * 1000);// 设置目标地址,请求超时限制

            // 判断是否连接成功
            if (socket.isConnected()) {
                // 发送消息
                DataOutputStream dos = new DataOutputStream(
                        socket.getOutputStream());
                Log.d(TAG, "连接成功,开始发送消息,发送内容：" + mSendMessage);
                dos.writeBytes(mSendMessage);// 服务区/客户端双方的写/读方式要一直,否则会报错
                //dos.flush();// 刷新输出流，使Server马上收到该字符串

                // 接收服务器消息
                DataInputStream dis = new DataInputStream(
                        socket.getInputStream());
                Log.d(TAG, "接收到服务器反馈消息：" + dis.readByte());

                dos.close();
                dis.close();
            } else {
                Log.e(TAG, "未能成功连接至服务器！");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (e instanceof SocketTimeoutException) {
                Log.e(TAG, "连接超时!");
            } else {
                Log.e(TAG, "通讯过程发生异常:" + e.toString());
            }
        } finally {
            try {
                socket.close();
                this.interrupt();
                Log.d(TAG, "关闭连接.");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
