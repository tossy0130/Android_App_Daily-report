package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Currency;

public class File_Send_log extends AppCompatActivity {

    Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<String>send_num, send_file_name, send_time, send_Stats, send_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__send_log);


        // recycleview　
        recyclerView = findViewById(R.id.RecyclerView1);

        send_num = new ArrayList<>();
        send_file_name = new ArrayList<>();
        send_time = new ArrayList<>();
        send_Stats = new ArrayList<>();
        send_count = new ArrayList<>();

        Get_View_List();

        // アダプターセット
        adapter = new Adapter(File_Send_log.this,send_num,send_file_name,
                send_time,send_Stats,send_count);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(File_Send_log.this));

        // リスト毎に線を引く
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void Get_View_List() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_09 = helper.getReadableDatabase();

        // トランザクション　開始
        db_09.beginTransaction();

        Cursor cursor = null;

        try {

            cursor = db_09.rawQuery("SELECT * from Send_table_01_log", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    do {

                        send_num.add(cursor.getString(0));
                        send_file_name.add(cursor.getString(1));
                        send_time.add(cursor.getString(2));
                        send_Stats.add(cursor.getString(3));
                        send_count.add(cursor.getString(4));

                    } while(cursor.moveToNext());
                }
            } //----------- END if

            // トランザクション　成功
            db_09.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            db_09.endTransaction();

            if(db_09 != null) {
                db_09.close();
            }
        }

    }

}