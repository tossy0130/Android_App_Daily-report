package com.example.t_prc_prototype_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {


    private final static String DB_NAME = "Send.db"; // DB 名

    // 送信用テーブル
    private final static String DB_TABLE = "Send_table_01"; // テーブル名
    // 送信ログ用　テーブル
    private final static String LOG_DB_TABLE = "Send_table_01_log"; // テーブル名
    // 総生産用　グラフ　テーブル
    private final static String GRAFU_DB_TABLE = "Send_Grafu_Table"; // テーブル名

    private final static int DB_VERSION = 1; // データベース　バージョン

    /**
     * DBのカラム名
     */
    public final static String COL_ID = "id"; // 項目　01
    public final static String SEND_COL_01 = "send_col_01"; //
    public final static String SEND_COL_02 = "send_col_02"; //
    public final static String SEND_COL_03 = "send_col_03"; //
    public final static String SEND_COL_04 = "send_col_04"; //
    public final static String SEND_COL_05 = "send_col_05"; //
    public final static String SEND_COL_06 = "send_col_06"; //
    public final static String SEND_COL_07 = "send_col_07"; //
    public final static String SEND_COL_08 = "send_col_08"; //
    public final static String SEND_COL_09 = "send_col_09"; //
    public final static String SEND_COL_10 = "send_col_10"; //
    public final static String SEND_COL_11 = "send_col_11"; //
    public final static String SEND_COL_12 = "send_col_12"; //
    public final static String SEND_COL_13 = "send_col_13"; //
    public final static String SEND_COL_14 = "send_col_14"; //
    public final static String SEND_COL_15 = "send_col_15"; //
    public final static String SEND_COL_16 = "send_col_16"; //
    public final static String SEND_COL_17 = "send_col_17"; //
    // 加工数追加
    public final static String SEND_COL_18 = "send_col_18";
    // 送信スタッツ
    public final static String SEND_COL_19 = "send_col_19"; //
    // 開始時間
    public final static String SEND_COL_20 = "send_col_20";
    // 終了時間
    public final static String SEND_COL_21 = "send_col_21";
    // グラフ用 ID Send_id
    public final static String SEND_COL_22 = "send_col_22";

    // ****** CSV 項目 3用 *******
    public final static String SEND_COL_23 = "send_col_23";
    /**
     * 　「樹脂成型課」用　項目　３カラム　追加
     */
    public final static String SEND_COL_24 = "send_col_24"; // 色段取時間

    public final static String SEND_COL_25 = "send_col_25"; // 型段取時間

    public final static String SEND_COL_26 = "send_col_26"; // 機械コード



    /**
     * 　ログ　テーブル　カラム名
     */
    public final static String LOG_COL_ID = "log_id"; // 項目　01
    public final static String LOG_SEND_COL_01 = "log_send_col_01"; // 送信ファイル名
    public final static String LOG_SEND_COL_02 = "log_send_col_02"; //　送信時間
    public final static String LOG_SEND_COL_03 = "log_send_col_03"; //　送信スタッツ
    public final static String LOG_SEND_COL_04 = "log_send_col_04"; //　ファイル数

    /**
     *  Send_Grafu_Table => 総生産用　グラフ　テーブル
     *
     */

    public final static String GRAFU_DB_TABLE_ID =  "id"; // id
    public final static String GRAFU_DB_TABLE_SEND_ID = "Send_id"; // タスク番号
    public final static String GRAFU_DB_TABLE_SOU_NUM_01 = "Sou_Num_01"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_02 = "Sou_Num_02"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_03 = "Sou_Num_03"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_04 = "Sou_Num_04"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_05 = "Sou_Num_05"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_06 = "Sou_Num_06"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_07 = "Sou_Num_07"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_08 = "Sou_Num_08"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_09 = "Sou_Num_09"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_10 = "Sou_Num_10"; // 総生産　値 01
    public final static String GRAFU_DB_TABLE_SOU_NUM_11 = "Sou_Num_11"; // 総生産　値 01




    public SQLiteDatabase db = null; // SQLiteDatabase
    public DBHelper dbHelper = null;           // DBHepler
    private Context context;

    // コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DBの読み書き
     * openDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase(); // DB の読み書き
        return this;
    }

    /**
     * DBの読み込み 今回は未使用
     * readDB()
     *
     * @return this 自身のオブジェクト
     */

    public DBAdapter readDB() {
        db = dbHelper.getReadableDatabase(); // DB の　読み込み
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


    /**
     * DBのレコードへ登録
     * saveDB()
     *
     *
     */

    // ************* 加工数　追加
    public void saveDB(String send_col_num_01, String send_col_num_02, String send_col_num_03,
                       String send_col_num_04, String send_col_num_05, String send_col_num_06,
                       String send_col_num_07, String send_col_num_08, String send_col_num_09,
                       String send_col_num_10, String send_col_num_11, String send_col_num_12,
                       String send_col_num_13, String send_col_num_14, String send_col_num_15,
                       String send_col_num_16, String send_col_num_17,String send_col_num_18,
                       String send_col_num_19,String send_col_num_20,String send_col_num_21,
                       String send_col_num_22,String send_col_num_23,String send_col_num_24,
                       String send_col_num_25,String send_col_num_26
    ) {

        // トランザクション　開始
        db.beginTransaction();

        try {
            // ContentValuesでデータを設定していく
            ContentValues values = new ContentValues();
            values.put(SEND_COL_01, send_col_num_01);
            values.put(SEND_COL_02, send_col_num_02);
            values.put(SEND_COL_03, send_col_num_03);
            values.put(SEND_COL_04, send_col_num_04);
            values.put(SEND_COL_05, send_col_num_05);
            values.put(SEND_COL_06, send_col_num_06);
            values.put(SEND_COL_07, send_col_num_07);
            values.put(SEND_COL_08, send_col_num_08);
            values.put(SEND_COL_09, send_col_num_09);
            values.put(SEND_COL_10, send_col_num_10);
            values.put(SEND_COL_11, send_col_num_11);
            values.put(SEND_COL_12, send_col_num_12);
            values.put(SEND_COL_13, send_col_num_13);
            values.put(SEND_COL_14, send_col_num_14);
            values.put(SEND_COL_15, send_col_num_15);
            values.put(SEND_COL_16, send_col_num_16);
            values.put(SEND_COL_17, send_col_num_17);
            /**
             * 加工数　追加  send_col_num_18
             */
            values.put(SEND_COL_18, send_col_num_18);
            values.put(SEND_COL_19, send_col_num_19); // 送信フラグ
            values.put(SEND_COL_20, send_col_num_20); // 開始時間　追加
            values.put(SEND_COL_21, send_col_num_21); // 終了時間　追加
            values.put(SEND_COL_22, send_col_num_22); // グラフ用 id 追加
            values.put(SEND_COL_23, send_col_num_23); // グラフ用 id 追加
            /**
             *  「樹脂成型課」　用　３カラム　追加
             */
            values.put(SEND_COL_24, send_col_num_24); // 色段取時間
            values.put(SEND_COL_25, send_col_num_25); // 型段取時間
            values.put(SEND_COL_26, send_col_num_26); // 機械コード

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE, null, values);      // レコードへ登録
            System.out.println("DB_TABLE インサート　完了");
            db.setTransactionSuccessful(); // トランザクションへコミット
            System.out.println("インサート　トランザクション　完了");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //------------------> トランザクション終了
        }

    }


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
        return db.query(DB_TABLE, columns, null, null, null, null, "id ASC");
    }


    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */

    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(DB_TABLE, columns, column + " like ?", name, null,null,null);
    }

    /**
     * DBのレコードを全削除
     * allDelete()
     */
    public void allDelete() {

        db.beginTransaction(); // トランザクションの開始

        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用

            db.delete(DB_TABLE, null,null); // DB のレコードを全削除

            db.setTransactionSuccessful(); // トランザクションへのコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //----------------------------> トランザクション終了
        }
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    public void selectDelete(String position) {

        db.beginTransaction(); //------ トランザクション開始

        try {

            db.delete(DB_TABLE, COL_ID + "=?", new String[]{position});

            db.setTransactionSuccessful(); // トランザクションへコミット

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //-----------------------------> トランザクション　終了
        }
    }

    public void selectDelete_02(String position) {

        db.beginTransaction(); //------ トランザクション開始

        try {

            db.delete(GRAFU_DB_TABLE, GRAFU_DB_TABLE_ID + "=?", new String[]{position});

            db.setTransactionSuccessful(); // トランザクションへコミット

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //-----------------------------> トランザクション　終了
        }
    }

    public void selectUpdate(String send_col_num_08, String position) {

        db.beginTransaction(); //------ トランザクジョン開始

        try {
            // ContentValuesでデータを設定していく
            ContentValues values = new ContentValues();
            values.put(SEND_COL_08,send_col_num_08);

            db.update(DB_TABLE, values,COL_ID + "=?",new String[]{position});
            System.out.println("DB_TABLE アップデート　完了");

            db.setTransactionSuccessful(); // トランザクションへコミット
            System.out.println("DB_TABLE アップデート　トランザクション　完了");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //-----------------------------> トランザクション　終了
        }

    }



    /**
     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
     * DBHelper
     */
    static class DBHelper extends SQLiteOpenHelper {

        // コンストラクタ
        public DBHelper(Context context) {
            //第1引数：コンテキスト
            //第2引数：DB名
            //第3引数：factory nullでよい
            //第4引数：DBのバージョン
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * DB生成時に呼ばれる
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            // テーブルの作成 ※　スペースに気を付ける 最初の( &&  カラムの頭にスペース
            String createTb1 = "CREATE TABLE " + DB_TABLE +" ("
                    + COL_ID + " INTEGER primary key,"
                    + SEND_COL_01 + " TEXT,"
                    + SEND_COL_02 + " TEXT,"
                    + SEND_COL_03 + " TEXT,"
                    + SEND_COL_04 + " TEXT,"
                    + SEND_COL_05 + " TEXT,"
                    + SEND_COL_06 + " TEXT,"
                    + SEND_COL_07 + " TEXT,"
                    + SEND_COL_08 + " TEXT,"
                    + SEND_COL_09 + " TEXT,"
                    + SEND_COL_10 + " TEXT,"
                    + SEND_COL_11 + " TEXT,"
                    + SEND_COL_12 + " TEXT,"
                    + SEND_COL_13 + " TEXT,"
                    + SEND_COL_14 + " TEXT,"
                    + SEND_COL_15 + " TEXT,"
                    + SEND_COL_16 + " TEXT,"
                    + SEND_COL_17 + " TEXT,"
                    + SEND_COL_18 + " TEXT,"
                    + SEND_COL_19 + " TEXT,"
                    + SEND_COL_20 + " TEXT,"
                    + SEND_COL_21 + " TEXT,"
                    + SEND_COL_22 + " TEXT,"
                    + SEND_COL_23 + " TEXT,"
                    + SEND_COL_24 + " TEXT," // 色段取時間
                    + SEND_COL_25 + " TEXT," // 型段取時間
                    + SEND_COL_26 + " TEXT" // 機械コード  3000 番台　（現品票）
                    + ");";

            String createTb2 = "CREATE TABLE " + LOG_DB_TABLE + " ("
                    + LOG_COL_ID + " INTEGER primary key,"
                    + LOG_SEND_COL_01 + " TEXT,"
                    + LOG_SEND_COL_02 + " TEXT,"
                    + LOG_SEND_COL_03 + " TEXT,"
                    + LOG_SEND_COL_04 + " TEXT"
                    + ");";

            String createTb3 = "CREATE TABLE " + GRAFU_DB_TABLE + " ("
                    + GRAFU_DB_TABLE_ID + " INTEGER primary key,"
                    + GRAFU_DB_TABLE_SEND_ID + " TEXT,"
                    + GRAFU_DB_TABLE_SOU_NUM_01 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_02 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_03 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_04 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_05 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_06 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_07 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_08 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_09 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_10 + " INTEGER,"
                    + GRAFU_DB_TABLE_SOU_NUM_11 + " INTEGER"
                    + ");";

            db.execSQL(createTb1);  //SQL文の実行
            db.execSQL(createTb2); // ログ用　table 作成
            db.execSQL(createTb3); // 総生産　グラフ用

            System.out.println("テーブル Send_table_01 作成完了");
            System.out.println("テーブル Send_table_01_log 作成完了");
            System.out.println("テーブル Send_Grafu_Table 作成完了");

        }

        /**
         * DBアップグレード(バージョンアップ)時に呼ばれる
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOG_DB_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GRAFU_DB_TABLE);
            // テーブル生成
            onCreate(db);
        }
    }//------------------------------------ END sub class


}
