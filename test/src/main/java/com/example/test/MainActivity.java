package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TBL_NAME_STUDENT="student";
    private static final String CONTENT = "content://";
    private static final String AUTHORIY = "edu.niit.android.content.provider";
    private static final String URI = CONTENT + AUTHORIY + "/" + TBL_NAME_STUDENT;

    private static final String NAME="name";
    private static final String CLASSMATE="classmate";
    private static final String AGE="age";

    private String newId;          //insert返回值
    private List<String> contacts;//Student表得属性组成的字符串的集合
    private String selected;       //ListView 中选中的选中项的内容
    private int selectedPos;         //LisView 中选中的索引号
    private Uri uri;
    //listview相关的对象
    private ListView listView;
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       uri=Uri.parse(URI);
        //listview配置适配器
        contacts =query(uri);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,contacts);
        listView=findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //listview选中项的事件监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPos=position;
                selected= (String) parent.getItemAtPosition(position);
            }
        });


    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_query:
            adapter.clear();
            List<String> students=query(uri);
            if (students!=null){
                adapter.addAll(students);
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.btn_add:
           Student student=new Student("张三","软件1721",20);
                ContentValues values=new ContentValues();
                values.put(NAME,student.getName());
                values.put(CLASSMATE,student.getClassmate());
                values.put(AGE,student.getAge());

                Uri newUri=getContentResolver().insert(uri,values);
                if (newUri!=null){
                    newId=newUri.getPathSegments().get(1);
                    String str=newId+"\t"+student.getName()+"\t"+student.getClassmate()+"\t"+student.getAge();
                    adapter.add(str);
                    adapter.notifyDataSetChanged();
                }

                break;
            case  R.id.btn_update:

                break;
            case R.id.btn_delete:

                break;
                default:
                    break;
        }
    }
   private List<String> query(Uri uri){
     List<String> result=new ArrayList<>();
       Cursor cursor=getContentResolver().query(uri,null,null,null,null);
       if (cursor!=null){
           while (cursor.moveToNext()){
               int id=cursor.getInt(cursor.getColumnIndex("_id"));
               String name=cursor.getString(cursor.getColumnIndex(NAME));
               String classmate=cursor.getString(cursor.getColumnIndex(CLASSMATE));
               int age=cursor.getInt(cursor.getColumnIndex(AGE));
               result.add(id+"\t"+name+"\t"+classmate+"\t"+age);
           }
           cursor.close();

       }
        return  result;
   }
}
