package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.t_prc_prototype_01.Home.Icon_Send_Flg;

public class Send_CSV_01 extends AppCompatActivity {

    //****** JIM 社内 01
    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/UploadToServer.php";
    //****** JIM 社内 02
    private static final String JIM_TEST_URL_02 = "http://192.168.254.226/tana_phppost_file/UploadToServer.php";

    // オブジェクト作成
    private SendAdapter sendAdapter;

    private PostAsyncTask postAsyncTask;

    // 現在時刻　取得
    private String Saghou_Code, Sagyou_date, Sagyou_yymmdd;

    // CSV 取得用
    private ArrayList<String> csv_itme_list = new ArrayList<>();
    private ArrayList<String> csv_count_list = new ArrayList<>();
    private ArrayList<String> csv_get_file_item = new ArrayList<>();

    //------- CSV 取得用
    private TextView test_csv;
    private String get_busho_c_str;
    private String Send_time_str;

    //------------ ディレクトリ
    // ディレクトリ　取得
    private File dir_mk;
    private File target_csv_file;
    private String csv_file_name_01,csv_file_name_02;
    public static String send_csv_file_name;

    //------------ 送信 Flg
    private String saisou_Flg;

    //------------ 送信テスト　ボタン
    private Button send_csv_btn_001;

    //------------ 送信チェック
    private int send_Flg;

    //------------ Intent 値取得用
    private String get_TMNF_01,get_TMNF_02,dia_edit_01_num, ch_user_name_num,
            ch_busyo_view_Hon_num,ch_busyo_view_num,dia_user_key;

    private int i_dia_chack_01_num;

    private WeakReference<Context> mContext;
    private WeakReference<Activity> activityWeakReference;

    //------------ 送信フラグ
    private int Time_Sum_Flg_num;

    private String Sagyou_B;

    private String icon_get_TMNF_01,icon_get_TMNF_02,icon_get_TMNF_03,icon_get_BUMF_01;

    private TextView result_view;
    private String result_str;

    // ******* Flg_Table から　作業コード取得用　
    private String Flg_send_sagyou_C;

    /**
     *   作業部署（ファイル送信用） 追加 2021/01/16
     */
    private String Sagyou_Get_file_name;

