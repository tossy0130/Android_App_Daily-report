package com.example.t_prc_prototype_01;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.BindException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.ConnectionShutdownException;

public class MainActivity extends AppCompatActivity {



    /***
     *  JIM 社内　作業用　パス
     */
    private static final String [] JIM_Test = {
            "http://192.168.254.226/tana_phppost_file/TNMF.csv",  // 0
            "http://192.168.254.226/tana_phppost_file/BUMF.csv",  // 1
            "http://192.168.254.226/tana_phppost_file/SHKB.csv",  // 2
            "http://192.168.254.226/tana_phppost_file/SOMF.csv",  // 3
            "http://192.168.254.226/tana_phppost_file/NP_data.csv",// 4
            "http://192.168.254.226/tana_phppost_file/Tosou_Data.csv" // 5

    };

    /***
     *  JIM 社内　作業用　パス 02
     */
    private static final String [] JIM_Test_02 = {
            "http://192.168.11.100/tana_phppost_file/get_csv/TNMF.csv",  // 0
            "http://192.168.11.100/tana_phppost_file/get_csv/BUMF.csv",  // 1
            "http://192.168.11.100/tana_phppost_file/get_csv/SHKB.csv",  // 2
            "http://192.168.11.100/tana_phppost_file/get_csv/SOMF.csv",  // 3
            "http://192.168.11.100/tana_phppost_file/get_csv/NP_data.csv",// 4
            "http://192.168.11.100/tana_phppost_file/get_csv/Tosou_Data.csv" // 5

    };

    public static boolean app_finish_flag = false;

    // プログレスバー
    private ProgressBar progressBar;
    private int p_val = 0;

    // Flg テーブルから　送信フラグ　取得
    private String Send_data_Flg;

    // タイトル表示
    private TextView app_title_01;

    /*------ ログイン　処理系 ------*/
    // ログインボタン
    private Button login_btn;

    // CSV ダウンロードボタン
    private Button csv_btn_01;

    // ユーザーテキスト　入力用
    private EditText user_input;

    //----------DB 接続用 ----------
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    //--------- ログイン　アカウント格納用 -----------
    private String login_check;  // アカウント　番号　格納用

    // 担当コード　格納用　リスト
    private ArrayList<String> arr_user = new ArrayList<>();

    //---------- 再起動設定 -----------------------
    private Context context;
    private int waitperiod;

    private boolean user_flg;

    //---------- 画面遷移 --------
    private Intent next_intent;

    //---------- TMNF アカウント情報格納
    private String get_TMNF_01; // ユーザー ID
    private String get_TMNF_02; // ユーザー　ネーム
    private String get_TMNF_03; // 部署コード

    //----------- BUMF 所属部署　情報
    private String get_BUMF_01; // 部署

    //---------- textview
    private TextView user_view;

    private TextView date_time_text,sagyou_sentaku_view;

    //---------- table NULL 確認用　変数
    private int TNMF_db_ch_num;
    private int BUMF_db_ch_num;

    //******** Flg_Table  用　作成日　（端末が　ログインした　日付 & 日時）
    private String Flg_date_column;

    private Button sagyou_btn;

    // ID 用 day
    private String Get_ID_day;

    //****** 日付取得用
    private String Now_date_str;

    private String get_year, get_month,get_day;
    private int get_year_i, get_month_i,get_day_i;

    //****** アプリを最新にする　ボタン
    private MaterialButton kousin_w; //　最新ボタン　

    private WebView webView;

    //****** エラー表示用　
    private TextView err_view_00;

    //****** CSV ダウンロードチェック用
    private String TNMF_CSV_IN_str, BUMF_CSV_IN_str, SHKB_CSV_IN_str, SOMF_CSV_IN_str, NP_data_IN_str,Tosou_Data_IN_str;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // コンポーネント　接続
        conm_connection();

        // 初期化
        init();

        // TNMF エラーチェック用 変数挿入
    //    TNMF_Null_Check();
    //    BUMF_Null_Check();

        user_flg = false;

        // Wi-Fi 接続チェック
        checkWifiOnAndConnected();

        /**
         *  Flg_Table 用　日付　（ログイン時間　作成）
         *
         *  yyyy/MM/dd HH:mm:ss
         */
        Flg_date_column = getNowDate_03();

        /**
         *   作業日　デフォルト値　セット
         */

        Now_date_str = getNowDate();
        get_year = Now_date_str.substring(0,4); //yyyy
        get_month = Now_date_str.substring(4,6); // MM
        get_day = Now_date_str.substring(6,8); // dd


        //****** 作業日　初期値　セット
        sagyou_sentaku_view.setText(get_year + "年" + get_month + "月" + get_day + "日");

        webView = new WebView(this);

        //--------------- マスターファイル CSV ---------------

        csv_btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //********* エラーメッセージ非表示
                err_view_00.setVisibility(View.GONE);
                err_view_00.setText("");

                toastMake("ダウンロード処理を開始しました。そのままお待ちください。",0,-200);

                        //------ CSV インサート -----
                        TNMF_CSV_IN();

                        BUMF_CSV_IN();

                        SHKB_CSV_IN();

                        SOMF_CSV_IN();

                        // 作業日報　段取り時間　入力
                        NP_data_IN();

                        Tosou_Data_IN();

