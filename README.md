# Android-File

Android使用Linux的文件系统，开发人员可以建立和访问程序自身建立的私有文件，也可以访问保存在资源目录中的原始文件和XML文件，还可以将文件保存在TF卡等外部存储设备中

 **内部存储**

Android系统允许应用程序创建仅能够自身访问的私有文件，文件保存在设备的内部存储器上，在Android系统下的/data/data/<package name>/files目录中

Android系统不仅支持标准Java的IO类和方法，还提供了能够简化读写流式文件过程的函数

**openFileOutput()函数**

​	openFileOutput()函数为写入数据做准备而打开文件

​	如果指定的文件存在，直接打开文件准备写入数据

​	如果指定的文件不存在，则创建一个新的文件

​	openFileOutput()函数的语法格式如下：

​		public FileOutputStream openFileOutput(String name, int mode)

​		•第1个参数是文件名称，这个参数不可以包含描述路径的斜杠

​		•第2个参数是操作模式，Android系统支持四种文件操作模式

​		•函数的返回值是FileOutputStream类型

四种文件操作模式

| **模式**             | **说明**                                                     |
| -------------------- | ------------------------------------------------------------ |
| MODE_PRIVATE         | 私有模式，缺陷模式，文件仅能够被创建文件的程序访问，或具有相同UID的程序访问。 |
| MODE_APPEND          | 追加模式，如果文件已经存在，则在文件的结尾处添加新数据。     |
| MODE_WORLD_READABLE  | 全局读模式，允许任何程序读取私有文件。                       |
| MODE_WORLD_WRITEABLE | 全局写模式，允许任何程序写入私有文件。                       |

**openFileInput()函数**

​	openFileInput()函数为读取数据做准备而打开文件

​	openFileInput()函数的语法格式如下：

​		public FileInputStream openFileInput (String name) 

​		第1个参数也是文件名称，同样不允许包含描述路径的斜杠

​		使用openFileInput()函数打开已有文件，并以二进制方式读取数据的



**<u>读取文件的一般步骤</u>**

*1.使用openFileInput()获得FileInputStream对象*

*2.通过FileInputStream对象的read()方法读取数据*

*3.关闭FileInputStream对象*



**<u>向文件写入数据的一般步骤</u>**

*1.使用openFileOutput()获得FileOutputStream对象，如果mode为MODE_PRIVATE，则文件不存在时创建文件，文件存在时删除文件内容；如果mode为MODE_APPEND，则文件不存在时创建文件，文件存在时在最后追加。*

*2.通过FileOutputStream对象的write()方法写入数据*

*3.关闭FileOutputStream对象*



**openFileOutput()函数**

为了提高文件系统的性能，一般调用write()函数时，如果写入的数据量较小，系统会把数据保存在数据缓冲区中，等数据量积攒到一定程度时再将数据一次性写入文件

因此，在调用close()函数关闭文件前，务必要调用flush()函数，将缓冲区内所有的数据写入文件

如果开发人员在调用close()函数前没有调用flush()，则可能导致部分数据丢失



示例代码：

**MainActivity.java**

```
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

                    output.close();
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

                    inputStream.close();
                    input.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
```

**activity_main.xml**

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/writeable"
        android:text="Write data to file"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toLeftOf="parent" />

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/readable"
        android:text="Read data from file"
        app:layout_constraintTop_toBottomOf="@id/writeable"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toLeftOf="parent" />

    <EditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/write"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

