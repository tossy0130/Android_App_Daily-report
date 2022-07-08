package com.example.t_prc_prototype_01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiManager;
import android.util.Log;

public class TestOpenHelper extends SQLiteOpenHelper {

    public SQLiteOpenHelper db;
    private TestOpenHelper dbHelper = null;

    public static final String TABLE_NAME = "Send_table";

    // データベースのバージョン
    public static final int DATABASE_VERSION = 1;

    // -------------- マスター用　データベース 名 ------------------
    public static final String DATABASE_NAME = "Master.db";

    //--------------- マスター CSV 受信用　テーブル　一覧 -----------

    // 商品マスター　テーブル
   // public static final String SHMF_TABLE = "SHMF_table";

    // カラム名 一覧
    /*
    public static final String COLUMN_01 = "SHMF_c_01";
    public static final String COLUMN_02 = "SHMF_c_02";
    public static final String COLUMN_03 = "SHMF_c_03";
    public static final String COLUMN_04 = "SHMF_c_04";
    public static final String COLUMN_05 = "SHMF_c_05";
    public static final String COLUMN_06 = "SHMF_c_06";
    public static final String COLUMN_07 = "SHMF_c_07";
    public static final String COLUMN_08 = "SHMF_c_08";
    public static final String COLUMN_09 = "SHMF_c_09";
    */

    /**
     *   ( SHMF_c_01 text, SHMF_c_02 text   => カラムの頭　に　「空白行」　を入れる。
     */
    public static final String SHMF_TB = "create table SHMF_table( SHMF_c_01 text, SHMF_c_02 text, SHMF_c_03 text, SHMF_c_04 text, SHMF_c_05 text, SHMF_c_06 text, SHMF_c_07 text, SHMF_c_08 text, SHMF_c_09 text);";

    // TNMF_table 作成 ==>  TNMF.csv　格納用 （ユーザーデータ）    EMPLOYEE_TB
    public static final String TNMF_TB = "create table TNMF_table( TNMF_c_01 text, TNMF_c_02 text, TNMF_c_03 text, TNMF_c_04 text);";

    // BUMF_table ==> BUMF.csv 格納用 （課コード、データ）
    public static final String BUMF_TB = "create table BUMF_table( BUMF_c_01 text, BUMF_c_02 text);";

    // SHKB_table ==> SHKB.csv 格納用 データ： 1,製　2,半製品 3,"材  料" etc
    public static final String SHKB_TB = "create table SHKB_table( SHKB_c_01 text, SHKB_c_02 text);";

    // SOMF_table ==> SOMF.csv 場所コード,場所　 用　データベース
    public static final String SOMF_TB = "create table SOMF_table( SOMF_c_01 text, SOMF_c_02 text);";

    // COMB_SEND_table => COMB_SEBD_TB  （カラム　4個）   // ユーザー名 , コンボリストの値, 作業時間, 送信時間
    public static final String COMB_SEBD_TB = "create table COMB_SEND_table( COMB_SEND_c_01 text, COMB_SEND_c_02 text, COMB_SEND_c_03 text,COMB_SEND_c_04 integer);";

    // 段取り時間　品目　テーブル
    public static final String NP_data_TB = "create table NP_data_table( NP_data_c_01 text, NP_data_c_02 text, NP_data_c_03 text);";

    // Tosou_Data  9, T-11, 品名
    public static final String Tosou_Data_TB = "create table Tosou_Data( Tosou_Data_c_01 text, Tosou_Data_c_02 text, Tosou_Data_c_03 text);";


    /** // ********* フラグ用テーブル ***********
     * id,
     * Time_Sum_Flg, => 480 合計時間　フラグ,デフォルト:0  ,  Syuuzitu_Flg, => 終日チェック　フラグ,デフォルト:0
     * Send_data_Flg, => 送信ボタンを押したかの　フラグ,デフォルト:0 , Data_zan,  => 作業データが残っているかの　フラグ,デフォルト:0
     * Sagyousya_code, => 作業者コード ,  Sagyou_busyo_code, => 作業部署コード取得
     * create_date => データ作成日
     */
    public static final String FLG_TB = "create table Flg_Table( id INTEGER  primary key, Time_Sum_Flg integer DEFAULT 0,Syuuzitu_Flg integer DEFAULT 0," +
            "Send_data_Flg integer DEFAULT  0, Data_zan inreger DEFAULT 0,Sagyousya_code text,Sagyou_busyo_code text, create_date text);";

