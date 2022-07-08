package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Worker_TEST_01 extends AppCompatActivity  {

    private static Worker_TEST_01 instance = null;

    public Button w_btn;

    // オブジェクト作成
    public SendAdapter sendAdapter;

    // 現在時刻　取得
    public String Saghou_Code, Sagyou_date, Sagyou_yymmdd;

    // CSV 取得用
    public ArrayList<String> csv_itme_list = new ArrayList<>();
    public ArrayList<String> csv_count_list = new ArrayList<>();
    public ArrayList<String> csv_get_file_item = new ArrayList<>();

    //------- CSV 取得用
    public TextView test_csv,work_setumei;
    public String get_busho_c_str;
    public String Send_time_str;

    //------------ ディレクトリ
    // ディレクトリ　取得
    public File dir_mk;
    public File target_csv_file;
    public String csv_file_name_01,csv_file_name_02;
    public String send_csv_file_name;

    //------------ 送信 Flg
    public String saisou_Flg;

    //------------ 送信テスト　ボタン
    public Button send_csv_btn_001;

    //------------ 送信チェック
    public int send_Flg;

    //------------ Intent 値取得用
    public String get_TMNF_01,get_TMNF_02,dia_edit_01_num, ch_user_name_num,
            ch_busyo_view_Hon_num,ch_busyo_view_num,dia_user_key;

    public int i_dia_chack_01_num;

    public WeakReference<Context> mContext;
    public WeakReference<Activity> activityWeakReference;

    // public static File_make file_make;

    private SendWorker sendWorker;

    // インサート用　送信時間
    private String sousin_time;
    // インサート用　送信スタッツ
    private String send_log_Stats;
    // インサート用　タスク　count 数
    private String Task_Count_num,Sagyou_B;

    // 時間設定　入力用エディットテキスト
    private EditText work_time_edit_01;

    private String work_time_edit_01_num,Defalut_num;

    private int Teiki_Send_Flg;

    private String time_001, time_002, time_003;

    private String tmp_s_01;

    /**
     *   作業部署（ファイル送信用） 追加 2021/01/16
     */
    private String Sagyou_Get_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__t_e_s_t_01);


        /**
         *  値受け取り
         */
        if(getIntent() != null) {

            get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
            get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");

            dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");

            Sagyou_B = getIntent().getStringExtra("Sagyou_B");

        }

        instance = this;

        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());

        String getNowDate = df.format(date);

        // アダプター　オブジェクト作成
        sendAdapter = new SendAdapter(getApplicationContext());
        //------------ 現在時刻　取得
        String time_str = getNowDate;
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

        //********** 時間取得
        time_001 = time_str.substring(8,10);
        time_002 = time_str.substring(10,12);
        time_003 = time_str.substring(12,14);

        //------ 送信時間
        Send_time_str = year + "/" + month + "/" + Saghou_Code + " " + hour + ":" + minute + ":" + second;
        System.out.println(Send_time_str);
        // ＊＊＊＊＊  CSV 送信ファイル名　作成 　＊＊＊＊＊　ANDROID の前に　作業者コード　が入る。
        //   send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_busho_c_str + ".csv";
        // send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + ".csv";

        //-----------

        // listenerをセット

        //**************** 初期化　処理 ***************
        init();

        //********* 定期送信用　フラグ　select
        Teiki_Send_Flg_SELECT();

        work_time_edit_01.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    if(work_time_edit_01.getText().toString().equals("")) {

                        tostMake("値が入力されていません。", 0 ,-200);
                        return false;

                    } else {

                        String work_time_edit_01_tmp = work_time_edit_01.getText().toString();

                        int hantei = Integer.parseInt(work_time_edit_01_tmp);

                        if(hantei >= 16 && hantei <= 120) {
                            work_time_edit_01_num = work_time_edit_01_tmp;
                        } else {
                            tostMake("16分 以上 120分以下で　入力してください。", 0 ,-200);

                            work_time_edit_01.setText("0");
                            return false;
                        }

                    }

                }

                return false;
            }
        });




        w_btn = (Button) findViewById(R.id.w_btn);

        w_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------
                // ****************  OneTimeWorkRequest １回だけ　実行 Start ******************
                /*
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendWorker.class)
                        .setInitialDelay(10, TimeUnit.SECONDS)
                        .build();

                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);

                 */
                // ****************  OneTimeWorkRequest １回だけ　実行  END *****************

                // **************** 複数回実行 Start ******************************

                /*
                //制約（起動条件）を設定
                //　　このサンプルでは制約なしでもいいが
                //　　とりあえずバッテリー容量が少ないときはNGとした
                Constraints constraints = new Constraints.Builder()
                        // デバイスがWiFiに接続されている場合にのみ機能する。
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build();

                //WorkRequestの作成
                // PeriodicWorkRequestを使って16分毎に定期実行
                PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                        SendWorker.class,
                        16, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

              */

                /**
                 *  定期送信　フラグが　立っているかチェック　立っていたら　送信　OK  Teiki_Send_Flg 0:NG  1:OK
                 */

                if(Teiki_Send_Flg == 0) {
                    //************* エラー処理 ********
                    tostMake("「作業追加」を行ってから、「定期送信」を実行してください。", 0,-200);
                    return;

                }  else {

                    //************* フラグが立っていた場合

                    if(work_time_edit_01.getText().toString().equals("")) {

                        tostMake("デフォルト時間 16分　で起動しました。", 0, -200);

                        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                                SendWorker.class,
                                16, TimeUnit.MINUTES)
                                .build();

                        finish();

                    }


                    if(work_time_edit_01.getText().toString().equals("") == false) {
                        //WorkRequestの作成
                        // PeriodicWorkRequestを使って16分毎に定期実行

                        String work_time_edit_01_num = work_time_edit_01.getText().toString();

                        int work_time = Integer.parseInt(work_time_edit_01_num);

                        if(work_time >= 16 && work_time <= 120) {

                            tostMake("入力された" + work_time + "分で起動しました。", 0, -200);

                            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                                    SendWorker.class,
                                    work_time, TimeUnit.MINUTES)
                                    .build();

                            WorkManager.getInstance(getApplicationContext()).enqueue(request);

                            finish();

                        } else {

                            tostMake("16分 以上 120分以下で　入力してください。", 0 ,-200);
                            work_time_edit_01.setText("0");
                            return;

                        }

                    } else {

                        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                                SendWorker.class,
                                16, TimeUnit.MINUTES)
                                .build();

                    } //**************** END if

                } //************************************ END if

                // **************** 複数回実行 END ******************************

            }
        });
    } //----------------------- create END

    public static Worker_TEST_01 getInstance() {//インスタンスを取得
        return instance;
    }

    /**
     * ------------- Send_table_01 から CSV ファイルを作成　処理 & ファイル送信処理  WorkerManager
     */
    public void Task_schedule_Send_CSV() {


        /**
         * 　作業用　ファイル名　追加 2021/01/16
         */
        //　部署用　ファイル名　取得
        File_name_SELECT_BUSYO();
        //  send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_busho_c_str + ".csv";
        // 変更 2021-01-16 send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + ".csv";
        send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_Get_file_name + "-" + get_TMNF_01 + ".csv";

          DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
          SQLiteDatabase schedule_db_01 = helper.getReadableDatabase();

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

            Cursor cursor = schedule_db_01.rawQuery("SELECT * FROM Send_table_01",null);

            if(cursor.moveToFirst()) {

                do {

                    // 作業コード
                    arr_item[0] = Saghou_Code;

                    // 作業番号 作成
                    int tmp_i_01 = cursor.getInt(0);
                    tmp_s_01 = String.valueOf(tmp_i_01);

                    String Sagyou_date_str = Saghou_Code + "-" + "0" + tmp_s_01;
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

                    //******* 加工数　「空」　で　送信
                    String kakou_num = "";

                    //----- 再送フラグ　基本　この段階では 0
                    idx = cursor.getColumnIndex("send_col_19");
                    arr_item[21] = cursor.getString(idx);

                    saisou_Flg = arr_item[21];

                    /**
                     *   「開始時間」　「終了時間」　追加
                     */
                    idx = cursor.getColumnIndex("send_col_20");
                    arr_item[22] = cursor.getString(idx);

                    System.out.println(arr_item[22]);

                    // 開始時間
                    String tmp_time_01 = arr_item[22];

                    /** 0 埋め
                     *        1:9 ,  10:9  , 1:30  などのデータ
                     */

                    //******　「終了時間」
                    idx = cursor.getColumnIndex("send_col_21");
                    arr_item[23] = cursor.getString(idx);

                    //****** 終了時間
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

                     */

                    // ******* send_col_22 取得  Start ***************
                    idx = cursor.getColumnIndex("send_col_22");
                    arr_item[24] = cursor.getString(idx);

                    // 作業番号 ファイルカラム 2
                    String GET_id = arr_item[24];

                    // 作業 DD ファイルカラム 1
                    String ID_DD = GET_id.substring(0,2);
                    // ******* send_col_22 取得  END ***************

                    //*******　作業 yyyymmdd
                    idx = cursor.getColumnIndex("send_col_23");
                    arr_item[25] = cursor.getString(idx);

                    //------------ csv_get_file_item に　INSERT 用に　値を格納  ---------
                    for (int i = 0; i < arr_item.length; i++) {
                        csv_get_file_item.add(arr_item[i]);
                        ;
                    }

                    csv_get_file_item.add(Send_time_str);
                    csv_get_file_item.add(saisou_Flg);
                    //------------ csv_get_file_item に　INSERT 用に　値を格納  END -------

                    /**
                     *  「樹脂成型課用」　追加
                     */
                    idx = cursor.getColumnIndex("send_col_24");
                    arr_item[26] = cursor.getString(idx); //***************** 色段取時間

                    idx = cursor.getColumnIndex("send_col_25");
                    arr_item[27] = cursor.getString(idx); //*****************　型段取時間

                    idx = cursor.getColumnIndex("send_col_26");
                    arr_item[28] = cursor.getString(idx); //*****************  機械コード

                    /**
                     *   CSV ファイルへ　値を格納
                     */
                    String record = ID_DD + "," + GET_id + "," +  arr_item[25] + "," + arr_item[3] + "," + arr_item[4] +
                            "," + arr_item[5] + "," + arr_item[6] + "," + arr_item[7] + "," + Himoku_Name + "," + Himoku_Bikou +
                            "," + arr_item[10] + "," + arr_item[11] + "," + arr_item[12] + "," + arr_item[13] + "," + arr_item[14] + "," + arr_item[15] +
                            "," + arr_item[16] + "," + arr_item[17] + "," + arr_item[18] + "," + arr_item[19] + "," +
                            sousin_time + "," + kakou_num + "," + saisou_Flg + "," +
                            tmp_time_01 + "," + tmp_time_02 + "," +
                            arr_item[26] + "," + arr_item[27] + "," + arr_item[28];
                    //   tmp_time_01 + "," + tmp_time_02;

                    csv_itme_list.add(record);
                    // ファイルへ　格納
                    printWriter.println(record);
                    printWriter.flush();

                } while (cursor.moveToNext());

                printWriter.close();

            } //------------ END if

            cursor.close();

            StringBuilder stb = new StringBuilder();
            for(int i = 0; i < csv_itme_list.size(); i++) {
                stb.append(csv_itme_list.get(i) + "\n");
                System.out.println("＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ csvデータ出力：：：" + stb);
            }

            System.out.println("csv_count_list：：：");
            for(String a : csv_get_file_item) {
                System.out.println(a);
            }

            System.out.println("＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ 送信ファイル名:::" + send_csv_file_name);
            Log.d("送信ファイル名:::",send_csv_file_name + "*** ファイル送信成功***");

        } catch(SQLiteDatabaseLockedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }  finally {

            if(schedule_db_01 != null) {
                schedule_db_01.close();
            }
        }


        /***
         * --------------------- ファイル　作成処理 END ------------------------->
         */

        /***
         * ---------------------------------------- POST 送信処理　Start ------------
         */

        String jim = "http://192.168.254.87/tana_phppost_file/UploadToServer.php";  // JIM　社内 OK *****
        //  String jim_02 = "http://192.168.254.51/tana_phppost_file/UploadToServer.php";



        /**
         * 　送信　プロパティ
         */
     //   String uploadURL = jim; //------ 社内
        String uploadURL = toyama; //************* 本番
        String title = send_csv_file_name;
        String uploadFile = getFilesDir() + "/" + send_csv_file_name;

        // 1 送信 ok   , 0　未送信  , 2 送信エラー
        send_log_Stats = "0";

        try {
            //⑧PostAsyncTaskに渡すパラメータをObject配列に設定
            Object[] postParams = new Object[3];
            postParams[0] = uploadURL;
            postParams[1] = title;
            postParams[2] = uploadFile;

            //⑨PostAsyncTaskを実行
            //          new PostAsyncTask(getCallingActivity()).execute(postParams);
            new PostAsyncTask_02().execute(postParams);

            Log.d("CSVファイルを送信しました。", "*** ＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ 送信成功***WorkerManager");

            // ここで　送信フラグを１に変える　アップデート処理
            //  Send_Flg_ON();

            // ログ用　送信スタッツ　
            send_log_Stats = "1";

        }  catch (Exception e) {
            e.printStackTrace();

            //****** ログ用　送信スタッツ *******
            send_log_Stats = "2";
            Log.d("送信処理が失敗しました。", " ＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ :::送信失敗:::WorkerManager");
        }
        /***
         * ---------------------------------------- POST 送信処理　END ------------>
         */

        // タスク数　取得 SELECT
        Get_Task_Num();

        //************* ログ用　に　Insert
        Send_table_01_log_Insert();


    } //------------ Read_Send_File_01 END


    /***
     *  ログ用　テーブル　(Send_table_01_log)  インサート
     */
    private void Send_table_01_log_Insert() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_0001_2 = helper.getWritableDatabase();

        // トランザクション　開始
        db_0001_2.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            String sousinn_time = time_001 + ":" + time_002 + ":" + time_003 + "（定期送信）";

            values.put("log_send_col_01",send_csv_file_name); //　送信ファイル名
            values.put("log_send_col_02",sousinn_time); // 送信時間
            values.put("log_send_col_03",send_log_Stats);
            values.put("log_send_col_04",Task_Count_num);

            db_0001_2.insert("Send_table_01_log",null, values);

            // トランザクション　成功
            db_0001_2.setTransactionSuccessful();

            System.out.println("＊＊＊＊＊＊　定期実行タスク　＊＊＊＊＊＊ Send_table_01_log インサート成功:::" + send_csv_file_name + ":" + sousin_time + ":" +
                    send_log_Stats + ":" + Task_Count_num);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            // トランザクション　終了
            db_0001_2.endTransaction();

            if(db_0001_2 != null) {
                db_0001_2.close();
            }
        }

    }


    /**
     *  　　Send_table_01　タスク数　取得
     */
    private void Get_Task_Num() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Worker_db_03 = helper.getReadableDatabase();

        // トランザクション　開始
        Worker_db_03.beginTransaction();
        Cursor cursor = null;

        try {

            cursor = Worker_db_03.rawQuery("select count(id) from Send_table_01",null);

            // *********** カーソルが　空　じゃなくて、　0 以上
            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    //*************** タスク　数　取得
                    Task_Count_num = cursor.getString(0);

                    System.out.println("タスク数 COUNT::::" + Task_Count_num);

                    // トランザクション　成功
                    Worker_db_03.setTransactionSuccessful();

                }

            } // end if cursor

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            Worker_db_03.endTransaction();

            if(Worker_db_03 != null) {
                Worker_db_03.close();
            }

        }


    }



    /**
     *  アラートダイアログ　表示
     */
    public void Send_CSV_POST() {

        /***
         * ---------------------------------------- POST 送信処理　Start ------------
         */

        String jim = "http://192.168.254.87/tana_phppost_file/UploadToServer.php";  // JIM　社内 OK *****
        //  String jim_02 = "http://192.168.254.51/tana_phppost_file/UploadToServer.php";

        /**
         * 　送信　プロパティ
         */
     //   String uploadURL = jim; //------ 社内
        String uploadURL = jim;

        String title = send_csv_file_name;
        String uploadFile = getFilesDir() + "/" + send_csv_file_name;


        try {
            //⑧PostAsyncTaskに渡すパラメータをObject配列に設定
            Object[] postParams = new Object[3];
            postParams[0] = uploadURL;
            postParams[1] = title;
            postParams[2] = uploadFile;

            //⑨PostAsyncTaskを実行
            //          new PostAsyncTask(getCallingActivity()).execute(postParams);
            new PostAsyncTask().execute(postParams);

            //    toastMake("CSVファイルを送信しました。", -200, 0);

            // ここで　送信フラグを１に変える　アップデート処理
            Send_Flg_ON();

        }  catch (Exception e) {
            e.printStackTrace();
            // toastMake("送信処理が失敗しました。", -200, 0);
        }

        /***
         * ---------------------------------------- POST 送信処理　END ------------>
         */


    } //-------------- Allahto_Dailog_01  END

    /**
     * ---------------- 送信　フラグを　１ する　Update -----------------
     *
     */
    public void Send_Flg_ON() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_001 = helper.getWritableDatabase();

        try {

            // トランザクション　開始
            //  db_001.setTransactionSuccessful();

            String send_Flg = "1";
            String send_Flg_zero = "0";
            ContentValues values = new ContentValues();
            values.put("send_col_19",send_Flg);

            //データベースを更新
            db_001.update("Send_table_01", values,"send_col_19 = ?",new String[]{"0"});

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


    private void init() {

        // 説明文挿入
        work_setumei = findViewById(R.id.work_setumei);

        // 入力用　エディットテキスト
        work_time_edit_01 = findViewById(R.id.work_time_edit_01);
        work_time_edit_01.setInputType(InputType.TYPE_CLASS_NUMBER);

        String work_str = "定期送信する時間を入力してください。" + "\n" +
                "※「作業追加」を１件以上追加した後で設定を行ってください。" + "\n" +
                "デフォルト時間は「16分」です。" + "\n" +
                "設定時間は 16分　～ 120分　までの間で入力してください。";

        work_setumei.setText(work_str);

    }

    private void tostMake(String msg, int x, int y) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);

        // 表示位置　調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    } //----------------- END tostMake


    /***
     *
     *     Data_zan を　SELECT する === 0:定期送信　NG  1: 定期送信 OK
     *
     *     作業追加 が０件だと、フラグ 0, 作業追加が１件でもされていると　フラグ : 1
     *
     */
    private void Teiki_Send_Flg_SELECT() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Teiki_Send_Flg_SELECT_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Teiki_Send_Flg_SELECT_db.rawQuery("select Data_zan from Flg_Table", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Teiki_Send_Flg = cursor.getInt(0);

                    System.out.println("Teiki_Send_Flg::: 値:::" + Teiki_Send_Flg);
                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if(Teiki_Send_Flg_SELECT_db != null) {
                Teiki_Send_Flg_SELECT_db.close();
            }

        }

    } //----------------- END Syuuzitu_Flg_Update


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





}