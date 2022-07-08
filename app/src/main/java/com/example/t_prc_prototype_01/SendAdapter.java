package com.example.t_prc_prototype_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SendAdapter {

    private SQLiteDatabase db = null;
    // DBOpenHelper
    public TestOpenHelper helper;

    private Context context;

    private final static String DB_TABLE = "Send_table"; // テーブル名

    // コンストラクタ
    public SendAdapter(Context context) {
       this.context = context;
       helper = new TestOpenHelper(this.context);
    }

    public SendAdapter() {

    }

    // リストを取得 select
    public Cursor getAllList() {
        db = helper.getWritableDatabase(); // DB の読み書き
        return db.query(TestOpenHelper.TABLE_NAME, null,null,null,null,null,"");
    }



    public SendAdapter openDB() {
        db = helper.getWritableDatabase(); // DB の読み書き
        return this;
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    public void closeDB() {
        db.close(); // DB を閉じる
        db = null;
    }


    // 1 = 商品名  2 = 段取時間 3 = 作業時間 4 = 良品数 5 = 最終チェック
    public void saveDB(String Product_Name, String Processing_Time,String Work_Hours, String Good_Products_Num, String Final_Process_Check) {


        db = helper.getWritableDatabase();

        try {
            // トランザクション　開始
            db.beginTransaction();

            ContentValues values = new ContentValues();
            //------ インサート用　キー　, カラム名
            // 担当者名
            //    values.put("Tantou_Name",Tantou_Name);
            // 商品名
            values.put("Product_Name",Product_Name);
            // 標準工数
            //    values.put("Hyoujyun_Hours",Hyoujyun_Hours);
            // 段取時間
            values.put("Processing_Time",Processing_Time);
            // 作業時間
            values.put("Work_Hours",Work_Hours);
            // 良品数
            values.put("Good_Products_Num", Good_Products_Num);
            // 最終工程　チェック
            values.put("Final_Process_Check",Final_Process_Check);

            // インサート
            db.insert(TestOpenHelper.TABLE_NAME, null,values);

            // トランザクション　成功
            db.setTransactionSuccessful();

            System.out.println("Send インサート　成功");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了

            db.endTransaction();  //------------------> トランザクション終了

        }

    }//---------------- insert END


    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, "");
    }


}
