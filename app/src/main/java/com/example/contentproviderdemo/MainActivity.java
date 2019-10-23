package com.example.contentproviderdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private ListView listView;
   List<String> contacts=new ArrayList<>();
   ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRead=findViewById(R.id.btn_read_caontact);
        btnRead.setOnClickListener(this);

        listView=findViewById(R.id.list_view);
        contacts=new ArrayList<>();
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,contacts);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
     //判断是否有运行的权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
            return;
        }
        //读取联系人
       readContacts();
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults){
        if (requestCode==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContacts();
        }else {
            Toast.makeText(MainActivity.this,"申请权限被拒绝",Toast.LENGTH_SHORT).show();
        }
    }

    private void readContacts() {
        Cursor cursor=null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor!=null){
                while (cursor.moveToNext()){
                    //获取联系人姓名
                    String displayName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds
                    .Phone.DISPLAY_NAME));
                    //获取联系人手机号码
                    String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds
                            .Phone.NUMBER));
                    contacts.add(displayName+"\n"+number);
                }
                adapter.notifyDataSetChanged();//通知adapter更新数据，要保证contacts的数据是有长度变化的
                  //设置listview的监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String contact=(String) parent.getItemAtPosition(position);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
    }
}