    /**
     *   作業日　用=> Flg_table
     */
    private String Sagyou_date_Flg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__c_s_v_01);

        // コンポーネント　初期化
        init();
        // 部署コード　取得
     //   GET_busho_C();
        GET_busho_C_02();

        //******* 480分　以上　超えたら　フラグ: 1 超えてない場合: 0
        Time_Sum_Flg_SELECT_01();




        /**
         *  別　ページから Intent で　値を　取得
         */

        if(getIntent() != null) {

                // 担当者コード
                get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
                // 担当者名
                get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");
                // 作業部署名
                dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");
                // 作業コード
                dia_user_key = getIntent().getStringExtra("dia_user_key");

                // 終日チェック  0: チェックなし,  1: チェックあり
                i_dia_chack_01_num = getIntent().getIntExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                System.out.println(get_TMNF_01 + get_TMNF_02 + dia_edit_01_num + dia_user_key + dia_user_key +
                        i_dia_chack_01_num);

                /**
                 *  　確実　入力データ
                 */

                // 担当
                ch_user_name_num = getIntent().getStringExtra("ch_user_name_num");
                // 部署
                ch_busyo_view_Hon_num = getIntent().getStringExtra("ch_busyo_view_Hon_num");
                // 作業部署
                ch_busyo_view_num = getIntent().getStringExtra("ch_busyo_view_num");
                // 作業部署コード
                dia_user_key = getIntent().getStringExtra("dia_user_key");

                System.out.println(ch_user_name_num + ch_busyo_view_Hon_num + ch_busyo_view_num +
                        dia_user_key);

                /***
                 *  作業部署　取得
                 */

                Sagyou_B = getIntent().getStringExtra("Sagyou_B");

                /**
                 *  送信　アイコンから　きた場合
                 */
                icon_get_TMNF_01 = getIntent().getStringExtra("icon_get_TMNF_01"); // 担当者コード
                icon_get_TMNF_02 = getIntent().getStringExtra("icon_get_TMNF_02"); // 担当者名

                icon_get_TMNF_03 = getIntent().getStringExtra("icon_get_TMNF_03"); // 部署コード
                icon_get_BUMF_01 = getIntent().getStringExtra("icon_get_BUMF_01"); // 部署名

        }


        // findViewById(R.id.send_csv_btn_001).setOnClickListener();

        // アダプター　オブジェクト作成
        sendAdapter = new SendAdapter(getApplicationContext());
        //------------ 現在時刻　取得
        String time_str = getNowDate();
        System.out.println(time_str);
        // DB 格納用
        Saghou_Code = time_str.substring(6,8); // dd 取得
        Sagyou_date = time_str.substring(0, 6); // yyyymm 取得
        Sagyou_yymmdd = time_str.substring(0,8); // yyyymmdd


        csv_file_name_01 = time_str.substring(0,8); // yyyymmdd 取得
        csv_file_name_02 = time_str.substring(8,14); // HHmmss

        //------ 送信時間作成
        String year = time_str.substring(0,4); // 年
        String month = time_str.substring(4,6); // 月

        String hour = time_str.substring(8,10); //  １時間
        String minute = time_str.substring(10,12); //　１分
        String second = time_str.substring(12, 14); // 1秒

        //------ 送信時間
        Send_time_str = year + "/" + month + "/" + Saghou_Code + " " + hour + ":" + minute + ":" + second;
        System.out.println(Send_time_str);
        // ＊＊＊＊＊  CSV 送信ファイル名　作成 　＊＊＊＊＊　ANDROID の前に　作業者コード　が入る。
     //   send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_busho_c_str + ".csv";

        /*
        if(icon_get_TMNF_01 == null || icon_get_TMNF_03 == null) {
            send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + "-END" + ".csv";

            System.out.println("ファイル名：：：テスト：：：" + Sagyou_B + ":" + get_TMNF_01);

        } else {
            send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" +
                    icon_get_TMNF_03 + "-" + icon_get_TMNF_01 + "-END" + ".csv";

            System.out.println("ファイル名：：：テスト：：：" + icon_get_TMNF_03 + ":" + icon_get_TMNF_01);
        }

         */




        /****
         *  ＊＊＊＊＊＊＊＊＊＊＊＊　 送信　ボタン　タップ処理　＊＊＊＊＊＊＊＊＊＊＊＊
         */
        send_csv_btn_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Allahto_Dailog_01();

                /**
                 *  合計時間が　480 分　を超えていたら、「送信不可」
                 *
                 *  */
                /*
                if(Time_Sum_Flg_num == 1) {

                    Allahto_Dailog_02_error();

                } else {

                        // 送信処理

                    Allahto_Dailog_01();

                } //---- END if
               */
            }
        });

    } //---------------------------- onCreate END

    /**
     * ------------- Send_table_01 から CSV ファイルを作成　処理
     */
    private void Read_Send_File_02() {

        /**
         * 　作業用　ファイル名　追加 2021/01/16
         */
        //　部署用　ファイル名　取得
        File_name_SELECT_BUSYO();
        //  send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_busho_c_str + ".csv";
        // 変更 2021-01-16 send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + ".csv";
      //  send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_Get_file_name + "-" + get_TMNF_01 + ".csv";

        if(icon_get_TMNF_01 == null || icon_get_TMNF_03 == null) {
            send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_Get_file_name + "-" + get_TMNF_01 + "-END" + ".csv";

            System.out.println("ファイル名：：：テスト：：：" + Sagyou_B + ":" + get_TMNF_01);

        } else {
            send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" +
                    Sagyou_Get_file_name + "-" + icon_get_TMNF_01 + "-END" + ".csv";

            System.out.println("ファイル名：：：テスト：：：" + icon_get_TMNF_03 + ":" + icon_get_TMNF_01);
        }


        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String [] arr_item = new String[29];

        try {

            /***
             * --------------------- ファイル　作成処理 Start -------------------------
             */
            target_csv_file = new File(getFilesDir() + "/" + send_csv_file_name);

            // 書き込み用　ストリーム
            FileOutputStream outStream = openFileOutput(send_csv_file_name, MODE_PRIVATE);
            // ファイル書き込み　準備
            OutputStreamWriter out = new OutputStreamWriter(outStream, "Shift_JIS");

            PrintWriter printWriter = new PrintWriter(out);

            Cursor cursor = db.rawQuery("SELECT * FROM Send_table_01",null);

            if(cursor.moveToFirst()) {

                do {

                    // 作業コード
                    arr_item[0] = Saghou_Code;

                    // 作業番号 作成
                    int tmp_i_01 = cursor.getInt(0);
                    String tmp_s_01 = String.valueOf(tmp_i_01);

                    String Sagyou_date_str = Saghou_Code + "-" +  "0" + tmp_s_01;
                    // 作業番号
                    arr_item[1] = Sagyou_date_str;

                    // 作業日
                    arr_item[2] = Sagyou_yymmdd;

                    //----- 担当者C
                    int idx = cursor.getColumnIndex("send_col_01");
                    arr_item[3] = cursor.getString(idx);

                    //----- 部署C
                    idx = cursor.getColumnIndex("send_col_02");
                    arr_item[4] = cursor.getString(idx);

                    //----- 現品票C
                    idx = cursor.getColumnIndex("send_col_03");
                    arr_item[5] = cursor.getString(idx);

                    //----- 品目K 区分
                    idx = cursor.getColumnIndex("send_col_04");
                    arr_item[6] = cursor.getString(idx);

                    //----- 品目C コード
                    idx = cursor.getColumnIndex("send_col_05");
                    arr_item[7] = cursor.getString(idx);

                    //----- 品目名
                    idx = cursor.getColumnIndex("send_col_06");
                    arr_item[8] = cursor.getString(idx);
                    String Himoku_Name = "\"" + arr_item[8] + "\"";

                    //----- 品備考
                    idx = cursor.getColumnIndex("send_col_07");
                    arr_item[9] = cursor.getString(idx);
                    String Himoku_Bikou = "\"" + arr_item[9] + "\"";

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_08");
                    arr_item[10] = cursor.getString(idx);

                    // 段取時間
                    idx = cursor.getColumnIndex("send_col_09");
                    arr_item[11] = cursor.getString(idx);

                    // 作業時間
                    idx = cursor.getColumnIndex("send_col_10");
                    arr_item[12] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_11");
                    arr_item[13] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_12");
                    arr_item[14] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_13");
                    arr_item[15] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_14");
                    arr_item[16] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_15");
                    arr_item[17] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_16");
                    arr_item[18] = cursor.getString(idx);

                    // 最終工程
                    idx = cursor.getColumnIndex("send_col_17");
                    arr_item[19] = cursor.getString(idx);

                    // 送信時間
                    String sousin_time = "\"" + Send_time_str + "\"";

                    /**
                     *  加工数　追加 **************
                     */
                    idx = cursor.getColumnIndex("send_col_18");
                    arr_item[20] = cursor.getString(idx);

                    //----- 再送フラグ　基本　この段階では 0
                    idx = cursor.getColumnIndex("send_col_19");
                    arr_item[21] = cursor.getString(idx);

                    saisou_Flg = arr_item[21];

                    String Saisou_Flg_GO = "1";

                    /**
                     *   「開始時間」　「終了時間」　追加
                     */
                    idx = cursor.getColumnIndex("send_col_20");
                    arr_item[22] = cursor.getString(idx);

                    System.out.println(arr_item[22]);

                    String tmp_time_01 = arr_item[22];
                    

                    /** 0 埋め
                     *        1:9 ,  10:9  , 1:30  などのデータ
                     */
                    /*
                    int idxx = tmp_time_01.indexOf( ':' );
                    String result;

                    if(tmp_time_01.length() == 3) {
                        result = "0" + tmp_time_01.charAt(0) + tmp_time_01.charAt(1) + "0" + tmp_time_01.charAt(2);
                        System.out.println(result);

                    } else if(idxx == 1){
                        result = "0" + tmp_time_01.charAt(0) + tmp_time_01.charAt(1) + tmp_time_01.charAt(2) + tmp_time_01.charAt(3);
                        System.out.println(result);

                    } else if (idxx == 2 && tmp_time_01.length() == 4) {
                        result ="" + tmp_time_01.charAt(0) + tmp_time_01.charAt(1) + tmp_time_01.charAt(2)+ "0" + tmp_time_01.charAt(3);
                        System.out.println(result);
                        System.out.println(idxx);

                    } else {
                        result ="" + tmp_time_01.charAt(0) + tmp_time_01.charAt(1) + tmp_time_01.charAt(2) + tmp_time_01.charAt(3) + tmp_time_01.charAt(4);
                        System.out.println(result);
                        System.out.println(idxx);
                    }

                    result = result.trim();

                    */


                    //******　「終了時間」
                    idx = cursor.getColumnIndex("send_col_21");
                    arr_item[23] = cursor.getString(idx);

                    String tmp_time_02 = arr_item[23];

                    /** 0 埋め
                     *        1:9 ,  10:9  , 1:30  などのデータ
                     */

                    /*
                    int idxx_02 = tmp_time_02.indexOf( ':' );
                    String result_02;

                    if(tmp_time_01.length() == 3) {
                        result_02 = "0" + tmp_time_02.charAt(0) + tmp_time_02.charAt(1) + "0" + tmp_time_02.charAt(2);
                        System.out.println(result_02);

                    } else if(idxx_02 == 1){
                        result_02 = "0" + tmp_time_02.charAt(0) + tmp_time_02.charAt(1) + tmp_time_02.charAt(2) + tmp_time_02.charAt(3);
                        System.out.println(result_02);

                    } else if(idxx_02 == 2 && tmp_time_02.length() == 4) {
                        result_02 ="" + tmp_time_02.charAt(0) + tmp_time_02.charAt(1) + tmp_time_02.charAt(2)+ "0" + tmp_time_02.charAt(3);
                        System.out.println(result_02);
                        System.out.println(idxx_02);

                    } else {
                        result_02 ="" + tmp_time_02.charAt(0) + tmp_time_02.charAt(1) + tmp_time_02.charAt(2) + tmp_time_02.charAt(3) + tmp_time_02.charAt(4);
                    }

                    result_02 = result_02.trim();

                    //------------ csv_get_file_item に　INSERT 用に　値を格納  ---------
                    for(int i = 0; i < arr_item.length; i++) {
                        csv_get_file_item.add(arr_item[i]);;
                    }

                    csv_get_file_item.add(Send_time_str);
                    csv_get_file_item.add(saisou_Flg);
                    //------------ csv_get_file_item に　INSERT 用に　値を格納  END -------

                     */

                    // ******* send_col_22 取得  Start ***************
                    idx = cursor.getColumnIndex("send_col_22");
                    arr_item[24] = cursor.getString(idx);

                    // 作業番号 ファイルカラム 2
                    String GET_id = arr_item[24];

                    // 作業 DD ファイルカラム 1
                    String ID_DD_01 = GET_id.substring(0,2);
                    // ******* send_col_22 取得  END ***************

                    //************ CSV 項目３　修正　追加
                    idx = cursor.getColumnIndex("send_col_23");
                    arr_item[25] = cursor.getString(idx);


                    /***
                     *  樹脂成型課用　追加　0327
                     */
                    // ------ 色段取時間
                    idx = cursor.getColumnIndex("send_col_24");
                    arr_item[26] = cursor.getString(idx);

                    // ------ 型段取り時間
                    idx = cursor.getColumnIndex("send_col_25");
                    arr_item[27] = cursor.getString(idx);

                    // ------ 機械コード
                    idx = cursor.getColumnIndex("send_col_26");
                    arr_item[28] = cursor.getString(idx);


                    /**
                     *   CSV ファイルへ　値を格納
                     */
                    String record = ID_DD_01 + "," + GET_id + "," +   arr_item[25] + "," + arr_item[3] + "," + arr_item[4] +
                            "," + arr_item[5] + "," + arr_item[6] + ","  + arr_item[7] + "," + Himoku_Name + "," + Himoku_Bikou +
                            "," + arr_item[10] + "," + arr_item[11] + "," + arr_item[12] + "," + arr_item[13] + "," + arr_item[14] + "," + arr_item[15] +
                            "," + arr_item[16] + "," + arr_item[17] + "," + arr_item[18] + "," + arr_item[19] + "," + sousin_time + "," +
                            arr_item[20] + "," + Saisou_Flg_GO + "," + tmp_time_01 + "," + tmp_time_02 +
                            "," + arr_item[26] + "," + arr_item[27] + "," + arr_item[28];

                    csv_itme_list.add(record);
                    // ファイルへ　格納
                    printWriter.println(record);
                    printWriter.flush();

                } while(cursor.moveToNext());

                printWriter.close();

            } //------------ END if

            cursor.close();

            StringBuilder stb = new StringBuilder();
            for(int i = 0; i < csv_itme_list.size(); i++) {
                stb.append(csv_itme_list.get(i) + "\n");
                System.out.println("csvデータ出力：：：" + stb);
            }

            System.out.println("csv_count_list：：：");
            for(String a : csv_get_file_item) {
                System.out.println(a);
            }

            test_csv.setText(stb);

            System.out.println("送信ファイル名:::" + send_csv_file_name);

        } catch(SQLiteDatabaseLockedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***
         * --------------------- ファイル　作成処理 END ------------------------->
         */

    } //------------ Read_Send_File_01 END

    private void GET_busho_C() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase busho_db = helper.getReadableDatabase();

        try {

            Cursor cursor = busho_db.rawQuery("select Tantou_c from Send_table", null);

            if(cursor.moveToFirst()) {
                get_busho_c_str = cursor.getString(0);
            }

            System.out.println("部署コードGET:::" + get_busho_c_str);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(busho_db != null) {
                busho_db.close();
            }
        }

    }

    /**
     * 部署コード　select
     */

    private void GET_busho_C_02() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase busho_db = helper.getReadableDatabase();

        try {

            Cursor cursor = busho_db.rawQuery("select send_col_01 from Send_table_01", null);

            if(cursor.moveToFirst()) {
                get_busho_c_str = cursor.getString(0);
            }

            System.out.println("部署コードGET:::" + get_busho_c_str);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(busho_db != null) {
                busho_db.close();
            }
        }

    }

    /***
     *     table 名　 = Send_table_01_log へ　インサート
     */

    private void Send_table_01_log_Insert() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_0001 = helper.getWritableDatabase();

        // トランザクション　開始
        db_0001.beginTransaction();

        try {

            ContentValues values = new ContentValues();

           // values.put("")


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * ---------------- 送信　フラグを　１ する　Update -----------------
     *
     */
    private void Send_Flg_ON() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_001 = helper.getWritableDatabase();

        try {

            // トランザクション　開始
         //  db_001.setTransactionSuccessful();

            String send_Flg = "1";
            String send_Flg_zero = "0";
            ContentValues values = new ContentValues();
            values.put("send_col_18",send_Flg);

            //データベースを更新
            db_001.update("Send_table_01", values,"send_col_18 = ?",new String[]{"0"});

            //　トランザクション　成功
       //     db_001.beginTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

       //     db_001.endTransaction();

            if(db_001 != null) {
                db_001.close();
            }

            System.out.println("フラグ 1　変更 OK");
            Log.d("Send_CSV_01.java ログ", "送信フラグ 1 変更　OK");
        }

    } //------------------ Send_Flg_ON END


    /**
     * @param message
     * @param x
     * @param y
     *
     * // トーストメッセージ　表示用
     */

    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x,y);
        toast.show();

    }

    /**
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_01() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Send_CSV_01.this);
        titleView.setText("ファイル送信の確認");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(Send_CSV_01.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("ファイルを送信しますか？");

        bilder.setNegativeButton("送信する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // CSV ファイル作成
                //  Read_Send_File_01();
                Read_Send_File_02();

                /***
                 * ---------------------------------------- POST 送信処理　Start ------------
                 */

                  String jim = "http://192.168.254.87/tana_phppost_file/UploadToServer.php";  // JIM　社内 OK *****
                //  String jim_02 = "http://192.168.254.51/tana_phppost_file/UploadToServer.php";

                /**
                 * 　送信　プロパティ
                 */
                String uploadURL = JIM_TEST_URL_02; // 社内
             //   String uploadURL = TOYAMA_TEST_URL; // 本番
                String title = send_csv_file_name;
                String uploadFile = getFilesDir() + "/" + send_csv_file_name;

                //**********  送信結果用　アラートメッセージ表示 ************
                toastMake("ファイル送信を行っています。\n送信結果が表示されますので、しばらくお待ちください。",0,-200);

                /**
                 *  //************************************* コールバック
                 *   ＊＊＊＊＊＊＊＊＊＊＊＊ 送信処理　＊＊＊＊＊＊＊＊＊＊＊＊＊
                 */

                PostAsyncTask task = new PostAsyncTask();
                task.setOnCallBack(new PostAsyncTask.CallBackTask(){

                    @Override
                    public void CallBack(Object result) {
                        super.CallBack(result);

                        System.out.println("CallBakc::::値" + result);

                        String obj = (String) result;
                        result_str = obj;
                        result_view.setText(result_str);

                        String get_callback_str = result_view.getText().toString();
                        //**************************  コールバック　の　値が 0: の場合は　エラーメッセージを返す
                        if(get_callback_str.startsWith("*")) {
                            toastMake("お疲れ様でした。データ送信完了しました。",0, -200);

                            /***
                             *  ファイル　送信後　メッセージ
                             */

                            // ここで　送信フラグを１に変える　アップデート処理
                            Send_Flg_ON();

                            // ****** Flg_Table の　送信用フラグを　１　にする
                            Send_data_Flg_ON();


                            //    toastMake("送信結果が表示されるまで、しばらくお待ちください。", -0,-200);

                            //***************************** 送信後　メッセージ ********************

                            //-------　タイトル
                            TextView titleView_02;
                            // アクティビティ名を入れる
                            titleView_02 = new TextView(Send_CSV_01.this);
                            titleView_02.setText("お疲れ様でした。データ送信完了しました。");
                            titleView_02.setTextSize(20);
                            titleView_02.setTextColor(Color.WHITE);
                            titleView_02.setBackgroundColor(getResources().getColor(R.color.menu_color));
                            titleView_02.setPadding(20,20,20,20);
                            titleView_02.setGravity(Gravity.CENTER);

                            //-------- アラートログの表示 開始 ----------
                            AlertDialog.Builder bilder_02 = new AlertDialog.Builder(Send_CSV_01.this);
                            //------- タイトルセット
                            bilder_02.setCustomTitle(titleView_02);
                            //------- メッセージセット
                            bilder_02.setMessage("アプリを終了しますか？");

                            bilder_02.setPositiveButton("アプリ終了画面へ移動する", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    MainActivity.app_finish_flag = true;
                                    finish();

                                    // moveTaskToBack(true);
                                    // finishAndRemoveTask();
                                    // appExit();

                                }
                            });

                            bilder_02.setNegativeButton("もう一度送信処理へ戻る", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });

                            AlertDialog dialog_02 = bilder_02.create();
                            dialog_02.show();

                            //******************************************* ボタン　配色　変更
                            //********* ボタン はい **********
                            dialog_02.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
                            //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                            //********* ボタン いいえ **********
                            dialog_02.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
                            //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

                            return;

                        } else {
                            toastMake("データ送信に失敗しました。\n時間をおいてもう一度送信してください。",0, -200);

                            //    toastMake("送信結果が表示されるまで、しばらくお待ちください。", -0,-200);

                            //***************************** 送信後　メッセージ ********************

                            //-------　タイトル
                            TextView titleView_02;
                            // アクティビティ名を入れる
                            titleView_02 = new TextView(Send_CSV_01.this);
                            titleView_02.setText("データ送信に失敗しました。\n時間をおいてもう一度送信してください。");
                            titleView_02.setTextSize(20);
                            titleView_02.setTextColor(Color.WHITE);
                            titleView_02.setBackgroundColor(getResources().getColor(R.color.menu_color));
                            titleView_02.setPadding(20,20,20,20);
                            titleView_02.setGravity(Gravity.CENTER);

                            //-------- アラートログの表示 開始 ----------
                            AlertDialog.Builder bilder_02 = new AlertDialog.Builder(Send_CSV_01.this);
                            //------- タイトルセット
                            bilder_02.setCustomTitle(titleView_02);
                            //------- メッセージセット
                            bilder_02.setMessage("ファイル送信に失敗しました。");

                            bilder_02.setNegativeButton("もう一度送信処理へ戻る", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });

                            AlertDialog dialog_02 = bilder_02.create();
                            dialog_02.show();

                            //******************************************* ボタン　配色　変更
                            //********* ボタン はい **********
                            dialog_02.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
                            //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                            //********* ボタン いいえ **********
                            dialog_02.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
                            //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

                            return;
                        }


                    }


                });

                try {
                    //⑧PostAsyncTaskに渡すパラメータをObject配列に設定
                    Object[] postParams = new Object[3];
                    postParams[0] = uploadURL;
                    postParams[1] = title;
                    postParams[2] = uploadFile;

                    //⑨PostAsyncTaskを実行
                    //          new PostAsyncTask(getCallingActivity()).execute(postParams);
                 //   postAsyncTask.execute(postParams[0], postParams[1], postParams[2]);

                  //  new PostAsyncTask().execute(postParams);
                    task.execute(postParams);
                    //   toastMake("CSVファイルを送信しました。", -200, 0);



                }  catch (Exception e) {
                    e.printStackTrace();

                    return;
                }





                /***
                 * ---------------------------------------- POST 送信処理　END ------------>
                 */

            }
        });

        bilder.setPositiveButton("アプリを終了しない。", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //　終了しない
                return;
            }
        });

        AlertDialog dialog = bilder.create();
        dialog.show();

        //******************************************* ボタン　配色　変更
        //********* ボタン はい **********
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //********* ボタン いいえ **********
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));



    } //-------------- Allahto_Dailog_01  END




    /**
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_02_error() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Send_CSV_01.this);
        titleView.setText("ファイル送信できません");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.red));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(Send_CSV_01.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("作業時間の合計が「480分」を超えています。作業時間を修正してください。");

        bilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            finish();

            }
        });


        AlertDialog dialog = bilder.create();
        dialog.show();

        //******************************************* ボタン　配色　変更 => 赤
        //********* ボタン はい **********
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B20000"));
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //********* ボタン いいえ **********
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B20000"));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));


    } //-------------- Allahto_Dailog_01  END


    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate(){
        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    /**
     * 　コンポーネント　初期化
     */
    private void init() {

        test_csv = findViewById(R.id.test_csv);
        test_csv.setText("");

        //******** コールバック　表示用 view
        result_view = findViewById(R.id.result_view);

        // 送信テスト　ボタン
        send_csv_btn_001 = findViewById(R.id.send_csv_btn_001);
    }

    /**
     *   Flg_Table  テーブル => Time_Sum_Flg
     */
    private void Time_Sum_Flg_SELECT_01() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Time_Sum_S_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Time_Sum_S_db.rawQuery("select Time_Sum_Flg from Flg_Table", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Time_Sum_Flg_num = cursor.getInt(0);
                    System.out.println("Time_Sum_Flg_num:::値::::" + Time_Sum_Flg_num);
                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Time_Sum_S_db != null) {
                Time_Sum_S_db.close();
            }
        }

    }


    public void finishAndRemoveTask() {

        super.finishAndRemoveTask();
    }


    public void appExit () {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }  //close method


    private void Send_data_Flg_ON() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Send_data_Flg_ON_db = helper.getReadableDatabase();

        Send_data_Flg_ON_db.beginTransaction();

        try {

            // トランザクション　開始
            //  db_001.setTransactionSuccessful();

            String send_Flg = "1";
            String send_Flg_zero = "0";
            ContentValues values = new ContentValues();
            values.put("Send_data_Flg",send_Flg);

            //データベースを更新
            Send_data_Flg_ON_db.update("Flg_Table", values,"Send_data_Flg = ?",new String[]{"0"});

            Send_data_Flg_ON_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            Send_data_Flg_ON_db.endTransaction();

            if(Send_data_Flg_ON_db != null) {
                Send_data_Flg_ON_db.close();
            }

            System.out.println("フラグ 1　変更 OK");
            Log.d("Send_CSV_01.java ログ", "送信フラグ 1 変更　OK");
        }

    } //**************** END function

    private void allahto_color_change(AlertDialog dialog_target, String color_str) {

        //******************************************* ボタン　配色　変更
        //********* ボタン はい **********
        dialog_target.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(color_str));
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //********* ボタン いいえ **********
        dialog_target.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(color_str));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

    }

    /***
     *  Flg テーブル　から　作業用　部署を取得
     */
    private void Flg_Table_Sagyou_SELECT () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase FLG_S_DB_02 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            //**********  作業部署コード　取得
            cursor = FLG_S_DB_02.rawQuery("SELECT Sagyou_busyo_code FROM Flg_Table", null);

            if(cursor != null || cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Flg_send_sagyou_C = cursor.getString(0);
                }

                //**********  作業部署コード がない場合　
            } else {
                Flg_send_sagyou_C = dia_user_key;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }  finally {
            if(FLG_S_DB_02 != null) {
                FLG_S_DB_02.close();
            }
        }

    } //------------ end function

    /***
     * 　　「ファイル名用　部署コード　SELECT」
     */
    private void File_name_SELECT_BUSYO () {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase file_name_select = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = file_name_select.rawQuery("SELECT send_col_02 FROM Send_table_01" +
                    " WHERE send_col_22 LIKE \"%01\" or" +
                    " send_col_22 like \"%02\" or" +
                    " send_col_22 like \"%03\" or" +
                    " send_col_22 like \"%04\" or" +
                    " send_col_22 like \"%05\" " +
                    " order by send_col_23 desc;", null);

            if(cursor != null || cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    Sagyou_Get_file_name = cursor.getString(0);

                }

            } else {

                Sagyou_Get_file_name = "NO-Depa";

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if(file_name_select != null) {
                file_name_select.close();
            }

        }

    } //------------------- End function

    /**
     *     作業日を　Flg_table から　取得する。
     */
    private void GET_Sagayou_bi() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Sageyou_bi_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Sageyou_bi_db.rawQuery("SELECT create_date FROM Flg_Table", null);

            if(cursor != null || cursor.getCount() > 0) {

                //************* 作業日　Flg_table から　取得
                if(cursor.moveToFirst()) {

                    /**
                     *  作業日を　取得 Flg_table
                     */
                    Sagyou_date_Flg = cursor.getString(0);

                }

            } else {

                Sagyou_date_Flg = "選択なし";

            } // END if

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Sageyou_bi_db != null) {
                Sageyou_bi_db.close();
            }
        }

    } //***************** END function


}