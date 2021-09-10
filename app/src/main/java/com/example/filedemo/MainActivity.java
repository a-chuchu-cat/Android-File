package com.example.filedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button readable;
    private Button writeable;
    private EditText write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String fileName="file_demo.txt";

        readable=(Button) findViewById(R.id.readable);
        writeable=(Button) findViewById(R.id.writeable);
        write=(EditText) findViewById(R.id.write);

        writeable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream output=openFileOutput(fileName,MODE_PRIVATE);
                    String content=write.getText().toString();
                    BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(output);
                    bufferedOutputStream.write(content.getBytes());
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        readable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FileInputStream input=openFileInput(fileName);
                    BufferedInputStream inputStream=new BufferedInputStream(input);
                    byte[] content = new byte[1024];
                    int length=inputStream.read(content);
                    Log.d("length", String.valueOf(length));

                    String string=new String(content,"utf-8");

                    Log.d("byte",string);

                    Toast.makeText(MainActivity.this, "info:"+string, Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}