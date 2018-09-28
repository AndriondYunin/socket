package com.example.leon.mysocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;




public class MainActivity extends AppCompatActivity {
    EditText ip;
    EditText editText;
    TextView text;
    EditText send;
    private Socket socket = null;
    private static final String HOST = "192.168.0.101";
    private static final int PORT = 6666;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private String content = "";
    private StringBuilder sb = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.display);
        send = findViewById(R.id.sendText);
        ip = findViewById(R.id.ip);

        findViewById(R.id.connectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("连接IP:" + ip.getText() + " 中 ...");
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                //text.setText("发送: "+ send.getText());
                send();
            }
        });
    }
    //-----------------------------------
    private Socket listerSocket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    public void socketConnectThread()
    {
        new Thread() {
            public void run()
            {
                try {
                    listerSocket = new Socket(ip.getText().toString(),6666);
                    OutputStream os = listerSocket.getOutputStream();
                    try {
                        os.write(send.getText().toString().getBytes());
                        os.flush();
                        listerSocket.shutdownOutput();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void send()
    {
        String msg = send.getText().toString();
        text.setText("发送: "+ msg);
        socketConnectThread();
    }
}