                        //******** チェック用
                   //     ALL_SELECT_C();

            }
        });

        /***
         *  *********** アプリを最新にする *************
         */
        kousin_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*************** 最新PDF を　webview　で　表示 ********************
                Intent intent = new Intent(getApplicationContext(), WebViewActivity_06.class);
                startActivity(intent);

            }
        });

        //   restart(context, waitperiod);

        //------------ アプリ タイトル ------------ Start
        String title_01 = "生産管理アプリ";
        app_title_01.setText(title_01);
        //------------ アプリ タイトル ------------ END

        // ログインボタンの無効化
        //  login_btn.setEnabled(false);

        /**
         *  「作業日　選択　指定　ボタン」
         */
        sagyou_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //****************** 年　月　日
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //------- 日付　格納

                        // dd
                   //   get_day = String.format("%02s",dayOfMonth);

                        // yyyyMMdd
                        get_year = String.format("%02d", year); //yyyy
                        get_month = String.format("%02d", monthOfYear + 1); // MM
                        get_day = String.format("%02d", dayOfMonth); // dd

                        Now_date_str = get_year + get_month + get_day;

                        System.out.println("Get_ID_day::: 値" + Now_date_str);

                        // Setした日付を取得する
                        sagyou_sentaku_view.setText(String.format(Locale.US, "%02d年%02d月%02d日", year, monthOfYear + 1,dayOfMonth));

                    }
                }, year,month,day);

                datePickerDialog.show();

            }
        });

        //------------------------ 入力用　エディットテキストに　フォーカスが当たったとき
        user_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // キーボードの表示・非表示用
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //*** フォーカスを受け取ったとき ***
                if (hasFocus) {

                    // 判定用　配列 　DB から作成
                    User_Check_SELECT();

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    // キーボード　数値入力
                    user_input.setRawInputType(Configuration.KEYBOARD_QWERTY);

                } else { // *** フォーカスが外れた場合 ***
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    User_Check_SELECT();
                }

            }
        }); //-------------------- setOnFocusChangeListener END ------->

        user_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // エディット キーの「エンターボタン」が　押されたら
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // ボタンが離されたら
                    if (event.getAction() == KeyEvent.ACTION_UP) {

                        // ソフトキーボードを隠す
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);

                        // エディットテキスト　値　取得
                        login_check = user_input.getText().toString();

                        if(login_check == null) {
                            toastMake("入力欄が空白です。「担当コード」を入力してください。", 0, -200);
                            return false;
                        }

                        User_Check_SELECT();

                        /*------ ログインボタン　処理 ------*/

                        // ログインボタン
                        login_btn.setBackgroundColor(Color.rgb(227, 1, 127));

                    }

                    return true;

                } //  END if 　「エンターボタン」が押されたら END

                return false;
            }
        }); // ------------------- setOnEditorActionListener END --------------->

        //------------------------ 「ログイン」　ボタン　タップ処理 Start --------------------
        // ログイン　ボタン処理
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // エディットテキスト　値　取得
                login_check = user_input.getText().toString();

                User_Check_SELECT();

                /**
                 *   エラー処理　エディットテキストが 「空」 で　、 ボタンが押された場合
                 */
                if (login_check.length() == 0 || login_check.equals("")) {

                    // ソフトキーボードを隠す
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);

                    toastMake("入力欄が空白です。「担当コード」を入力してください。", 0, -200);
                    return;

                    /**
                     *  「作業日」が空の場合
                     */
                } else if(sagyou_sentaku_view.getText().toString().equals("")) {

                    // ソフトキーボードを隠す
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);

                    toastMake("「作業日」が未入力です。「作業日」を選択してください。", 0, -200);
                    return;

                } else {

                    if (arr_user.contains(login_check)) {

                        /**　「確定」 ボタン　処理 ------------------
                         *
                         **   ユーザーが　入力した文字が、 配列 arr_col に　担当者コードが存在していた場合の処理
                         **
                         */

                        // ソフトキーボードを隠す
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);

                        // ************ SQL アカウント　SELECT  Start ************* //
                        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());

                        SQLiteDatabase Ac_db = helper.getReadableDatabase();

                        String[] arr_tnmf = new String[4];

                        //-------------- トランザクション　開始 -------------------//
                        Ac_db.beginTransaction();

                        try {

                            Cursor cursor = Ac_db.rawQuery("select TNMF_c_01,TNMF_c_02,TNMF_c_04,BUMF_c_02 " +
                                    "from TNMF_table " +
                                    "left outer join BUMF_table on BUMF_table.BUMF_c_01 = TNMF_table.TNMF_c_04 " +
                                    "WHERE TNMF_table.TNMF_c_01 like " +  login_check + " LIMIT 1",null);

                            if(cursor.moveToFirst()) {

                                    int idx = cursor.getColumnIndex("TNMF_c_01");
                                 //  System.out.println("出力テスト idx" + idx);

                                    arr_tnmf[0] = cursor.getString(idx);

                                    idx = cursor.getColumnIndex("TNMF_c_02");
                                 //   System.out.println("出力テスト 02 idx" + idx);

                                    arr_tnmf[1] = cursor.getString(idx);

                                    idx = cursor.getColumnIndex("TNMF_c_04");
                                //    System.out.println("出力テスト 03 idx" + idx);

                                    arr_tnmf[2] = cursor.getString(idx);

                                    idx = cursor.getColumnIndex("BUMF_c_02");
                                  //  System.out.println("出力テスト 04 idx" + idx);

                                    arr_tnmf[3] = cursor.getString(idx);

                                    //------------- TNMF アカウント情報格納

                                    get_TMNF_01 = arr_tnmf[0]; // ユーザー ID
                                    get_TMNF_02 = arr_tnmf[1]; // ユーザー名

                                    //----------- ログイン　成功ユーザー テキストビュー　表示
                                    user_view.setText(get_TMNF_02);

                                    get_TMNF_03 = arr_tnmf[2]; // 部署コード
                                    get_BUMF_01 = arr_tnmf[3]; // 部署

                                //    System.out.println("結合sql ゲット" + arr_tnmf[3]);

                            }

                            // トランザクション　成功
                            Ac_db.setTransactionSuccessful();

                        } catch (SQLException e) {
                            e.printStackTrace();

                        } finally {
                            // トランザクション　終了
                            Ac_db.endTransaction();

                            //-------- DB を　閉じる
                            if(Ac_db != null) {
                                Ac_db.close();
                            }
                        }

                        //**************** Flg_Table へ　作業日　dd インサート
                        Flg_Table_init();

                        //----------------- Home.java へ 画面遷移 ----------------------
                        next_intent = new Intent(getApplication(), Home.class);
                        // 値を渡す
                        // intent.putExtra()
                        //------------------- HomeActivity へ　値を受け渡す -------
                        next_intent.putExtra("TNMF_01_val",get_TMNF_01); // 担当者コード
                        next_intent.putExtra("TNMF_02_val",get_TMNF_02); // 名前
                        next_intent.putExtra("TNMF_03_val",get_TMNF_03); // 部署コード

                        next_intent.putExtra("BUMF_01_val", get_BUMF_01); // 〇〇課

                        startActivity(next_intent);

                    } else {
                        // ********* ログイン　エラー処理 *********
                        toastMake("入力された「担当コード」は存在しません。確認して再度ご入力してください。", 0, -200);
                        return;
                    }


                }


            }
        });

    } //-------------------------- OnCreate END -----------------------


    /***
     *
     *    ヘッダー メニュー　追加
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top,menu);
        return true;
    }

    /**
     *  ヘッダー　ボタン　処理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_top_btn :
                //------- ボタンを押した後の処理
                Flg_Send_SELECT();

                if(Send_data_Flg.equals("0") || Send_data_Flg == null) {
                    /**
                     * 一度も送信されていないので　エラーを返す
                     */
                    Allahto_Dailog_Send_error();

                } else {
                    /**
                     *  アプリ終了　処理　起動
                     */
                    Allahto_Dailog_01();
                }

                break;

            /**
             *   「？」　ボタンを　タップした場合
             */
            case R.id.lesson_btn_01 :

               //***************** ？　動画説明へ　遷移
                Intent intent = new Intent(getApplicationContext(), WebViewActivity_03.class);
                startActivity(intent);

                break;
        }

        return true;
    }

    //------------- - CSV 読み込み（TNMF.csv）　メソッド  テーブル名 => TNMF_TB ------------
    private void Tnmf_csv_Load() {

    }

    // DB 初期化　処理
    private void db_init() {


    }

    //----------- TestOpenHelper DB 作成 ----------
    private void create_Master_DB() {

        if (helper == null) {
            // Master DB の 作成
            helper = new TestOpenHelper(getApplicationContext());
        } else {
            return;
        }

        // DB が作成されていなければ作成
        if (db == null) {
            db = helper.getWritableDatabase();
            System.out.println("Master用　データベース作成　完了");

        } else {
            return;
        }

    } //---------- create_Master_DB END -----------


    private void User_Check_SELECT() {

        /*----- DB 読み込み 準備 ------*/
        helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_select = helper.getReadableDatabase();

        String[] arr_item = new String[1];

        // トランザクション完了
        db_select.beginTransaction();
        try {

            Cursor cursor = db_select.rawQuery("SELECT TNMF_c_01 FROM TNMF_table WHERE TNMF_c_01 NOTNULL", null);

            if (cursor.moveToFirst()) {

                do {

                    int idx = cursor.getColumnIndex("TNMF_c_01");
                    arr_item[0] = cursor.getString(idx);

                  //  System.out.println("arr_item[0] 1つの値" + arr_item[0]);

                    arr_user.add(arr_item[0]);

                } while (cursor.moveToNext());

                // トランザクション　成功
                db_select.setTransactionSuccessful();

                // 出力テスト
                for (String a_u : arr_user) {
                 //   System.out.println(a_u);
                }

            } // END if

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            // トランザクション完了
            db_select.endTransaction();

            if (db_select != null) {
                db_select.close();
            }
        }

    } //---------------------- User_Check_SELECT END -----------------


    //------------- TNMF.csv データベース　インポート --------
    private void TNMF_CSV_IN() {



        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス
                String jim = "http://192.168.254.87/tana_phppost_file/get_csv/TNMF.csv";



                //------- リクエスト作成
                Request request = new Request.Builder()
                       // .url(JIM_Test_02[0]) //--- 社内 02
                        .url(JIM_Test[0]) //--- 社内
                       // .url(TOYAMA_CSV_DATA[0]) //****** 本番
                        .get()
                        .build();

                // クライアント　作成
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //---------------------- doInBackground END ---------->

            // DB へ　インサート処理
            @Override
            protected void onPostExecute(String result) {

                /*---------- TestOpenHelper インスタンス生成 ------------*/
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                //----------- 削除処理 ------------
                // トランザクション 開始 ------>
                db.beginTransaction();

                try {
                    db.delete("TNMF_table", null, null);

                    // トランザクション成功処理
                    db.setTransactionSuccessful();

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    //----------- トランザクション　完了
                    db.endTransaction();

                }

                //----------- 削除処理 END ------------>

                String line = null;

                String[] RowData = new String[4];

                //---------- インサート処理 --------------
                db.beginTransaction();

                try {

                    // result で　受け取った値を　バッファリーダーで読み込む
                    BufferedReader reader = new BufferedReader(new StringReader(result));


                    while ((line = reader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        values.put("TNMF_c_01", RowData[0]);
                        values.put("TNMF_c_02", RowData[1]);
                        values.put("TNMF_c_03", RowData[2]);
                        values.put("TNMF_c_04", RowData[3]);

                        // TNMF_TB へ　挿入
                        db.insert("TNMF_table", null, values);

                        /*
                        System.out.println("件数：" + loop_count + "件、" + "カラム01:" + RowData[0] + "カラム02:" +
                                RowData[1] + "カラム03:" + RowData[2] + "カラム04:" + RowData[3]);

                         */

                    }

                    // トランザクション　成功
                    db.setTransactionSuccessful();

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    // トランザクションの終了
                    db.endTransaction();
                    if(db != null) {
                        db.close();
                    }
                }

            }

        }.execute();

    } // ----------------- TNMF_Csv_IN END ------------------------->


    private void BUMF_CSV_IN() {

        new AsyncTask<Void, Void,String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス    BUMF.csv
                String jim_BUMF = "http://192.168.254.87/tana_phppost_file/get_csv/BUMF.csv";



                //----- リクエスト　作成 (GET) ------
                Request request = new Request.Builder()
                      //  .url(JIM_Test_02[1])     //--- 社内 02
                        .url(JIM_Test[1])     //--- 社内
                      //  .url(TOYAMA_CSV_DATA[1]) //******** 本番
                        .get()
                        .build();

                //------- クライアント　作成 -------
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();


                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //----------------- doInBackground


            //************** バックグラウンド完了処理
            // DB へ　インサート
            @Override
            protected void onPostExecute(String result) {

                //---------------- TestOpenHelper インスタンス生成
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                //------ 削除処理 ----------------->

                // トランザクション　開始------>
                db.beginTransaction();

                try {
                    // delete("テーブル名", )
                    db.delete("BUMF_table",null,null);

                    // トランザクション　成功完了
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();

                }

                //------ 削除処理 END----------------->

                //------ BUMF insert 処理 ----------------->
                String line = null;

                String [] RowData = new String[2];

                //---------- インサート処理 開始 -------------->
                // トランザクション　開始
                db.beginTransaction();
                try {

                    // GET で　取得した値を　BufferedReader に格納
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(result));


                    while ((line = bufferedReader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        // インサートの　値格納
                        // value.put("カラム名", 入力する値);
                        values.put("BUMF_c_01",RowData[0]);
                        values.put("BUMF_c_02",RowData[1]);

                        // インサート処理
                        // db.insert("テーブル名", null, インサートする ContentValues の　値);
                        db.insert("BUMF_table", null, values);

                        // 出力チェック
                   //     System.out.println("カラム名 1:" + RowData[0] + " " + "カラム名 2:" + RowData[1] + "カラム件数：" + count);

                    }

                    db.setTransactionSuccessful();
                    System.out.println("BUMF インサート　トランザクション　成功");

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    db.endTransaction();
                    if(db != null) {
                        db.close();
                    }

                   // toastMake("ダウンロード処理が完了しました。",0,-200);
                }


            }


        }.execute();

    }

    //-------------------- SHKB.csv インポート
    private void SHKB_CSV_IN() {

        new AsyncTask<Void, Void,String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス    BUMF.csv
                String jim_SHKB = "http://192.168.254.87/tana_phppost_file/get_csv/SHKB.csv";



                //----- リクエスト　作成 (GET) ------
                Request request = new Request.Builder()
                       // .url(JIM_Test_02[2]) //---- 社内 02
                         .url(JIM_Test[2])   //---- 社内
                      //  .url(TOYAMA_CSV_DATA[2]) //******* 本番
                        .get()
                        .build();

                //------- クライアント　作成 -------
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //----------------- doInBackground

            //************** バックグラウンド完了処理
            // DB へ　インサート
            @Override
            protected void onPostExecute(String result) {

                //---------------- TestOpenHelper インスタンス生成
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                //------ 削除処理 ----------------->

                // トランザクション　開始------>
                db.beginTransaction();

                try {
                    // delete("テーブル名", )
                    db.delete("SHKB_table",null,null);

                    // トランザクション　成功完了
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();

                }

                //------ 削除処理 END----------------->

                //------ BUMF insert 処理 ----------------->
                String line = null;

                String [] RowData = new String[2];

                //---------- インサート処理 開始 -------------->
                // トランザクション　開始
                db.beginTransaction();
                try {

                    // GET で　取得した値を　BufferedReader に格納
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(result));
                    int count = 0;

                    while ((line = bufferedReader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        // インサートの　値格納
                        // value.put("カラム名", 入力する値);
                        values.put("SHKB_c_01",RowData[0]);
                        values.put("SHKB_c_02",RowData[1]);

                        // インサート処理
                        // db.insert("テーブル名", null, インサートする ContentValues の　値);
                        db.insert("SHKB_table", null, values);

                        // 出力チェック
                    //    System.out.println("カラム名 1:" + RowData[0] + " " + "カラム名 2:" + RowData[1] + "カラム件数：" + count);

                        count++;
                    }

                    db.setTransactionSuccessful();
                    System.out.println("SHKB インサート　トランザクション　成功");

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    db.endTransaction();
                    if(db != null) {
                        db.close();
                    }


                }


            }


        }.execute();

    }


    //-------------------- SOMF.csv インポート
    private void SOMF_CSV_IN() {

        new AsyncTask<Void, Void,String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス    BUMF.csv
                String jim_SHKB = "http://192.168.254.87/tana_phppost_file/get_csv/SOMF.csv";

                //----- リクエスト　作成 (GET) ------
                Request request = new Request.Builder()
                      //  .url(JIM_Test_02[3]) //　社内 02
                        .url(JIM_Test[3])    // ----　社内
                     //   .url(TOYAMA_CSV_DATA[3]) // ****** 本番
                        .get()
                        .build();

                //------- クライアント　作成 -------
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //----------------- doInBackground

            //************** バックグラウンド完了処理
            // DB へ　インサート
            @Override
            protected void onPostExecute(String result) {

                //---------------- TestOpenHelper インスタンス生成
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                //------ 削除処理 ----------------->

                // トランザクション　開始------>
                db.beginTransaction();

                try {
                    // delete("テーブル名", )
                    db.delete("SOMF_table",null,null);

                    // トランザクション　成功完了
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();

                }

                //------ 削除処理 END----------------->

                //------ BUMF insert 処理 ----------------->
                String line = null;

                String [] RowData = new String[2];

                //---------- インサート処理 開始 -------------->
                // トランザクション　開始
                db.beginTransaction();
                try {

                    // GET で　取得した値を　BufferedReader に格納
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(result));
                 //   int count = 0;

                    while ((line = bufferedReader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        // インサートの　値格納
                        // value.put("カラム名", 入力する値);
                        values.put("SOMF_c_01",RowData[0]);
                        values.put("SOMF_c_02",RowData[1]);

                        // インサート処理
                        // db.insert("テーブル名", null, インサートする ContentValues の　値);
                        db.insert("SOMF_table", null, values);

                        // 出力チェック
                    //    System.out.println("カラム名 1:" + RowData[0] + " " + "カラム名 2:" + RowData[1] + "カラム件数：" + count);

                   //     count++;
                    }

                    db.setTransactionSuccessful();
                    System.out.println("SOMF インサート　トランザクション　成功");

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    db.endTransaction();
                    if(db != null) {
                        db.close();
                    }


                }


            }


        }.execute();

    }



    //-------------------- NP_data.csv インポート
    private void NP_data_IN() {

        new AsyncTask<Void, Void,String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス    BUMF.csv
                String jim_SHKB = "http://192.168.254.87/tana_phppost_file/get_csv/NP_data.csv";

                //----- リクエスト　作成 (GET) ------
                Request request = new Request.Builder()
                      //  .url(JIM_Test_02[4]) //----- 社内 02
                        .url(JIM_Test[4])  //------ 社内
                      //  .url(TOYAMA_CSV_DATA[4]) //*********** 本番
                        .get()
                        .build();

                //------- クライアント　作成 -------
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //----------------- doInBackground

            //************** バックグラウンド完了処理
            // DB へ　インサート
            @Override
            protected void onPostExecute(String result) {

                //---------------- TestOpenHelper インスタンス生成
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                //------ 削除処理 ----------------->

                // トランザクション　開始------>
                db.beginTransaction();

                try {
                    // delete("テーブル名", )
                    db.delete("NP_data_table",null,null);

                    // トランザクション　成功完了
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();

                }

                //------ 削除処理 END----------------->

                //------ BUMF insert 処理 ----------------->
                String line = null;

                String [] RowData = new String[3];

                //---------- インサート処理 開始 -------------->
                // トランザクション　開始
                db.beginTransaction();
                try {

                    // GET で　取得した値を　BufferedReader に格納
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(result));
                //    int count = 0;

                    while ((line = bufferedReader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        // インサートの　値格納
                        // value.put("カラム名", 入力する値);
                        values.put("NP_data_c_01",RowData[0]);
                        values.put("NP_data_c_02",RowData[1]);
                        values.put("NP_data_c_03",RowData[2]);

                        // インサート処理
                        // db.insert("テーブル名", null, インサートする ContentValues の　値);
                        db.insert("NP_data_table", null, values);

                        // 出力チェック
                        //    System.out.println("カラム名 1:" + RowData[0] + " " + "カラム名 2:" + RowData[1] + "カラム件数：" + count);

                 //       count++;
                    }

                    db.setTransactionSuccessful();
                    System.out.println("NP_data インサート　トランザクション　成功");

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    db.endTransaction();
                    if(db != null) {
                        db.close();
                    }


                }


            }


        }.execute();

    }


    /**
     *    Tosou_Data インサート
     */

    private void Tosou_Data_IN () {

        new AsyncTask<Void, Void,String>() {

            @Override
            protected String doInBackground(Void... params) {

                // 返却値
                String result = null;

                // CSV ダウンロード & パース用 対象 URL

                // 社内環境用　URL パス    BUMF.csv
                String jim_SHKB = "http://192.168.254.87/tana_phppost_file/get_csv/Tosou_Data.csv";

                //----- リクエスト　作成 (GET) ------
                Request request = new Request.Builder()
                      //  .url(JIM_Test_02[5]) //------ 社内 02
                        .url(JIM_Test[5])   //------ 社内
                      //  .url(TOYAMA_CSV_DATA[5]) //************ 本番
                        .get()
                        .build();

                //------- クライアント　作成 -------
                OkHttpClient client = new OkHttpClient();

                // GET リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();

                } catch(BindException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    System.out.println("マスターファイル　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("マスターファイル SQL　受信エラー。ネットワーク状況を確認してください。");
                    e.printStackTrace();
                }

                return result;

            } //----------------- doInBackground

            //************** バックグラウンド完了処理
            // DB へ　インサート
            @Override
            protected void onPostExecute(String result) {

                //---------------- TestOpenHelper インスタンス生成
                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db_t = helper.getReadableDatabase();

                //------ 削除処理 ----------------->

                // トランザクション　開始------>
                db_t.beginTransaction();

                try {
                    // delete("テーブル名", )
                    db_t.delete("Tosou_Data",null,null);

                    // トランザクション　成功完了
                    db_t.setTransactionSuccessful();

                } finally {
                    db_t.endTransaction();

                }

                //------ 削除処理 END----------------->

                //------ BUMF insert 処理 ----------------->
                String line = null;

                String [] RowData = new String[3];

                //---------- インサート処理 開始 -------------->
                // トランザクション　開始
                db_t.beginTransaction();
                try {

                    // GET で　取得した値を　BufferedReader に格納
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(result));
           //         int count = 0;

                    while ((line = bufferedReader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        // インサートの　値格納
                        // value.put("カラム名", 入力する値);
                        values.put("Tosou_Data_c_01",RowData[0]);
                        values.put("Tosou_Data_c_02",RowData[1]);
                        values.put("Tosou_Data_c_03",RowData[2]);

                        // インサート処理
                        // db.insert("テーブル名", null, インサートする ContentValues の　値);
                        db_t.insert("Tosou_Data", null, values);

                        // 出力チェック
                        //    System.out.println("カラム名 1:" + RowData[0] + " " + "カラム名 2:" + RowData[1] + "カラム件数：" + count);

                 //       count++;
                    }

                    db_t.setTransactionSuccessful();
                    System.out.println("Tosou_Data インサート　トランザクション　成功");

                    //************ ダウンロード　OK の場合 **************
                    toastMake("ダウンロード処理が完了しました。", 0, -200);

                    /**
                     **************  ログイン　ダウンロード　ボタンの色を変える ***************
                     */
                    csv_btn_01.setTextColor(Color.parseColor("#ffffff"));
                    csv_btn_01.setBackgroundColor(Color.parseColor("#FF4081"));

                } catch(SocketTimeoutException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();
                    return;

                } catch (ConnectionShutdownException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();

                } catch (ArrayIndexOutOfBoundsException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();

                } catch (NullPointerException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：NullPointerException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();

                } catch(IOException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();

                } catch (SQLException e) {
                    err_view_00.setVisibility(View.VISIBLE);
                    err_view_00.setText("ダウンロードエラー：：：SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();


                } finally {
                    db_t.endTransaction();
                    if(db_t != null) {
                        db_t.close();
                    }


                }


            }


        }.execute();

    }




    //-------------------- DB SELECT 反映のために　リスタートさせる -------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void restart(Context cnt, int period) {
        // intent 設定で自分自身のクラスを設定
        Intent mainActivity = new Intent(cnt, MainActivity.class);

        // PendingIntent , ID=0
        PendingIntent pendingIntent = PendingIntent.getActivity(cnt,
                0, mainActivity, PendingIntent.FLAG_CANCEL_CURRENT);

        // AlarmManager のインスタンス生成
        AlarmManager alarmManager = (AlarmManager) cnt.getSystemService(
                Context.ALARM_SERVICE);

        // １回のアラームを現在の時間からperiod（５秒）後に実行させる
        if (alarmManager != null) {
            long trigger = System.currentTimeMillis() + period;
            alarmManager.setExact(AlarmManager.RTC, trigger, pendingIntent);
        }

        // アプリ終了
        finish();
    }

    //-------------------- トースト作成 ----------------------
    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                System.out.println("アクセスポイント 接続　OK");
                return false; // Not connected to an access point
            }
            System.out.println("Wi-Fi 接続　OK");

            return true; // Connected to an access point
        } else {

            System.out.println("Wi-Fi 接続　NG");
            return false; // Wi-Fi adapter is OFF
        }
    } //-------------- checkWifiOnAndConnected END

    // **************** 商品マスター　ダウンロード *********************
    // --------------- CSV インポート SHMF.csv ------------------



    //---------------- 端末　戻るボタンが　押された処理
    @Override
    public void onBackPressed(){

        // 再度 ログインできるように　false に　する
        app_finish_flag = false;

        User_Check_SELECT();
    }



    //---------------- TNMF_table NULL チェック
    private void TNMF_Null_Check() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_TNMF_ch = helper.getReadableDatabase();

        // トランザクション　開始
        db_TNMF_ch.beginTransaction();

        try {

            Cursor cursor = db_TNMF_ch.rawQuery("SELECT count(*) FROM TNMF_table", null);

            cursor.moveToFirst();

            TNMF_db_ch_num = cursor.getInt(0);

            // トランザクション　成功
            db_TNMF_ch.setTransactionSuccessful();

        } catch (SQLiteDatabaseLockedException e) {
                System.out.println("TNMF テーブルSQLiteDatabaseLockedException エラー");
        } finally {
            db_TNMF_ch.endTransaction();

            if(db_TNMF_ch != null) {
                db_TNMF_ch.close();
            }
        }

    } //-------------------------- TNMF_Null_Check END ------------>

    private void BUMF_Null_Check() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_BUMF_ch = helper.getReadableDatabase();

        // トランザクション開始
        db_BUMF_ch.beginTransaction();

        try {

            Cursor cursor = db_BUMF_ch.rawQuery("SELECT count(*) FROM BUMF_table", null);

            cursor.moveToFirst();

            BUMF_db_ch_num = cursor.getInt(0);

            // トランザクション　成功
            db_BUMF_ch.setTransactionSuccessful();

        } catch (SQLiteDatabaseLockedException e) {
            System.out.println("BUMF_table チェック エラー : SQLiteDatabaseLockedException");

        } finally {
            db_BUMF_ch.endTransaction();

            if(db_BUMF_ch != null) {
                db_BUMF_ch.close();
            }
        }

    } //---------------- BUMF_Null_Check END

    //----------------------------  コンポーネント　接続 定義
    private void conm_connection() {

        // アプリタイトル　テキストビュー
        app_title_01 = findViewById(R.id.app_title_01);

        // ログイン後　表示用　テキストビュー
        user_view = findViewById(R.id.user_view);

        //----- ログイン処理系 ------*/
        // ログインボタン
        login_btn = (MaterialButton) findViewById(R.id.login_btn);

        // ダウンロードボタン
        csv_btn_01 = (MaterialButton) findViewById(R.id.csv_btn_01);

        // ユーザーID 入力欄
        user_input = (EditText) findViewById(R.id.user_input);

        //------- 日付　& 時間　表示用　テキスト
        date_time_text = findViewById(R.id.date_time_text);

        //------- 作業日　選択ボタン
        sagyou_btn = findViewById(R.id.sagyou_btn);

        //------- 作業日　表示　テキストビュー
        sagyou_sentaku_view = findViewById(R.id.sagyou_sentaku_view);

        // ********** アプリを最新にする　ボタン
        kousin_w = (MaterialButton) findViewById(R.id.kousin_w);

    }

    // --------------------------- コンポーネント　初期化　処理
    private void init() {

        create_Master_DB();

        System.out.println("Master用　データベース作成　完了");

        user_view.setText("");

        user_input.setText("");
        //------- エディットテキスト　インプットタイプ設定
        user_input.setInputType(InputType.TYPE_CLASS_NUMBER);

        /*
        //-------- 再起動設定
        context = getApplicationContext();
        waitperiod = 639;

         */

        //******* エラー表示用
        err_view_00 = (TextView) findViewById(R.id.err_view_00);
        err_view_00.setText("");
        err_view_00.setVisibility(View.GONE);

    }

    // リスタートした場合
    @Override
    protected void onRestart() {
        super.onRestart();

        // 再度 ログインできるように　false に　する
        app_finish_flag = false;

        // ユーザー入力　エディットテキストを　「空」　にする。
        user_input.setText("");
        user_view.setText("");

        // ユーザーセレクト
        User_Check_SELECT();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 再度 ログインできるように　false に　する
        app_finish_flag = false;

        // ユーザー入力　エディットテキストを　「空」　にする。
        user_input.setText("");
        user_view.setText("");

        // ユーザーセレクト
        User_Check_SELECT();
    }

    /**
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_01() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(MainActivity.this);
        titleView.setText("アプリケーションの終了");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(MainActivity.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("お疲れ様でした。アプリケーションを終了しますか？（作業データは全て削除されます）");

        bilder.setNegativeButton("終了する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Sned_Table_Data_Delete(); // 送信テーブル　&  グラフテーブル　データ削除

                Flg_Table_Delete(); // フラグテーブル　データ削除
            }
        });

        bilder.setPositiveButton("終了しない", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //********　終了しない *********
                // 　再度ログインできるように、フラグを false にする。
                app_finish_flag = false;
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
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate_03(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);

    } //******* getNowDate_03 END


    /***
     *     Flg_Table ===== create_date => データ作成日  インサート
     */
    private void Flg_Table_init() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Flg_db = helper.getWritableDatabase();

        // トランザクション開始
        Flg_db.beginTransaction();

        try {

            Flg_db.delete("Flg_Table",null,null);

            Flg_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Flg_db.endTransaction();
        }

            // トランザクション開始
            Flg_db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            //********** 作業者　コード　インサート
            values.put("Sagyousya_code",get_TMNF_01);

            /**
             *    「作業日」 dd を　インサートする。
             */

            Now_date_str = get_year + get_month + get_day;
            values.put("create_date ", Now_date_str);
            System.out.println("Insert_Date_id:::値" + Now_date_str);

            // ******  インサート処理
            Flg_db.insert("Flg_Table", null, values);

            // トランザクション成功
            Flg_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            Flg_db.endTransaction();

            if(Flg_db != null) {
                Flg_db.close();
            }

        }

    } // ---------------- END Flg_Table_init

    /**
     *   アプリ終了時　削除処理
     */
    private void Sned_Table_Data_Delete () {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_delete = helper.getWritableDatabase();

        db_delete.beginTransaction();

        try {

            db_delete.delete("Send_table_01",null,null);
            db_delete.delete("Send_Grafu_Table",null, null);


            System.out.println("Sned_Table_Data_Delete  削除 OK");

            db_delete.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db_delete.endTransaction();
            if(db_delete != null) {
                db_delete.close();
            }
        }

        /**
         *  削除　アラートダイアログ
         */
        Allahto_Dailog_DELETE();

    }

    /***
     *  データ　削除　アラートダイアログ
     */

    /**
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_DELETE() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(MainActivity.this);
        titleView.setText("作業データの削除が完了しました。");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(MainActivity.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("データ削除が完了しました。");

        bilder.setNegativeButton("アプリを閉じる", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 終了
                finish();
                //******* プロセスを　終了させる ********
                android.os.Process.killProcess(android.os.Process.myPid());

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

        //******************************************* END ボタン　配色　変更


    } //-------------- Allahto_Dailog_01  END


    /***
     *     Flg_Table ===== Send_data_Flg 取得
     */
    private void Flg_Send_SELECT() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Flg_db_Send = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Flg_db_Send.rawQuery("select Send_data_Flg from Flg_Table", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Send_data_Flg = cursor.getString(0);
                }

            } else {
                Send_data_Flg = "0";
                return;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Flg_db_Send != null) {
                Flg_db_Send.close();
            }
        }

    } // ---------------- END Flg_Send_SELECT


    private void Allahto_Dailog_Send_error() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(MainActivity.this);
        titleView.setText("ファイル送信エラー");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.red));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(MainActivity.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("ファイル送信を行ってから、アプリケーションを終了してください。");

        bilder.setNegativeButton("アプリケーションへ戻る", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;

            }
        });

        AlertDialog dialog = bilder.create();
        dialog.show();

        //********* ボタン いいえ **********
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B20000"));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

    } //-------------- Allahto_Dailog_Send_error  END

    private void Flg_Table_Delete() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Flg_db_delete = helper.getWritableDatabase();

        Flg_db_delete.beginTransaction();

        try {

            Flg_db_delete.delete("Flg_Table", null,null);

            System.out.println("Flg_Table  削除 OK");

            Flg_db_delete.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Flg_db_delete.endTransaction();

            if(Flg_db_delete != null) {
                Flg_db_delete.close();
            }
        }

    } //*********************************** END funciton

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate() {

        final DateFormat df = new SimpleDateFormat("yyyyMMdd");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    //**************** webview中 戻る
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 端末の戻るボタンでブラウザバック
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *  　CSV ファイル　ダウンロード後　チェック  各テーブルから　カラムを呼んで確認
     */
    private void ALL_SELECT_C () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase ALL_C_DB = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = ALL_C_DB.rawQuery("SELECT TNMF_c_01, BUMF_c_01, SHKB_c_01, SOMF_c_01,NP_data_c_01, Tosou_Data_c_01" +
                    " FROM TNMF_table" +
                    " JOIN BUMF_table on BUMF_c_01" +
                    " JOIN  SHKB_table on SHKB_c_01" +
                    " JOIN SOMF_table on SOMF_c_01" +
                    " JOIN NP_data_table on NP_data_c_01" +
                    " JOIN Tosou_Data on Tosou_Data_c_01 limit 1", null);

            if (cursor != null || cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {

                    TNMF_CSV_IN_str = cursor.getString(0);
                    BUMF_CSV_IN_str = cursor.getString(1);
                    SHKB_CSV_IN_str = cursor.getString(2);
                    SOMF_CSV_IN_str = cursor.getString(3);
                    NP_data_IN_str = cursor.getString(4);
                    Tosou_Data_IN_str = cursor.getString(5);

                    System.out.println("TNMF_CSV_IN_str" + TNMF_CSV_IN_str);
                    System.out.println("BUMF_CSV_IN_str" + BUMF_CSV_IN_str);
                    System.out.println("SHKB_CSV_IN_str" + SHKB_CSV_IN_str);
                    System.out.println("SOMF_CSV_IN_str" + SOMF_CSV_IN_str);
                    System.out.println("NP_data_IN_str" + NP_data_IN_str);
                    System.out.println("Tosou_Data_IN_str" + Tosou_Data_IN_str);

                }

            } else {

                TNMF_CSV_IN_str = null;
                BUMF_CSV_IN_str = null;
                SHKB_CSV_IN_str = null;
                SOMF_CSV_IN_str = null;
                NP_data_IN_str = null;
                Tosou_Data_IN_str = null;

                System.out.println("TNMF_CSV_IN_str" + TNMF_CSV_IN_str);
                System.out.println("BUMF_CSV_IN_str" + BUMF_CSV_IN_str);
                System.out.println("SHKB_CSV_IN_str" + SHKB_CSV_IN_str);
                System.out.println("SOMF_CSV_IN_str" + SOMF_CSV_IN_str);
                System.out.println("NP_data_IN_str" + NP_data_IN_str);
                System.out.println("Tosou_Data_IN_str" + Tosou_Data_IN_str);


            }

            if (TNMF_CSV_IN_str == null && BUMF_CSV_IN_str == null) {

                //************ ダウンロード　NG の場合 **************
                toastMake("ダウンロードに失敗しました。\n" +
                        "Wi-Fi環境を確認して、もう一度お試しください。", 0, -200);

                return;

            } else {

                //************ ダウンロード　OK の場合 **************
                toastMake("ダウンロード処理が完了しました。", 0, -200);

                /**
                 *  ログイン　ダウンロード　ボタンの色を変える
                 */
                csv_btn_01.setTextColor(Color.parseColor("#ffffff"));
                csv_btn_01.setBackgroundColor(Color.parseColor("#FF4081"));

                return;

            }

        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if(ALL_C_DB != null) {
                ALL_C_DB.close();
            }
        }

    }


    /**
     *  　CSV ファイル　ダウンロード後　チェック  各テーブルから　カラムを呼んで確認
     */
    private void ALL_SELECT_Rstart () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase ALL_C_DB = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = ALL_C_DB.rawQuery("SELECT TNMF_c_01, BUMF_c_01, SHKB_c_01, SOMF_c_01,NP_data_c_01, Tosou_Data_c_01" +
                    " FROM TNMF_table" +
                    " JOIN BUMF_table on BUMF_c_01" +
                    " JOIN  SHKB_table on SHKB_c_01" +
                    " JOIN SOMF_table on SOMF_c_01" +
                    " JOIN NP_data_table on NP_data_c_01" +
                    " JOIN Tosou_Data on Tosou_Data_c_01 limit 1", null);

            if (cursor != null || cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {

                    TNMF_CSV_IN_str = cursor.getString(0);
                    BUMF_CSV_IN_str = cursor.getString(1);
                    SHKB_CSV_IN_str = cursor.getString(2);
                    SOMF_CSV_IN_str = cursor.getString(3);
                    NP_data_IN_str = cursor.getString(4);
                    Tosou_Data_IN_str = cursor.getString(5);

                    System.out.println("TNMF_CSV_IN_str" + TNMF_CSV_IN_str);
                    System.out.println("BUMF_CSV_IN_str" + BUMF_CSV_IN_str);
                    System.out.println("SHKB_CSV_IN_str" + SHKB_CSV_IN_str);
                    System.out.println("SOMF_CSV_IN_str" + SOMF_CSV_IN_str);
                    System.out.println("NP_data_IN_str" + NP_data_IN_str);
                    System.out.println("Tosou_Data_IN_str" + Tosou_Data_IN_str);

                }

            } else {

                TNMF_CSV_IN_str = null;
                BUMF_CSV_IN_str = null;
                SHKB_CSV_IN_str = null;
                SOMF_CSV_IN_str = null;
                NP_data_IN_str = null;
                Tosou_Data_IN_str = null;

                System.out.println("TNMF_CSV_IN_str" + TNMF_CSV_IN_str);
                System.out.println("BUMF_CSV_IN_str" + BUMF_CSV_IN_str);
                System.out.println("SHKB_CSV_IN_str" + SHKB_CSV_IN_str);
                System.out.println("SOMF_CSV_IN_str" + SOMF_CSV_IN_str);
                System.out.println("NP_data_IN_str" + NP_data_IN_str);
                System.out.println("Tosou_Data_IN_str" + Tosou_Data_IN_str);


            }

            if (TNMF_CSV_IN_str == null || BUMF_CSV_IN_str == null || SHKB_CSV_IN_str == null ||
                    SOMF_CSV_IN_str == null || NP_data_IN_str == null || Tosou_Data_IN_str == null) {

                //************ ダウンロード　NG の場合 **************
                toastMake("「ログイン情報」がダウンロードされていません。\n" +
                        "Wi-Fi環境を確認して、もう一度お試しください。", 0, -200);

                return;

            } else {

                //************ ダウンロード　OK の場合 **************
                toastMake(" 「ログイン情報 ダウンロード」　完了済みです。", 0, -200);

                /**
                 *  ログイン　ダウンロード　ボタンの色を変える
                 */
                csv_btn_01.setTextColor(Color.parseColor("#ffffff"));
                csv_btn_01.setBackgroundColor(Color.parseColor("#FF4081"));

                return;

            }

        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if(ALL_C_DB != null) {
                ALL_C_DB.close();
            }
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //******** チェック用
      //  ALL_SELECT_Rstart();

    }




}