    // データ挿入用　データベース
    /*
    public static final String SEND_DB = "create table " + TABLE_NAME + "(" +
            "Send_id Integer primary key, Tantou_Code text , Tantou_Name text,Tantou_Busho_Code text, Tantou_Busho_Name text," +
            " Item_Id text,Item_Code text, Product_Name text, Item_Bikou text, Hyoujyun_Hours text, Processing_Time text," +
            " Work_Hours text, Start_Quantity text, End_Quantity text, Defective_Products text, Num_Production text," +
            " Good_Products_Num text, Final_Process_Check text);";

     */

    /**
     *  送信用　テーブル （カラム　18個） 6個　（テスト時点）
     *
     *  1: Send_id  ,  2: 担当者C   ,3: 部署C  ,4: 現品票  , 5: 品目K
     * 6: 品目C , 7: 品目名 , 8: 品備考 , 9: 作業場所  ,  10:段取り時間
     * 11:作業時間  , 12:予定数量  ,  13:開始数量  , 14:終了数量  , 15:総生産数
     * 16:不良品数  , 17:良品数  ,  18:最終工程
     *------------------ 入れるかも　
     * 19:ファイル作成時間
     * 20:更新回数
     *
     */
    public static final String SEND_DB = "create table " + TABLE_NAME + "(" +
            "Send_id Integer primary key, Tantou_c text," +
            " Busho_c text,GenpinHyou_c text,Hinmoku_k text, Hinmoku_c ," +
            "Hinmoku_name text,Hinmoku_bikou text,Sagyou_basho text, " +
            "Dandori_time text,Sagyou_time text,Yotei_num text,Kaishi_num text," +
            "Syuuryou_num text, Souseisan_num text,Furyouhin_num text,Ryouhinn_num text," +
            "Saishyuu_k_check text, Saisou_Flg text);";

    /*
    //------------ SOMF テーブル　定義　メソッド
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +  SHMF_TABLE + "(" +
                    COLUMN_01 + " TEXT," +
                    COLUMN_02 + " TEXT," +
                    COLUMN_03 + " TEXT," +
                    COLUMN_04 + " TEXT," +
                    COLUMN_05 + " TEXT," +
                    COLUMN_06 + " TEXT," +
                    COLUMN_07 + " TEXT," +
                    COLUMN_08 + " TEXT," +
                    COLUMN_09 + " TEXT)";

     */

    //------------------------ コンストラクタ -----------------------
    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //------------------------ テーブル作成　実行 --------------------
    @Override
    public void onCreate(SQLiteDatabase db) {

        //-------- テーブル　作成 -------

        db.execSQL(SHMF_TB);
        System.out.println("SHMF_TB 商品マスター テーブル 作成完了");

        db.execSQL(TNMF_TB);
        System.out.println("TNMF_TB テーブル 作成完了");

        db.execSQL(BUMF_TB);
        System.out.println("BUMF_TB テーブル 作成完了");

        db.execSQL(SHKB_TB);
        System.out.println("SHKB_TB テーブル 作成完了");

        db.execSQL(SOMF_TB);
        System.out.println("SOMF_TB テーブル 作成完了");

        // 作業日報　段取り時間　区分
        db.execSQL(NP_data_TB);
        System.out.println("NP_data_TB テーブル　作成完了");

        // フラグ用　テーブル
        db.execSQL(FLG_TB);
        System.out.println("FLG_TB テーブル　作成完了");

        db.execSQL(Tosou_Data_TB);
        System.out.println("Tosou_Data_TB テーブル　作成完了");

        //-------------------- 送信用テーブル -------------------

        Log.d("debug", "execSQL 実行完了****** テーブル作成完了 *****");

    }

    //------------------------ アップグレード　判別 --------------------
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // アップデート判別
        db.execSQL(SHMF_TB);
        System.out.println("SHMF_TB 商品マスター テーブル 作成完了");

        db.execSQL(TNMF_TB);
        System.out.println("TNMF_TB テーブル onUpgrade アップグレード完了");

        db.execSQL(BUMF_TB);
        System.out.println("BUMF_TB テーブル onUpgrade アップグレード完了");

        db.execSQL(SHKB_TB);
        System.out.println("SHKB_TB テーブル onUpgrade アップグレード完了");

        db.execSQL(SOMF_TB);
        System.out.println("SOMF_TB テーブル  onUpgrade アップグレード完了");

        // 作業日報　段取り時間　区分
        db.execSQL(NP_data_TB);
        System.out.println("NP_data_TB テーブル　作成完了");

        // フラグ用　テーブル
        db.execSQL(FLG_TB);
        System.out.println("FLG_TB テーブル　作成完了");

        db.execSQL(Tosou_Data_TB);
        System.out.println("Tosou_Data_TB テーブル　作成完了");


        //-------------------- 送信用テーブル -------------------

        Log.d("debug", "execSQL 実行完了****** テーブ アップグレード 成完了 *****");

    }

    //-------------- ダウングレード　メソッド --------------------------------
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}
