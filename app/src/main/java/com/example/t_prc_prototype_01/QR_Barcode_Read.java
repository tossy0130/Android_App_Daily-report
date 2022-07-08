package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.RunnableScheduledFuture;

public class QR_Barcode_Read extends AppCompatActivity {

    /**
     *  JIM テスト　パス
     */
    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/UploadToServer.php";


    private static QR_Barcode_Read instance = null;

    public static String Sousin_log_Flg_QR = "0";

    // ログ用 TAG
    private String TAG = "QR_Barcode_Read:";

    private LinearLayout qr_const_01;

    //--------------- DB Insert 用　データ格納
    // 作業コード用  dd が　入る
    private String Saghou_Code;

    // 作業番号用  yyyymm
    private String Sagyou_date;

    //--------------- 入力用　エディットテキスト --------------
    // 商品コード　検索エディットテキスト
    private EditText jan_input_01;
    private String jan_set_text;

    // 段取り時間
    private EditText input_setup_edit;
    // 作業時間
    private EditText input_work_edit;
    // 予定数　エディットテキスト
    private EditText input_Yotei_num;
    // 開始時
    private EditText input_starttime;
    // 終了時
    private EditText Input_endtime;
    // 不良品数
    private EditText input_furyou;
    // 生産数
    private EditText Input_seisan;
    // 良品数
    private EditText Input_ryouhinn;

    //--------------------- エディットテキスト　値取得用　 Start

    // エディットテキスト値　取得用
    private String input_setup_edit_num, input_work_edit_num, input_starttime_num,
            Input_endtime_num, Input_ryouhinn_num;

    // i_input_Yotei_num => 予定数 i_input_setup_edit_num => 段取時間 , i_input_work_edit_num => 作業時間
    private int i_input_Yotei_num, i_input_setup_edit_num, i_input_work_edit_num;

    // 開始時間　値　取得用
    private int starttime_num;

    // 終了時間 値　取得用
    private int endtime_num;

    // 総生産数 値　取得用
    private int seisan_num;

    // 総生産数　用　格納　変数
    private int goukei;

    // 不良品数　値　取得用
    private int furyou_num;

    // 良品数　用　格納　変数
    private int sum_ryouhinn;

    //--------------------- エディットテキスト　値取得用　 End

    // ---- 最終工程　チェック判別
    private CheckBox last_check;

    // カメラ　起動用
    private IntentIntegrator integrator;

    //--------- 部署コード 判定用　
    private Map<String, String> spi_map_busyo = new HashMap<>();
    private Map<String, String> map_hikaku_busyo = new HashMap<>();

    //--------- 品目区分　判別用
    private Map<String, String> map_SHKB_csv = new HashMap<>();
    private Map<String, String> map_SHKB_csv_code = new HashMap<>();

    //--------- 部署コード　スピナー　データ
    private Spinner spinner_busyo;

    private String get_barcode_str;

    private String tmp;

    // 場所
    private Map<String, String> somf_map = new HashMap<>();

    // チェックボックス判別用
    private String check_str;

    //----------------------------------------------------- QR , バーコード　読み取り -------
    //-------------------　値受け取り用　テキストビュー
    private TextView hinmoku_label_text; // 品目コード
    private TextView hinmei_label_text; // 品目名
    private TextView bikou_label_text; // 品備考
    private TextView place_label_text; // 作業場所

    private String tmp_qr_str;

    // 品目区分
    private String br_val_01;

    //　バーコード　取得用 DB 変数 ------------------
    private String SH_col_2; // カラム 02
    private String SH_col_3; // カラム 03
    private String SH_col_6; // カラム 06
    private String SH_col_7; // カラム 07
    private String SH_col_8; // カラム 08
    private String SH_col_9; // カラム 09

    //保存ボタン
    private MaterialButton save_btn;

    private SendAdapter adapter;

    // PDF 一覧表示　ボタン
    private MaterialButton yotei_btn;

    // webview PDF 一覧
    private WebView webView;

    //----------- Work_Choice.java から　値を受け取る
    private String get_TMNF_01, get_TMNF_02, dia_edit_01_num, dia_user_key;
    private int i_dia_chack_01_num;

    //----------- 確実データ　取得用 Work_Choice.java
    private String ch_user_name_num, ch_busyo_view_Hon_num, ch_busyo_view_num;

    //----------- インサート用　部署コード　取得用
    private String sagyou_busyo, busyo;

    private String Inser_Hinmoku_K;

    //------------ 予定数
    private String Yotei_Str;
    private int Yotei_i;

    //------------- CSV ファイル作成 & ファイル　送信用 ------------
    private File target_csv_file;
    //------------ 送信 Flg
    private String saisou_Flg;
    private String Send_time_str;
    public static String send_csv_file_name;
    private ArrayList<String> csv_itme_list = new ArrayList<>();
    private ArrayList<String> csv_get_file_item = new ArrayList<>();
    private String csv_file_name_01, csv_file_name_02;
    // 現在時刻　取得
    private String Sagyou_yymmdd;

    // インサート用　送信時間
    private String sousin_time;
    // インサート用　送信スタッツ
    private String send_log_Stats;
    // インサート用　タスク　count 数
    private String Task_Count_num;

    private String Sagyou_date_str;

    private String select_souseisan;

    private EditText input_kakou;

    private String input_kakou_str;

    private int tmp_input_kakou_str;

    // グラフ用
    private String HH_hikaku_01;
    private int HH_hikaku_01_i;

    private int tmp_i_01_g;
    private String tmp_s_01_g;

    private  int get_id_num;

    private String g_num_str_01, g_num_str_02;

    private String time_001, time_002, time_003;

    //　開始時間　& 終了時間
    private Button qr_start_time,qr_end_time;
    private TextView qr_start_time_view,qr_end_time_view;

    // Send_Table の id max
    private int Send_Table_id_max;

    // Send_Table インサート用　グラフ ID
    private String G_id_str;

    // 時間計算用　
    private int time_01_a,time_01_b,time_02_a,time_02_b;

    private String Sagyou_B, DD_id, T_ID_Str;
    private int DD_id_i;

    private TextView mFocusView,Tosou_label;

    /**
     *  「開始時間」 & 「終了時間」　追加
     */
    private String K_Zikan, S_Zikan;
    private String tmp_result, tmp_result_02;

    /**
     *  エディットテキスト　値取得用
     */
    private String Dandori_str;
    private int Dandori_i;

    private String YOTEI_str;

    private String Create_Date_NUM;

    private String yy_num,dd_num;

    /**
     *    「樹脂成型課」　用
     */
    private TextView d04_label_01,d04_label_02;
    private EditText iro_input, kata_input, kikai_input,iro_z_edit,Kata_z_edit,Kikai_z_input;
    private MaterialButton dia_4_touroku_btn_001, dia_4_touroku_btn_002;
    private Button Zyushi_Seikei_btn;
    private TextInputLayout iro_z_box,Kata_z_box,Kikai_z_box;

    private String iro_input_tmp,kata_input_tmp,kikai_input_tmp; // 色段取時間 // 型段取時間  // 機械コード

    /**
     *   作業部署（ファイル送信用） 追加 2021/01/16
     */
    private String Sagyou_Get_file_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r__barcode__read);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //------------ Work_Choice.java からの値の受け取り
        if (getIntent() != null) {
            // 担当者コード　,  担当者名 ,  部署名 , 部署コード, 最終チェック int
            get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
            get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");
            dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");
            dia_user_key = getIntent().getStringExtra("dia_edit_01_num");


            System.out.println("前取得データ：" + get_TMNF_01 + get_TMNF_02 + dia_edit_01_num +
                    dia_user_key);

            ch_user_name_num = getIntent().getStringExtra("ch_user_name_num");
            ch_busyo_view_Hon_num = getIntent().getStringExtra("ch_busyo_view_Hon_num");
            ch_busyo_view_num = getIntent().getStringExtra("ch_busyo_view_num");

            /**
             *  作業部署　取得
             */
            Sagyou_B = getIntent().getStringExtra("Sagyou_B");

        }

        qr_const_01 = (LinearLayout) findViewById(R.id.qr_const_01);

        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // コンポーネント　紐づけ
        new_Comp();

        // コンポーネント初期化
        init();

        // PDF 一覧　表示用　webview
        webView = new WebView(this);

        check_str = "0";

        // 品目区分　比較用 SHKB.csv 取得
         Get_SHKB_Csv();

         get_Place();

        getSpinner_01();


        /**
         *   時刻から　ファイル名, ファイルデータを作成　などを作成
         */
        //------------ 現在時刻　取得
        String time_str = getNowDate();
        System.out.println(time_str);
        // DB 格納用
        Saghou_Code = time_str.substring(6, 8); // dd 取得
        Sagyou_date = time_str.substring(0, 6); // yyyymm 取得
        Sagyou_yymmdd = time_str.substring(0, 8); // yyyymmdd

        //********* グラフ用　 HH 取得
        HH_hikaku_01 = time_str.substring(8, 10);
        HH_hikaku_01_i = Integer.parseInt(HH_hikaku_01);

        //********** 時間取得
        time_001 = time_str.substring(8,10);
        time_002 = time_str.substring(10,12);
        time_003 = time_str.substring(12,14);

        // グラフインサート用
        g_num_str_01 = time_str.substring(6,8);


        csv_file_name_01 = time_str.substring(0, 8); // yyyymmdd 取得
        csv_file_name_02 = time_str.substring(8, 14); // HHmmss

        //------ 送信時間作成
        String year = time_str.substring(0, 4); // 年
        String month = time_str.substring(4, 6); // 月

        String hour = time_str.substring(8, 10); //  １時間
        String minute = time_str.substring(10, 12); //　１分
        String second = time_str.substring(12, 14); // 1秒

        //------ 送信時間
        Send_time_str = year + "/" + month + "/" + Saghou_Code + " " + hour + ":" + minute + ":" + second;
        System.out.println(Send_time_str);
        // ＊＊＊＊＊  CSV 送信ファイル名　作成 　＊＊＊＊＊　ANDROID の前に　作業者コード　が入る。


        /**
         * function テスト
         */


        /**
         *  開始時間　& 終了時間　（追加）
         */

        /**
         *  フォーカスの位置
         */

        //＊＊＊ 「JAN コード　検索　エディットテキスト」　へ　フォーカスを移動させる　＊＊＊
        jan_input_01.requestFocus();

        /*
        //****** フォーカス　「開始時間」 ->   「終了時間」
        qr_start_time_view.setNextFocusDownId(R.id.qr_end_time_view);

        //*******  「段取り時間」 -> 「作業時間」
        input_setup_edit.setNextFocusDownId(R.id.input_work_edit);

        input_work_edit.setNextFocusDownId(R.id.input_Yotei_num);

        //******* 「開始時」->「終了時」->「加工数」
        input_Yotei_num.setNextFocusDownId(R.id.input_starttime);
        input_starttime.setNextFocusDownId(R.id.Input_endtime);
        Input_endtime.setNextFocusDownId(R.id.input_kakou);

         */

        /**
         *   「樹脂成型課」　ダイアログ　を開く　ボタン
         */
        Zyushi_Seikei_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // アラート　ダイヤログ　表示
                Allahto_Dailog_Zyushi_Seikei();
            }
        });

        /***
         *   開始時間　タップ
         */
        qr_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(QR_Barcode_Read.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        qr_start_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));

                        time_01_a = hourOfDay;
                        time_01_b = minute;


                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });


        /***
         *   終了時間　タップ
         */
        qr_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(QR_Barcode_Read.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        qr_end_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));

                        time_02_a = hourOfDay;
                        time_02_b = minute;


                        //*** 開始時間  分に　変換
                        int hour_01_sum = time_01_a * 60 + time_01_b; //*** 開始時間
                        int hour_02_sum = time_02_a * 60 + time_02_b; //*** 終了時間

                        int time_sum = hour_02_sum - hour_01_sum;

                        //********* 計算結果を表示 *********
                        input_work_edit.setText(String.valueOf(time_sum));

                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });


        /**
         * **** インサート用　データ ****
         *  get_TMNF_01　=> 担当者コード
         *  busyo => 所属部署コード
         *  sagyou_busyo => 作業部署コード
         *
         *   //----------------- 「jan_input_01」　エディットテキストが押された時の処理
         */
        jan_input_01.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // ###### エンターボタンが押された時の処理
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    // ########### ソフトキーボードを非表示
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    // エディットテキスト　入力データを　取得
                    jan_set_text = jan_input_01.getText().toString();

                    //************ エディットテキストの値が「空じゃなかったら」 ***************
                    if (jan_set_text.length() != 0) {

                        // ************ SQL 手打ち バーコード、QR、現品票コード　SELECT  Start ************* //

                        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                        SQLiteDatabase Ac_db = helper.getReadableDatabase();
                        String[] arr_item = new String[9];

                        String edit_qr_num01 = "";
                        String edit_qr_num02 = "";
                        //  String edit_qr_num03 = "";

                        try {

                            Cursor cursor = Ac_db.rawQuery("SELECT * FROM SHMF_table WHERE SHMF_c_06 =" + "\"" + jan_set_text + "\"" + "OR SHMF_c_05 =" + "\"" + jan_set_text + "\"" + "OR SHMF_c_02 =" + "\"" + jan_set_text + "\"" + " LIMIT 1", null);

                            // Cursor cursor = Ac_db.rawQuery("SELECT * FROM SHMF_table WHERE SHMF_c_06 = " + "\"" + jan_set_text + "\"" + " LIMIT 1", null);

                            if (cursor.moveToNext()) {

                                // カラム 01
                                int idx = cursor.getColumnIndex("SHMF_c_01");
                                arr_item[0] = cursor.getString(idx);
                                edit_qr_num01 = arr_item[0];

                                System.out.println("edit_qr_num01:::値" + edit_qr_num01);

                                //------ 品区   INSERT 用
                                br_val_01 = arr_item[0];

                                // ----------- 品区　テキストビュー 表示用（品目区分）　判別
                                switch (br_val_01) {
                                    case "1":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("1"));
                                        break;
                                    case "2":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("2"));
                                        break;
                                    case "3":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("3"));
                                        break;
                                    case "4":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("4"));
                                        break;
                                    case "5":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("5"));
                                        break;
                                    case "6":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("6"));
                                        break;
                                    case "7":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("7"));
                                        break;
                                    case "8":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("8"));
                                        break;
                                    case "9":
                                        hinmoku_label_text.setText(map_SHKB_csv.get("9"));
                                        break;
                                }

                                // カラム 02
                                idx = cursor.getColumnIndex("SHMF_c_02");
                                arr_item[1] = cursor.getString(idx);
                                edit_qr_num02 = arr_item[1];

                                // INSERT 用　品目コード
                                SH_col_2 = arr_item[1];

                                System.out.println(SH_col_2 + "SH_col_2　出力テスト");


                                //         h_moku_text_area.setText(edit_qr_num02); // 品目コード テキストビュー
                                //          bikou_label_text.setText(edit_qr_num02); // 品目備考 　テキストビュー

                                // --------------- カラム 03 テキストビュー　（品名）
                                idx = cursor.getColumnIndex("SHMF_c_03");
                                arr_item[2] = cursor.getString(idx);

                                String tmp_02 = arr_item[2].replaceAll("\"","");

                                //--------------- テキストビュー　（品名）
                                hinmei_label_text.setText(tmp_02);


                                //--------------- カラム 04 備考
                                idx = cursor.getColumnIndex("SHMF_c_04");
                                arr_item[3] = cursor.getString(idx);

                                String tmp_03 = arr_item[3].replaceAll("\"","");
                                System.out.println(tmp_03);

                                //---------------- 表示用　（備考）
                                bikou_label_text.setText(tmp_03);

                                // カラム 05
                                idx = cursor.getColumnIndex("SHMF_c_05");
                                arr_item[4] = cursor.getString(idx);

                                // カラム 06
                                idx = cursor.getColumnIndex("SHMF_c_06");
                                arr_item[5] = cursor.getString(idx);

                                // カラム 07
                                idx = cursor.getColumnIndex("SHMF_c_07");
                                arr_item[6] = cursor.getString(idx);


                                SH_col_7 = arr_item[6]; // isert 用　在庫場所

                                System.out.println("テスト　SH_col_7" + SH_col_7);

                                //-------------- 場所コード　= 場所コード 判定　出力
                                place_label_text.setText(somf_map.get(SH_col_7)); // 在庫場所

                                // カラム 08
                                idx = cursor.getColumnIndex("SHMF_c_08");
                                arr_item[7] = cursor.getString(idx);

                                // カラム 09
                                idx = cursor.getColumnIndex("SHMF_c_09");
                                arr_item[8] = cursor.getString(idx);

                            }

                            //----------- 検索　キーワード　判定 -------->
                            if (edit_qr_num01 == null || edit_qr_num01.equals("") || edit_qr_num01.isEmpty() && edit_qr_num02 == null || edit_qr_num02.equals("") || edit_qr_num02.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "「該当」する商品はありません。もう一度入力してください。", Toast.LENGTH_LONG).show();

                                // テキストビュー　初期化
                                init();

                                return false;

                            } else {
                                //************** 検索ヒット時の処理 *************//
                                //   Toast.makeText(getApplicationContext(), "「検索結果」はこちらです。", Toast.LENGTH_LONG).show();
                            }

                        } finally {
                            if (Ac_db != null) {
                                Ac_db.close();
                            }


                            /**
                             *  「塗装課」　の 場合は 予定数に　フォーカスを飛ばす
                             */
                            if(Sagyou_B.contains("B0887")) {

                                //********** 「予定数」にフォーカスを飛ばす
                                input_Yotei_num.setFocusableInTouchMode(true);
                                input_Yotei_num.requestFocus();
                                // ソフトキーボードを表示する
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                //****************

                            }

                        }

                    } else {
                        //************ エディットテキストの値が「空の場合」処理 **************

                        //   Toast.makeText(getApplicationContext(), "「現品票」 または 「バーコード」を入力してください", Toast.LENGTH_LONG).show();
                    }
                }

                //**************** 時間入力 エディットテキスト　フォーカス　移動
                input_work_edit.setFocusableInTouchMode(true);
                // EditTextにフォーカスを移動
                input_work_edit.requestFocus();
                // ソフトキーボードを表示する
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                //****************

                return true;
            }
        });

        /**
         *    ＊＊＊＊＊＊「段取り時間」　フォーカスチェンジ  ＊＊＊＊＊＊
         * */
        input_setup_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************* フォーカスがあたった場合
                if(hasFocus) {
                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    /*
                    //**************** 段取時間　以外　フォーカス　
                    Edit_TO_OFF_07(input_setup_edit, input_work_edit, input_Yotei_num, input_starttime,
                            Input_endtime, input_kakou, input_furyou);

                     */

                } else {

                    // ## フォーカスが外れた時
                 //   inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    // ## キーボード　非表示
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                } //********** END if

            }
        });

        /***
         * 段取り時間　エディットテキスト　ソフトキーボード　エンターが押されたら
         * input_setup_edit
         */
        input_setup_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {


                    //****** エラー 「段取り時間」の値が　空だった場合  0 を　代入
                    if(input_setup_edit.getText().toString().equals("") != false) {

                        tostMake("「段取り時間」の「値」が空です。", 0,-200);
                        input_setup_edit.setText("");
                        return false;

                    } else {

                        //＊＊＊＊＊＊　「値」が　空じゃなかった場合 ＊＊＊＊＊＊
                        // String型　の変数へ　エディットテキストの値を格納
                        input_setup_edit_num = input_setup_edit.getText().toString();

                        // int 型に格納する
                        i_input_setup_edit_num = Integer.valueOf(input_setup_edit_num);
                        System.out.println("値取得：：：段取時間 int::::" + i_input_setup_edit_num);

                    }

                } //************* actionId END if

                return false;
            }
        });

        /**
         *      ＊＊＊＊＊＊「作業時間」　フォーカス　チェンジ ＊＊＊＊＊＊＊
         * */
        input_work_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (hasFocus) {
                    // ソフトキーボードの表示
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input_work_edit, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    // ## フォーカスが外れた時
                //   inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    // ## キーボード　非表示
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input_work_edit.getWindowToken(), 0);


                }

            }

        });

        /***
         * 作業時間　エディットテキスト　ソフトキーボード　エンターボタンだが押された処理
         */
        input_work_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //************* 「作業時間」　ソフトキーボード　エンターボタン　が押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    //****** 「作業時間」の値が空だったら　０　を　代入する
                    if(input_work_edit.getText().toString().equals("") != false) {
                        tostMake("「作業時間の「値」が空です。", 0,-200);
                        input_work_edit.setText("");;

                        return false;

                        //****** 「作業時間」の値が空じゃなかったら　値を代入
                    }  else {

                        // String型　の変数へ　エディットテキストの値を格納
                        input_work_edit_num = input_work_edit.getText().toString();
                        // int 型に値を格納
                        i_input_work_edit_num = Integer.valueOf(input_work_edit_num);


                    }

                } //************* actionId END if

                return false;
            }
        });


        /**
         *  「予定数」フォーカス　チェンジ　イベント　input_starttime　
         */
        input_Yotei_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);


                } else {
                    //************* フォーカスが離れたら ***************

                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    /**
                     *  「塗装課」　だったら
                     */
                    if(Sagyou_B.contains("B0887")) {

                        if(input_Yotei_num.getText().toString().equals("") == false) {
                            //****** 予定数　を　総生産数へセット
                            String input_Yotei_num_tmp = input_Yotei_num.getText().toString();
                            Input_seisan.setText(input_Yotei_num_tmp);
                        }

                    } //************ END if

                    //********** 「値」が空だったら
                    if(input_Yotei_num.getText().toString().equals("")) {

                        tostMake("値が「空」です。", 0, -200);
                        input_Yotei_num.setText("");
                        return;

                    } else {
                        //＊＊＊＊＊＊＊　「値」が空じゃなかったら　＊＊＊＊＊＊＊
                        YOTEI_str = input_Yotei_num.getText().toString();

                    }

                } //************* END if
            }
        });


        /***
         * 　「予定数」　エディットテキストが押されたら
         */
        input_Yotei_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // ############ エンターボタン　が押された時の処理
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    /**
                     *   ＊＊＊＊＊＊　「塗装課」　の　場合
                     */
                    if(Sagyou_B.contains("B0887")) {

                        String input_Yotei_num_tmp_str = input_Yotei_num.getText().toString();

                        /**
                         *  　「予定数」　を　「総生産数」　に　入れる
                         */
                        Input_seisan.setText(input_Yotei_num_tmp_str);

                    } else {

                        /**
                         *   ＊＊＊＊＊＊　「塗装課」　以外　の　通常処理
                         */

                        // ***************** フォーカス　解除
                        //   Edit_TO_ON_06(input_starttime, Input_endtime, input_kakou, input_furyou,input_setup_edit,input_work_edit);

                        //＊＊＊ 「終了時」　へ　フォーカスを移動させる　＊＊＊
                        //   input_starttime.requestFocus();

                        if (input_Yotei_num.getText().toString().equals("")) {

                            tostMake("「予定数」　が空です。", -200, 0);
                            return false;

                        } else if (input_Yotei_num.getText().toString().equals("") == false && input_starttime.getText().toString().equals("") ||
                                Input_endtime.getText().toString().equals("") || Input_seisan.getText().toString().equals("") ||
                                Input_ryouhinn.getText().toString().equals("")) {

                            //---------- 予定数を格納する String
                            Yotei_Str = input_Yotei_num.getText().toString();
                            // Int に　パース
                            Yotei_i = Integer.parseInt(Yotei_Str);

                            //------- 予定数 < 終了時　エラー
                            if (Yotei_i < endtime_num) {
                                tostMake("「終了時」の値が不正です", -200, 0);
                                Input_endtime.setText("");


                                //------ 予定数 < 総生産数 エラー
                            } else if (Yotei_i < goukei) {
                                tostMake("「総生産数」の値が不正です。", -200, 0);
                                Input_endtime.setText("");


                            }

                        } //******************** END if

                    } //********************************* END if

                }
                return false;
            }
        });


        /**
         *  「開始時」フォーカス　チェンジ　イベント　input_starttime　
         */
        input_starttime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //*********************  開始時　以外　には　フォーカスがいかない　排他処理
                    Edit_TO_OFF_07(input_starttime, input_Yotei_num,Input_endtime,input_kakou,input_furyou,
                            input_setup_edit,input_work_edit);

                     */

                } else {
                    //************* フォーカスが離れたら ***************

                    // ## フォーカスが外れた時
                  //  inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    // ## キーボード　非表示
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }



                } //****************** END if
            }
        });


        /**
         * 「開始時」 エディットテキスト　ソフトキーボード 「決定」　ボタンが押された処理
         */
        //-------- input_starttime
        input_starttime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // ############ エンターボタン　が押された時の処理
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //********* フォーカス 排他処理　解除
                    Edit_TO_ON_06(input_work_edit,input_Yotei_num,input_setup_edit,Input_endtime,input_kakou,input_furyou);


                    //************　エラー 「開始時」　エディット　が空の場合　返す
                    if (input_starttime.getText().toString().equals("")) {

                        tostMake("「開始時」　の値が　「空」　です。", -200, 0);

                        return false;


                    } else if (input_starttime.getText().toString().equals("") == false) {

                        //------ 「開始時」　の値を　格納する。
                        String get_num = input_starttime.getText().toString();
                        // int starttime_num に格納
                        starttime_num = Integer.parseInt(get_num);

                        //＊＊＊ 「開始時」 => 「終了時」　へ　フォーカスを移動させる　＊＊＊
                        Input_endtime.requestFocus();

                        // *********** 「開始時」　&&  「予定数」　が　空　じゃなければ
                        if (input_Yotei_num.getText().toString().equals("") == false) {

                            if (starttime_num > Yotei_i) {

                                /**
                                 *  「開始時」　>  予定数」　の場合は　アラートを出して確認
                                 */

                                //＊＊＊＊＊＊＊　「予定数」　オーバー　アラート　＊＊＊＊＊＊＊＊＊
                                TextView titleView;
                                // アクティビティ名を入れる
                                titleView = new TextView(QR_Barcode_Read.this);
                                titleView.setText("生産数オーバー");
                                titleView.setTextSize(20);
                                titleView.setTextColor(Color.WHITE);
                                titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                                titleView.setPadding(20, 20, 20, 20);
                                titleView.setGravity(Gravity.CENTER);

                                //-------- アラートログの表示 開始 ----------
                                android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(QR_Barcode_Read.this);

                                /**
                                 *  ダイアログの項目セット
                                 */
                                //------- タイトルセット
                                bilder.setCustomTitle(titleView);

                                //------- メッセージセット
                                bilder.setMessage("終了時の生産数が予定数を超えています。このままでよろしいですか？");

                                bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //********   「開始時」　の値を　取得
                                        String input_starttime_tmp = input_starttime.getText().toString();
                                        input_starttime.setText(input_starttime_tmp);

                                    }
                                });

                                bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        input_starttime.setText("");
                                        Input_endtime.setText("");
                                        Input_seisan.setText("");
                                        input_furyou.setText("");
                                        Input_ryouhinn.setText("");

                                        //******* 「加工数」に 1 を セット
                                        input_kakou.setText("1");
                                        return;

                                    }
                                });

                                android.app.AlertDialog dialog = bilder.create();
                                dialog.show();

                            }

                            //＊＊＊ 　「開始時」 && 「終了時」が　空じゃなければ
                            //＊＊＊
                        } else if (Input_endtime.getText().toString().equals("") == false) {

                            //***** 「終了時」　値  tmp_Syuryou
                            String tmp_Syuryou = Input_endtime.getText().toString();
                            int tmp_Syuuryou_i = Integer.parseInt(tmp_Syuryou);

                            if (starttime_num > tmp_Syuuryou_i) {
                                tostMake("「開始時」の値が不正です。", -200, 0);
                                input_starttime.setText("0");

                                Input_seisan.setText("0");
                                Input_ryouhinn.setText("0");
                                return false;

                            } else {

                                input_starttime.setText(get_num);

                                // ################ 「生産数」　の　計算
                                /**
                                 *  加工数　追加
                                 */

                                //＊＊＊　「加工数」　で割り切れるか　判別
                                //＊＊＊
                                String tmp_input_kakou = input_kakou.getText().toString();
                                tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                if((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {

                                    tostMake("「加工数」で割り切れません。", 0,-200);
                                    input_kakou.setText("1");

                                    System.out.println("「開始時 01-01」");
                                    return false;

                                } else {
                                    //**************  加工数 **************

                                    goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                                    // 計算結果 を　「総生産数」　に　セットする
                                    Input_seisan.setText(String.valueOf(goukei));

                                    // エディットテキスト　フォーカス　開始
                                    Go_Edit_01(input_Yotei_num,Input_endtime,input_furyou);

                                    // ########### OK 処理
                                    // 「良品数」　合計
                                    sum_ryouhinn = goukei - furyou_num;
                                    Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                                    //＊＊＊ 「開始時」 => 「終了時」　へ　フォーカスを移動させる　＊＊＊
                                    Input_endtime.requestFocus();

                                    System.out.println("「開始時 01-02」");

                                }

                            }

                            //************** 「不良品数」が空じゃなかったら
                        } else if (input_furyou.getText().toString().equals("") == false) {

                            //*********　エラー 「開始時」 > 「不良品数」　の方が　大きい場合
                            if (starttime_num < furyou_num) {
                                tostMake("「開始時」の値が不正です。", -200, 0);
                                input_starttime.setText("0");

                                Input_seisan.setText("0");
                                Input_ryouhinn.setText("0");
                                return false;

                            }

                                //***********  「終了時が空じゃなかったら」
                                if (Input_endtime.getText().toString().equals("") == false) {

                                    //********
                                    input_starttime.setText(get_num);

                                    // ################ 「生産数」　の　計算

                                    /**
                                     *  加工数　追加
                                     */
                                    String tmp_input_kakou = input_kakou.getText().toString();
                                    tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                    if ((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {

                                        tostMake("「加工数」で割り切りません。", 0, -200);
                                        input_kakou.setText("1");

                                        System.out.println("「開始時 02-01」");

                                        return false;

                                    } else {

                                        System.out.println("「開始時 02-02」");

                                        goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                                        // 計算結果 を　「総生産数」　に　セットする
                                        Input_seisan.setText(String.valueOf(goukei));

                                        // エディットテキスト　フォーカス　開始
                                        Go_Edit_01(input_Yotei_num, Input_endtime, input_furyou);

                                        // ########### OK 処理
                                        // 「良品数」　合計
                                        sum_ryouhinn = goukei - furyou_num;
                                        Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                                        //＊＊＊ 「開始時」 => 「終了時」　へ　フォーカスを移動させる　＊＊＊
                                        Input_endtime.requestFocus();

                                    }

                                }

                        }

                    }

                } //-------------- END if // ############ エンターボタン　が押された時の処理
                return false;
            }
        });

        /**
         *  「終了時」フォーカス　チェンジ　イベント　Input_endtime　
         */
        Input_endtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //************** 「終了時」　の　値　以外には　フォーカスが映らない　排他処理
                    Edit_TO_OFF_07(Input_endtime, input_Yotei_num, input_starttime, input_kakou,input_kakou,
                            input_setup_edit,input_work_edit);

                     */

                } else {
                    //************* フォーカスが離れたら ***************

                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    if(Input_endtime.getText().toString().equals("")) {

                        tostMake("値が「空」です。", 0,-200);
                        Input_endtime.setText("");


                    } else {

                    }

                } //**************** END if
            }
        });


        /**
         *
         * --------------- 「終了時」 テキストエディットが　「確定」ボタンが押された処理
         *
         */
        Input_endtime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // 「確定」ボタンが押されたら
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //******* エディットテキスト　排他処理　解除
                    Edit_TO_ON_06(input_Yotei_num,input_starttime,input_kakou,input_furyou,
                            input_setup_edit,input_work_edit);


                    //------------- エラー　終了時　エディットが　「空」　だった場合
                    if (Input_endtime.getText().toString().equals("")) {

                        tostMake("「終了時」　の値が　「空」　です。", -200, 0);
                        Input_endtime.setText("");
                        return false;

                        //-------------- 「終了時」　エディットが　「空」　じゃ　ない　場合
                    } else if (Input_endtime.getText().toString().equals("") == false) {

                        // 「終了時」　 値を変数へ格納する
                        String tmp = Input_endtime.getText().toString();
                        endtime_num = Integer.parseInt(tmp);

                        //****** 予定数　が　空じゃ　なかったら
                        if (input_Yotei_num.getText().toString().equals("") == false && Input_endtime.getText().toString().equals("") == false) {

                            // 終了時　> 予定数　エラー
                            /*if (endtime_num > Yotei_i) {
                                tostMake("「終了時」　の値が不正です。", -200, 0);
                                Input_endtime.setText("");

                                Input_seisan.setText("");
                                Input_ryouhinn.setText("");
                                return false;*/
                            if (endtime_num > Yotei_i) {
                                //＊＊＊＊＊＊＊　「予定数」　オーバー　アラート　＊＊＊＊＊＊＊＊＊
                                TextView titleView;
                                // アクティビティ名を入れる
                                titleView = new TextView(QR_Barcode_Read.this);
                                titleView.setText("生産数オーバー");
                                titleView.setTextSize(20);
                                titleView.setTextColor(Color.WHITE);
                                titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                                titleView.setPadding(20, 20, 20, 20);
                                titleView.setGravity(Gravity.CENTER);

                                //-------- アラートログの表示 開始 ----------
                                android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(QR_Barcode_Read.this);

                                /**
                                 *  ダイアログの項目セット
                                 */
                                //------- タイトルセット
                                bilder.setCustomTitle(titleView);

                                //------- メッセージセット
                                bilder.setMessage("終了時の生産数が予定数を超えています。このままでよろしいですか？");

                                bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Input_endtime.setText(tmp);
                                        /**
                                         *   総生産　＆ 良品数の計算　行う
                                         */
                                        //################ 「生産数」　の　計算

                                        // ********* 「加工数」 *********

                                        /**
                                         *  加工数　追加
                                         */
                                        String tmp_input_kakou = input_kakou.getText().toString();
                                        tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                        //＊＊＊＊＊＊＊ 加工数　が　割り切れるかチェック　＊＊＊＊＊＊＊
                                        if((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {

                                            // ******* エラー　割り切れなかったら
                                            tostMake("割り切れない「値」が入力されています。「値」を変更してください。", 0,-200);
                                            input_kakou.setText("1");
                                            return;

                                        } else {

                                            // ########### OK 処理 （割り切れた場合）
                                            goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                                            // 計算結果 を　「総生産数」　に　セットする
                                            Input_seisan.setText(String.valueOf(goukei));

                                            // 「良品数」　合計
                                            sum_ryouhinn = goukei - furyou_num;
                                            Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                                            //＊＊＊ 「加工数」　へ　フォーカスを移動させる　＊＊＊
                                            input_kakou.requestFocus();

                                        }



                                    }
                                });

                                bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Input_endtime.setText("0");

                                    }
                                });

                                android.app.AlertDialog dialog = bilder.create();
                                dialog.show();

                                //********* ボタン はい **********
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
                                //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                                //********* ボタン いいえ **********
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
                                //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

                                // 開始時　> 終了時　エラー
                            } else if (starttime_num > endtime_num){
                                tostMake("「終了時」　の値が不正です。", -200, 0);
                                Input_endtime.setText("");

                                Input_seisan.setText("");
                                Input_ryouhinn.setText("");
                                return false;

                            } else {

                                Input_endtime.setText(tmp);
                                /**
                                 *   総生産　＆ 良品数の計算　行う
                                 */
                                // ################ 「生産数」　の　計算


                                /**
                                 *  加工数　追加
                                 */
                                String tmp_input_kakou = input_kakou.getText().toString();
                                tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                if((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {
                                    tostMake("割り切れない「値」が入力されています。「値」を変更してください。" , 0,-200);
                                    return false;

                                } else {

                                    goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                                    // 計算結果 を　「総生産数」　に　セットする
                                    Input_seisan.setText(String.valueOf(goukei));

                                    // ########### OK 処理
                                    // 「良品数」　合計
                                    sum_ryouhinn = goukei - furyou_num;
                                    Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                                    //＊＊＊ 「加工数」　へ　フォーカスを移動させる　＊＊＊
                                    input_kakou.requestFocus();

                                }

                            }

                                // ********** 「開始時間」が空じゃない場合
                        } else if (input_starttime.getText().toString().equals("") == false) {

                            // 終了時　< 開始時　エラー
                            if (endtime_num < starttime_num) {
                                tostMake("「終了時」　の値が不正です。", -200, 0);
                                Input_endtime.setText("");

                                Input_seisan.setText("");
                                Input_ryouhinn.setText("");
                                return false;

                            } else {
                                // ************************** 「終了時」 >  「開始時」　の場合
                                Input_endtime.setText(tmp);
                                /**
                                 *   総生産　＆ 良品数の計算　行う
                                 */
                                // ################ 「生産数」　の　計算


                                /**
                                 *  加工数　追加
                                 */
                                String tmp_input_kakou = input_kakou.getText().toString();
                                tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                if((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {
                                    tostMake("「加工数」で割り切れません。", 0, -200);
                                    return false;

                                } else {

                                    //******* 「加工数」で割り切れた場合

                                    goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                                // 計算結果 を　「総生産数」　に　セットする
                                Input_seisan.setText(String.valueOf(goukei));

                                // ########### OK 処理
                                // 「良品数」　合計
                                sum_ryouhinn = goukei - furyou_num;
                                Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                                //＊＊＊ 「加工数」　へ　フォーカスを移動させる　＊＊＊
                                    input_kakou.requestFocus();

                                }

                            }

                            // ********** 「不良品数」が空じゃない場合
                        } else if (input_furyou.getText().toString().equals("") == false) {

                            if (endtime_num < furyou_num) {
                                tostMake("「終了時」　の値が不正です。", -200, 0);
                                Input_endtime.setText("");

                                Input_seisan.setText("");
                                Input_ryouhinn.setText("");
                                return false;

                            } else {

                                Input_endtime.setText(tmp);
                                /**
                                 *   総生産　＆ 良品数の計算　行う
                                 */
                                // ################ 「生産数」　の　計算


                                /**
                                 *  加工数　追加
                                 */
                                String tmp_input_kakou = input_kakou.getText().toString();
                                tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                                if((endtime_num - starttime_num) % tmp_input_kakou_str != 0) {
                                    tostMake("「加工数」が割り切れない値です。", 0, -200);
                                    return false;

                                } else {

                                    //******************
                                    goukei = (endtime_num - starttime_num) - tmp_input_kakou_str;
                                    // 計算結果 を　「総生産数」　に　セットする
                                    Input_seisan.setText(String.valueOf(goukei));

                                    // ########### OK 処理
                                    // 「良品数」　合計
                                    sum_ryouhinn = goukei - furyou_num;
                                    Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));
                                }


                            }

                        }

                    }

                } //------------------------- END if

                return false;
            }
        });


        /***
         *  ************** input_kakou 加工数　追加 *******************
         */
        input_kakou.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //************ 加工数　以外　は　フォーカス　不可
                    Edit_TO_OFF_07(input_kakou, input_Yotei_num,input_starttime,Input_endtime,input_furyou,
                            input_setup_edit,input_work_edit);

                     */

                } else {
                    //*************** フォーカスが　離れたら
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);


                }

            }
        });

        /***
         *  ************** input_kakou 加工数 ソフトキーボード　　追加 *******************
         */
        input_kakou.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    //********* フォーカス無効化　解除
                    Edit_TO_ON_06(input_Yotei_num,input_starttime,Input_endtime,input_furyou,input_setup_edit,input_work_edit);

                    //************ 「開始時」 && 「終了時」が入力されていたら　「加工数」　を　加えて　計算する
                    if(input_starttime.getText().toString().equals("") == false &&
                            Input_endtime.getText().toString().equals("") == false) {

                        /***
                         *  総生産　,  良品数　計算
                         */
                        String tmp_input_kakou = input_kakou.getText().toString();
                        tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                        if((endtime_num - starttime_num) % tmp_input_kakou_str == 0) {
                            goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;

                            // 計算結果 を　「総生産数」　に　セットする
                            Input_seisan.setText(String.valueOf(goukei));

                            // ########### OK 処理
                            // 「良品数」　合計
                            sum_ryouhinn = goukei - furyou_num;
                            Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                            //＊＊＊ 「不良品数」　へ　フォーカスを移動させる　＊＊＊
                            input_furyou.requestFocus();

                        } else {
                            //******* 割り切れない　エラー ********
                            tostMake("割り切れない「値」が入力されています。「値」を変更してください。",0,-200);
                            input_kakou.setText("1"); // 加工数
                            Input_seisan.setText("0"); // 総生産数
                            Input_ryouhinn.setText("0"); // 良品数
                            return false;
                        }


                    } else {

                        //****** エラー処理  　入力欄が　空の場合は　1 をセットする
                        tostMake("「入力項目が「空」です。」", 0,-200);
                        input_kakou.setText("1");
                        input_furyou.setText("");
                        return false;
                    }

                } //************************* END if

                return false;
            }
        });



        /**
         *  「不良品」フォーカス　チェンジ　イベント　
         */
        input_furyou.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //************ エディットテキスト　「不良品」　以外　フォーカス NG
                    Edit_TO_OFF_07(input_furyou, input_Yotei_num, input_starttime,Input_endtime,input_kakou,
                            input_setup_edit,input_work_edit);

                     */

                } else {
                    //************* フォーカスが離れたら ***************
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);


                } //***************** END if
            }
        });

        /**
         *
         * -------------------　「不良品数」 エディットテキスト　確定ボタン　処理
         *
         */
        input_furyou.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //　ソフトキーボードの「確定」ボタンを押したら
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //*********************** エディットテキスト　フォーカス　解除
                    Edit_TO_ON_06(input_starttime,Input_endtime,input_kakou,input_Yotei_num,
                            input_setup_edit,input_work_edit);

                    //**************　エラー 「不良品数」　が　空だった場合
                    if (input_furyou.getText().toString().equals("")) {

                        tostMake("「不良品数」　の値が　「空」　です。", -200, 0);
                        Input_endtime.setText("");
                        return false;

                        //********* 良品数が　「空」　じゃ　ない場合
                    } else if (input_furyou.getText().toString().equals("") == false && input_Yotei_num.getText().toString().equals("") == false ||
                            Input_endtime.getText().toString().equals("") == false || Input_ryouhinn.getText().toString().equals("") == false) {

                        // ########### OK 判定処理の為に　不良品数を　変数に格納する。
                        String tmp = input_furyou.getText().toString();
                        furyou_num = Integer.parseInt(tmp);

                            input_furyou.setText(tmp);
                            /**
                             *   総生産　＆ 良品数の計算　行う
                             */
                            // ################ 「生産数」　の　計算

                            /**
                             *  加工数　追加
                             */
                            String tmp_input_kakou = input_kakou.getText().toString();
                            tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

                            goukei = (endtime_num - starttime_num) / tmp_input_kakou_str;
                            // 計算結果 を　「総生産数」　に　セットする
                            Input_seisan.setText(String.valueOf(goukei));
                            // ########### OK 処理
                            // 「良品数」　合計
                            sum_ryouhinn = goukei - furyou_num;
                            Input_ryouhinn.setText(String.valueOf(sum_ryouhinn));

                    }
                }

                return false;
            }
        });


        /**
         * ---------------- 「PDF 表示」　ボタン　タップ処理
         */
        yotei_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------- PDF 一覧　表示　画面 （web へ　遷移）する
                Intent intent = new Intent(getApplicationContext(), WebViewActivity_02.class);
                startActivity(intent);

            }
        });

        /**
         * ---------------- 「最終工程」 チェックボックス　値取得
         */
        last_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // チェックステータス取得
                boolean check = last_check.isChecked();

                //　チェックが入ってたら
                if (check) {
                    check_str = "1";
                } else {
                    // チェックが入ってなかったら
                    check_str = "0";
                }

            }
        });


        /**
         *
         * -------------------------- save_btn 「保存」 ボタンを押した処理
         * Insert 処理
         *
         */
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /****
                 * ＊＊＊＊＊＊＊   作業コードが　塗装課か　そうでないか　で　分岐　＊＊＊＊＊＊＊＊＊
                 *              「塗装課」　だったら
                  */
                if(Sagyou_B.contains("B0887")) {

                    //******** エラー「予定数」 が　空の場合
                    if(input_Yotei_num.getText().toString().equals("")) {

                        tostMake("「予定数」を入力してください。（生産数として カウントされます）", 0 , -200);
                        return;
                    }


                    //**********  総生産数　取得
                    String tmp_souseisan = Input_seisan.getText().toString(); // 総生産数
                    int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                    //********** アラートを出して確認 ***********

                    //-------　タイトル
                    TextView titleView;
                    // アクティビティ名を入れる
                    titleView = new TextView(QR_Barcode_Read.this);
                    titleView.setText("作業登録（塗装課）を完了確認");
                    titleView.setTextSize(20);
                    titleView.setTextColor(Color.WHITE);
                    titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                    titleView.setPadding(20,20,20,20);
                    titleView.setGravity(Gravity.CENTER);

                    //-------- アラートログの表示 開始 ----------
                    AlertDialog.Builder bilder = new AlertDialog.Builder(QR_Barcode_Read.this);

                    /**
                     *  ダイアログの項目セット
                     */
                    //------- タイトルセット
                    bilder.setCustomTitle(titleView);

                    //------- メッセージセット
                    bilder.setMessage("生産数：" + tmp_souseisan + "：の値で作業登録をしますか？");

                    bilder.setNegativeButton("登録する", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //****************************** インサート処理 ************************************
                            // Insert_Send01();

                            //************* インサート処理　実行 **************

                           // Insert_Send_02_save();
                            Insert_Send_TOSOU_save(); //*********** 「塗装課」用　インサート
                            Send_CSV_POST();
                            Send_Grafu_Table_Insert(tmp_souseisan_i);
                            Teiki_Send_Flg(); //******* 定期送信　ok フラグ 1
                            System.out.println("Send_Grafu_Table_Insert(); 終了 4600" );
                            tostMake("タスク保存を完了しました。（＋）", -200, 0);

                            //*************  HOME へ　画面遷移させる ************
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO: ここで処理を実行する
                                    finish();

                                }
                            }, 400);

                            //********** ログ用　インサート
                            Send_table_01_log_Insert();

                        }
                    });

                    bilder.setPositiveButton("登録しない（値を修正する）", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // -------------- 修正
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

                    //******************************************* END ボタン　配色　変更

                    // ************ END　確認　アラート



                } else {

                    /**
                     * 　「塗装課」　以外　の　課　の場合 (通常処理)
                     */

                    //***************************** 「段取り時間」の値　「作業時間」の値　が　どちらも空だった場合
                    if (input_work_edit.getText().toString().equals("") != false && input_setup_edit.getText().toString().equals("") != false) {
                        tostMake("「段取り時間」 か　「作業時間」　のどちらかを入力してください。", 0, -200);
                        return;

                    }

                    //---------- 予定数　、　開始時　、　終了時、　不良品　、　良品数 , 品目区分, 　「空」　だったら　エラー
                    if (input_Yotei_num.getText().toString().equals("") || input_starttime.getText().toString().equals("") ||
                            Input_endtime.getText().toString().equals("") || input_furyou.getText().toString().equals("") ||
                            Input_ryouhinn.getText().toString().equals("") || hinmoku_label_text.getText().toString().equals("") ||
                            hinmoku_label_text.getText().toString().equals("")) {

                        tostMake("「入力項目」を入力してください。", 0, -200);
                        return;

                        /**
                         * ************  エディットテキストが　「空」じゃなかった場合  ************
                         */
                    }  else {

                        //************* エラー 加工数に 0 が　入っていた場合
                        if(input_kakou.getText().toString().equals("0")) {
                            tostMake("「加工数」を修正してください。", 0, -200);
                            return;
                        }

                        //**************　エラー 時間　マイナスの値チェック　
                        String input_work_edit_s = input_work_edit.getText().toString();
                        int input_work_edit_i = Integer.parseInt(input_work_edit_s);
                        if(input_work_edit_i < 0) {
                            tostMake("「作業時間」がマイナスの値になっています。", 0 , -200);
                            input_work_edit.setText("");
                            return;
                        }

                        /***
                         *  エディットテキストの　値取得
                         */
                        String tmp_yotei = input_Yotei_num.getText().toString(); // 予定数
                        String tmp_start = input_starttime.getText().toString(); // 開始時
                        String tmp_end = Input_endtime.getText().toString(); // 終了時
                        String tmp_futyou = input_furyou.getText().toString(); // 不良品数
                        String tmp_souseisan = Input_seisan.getText().toString(); // 総生産数
                        String tmp_ryouhin = Input_ryouhinn.getText().toString(); // 良品数

                        String tmp_input_kakou = input_kakou.getText().toString(); // 加工数

                        int tmp_yotei_i = Integer.parseInt(tmp_yotei);
                        int tmp_start_i = Integer.parseInt(tmp_start);
                        int tmp_end_i = Integer.parseInt(tmp_end);
                        int tmp_futyou_i = Integer.parseInt(tmp_futyou);
                        int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);
                        int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);

                        int tmp_input_kakou_i = Integer.parseInt(tmp_input_kakou); // 加工数

                        //******* グラフ　挿入用　値  「終了数」-「開始時」/ 「加工数」
                        int Grafu_Insert_SUM = (tmp_end_i - tmp_start_i) / tmp_input_kakou_i;

                        //***************** エラー　チェック *****************
                        //*********** 「良品数」 or 「総生産数」　が　マイナスの値の場合 **********
                        if(tmp_souseisan_i < 0 || tmp_ryouhin_i < 0) {

                            //********** 「良品数」 or 「総生産数」　が　マイナスの値の場合　アラートを出して確認 ***********

                            //-------　タイトル
                            TextView titleView;
                            // アクティビティ名を入れる
                            titleView = new TextView(QR_Barcode_Read.this);
                            titleView.setText("マイナスの値になっています。");
                            titleView.setTextSize(20);
                            titleView.setTextColor(Color.WHITE);
                            titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                            titleView.setPadding(20,20,20,20);
                            titleView.setGravity(Gravity.CENTER);

                            //-------- アラートログの表示 開始 ----------
                            AlertDialog.Builder bilder = new AlertDialog.Builder(QR_Barcode_Read.this);

                            /**
                             *  ダイアログの項目セット
                             */
                            //------- タイトルセット
                            bilder.setCustomTitle(titleView);

                            //------- メッセージセット
                            bilder.setMessage("「総生産数」良品数」がマイナスの値になっています。このまま登録しますか？");

                            bilder.setNegativeButton("登録する", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    //****************************** インサート処理 ************************************
                                    // Insert_Send01();

                                    //************* インサート処理　実行 **************
                                    Insert_Send_02_save();

                                    Send_CSV_POST();
                                    Send_Grafu_Table_Insert(Grafu_Insert_SUM);
                                    Teiki_Send_Flg(); //******* 定期送信　ok フラグ 1
                                    System.out.println("Send_Grafu_Table_Insert(); 終了 4600" );
                                    tostMake("タスク保存を完了しました。（－）", -200, 0);

                                    //*************  HOME へ　画面遷移させる ************
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO: ここで処理を実行する
                                            finish();

                                        }
                                    }, 400);

                                    //********** ログ用　インサート
                                    Send_table_01_log_Insert();

                                }
                            });

                            bilder.setPositiveButton("登録しない（値を修正する）", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // -------------- 修正
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

                            //******************************************* END ボタン　配色　変更

                            // ************ END　確認　アラート
                            return;
                        }

                        //****************************** インサート処理 ************************************
                        // Insert_Send01();

                        //************* インサート処理　実行 **************
                        Insert_Send_02_save();
                        Send_CSV_POST();
                        Send_Grafu_Table_Insert(Grafu_Insert_SUM);
                        Teiki_Send_Flg(); //******* 定期送信　ok フラグ 1
                        System.out.println("Send_Grafu_Table_Insert(); 終了 4600" );
                        tostMake("タスク保存を完了しました。（＋）", -200, 0);

                        //*************  HOME へ　画面遷移させる ************
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // TODO: ここで処理を実行する
                                finish();

                            }
                        }, 400);

                        //******** テーブル　ログ用　
                        Send_table_01_log_Insert();

                    }//------------------ END IF

                } //***************************************** 「塗装課」　判定  END if

            }
        });



    } //---------------------------------------------- on Create END ---------------------------->

    /***
     *  エディットテキスト　フォーカスを　画面外へ　移動　して　ソフトキーボードを消す
     */


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mFocusView.requestFocus();
        return super.onTouchEvent(event);
    }





    /***
     *  エディットテキスト　フォーカスを　画面外へ　移動　して　ソフトキーボードを消す
     */

/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mFocusView.requestFocus();
        return super.dispatchTouchEvent(ev);
    }

*/



    // カメラで　読み取った結果の取得　。　表示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanResult.getContents() == null) {
            return;
        }

        /**
         /* * QR データ　取得
         */

        if (scanResult != null) {

            String scan_tmp = scanResult.getContents();
            String scan_tmp_cut = scan_tmp.substring(0,2);

            if (scanResult.getContents().contains(",") || scanResult.getContents().equals(",")) {
                System.out.println("QR------2次元バーコード--------");

                // エディットテキストに値を格納
                //  br_text.setText(result.getContents(), TextView.BufferType.NORMAL);

                tmp_qr_str = (scanResult.getContents());
                String[] arr_qr = null;

                arr_qr = tmp_qr_str.split(",");

                // JAN　コード エディットテキスト
                jan_input_01.setText(arr_qr[0], TextView.BufferType.EDITABLE);

                //品区コード
                br_val_01 = arr_qr[1]; // 保存ボタン insert　用
                System.out.println(br_val_01 + "br_val_01 2次元バーコード　テスト");

                // 品区コード　判別
                switch (br_val_01) {
                    case "1":
                        //------------------ 品目　テキストビューに値をセットする hinmoku_label_text
                        hinmoku_label_text.setText(map_SHKB_csv.get("1"));
                        break;

                    case "2":
                        hinmoku_label_text.setText(map_SHKB_csv.get("2"));
                        break;

                    case "3":
                        hinmoku_label_text.setText(map_SHKB_csv.get("3"));
                        break;

                    case "4":
                        hinmoku_label_text.setText(map_SHKB_csv.get("4"));
                        break;

                    case "5":
                        hinmoku_label_text.setText(map_SHKB_csv.get("5"));
                        break;

                    case "6":
                        hinmoku_label_text.setText(map_SHKB_csv.get("6"));
                        break;

                    case "7":
                        hinmoku_label_text.setText(map_SHKB_csv.get("7"));
                        break;

                    case "8":
                        hinmoku_label_text.setText(map_SHKB_csv.get("8"));
                        break;

                    case "9":
                        hinmoku_label_text.setText(map_SHKB_csv.get("9"));
                        break;
                }

                //  String SH_col_2  =   データ：　101A08636　　
                SH_col_2 = arr_qr[2];

                //---------------------- 品目名称　　(品名)　テキストビューに表示 hinmei_label_text

                String tmp_03 = arr_qr[3].replaceAll("\"", "");
                hinmei_label_text.setText(tmp_03);

                //-----------------------　品目備考  テキストビュー に表示  bikou_label_text
                String tmp_04 = arr_qr[4].replaceAll("\"", "");
                bikou_label_text.setText(tmp_04);

                // 場所コード　保存　Insert 用
                SH_col_7 = arr_qr[5];

                System.out.println("*******QR 場所コード********" + SH_col_7);

                //---------------------------- ハッシュマップ 判定　出力用  テキストビューに表示　（場所） place_label_text
                place_label_text.setText(somf_map.get(SH_col_7));

                System.out.println("*********************** ２次元バーコード　で　読み取り OK *********************");

                return;

                /**
                 * ********************** JAN コード 13桁 && 8桁　読み取り用 **********************************
                 */
            } else if(scan_tmp.startsWith("49") || scan_tmp.startsWith("45")) {

                place_label_text.setText(scan_tmp);

                /**
                 * ************** 現品票コード ******************
                 */
            } else {

                // *********************** バーコード 読み取り処  *********************
                System.out.println("バーコード 読み取り処理　開始");

                tmp = (scanResult.getContents());

                char[] arr_ch = new char[8];
                for (int i = 0; i < tmp.length(); i++) {
                    arr_ch[i] = tmp.charAt(i);
                }

                // 0　頭の index 0　～　4 の 0切り
                for (int i = 0; i < arr_ch.length; i++) {
                    if (arr_ch[0] == '0') {
                        arr_ch[0] = ' ';

                    } else if (arr_ch[1] == '0') {
                        arr_ch[1] = ' ';

                    } else if (arr_ch[2] == '0') {
                        arr_ch[2] = ' ';

                    } else if (arr_ch[3] == '0') {
                        arr_ch[3] = ' ';
                    }
                }

                // test_t
                get_barcode_str = new String(arr_ch);

                System.out.println("QR 取得　０ replace 文字列" + get_barcode_str);

                //----------- QR コード　、　バーコード　、　現品票コード　の取得した値で　関数実行 -------------
                Qr_Select();

                // getPr();

                // -------------------------　エディットテキスト　「作業時間」　に　フォーカスを移動 ------------
                input_work_edit.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                // ソフトキーボードを表示する

            }


        }

    }// ----------------------- onActivityResult  END  ----------------------


    //----------------- メニュー　追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_02, menu);

        return true;
    }

    //----------------- メニューボタンが押された時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menubtn_02:

                integrator = new IntentIntegrator(QR_Barcode_Read.this);


                // Fragmentで呼び出す場合
                //IntentIntegrator integrator = IntentIntegrator.forFragment(this);

                //---------------- スナックバー　処理 -----------------

                // キャプチャ画面の下方にメッセージを表示
                integrator.setPrompt("戻る　ボタン タップで「キャンセル」できます。");

                // キャプチャ画面起動
                integrator.initiateScan();
        }

        return true;
    } //------------------------------- END onOptionsItemSelected


    /**
     *  エディットテキスト　処理　function
     */

    private void Edit_TO_OFF (EditText to_on,EditText et1, EditText et2, EditText et3, EditText et4) {

        //************* 対象の　エディットテキスト以外　動かな
        to_on.setFocusable(true);
        to_on.setFocusableInTouchMode(true);
        to_on.setEnabled(true);

        //****************** 以下の　エディットテキストは全て　効かなくする
        et1.setFocusable(false);
        et1.setFocusableInTouchMode(false);
        et1.setEnabled(false);

        et2.setFocusable(false);
        et2.setFocusableInTouchMode(false);
        et2.setEnabled(false);

        et3.setFocusable(false);
        et3.setFocusableInTouchMode(false);
        et3.setEnabled(false);

        et4.setFocusable(false);
        et4.setFocusableInTouchMode(false);
        et4.setEnabled(false);

    }


    private void Edit_TO_OFF_07 (EditText to_on,EditText et1, EditText et2, EditText et3,
                                 EditText et4, EditText et5, EditText et6) {

        //************* 対象の　エディットテキスト以外　動かな
        to_on.setFocusable(true);
        to_on.setFocusableInTouchMode(true);
        to_on.setEnabled(true);

        //****************** 以下の　エディットテキストは全て　効かなくする
        et1.setFocusable(false);
        et1.setFocusableInTouchMode(false);
        et1.setEnabled(false);

        et2.setFocusable(false);
        et2.setFocusableInTouchMode(false);
        et2.setEnabled(false);

        et3.setFocusable(false);
        et3.setFocusableInTouchMode(false);
        et3.setEnabled(false);

        et4.setFocusable(false);
        et4.setFocusableInTouchMode(false);
        et4.setEnabled(false);

        et5.setFocusable(false);
        et5.setFocusableInTouchMode(false);
        et5.setEnabled(false);

        et6.setFocusable(false);
        et6.setFocusableInTouchMode(false);
        et6.setEnabled(false);

    }


    private void Edit_TO_ON (EditText et1, EditText et2, EditText et3, EditText et4) {

        //****************** 以下の　エディットテキストを全て有効にする
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.setEnabled(true);

        et2.setFocusable(true);
        et2.setFocusableInTouchMode(true);
        et2.setEnabled(true);

        et3.setFocusable(true);
        et3.setFocusableInTouchMode(true);
        et3.setEnabled(true);

        et4.setFocusable(true);
        et4.setFocusableInTouchMode(true);
        et4.setEnabled(true);

    }

    private void Edit_TO_ON_06 (EditText et1, EditText et2, EditText et3, EditText et4,
                                EditText et5, EditText et6) {

        //****************** 以下の　エディットテキストを全て有効にする
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.setEnabled(true);

        et2.setFocusable(true);
        et2.setFocusableInTouchMode(true);
        et2.setEnabled(true);

        et3.setFocusable(true);
        et3.setFocusableInTouchMode(true);
        et3.setEnabled(true);

        et4.setFocusable(true);
        et4.setFocusableInTouchMode(true);
        et4.setEnabled(true);

        et5.setFocusable(true);
        et5.setFocusableInTouchMode(true);
        et5.setEnabled(true);

        et6.setFocusable(true);
        et6.setFocusableInTouchMode(true);
        et6.setEnabled(true);

    }


    //------------------------------
    public void getSpinner_01() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        ArrayList<String> spinner_item_01 = new ArrayList<>();

        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("SELECT BUMF_c_01, BUMF_c_02 FROM BUMF_table;", null);

            while (cursor.moveToNext()) {

                int idx = cursor.getColumnIndex("BUMF_c_01");
                arr_item[0] = cursor.getString(idx);

                idx = cursor.getColumnIndex("BUMF_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_01.add(arr_item[0] + ":" + arr_item[1]);

                // 比較用にハッシュマップに挿入
                spi_map_busyo.put(arr_item[0], arr_item[1]);

            } //-------- while END

            for (String str : spinner_item_01) {
                System.out.println("スピナーアイテム：" + str);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }


    } //-------------- getSpinner_01 END


    //-------------------  QRで読み込んで  SELECT 処理  Start  ---------------------
    public void Qr_Select() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Qr_sql = helper.getReadableDatabase();

        String[] arr_item = new String[9];

        ArrayList<String> Qr_item = new ArrayList<>();

        String[] arr = {get_barcode_str};
        System.out.println(arr[0] + "：格納前　String");
        //      arr[0] = test_t;

        try {

            Cursor cursor = Qr_sql.rawQuery("SELECT * FROM SHMF_table WHERE SHMF_c_06 = " + get_barcode_str + " OR SHMF_c_05 =" + get_barcode_str + " LIMIT 1", null);

            if (cursor.moveToNext()) {

                // columnのindex を取得して、　カラムの値を　取得する。

                //----- SHMF.csv  品区　取得 -----
                int idx = cursor.getColumnIndex("SHMF_c_01");
                arr_item[0] = cursor.getString(idx);

                // 品区 取得
                br_val_01 = arr_item[0];

                // 品目コード　判別
                switch (br_val_01) {
                    case "1":
                        //----------------------- テキストビュー　（品目区分　表示用）
                        hinmoku_label_text.setText(map_SHKB_csv.get("1"));
                        break;

                    case "2":
                        hinmoku_label_text.setText(map_SHKB_csv.get("2"));
                        break;

                    case "3":
                        hinmoku_label_text.setText(map_SHKB_csv.get("3"));
                        break;

                    case "4":
                        hinmoku_label_text.setText(map_SHKB_csv.get("4"));
                        break;

                    case "5":
                        hinmoku_label_text.setText(map_SHKB_csv.get("5"));
                        break;

                    case "6":
                        hinmoku_label_text.setText(map_SHKB_csv.get("6"));
                        break;

                    case "7":
                        hinmoku_label_text.setText(map_SHKB_csv.get("7"));
                        break;

                    case "8":
                        hinmoku_label_text.setText(map_SHKB_csv.get("8"));
                        break;

                    case "9":
                        hinmoku_label_text.setText(map_SHKB_csv.get("9"));
                        break;
                }


                //----- SHMF.csv  カラム ２　取得 -----
                idx = cursor.getColumnIndex("SHMF_c_02");
                arr_item[1] = cursor.getString(idx);
                System.out.println("カラム 01" + arr_item[1]);

                //-------- 品目コード　（テキストビューには表示しない）
                SH_col_2 = arr_item[1];

                //------------------------ テキストビュー　表示　（商品名）
                idx = cursor.getColumnIndex("SHMF_c_03");
                arr_item[2] = cursor.getString(idx);
                System.out.println("カラム 02" + arr_item[2]);

                String tmp_col_2 = arr_item[2].replaceAll("\"","");
                SH_col_3 = tmp_col_2;
                // hinmei_label_text => 品名　表示用　テキストビュー


                hinmei_label_text.setText(tmp_col_2);

                //------------------------ テキストビュー　表示 （備考）
                idx = cursor.getColumnIndex("SHMF_c_04");

                arr_item[3] = cursor.getString(idx);

                String tmp_col_03 = arr_item[3].replaceAll("\"","");

                System.out.println("カラム 03" + arr_item[3]);

                // bikou_label_text => .備考　表示用　テキストビュー
                bikou_label_text.setText(tmp_col_03);

                idx = cursor.getColumnIndex("SHMF_c_05");
                arr_item[4] = cursor.getString(idx);
                System.out.println("カラム 04" + arr_item[4]);

                //------------------ SHMF カラム　6 , 7 , 8 取得 ------------------
                idx = cursor.getColumnIndex("SHMF_c_06");
                arr_item[5] = cursor.getString(idx);

                SH_col_6 = arr_item[5];

                jan_input_01.setText(tmp, TextView.BufferType.EDITABLE);

                // 保存場所
                idx = cursor.getColumnIndex("SHMF_c_07");
                arr_item[6] = cursor.getString(idx);

                //-------- 場所コード　取得 **** 保存ボタン Insert 用 *****
                SH_col_7 = arr_item[6];


                // --------------- テキストビュー　表示 （場所） ハッシュマップ　判別 出力
                place_label_text.setText(somf_map.get(SH_col_7));

                idx = cursor.getColumnIndex("SHMF_c_08");
                arr_item[7] = cursor.getString(idx);

                SH_col_8 = arr_item[7];

                idx = cursor.getColumnIndex("SHMF_c_09");
                arr_item[8] = cursor.getString(idx);

                SH_col_9 = arr_item[8];

            }

            for (String s : arr_item) {
                System.out.println("出力　ループ　テスト" + s);
            }

        } finally {

            if (Qr_sql != null) {
                Qr_sql.close();
                System.out.println("SQL CLOSE　セレクト　失敗");
            }

            //------------ 棚卸　エディットテキスト処理


        }


    }  //-------------------  QRで読み込んで  SELECT 処理  End  ---------------------

    private void get_id_select() {

        // ヘルパー　メソッド
        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_id_get = helper.getReadableDatabase();


        String[] arr_item = new String[1];

        Cursor cursor = null;
        try {

            cursor = db_id_get.rawQuery("select max(id) from Send_table_01", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                   get_id_num = cursor.getInt(0);
                   System.out.println("get_id_num:::" + get_id_num);
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(db_id_get != null) {
                db_id_get.close();
            }
        }
    }


    private void get_Place() {

        // ヘルパー　メソッド
        TestOpenHelper helper_place = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Qr_hr = helper_place.getReadableDatabase();


        String[] arr_item = new String[2];

        ArrayList<String> Qr_item = new ArrayList<>();

        try {

            Cursor cursor = Qr_hr.rawQuery("SELECT * FROM SOMF_table;", null);

            while (cursor.moveToNext()) {

                int idx = cursor.getColumnIndex("SOMF_c_01");

                //    int idx = cursor.getColumnIndex(TestOpenHelper.SONF_DB_C_01);
                arr_item[0] = cursor.getString(idx);


                //   idx = cursor.getColumnIndex(TestOpenHelper.SONF_DB_C_02);
                idx = cursor.getColumnIndex("SOMF_c_02");
                arr_item[1] = cursor.getString(idx);


                Qr_item.add(arr_item[0] + ":" + arr_item[1]);

                // 比較用　ハッシュマップ
                somf_map.put(arr_item[0], arr_item[1]);

            }

            /*
            for (String str : Qr_item) {
                System.out.println("Qr_item 出力：：：" + str);
            }

             */

            /*
            for (Map.Entry<String, String> entry : somf_map.entrySet()) {
                System.out.println("マップキー" + entry.getKey() + " : " + entry.getValue());
            }

             */


        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (Qr_hr != null) {
                Qr_hr.close();
            }
        }

    }//-------------------------------------- get_Place END


    private void Get_SHKB_Csv() {

        // ヘルパー　メソッド
        TestOpenHelper helper_shkb = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase sql_shkb = helper_shkb.getReadableDatabase();

        // while 値　格納用
        String[] arr_item = new String[2];

        // SHKB.csv 格納用　リスト
        ArrayList<String> SHKB_item = new ArrayList<>();

        try {

            // カーソル　& rawQuery で　select
            Cursor cursor = sql_shkb.rawQuery("SELECT * FROM SHKB_table;", null);

            while (cursor.moveToNext()) {

                //------- 1 カラム　取得
                int idx = cursor.getColumnIndex("SHKB_c_01");
                arr_item[0] = cursor.getString(idx);
                Inser_Hinmoku_K = arr_item[0];


                //------- 2　カラム　取得
                idx = cursor.getColumnIndex("SHKB_c_02");
                arr_item[1] = cursor.getString(idx);

                //------- 比較用　ハッシュマップ　に　挿入
                map_SHKB_csv.put(arr_item[0], arr_item[1]);

                //------- 文字から　コード取得用　ハッシュマップ
                map_SHKB_csv_code.put(arr_item[1], arr_item[0]);


            }

            /*
            // テスト　出力
            for (Map.Entry<String, String> entry : map_SHKB_csv.entrySet()) {
                System.out.println("マップキー" + entry.getKey() + "  " + "マップバリュー" + entry.getValue());
            }

             */

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // DB オブジェクトが　空じゃなかったら　DB を　閉じる
            if (sql_shkb != null) {
                sql_shkb.close();
            }
        }

    } //--------------------- Get_SHKB_Csv END

    private void tostMake_S(String msg, int x, int y) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);

        // 表示位置　調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    } //----------------- END tostMake

    private void tostMake(String msg, int x, int y) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);

        // 表示位置　調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    } //----------------- END tostMake

    private void new_Comp() {

        //------ エディットテキスト ----------------
        jan_input_01 = findViewById(R.id.jan_input_01);
        input_setup_edit = findViewById(R.id.input_setup_edit);
        input_work_edit = findViewById(R.id.input_work_edit);
        // 開始時
        input_starttime = findViewById(R.id.input_starttime);
        // 終了時
        Input_endtime = findViewById(R.id.Input_endtime);
        // 不良品数
        input_furyou = findViewById(R.id.input_furyou);
        // 生産数
        Input_seisan = findViewById(R.id.Input_seisan);
        // 良品数
        Input_ryouhinn = findViewById(R.id.Input_ryouhinn);


        // 商品コード　検索エディットテキスト
        jan_input_01 = findViewById(R.id.jan_input_01);

        //-------------------　値受け取り用　テキストビュー --------------
        hinmoku_label_text = findViewById(R.id.hinmoku_label_text);
        hinmei_label_text = findViewById(R.id.hinmei_label_text);
        bikou_label_text = findViewById(R.id.bikou_label_text);
        place_label_text = findViewById(R.id.place_label_text);

        //------- 保存ボタン
        save_btn = findViewById(R.id.save_btn);

        //------- 最終工程　チェックボックス
        last_check = findViewById(R.id.last_check);

        //------- 予定数　エディットテキスト
        input_Yotei_num = findViewById(R.id.input_Yotei_num);

        //------- PDF 表示ボタン
        yotei_btn = findViewById(R.id.yotei_btn);

        /**
         *  加工数   エディットテキスト
         */
        input_kakou = findViewById(R.id.input_kakou);

        /**
         *  開始時間　&  終了時間　追加
         */
        qr_start_time = findViewById(R.id.qr_start_time); // ボタン
        qr_end_time = findViewById(R.id.qr_end_time); // ボタン
        qr_start_time_view = findViewById(R.id.qr_start_time_view); // テキストビュー
        qr_end_time_view = findViewById(R.id.qr_end_time_view); // テキストビュー

        /**
         *  エディットテキスト　フォーカス　キーボード外し用
         */
        mFocusView = (TextView) findViewById(R.id.focusView);

        /**
         * 　「塗装課」用　ラベル
         */
        Tosou_label = (TextView) findViewById(R.id.Tosou_label);
        Tosou_label.setVisibility(View.GONE);

        /**
         *  「樹脂成型課」　用　ラベル
         */
        Zyushi_Seikei_btn = (Button) findViewById(R.id.Zyushi_Seikei_btn); // ボタン => ダイアログ　表示
        Zyushi_Seikei_btn.setVisibility(View.GONE);

        iro_z_edit = (EditText) findViewById(R.id.iro_z_edit); // 色段取時間
        Kata_z_edit = (EditText) findViewById(R.id.Kata_z_edit); // 型段取時間
        Kikai_z_input = (EditText) findViewById(R.id.Kikai_z_input); // 機械コード

        iro_z_box = (TextInputLayout) findViewById(R.id.iro_z_box);
        Kata_z_box = (TextInputLayout) findViewById(R.id.Kata_z_box);
        Kikai_z_box = (TextInputLayout) findViewById(R.id.Kikai_z_box);

    }

    private void init() {

        /**
         *   ＊＊＊＊＊＊＊＊＊＊　「塗装課」　用　入力
         */
        if(Sagyou_B.contains("B0887")) {

            //**** ラベル　表示
            Tosou_label.setVisibility(View.VISIBLE);
            Tosou_label.setText("「塗装課」用入力 フォーム");

            //**** 樹脂成型課　非表示
            Zyushi_Seikei_btn.setVisibility(View.GONE);
            iro_z_box.setVisibility(View.GONE);
            Kata_z_box.setVisibility(View.GONE);
            Kikai_z_box.setVisibility(View.GONE);


            // 商品コード　検索エディットテキスト
            // #### setInputType => InputType.TYPE_CLASS_NUMBER ### 数字入力
            jan_input_01.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 「予定数」
            input_Yotei_num.setInputType(InputType.TYPE_CLASS_NUMBER);

            //*********  入力可　「予定数」
            //*********  入力不可　「段取り時間」「作業時間」「開始時」「終了時」「加工数」
            TousouKa_Edit_TextHandle(input_Yotei_num, input_setup_edit, input_work_edit, input_starttime,
                    Input_endtime, input_furyou, input_kakou);

            qr_start_time.setEnabled(false); // ---- 開始時間　ボタン　無効
            qr_end_time.setEnabled(false);   // ---- 終了時間　ボタン　無効

            // 入力不可処理 （総生産数）
            Input_seisan.setFocusable(false);
            Input_seisan.setFocusableInTouchMode(false);
            Input_seisan.setEnabled(false);

            //-------- 良品数
            //  Input_ryouhinn.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 入力不可処理
            Input_ryouhinn.setFocusable(false);
            Input_ryouhinn.setFocusableInTouchMode(false);
            Input_ryouhinn.setEnabled(false);

            // エディットテキスト　空にして、　読み取り専用にする。
            jan_input_01.setText("");
            // 段取り時間
            input_setup_edit.setText("");
            // 作業時間
            input_work_edit.setText("");

            //--------- エディットテキスト　空にする
            hinmoku_label_text.setText("");
            hinmei_label_text.setText("");
            hinmoku_label_text.setText("");
            bikou_label_text.setText("");
            place_label_text.setText("");
            input_Yotei_num.setText("");
            input_starttime.setText("");
            Input_endtime.setText("");
            Input_seisan.setText("");
            input_furyou.setText("");
            Input_ryouhinn.setText("");

            // 開始時間　& 終了時間
            qr_start_time_view.setText("");
            qr_end_time_view.setText("");

            /**
             *  加工数　追加   1　を　セットする
             */
            input_kakou.setInputType(InputType.TYPE_CLASS_NUMBER);
            input_kakou.setText("1");
            //  input_kakou.setText("");

            /**
             *  加工数　セット
             */
            String tmp_input_kakou = input_kakou.getText().toString();
            tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);


            /**
             * 　＊＊＊＊＊＊ 「樹脂成型課」　＊＊＊＊＊＊
             */
        } else if (Sagyou_B.contains("B0889")) {

            Tosou_label.setVisibility(View.VISIBLE);
            Tosou_label.setText("樹脂成型課 入力項目");

            //**** 樹脂成型課　表示 設定
            Zyushi_Seikei_btn.setVisibility(View.VISIBLE);
            iro_z_box.setVisibility(View.VISIBLE);
            Kata_z_box.setVisibility(View.VISIBLE);
            Kikai_z_box.setVisibility(View.VISIBLE);

            iro_z_edit.setFocusable(false);
            iro_z_edit.setFocusableInTouchMode(false);
            iro_z_edit.setEnabled(false);

            Kata_z_edit.setFocusable(false);
            Kata_z_edit.setFocusableInTouchMode(false);
            Kata_z_edit.setEnabled(false);

            Kikai_z_input.setFocusable(false);
            Kikai_z_input.setFocusableInTouchMode(false);
            Kikai_z_input.setEnabled(false);
            //****　END 樹脂成型課　表示 設定　

            // 商品コード　検索エディットテキスト
            // #### setInputType => InputType.TYPE_CLASS_NUMBER ### 数字入力
            jan_input_01.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 時間入力　エディットテキスト  ->  数字入力
            input_work_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 段取時間　エディットテキスト -> 数字
            input_setup_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

            //----- 入力 エディットテキスト -> 数字
            input_work_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            input_starttime.setInputType(InputType.TYPE_CLASS_NUMBER);
            Input_endtime.setInputType(InputType.TYPE_CLASS_NUMBER);
            input_furyou.setInputType(InputType.TYPE_CLASS_NUMBER);
            input_Yotei_num.setInputType(InputType.TYPE_CLASS_NUMBER);

            //------ 総生産数
            //   Input_seisan.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 入力不可処理 （総生産数）
            Input_seisan.setFocusable(false);
            Input_seisan.setFocusableInTouchMode(false);
            Input_seisan.setEnabled(false);

            //-------- 良品数
            //  Input_ryouhinn.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 入力不可処理
            Input_ryouhinn.setFocusable(false);
            Input_ryouhinn.setFocusableInTouchMode(false);
            Input_ryouhinn.setEnabled(false);



            // エディットテキスト　空にして、　読み取り専用にする。
            jan_input_01.setText("");
            // 段取り時間
            input_setup_edit.setText("");
            // 作業時間
            input_work_edit.setText("");

            //--------- エディットテキスト　空にする
            hinmoku_label_text.setText("");
            hinmei_label_text.setText("");
            hinmoku_label_text.setText("");
            bikou_label_text.setText("");
            place_label_text.setText("");
            input_Yotei_num.setText("");
            input_starttime.setText("");
            Input_endtime.setText("");
            Input_seisan.setText("");
            input_furyou.setText("");
            Input_ryouhinn.setText("");

            // 開始時間　& 終了時間
            qr_start_time_view.setText("");
            qr_end_time_view.setText("");

            /**
             *  加工数　追加   1　を　セットする
             */
            input_kakou.setInputType(InputType.TYPE_CLASS_NUMBER);
            input_kakou.setText("1");
            //  input_kakou.setText("");

            /**
             *  加工数　セット
             */
            String tmp_input_kakou = input_kakou.getText().toString();
            tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);



        } else {

            /**
         *   ＊＊＊＊＊＊＊＊＊＊　「塗装課」　じゃない　通常処理
         */

            //**** ラベル　非表示
            Tosou_label.setVisibility(View.GONE);
            Tosou_label.setText("");

            //**** 樹脂成型課　非表示
            Zyushi_Seikei_btn.setVisibility(View.GONE);
            iro_z_box.setVisibility(View.GONE);
            Kata_z_box.setVisibility(View.GONE);
            Kikai_z_box.setVisibility(View.GONE);

            // 商品コード　検索エディットテキスト
        // #### setInputType => InputType.TYPE_CLASS_NUMBER ### 数字入力
        jan_input_01.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 時間入力　エディットテキスト  ->  数字入力
        input_work_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 段取時間　エディットテキスト -> 数字
        input_setup_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        //----- 入力 エディットテキスト -> 数字
        input_work_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_starttime.setInputType(InputType.TYPE_CLASS_NUMBER);
        Input_endtime.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_furyou.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_Yotei_num.setInputType(InputType.TYPE_CLASS_NUMBER);

        //------ 総生産数
        //   Input_seisan.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 入力不可処理 （総生産数）
        Input_seisan.setFocusable(false);
        Input_seisan.setFocusableInTouchMode(false);
        Input_seisan.setEnabled(false);

        //-------- 良品数
        //  Input_ryouhinn.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 入力不可処理
        Input_ryouhinn.setFocusable(false);
        Input_ryouhinn.setFocusableInTouchMode(false);
        Input_ryouhinn.setEnabled(false);



        // エディットテキスト　空にして、　読み取り専用にする。
        jan_input_01.setText("");
        // 段取り時間
        input_setup_edit.setText("");
        // 作業時間
        input_work_edit.setText("");

        //--------- エディットテキスト　空にする
        hinmoku_label_text.setText("");
        hinmei_label_text.setText("");
        hinmoku_label_text.setText("");
        bikou_label_text.setText("");
        place_label_text.setText("");
        input_Yotei_num.setText("");
        input_starttime.setText("");
        Input_endtime.setText("");
        Input_seisan.setText("");
        input_furyou.setText("");
        Input_ryouhinn.setText("");

        // 開始時間　& 終了時間
        qr_start_time_view.setText("");
        qr_end_time_view.setText("");

        /**
         *  加工数　追加   1　を　セットする
         */
        input_kakou.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_kakou.setText("1");
      //  input_kakou.setText("");

        /**
         *  加工数　セット
         */
        String tmp_input_kakou = input_kakou.getText().toString();
        tmp_input_kakou_str = Integer.parseInt(tmp_input_kakou);

        }

    }

    /**
     * キーボード　非表示
     */
    public static void key_hide(Activity activity) {

        if (activity == null) return;

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // 現在のフォーカスを取得
        View v = activity.getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /***
     *  「保存」 = insert 処理
     */
    private void Insert_Send_02_save() {


        /**
         *   ログイン時　作業日　から　取得  yyyyMMdd
         */
        Sagyou_new_ID_SELECT();

        String date_num = Create_Date_NUM; // yyyyMMdd
        yy_num = date_num.substring(0,4); // yyyy
        dd_num = date_num.substring(6,8); // dd , dd-01 , dd-02  ,  Saghou_Code_num と　変更


        /***
         * ------------------- INSERT 用　データ ------------------------
         */
        String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用
        String Sagyou_date_num = Sagyou_date; // yyyymm 取得  => 作業日　用

        // 現品票コード
        String jan_input_01_num = jan_input_01.getText().toString();
        // 品目K
        String hinmoku_label_text_num = hinmoku_label_text.getText().toString();
        // 品名取得
        String hinmei = hinmei_label_text.getText().toString();
        //-------------------------- 現品票　コードデータ


        // 品目区分 -----------------------
        String Hin_kubun = hinmoku_label_text.getText().toString();
        // 品目区分　数字　取得
        String Hinmoku_KK = map_SHKB_csv_code.get(Hin_kubun);
        //----------------- 品目区分 END

        // 品目備考
        String Hin_bikou = bikou_label_text.getText().toString();

        // 作業場所
        String Sagyou_Place = place_label_text.getText().toString();
        //-------------------------- 現品票　コードデータ END

        // 段取時間
        String dandori = input_setup_edit.getText().toString();

        //***** 空だったら　０　を入れる
        if(dandori.length() == 0) {
            dandori = "0";
        }
        //   int h_time = Integer.parseInt(input_setup_edit.getText().toString());

        // 作業時間
        String sagyou = input_work_edit.getText().toString();

        //***** 空だったら 0 を　入れる
        if(sagyou.length() == 0) {
            sagyou = "0";
        }

        // 予定数量
        String input_Yotei_num_get = input_Yotei_num.getText().toString();

        // 開始数量
        String input_starttime_num = input_starttime.getText().toString();
        int input_starttime_num_i = Integer.parseInt(input_starttime_num);

        // 終了数量
        String Input_endtime_num = Input_endtime.getText().toString();
        int Input_endtime_num_i = Integer.parseInt(Input_endtime_num);



        // 総生産数
        String Input_seisan_num = Input_seisan.getText().toString();
        // 不良品数
        String input_furyou_num = input_furyou.getText().toString();

        // 良品数
        String ryouhin = Input_ryouhinn.getText().toString();

        // 加工数
        String kakou_num = input_kakou.getText().toString();
        int kakou_num_i = Integer.parseInt(kakou_num);

        //*********** 総生産計算 *************
        int Souseisan_SUM = (Input_endtime_num_i - input_starttime_num_i) / kakou_num_i;



        // 再送フラグ 基本 0
        String saisou_num = "0";

        // DB への　登録

        //-------------　 品目K （区分）　数字
        String hinmoku_KK = Inser_Hinmoku_K;

        //------------- 品目C （品目コード）
        String hinmoku_CC = SH_col_2;

        //-------------- 作業場所　コード
        String Sagyou_basyo_C = SH_col_7;

        //-------------- 開始時間
        String qr_start_time_view_in = qr_start_time_view.getText().toString();
        //-------------- 終了時間
        String qr_end_time_view_in = qr_end_time_view.getText().toString();
        //-------------- グラフ id

        String graf_id_tmp_s = String.valueOf(Send_Table_id_max);


        //************  変動用 id (max) **************************************
        DD_Task_GET_Num_Max();
        //*********** 変動用　ID (max) max(send_col_22) from Send_table_01 where send_col_22 like '26%';

        String Id_tmp_max;
        //******** １　～　９の時は　０をつける
        if(DD_id_i > 0 && DD_id_i < 10) {

            Id_tmp_max  = String.valueOf(DD_id_i);
            T_ID_Str = dd_num + "-" + "0" + Id_tmp_max;

        } else {

            //********** １０以上は　０　をつけない
            Id_tmp_max = String.valueOf(DD_id_i);
            T_ID_Str = dd_num + "-" + Id_tmp_max;

        }
        System.out.println("Id_tmp:::値:::" + Id_tmp_max);
        //************  変動用 id (max) ************************************** END

        /**
         *  「樹脂成型課」　３項目　追加
         */
        String Color_Time = "0"; // 色段取時間

        String Kata_Time = "0"; // 型段取時間

        String Kikai_Code = "0"; // 機械コード

        //******* 色段取時間
        if(iro_z_edit.getText().toString().equals("") == false) {
            Color_Time = iro_z_edit.getText().toString();
        } else {
            Color_Time = "0";
        }

         //******* 型段取時間
        if(Kata_z_edit.getText().toString().equals("") == false) {
            Kata_Time = Kata_z_edit.getText().toString();
        } else {
            Kata_Time = "0";
        }

        //******* 機械コード
        if(Kikai_z_input.getText().toString().equals("") == false) {
            Kikai_Code = Kikai_z_input.getText().toString();
        } else {
            Kikai_Code = "0";
        }


        //-------------- インサート　カラムデータ END ----------------


        DBAdapter dbAdapter = new DBAdapter(this);

        dbAdapter.openDB();

        //****** 追加 色段取時間 24  ,  型段取時間 25 , 機械コード 26
        dbAdapter.saveDB(get_TMNF_01, Sagyou_B, jan_input_01_num, Hinmoku_KK,
                hinmoku_CC, hinmei, Hin_bikou, Sagyou_basyo_C, dandori, sagyou,
                input_Yotei_num_get, input_starttime_num, Input_endtime_num,
                String.valueOf(Souseisan_SUM), input_furyou_num, ryouhin, check_str,kakou_num,
                saisou_num,qr_start_time_view_in,qr_end_time_view_in,T_ID_Str,date_num,
                Color_Time,Kata_Time,Kikai_Code);

        dbAdapter.closeDB();

        init();

    }

    /***
     *  「保存」 = insert 処理
     */
    private void Insert_Send_TOSOU_save() {

        /**
         * 　タスク数　取得  //------- グラフ用 ID       カラム 22
         */
        Send_TB_id_max();
        String tmp_id_str = String.valueOf(Send_Table_id_max);


        /**
         *   ログイン時　作業日　から　取得  yyyyMMdd
         */
        Sagyou_new_ID_SELECT();

        String date_num = Create_Date_NUM; // yyyyMMdd
        yy_num = date_num.substring(0,4); // yyyy
        dd_num = date_num.substring(6,8); // dd , dd-01 , dd-02  ,  Saghou_Code_num と　変更


        /***
         * ------------------- INSERT 用　データ ------------------------
         */
        String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用
        String Sagyou_date_num = Sagyou_date; // yyyymm 取得  => 作業日　用

        // 現品票コード
        String jan_input_01_num = jan_input_01.getText().toString();
        // 品目K
        String hinmoku_label_text_num = hinmoku_label_text.getText().toString();
        // 品名取得
        String hinmei = hinmei_label_text.getText().toString();
        //-------------------------- 現品票　コードデータ


        // 品目区分 -----------------------
        String Hin_kubun = hinmoku_label_text.getText().toString();
        // 品目区分　数字　取得
        String Hinmoku_KK = map_SHKB_csv_code.get(Hin_kubun);
        //----------------- 品目区分 END

        // 品目備考
        String Hin_bikou = bikou_label_text.getText().toString();

        // 作業場所
        String Sagyou_Place = place_label_text.getText().toString();
        //-------------------------- 現品票　コードデータ END

        // 段取時間
        String dandori = "0";
        //   int h_time = Integer.parseInt(input_setup_edit.getText().toString());

        // 作業時間
        String sagyou = "0";

        // 予定数量
        String input_Yotei_num_get = input_Yotei_num.getText().toString();

        // 開始数量
        String input_starttime_num = "0";
        // 終了数量
        String Input_endtime_num = "0";
        // 総生産数
        String Input_seisan_num = Input_seisan.getText().toString();
        // 不良品数
        String input_furyou_num = "0";
        // 良品数
        String ryouhin = "0";

        // 加工数
        String kakou_num = input_kakou.getText().toString();

        // 再送フラグ 基本 0
        String saisou_num = "0";

        // DB への　登録

        //-------------　 品目K （区分）　数字
        String hinmoku_KK = Inser_Hinmoku_K;

        //------------- 品目C （品目コード）
        String hinmoku_CC = SH_col_2;

        //-------------- 作業場所　コード
        String Sagyou_basyo_C = SH_col_7;

        //-------------- 開始時間
        String qr_start_time_view_in = "0";
        //-------------- 終了時間
        String qr_end_time_view_in = "0";
        //-------------- グラフ id

        String graf_id_tmp_s = String.valueOf(Send_Table_id_max);


        /**
         * タスク総数をセット
         */
        //********  dd-0 Send_Table の　max id + 1  or  1
        G_id_str = Saghou_Code_num + "-" + "0" + tmp_id_str;

        //*********** 変動用　ID
        //     T_ID_Str = Saghou_Code_num + "-" + "0" + Id_tmp;

        //************  変動用 id (max) **************************************
        DD_Task_GET_Num_Max();
        //*********** 変動用　ID (max) max(send_col_22) from Send_table_01 where send_col_22 like '26%';

        String Id_tmp_max;
        //******** １　～　９の時は　０をつける
        if(DD_id_i > 0 && DD_id_i < 10) {

            Id_tmp_max  = String.valueOf(DD_id_i);
            T_ID_Str = dd_num + "-" + "0" + Id_tmp_max;

        } else {

            //********** １０以上は　０　をつけない
            Id_tmp_max = String.valueOf(DD_id_i);
            T_ID_Str = dd_num + "-" + Id_tmp_max;

        }
        System.out.println("Id_tmp:::値:::" + Id_tmp_max);
        //************  変動用 id (max) ************************************** END

        //*********** yyyymmdd CSV 項目３　追加  Sagyou_yymmdd

        /**
         * 　＊＊＊＊＊＊ 「樹脂成型課」　３項目　追加　＊＊＊＊＊＊＊
         */
        String Color_Time = "0"; // 色段取時間

        String Kata_Time = "0"; // 型段取時間

        String Kikai_Code = "0"; // 機械コード

        //-------------- インサート　カラムデータ END ----------------


        DBAdapter dbAdapter = new DBAdapter(this);

        dbAdapter.openDB();

        //****** 追加 色段取時間 24  ,  型段取時間 25 , 機械コード 26
        dbAdapter.saveDB(get_TMNF_01, Sagyou_B, jan_input_01_num, Hinmoku_KK,
                hinmoku_CC, hinmei, Hin_bikou, Sagyou_basyo_C, dandori, sagyou,
                input_Yotei_num_get, input_starttime_num, Input_endtime_num,
                Input_seisan_num, input_furyou_num, ryouhin, check_str,kakou_num,
                saisou_num,qr_start_time_view_in,qr_end_time_view_in,T_ID_Str,date_num,
                Color_Time,Kata_Time,Kikai_Code);

        dbAdapter.closeDB();

        init();

    } //******************************** END function

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate() {
        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    /**
     * Back ボタンが押された処理
     */
    @Override
    public void onBackPressed() {

        /***
         *input_setup_edit = 段取時間
         * input_work_edit = 作業時間
         * input_Yotei_num = 予定数
         * input_starttime = 開始時間
         * Input_endtime = 終了時間
         * input_furyou = 不良品数
         * Input_seisan = 総生産数
         * Input_ryouhinn = 良品数
         */

        // 入力エディットテキストの　値が　空じゃなかったら　アラートログを出す
        if (input_setup_edit.getText().toString().equals("") == false || input_work_edit.getText().toString().equals("") == false ||
                input_Yotei_num.getText().toString().equals("") == false || input_starttime.getText().toString().equals("") == false ||
                Input_endtime.getText().toString().equals("") == false || input_furyou.getText().toString().equals("") == false ||
                Input_seisan.getText().toString().equals("") == false || Input_ryouhinn.getText().toString().equals("") == false) {

            // ---------- どれか 1　つでも　値が入っていた場合
            //--------------- アラートダイヤログ　タイトル　設定 ---------------//
            // タイトル
            TextView titleView;
            titleView = new TextView(QR_Barcode_Read.this);
            titleView.setText("入力内容が破棄されます！？");
            titleView.setTextSize(22);
            titleView.setTextColor(Color.WHITE);
            titleView.setBackgroundColor(getResources().getColor(R.color.red));
            titleView.setPadding(20, 30, 20, 30);
            titleView.setGravity(Gravity.CENTER);

            //-------------- アラートログの表示 開始 -------------- //
            AlertDialog.Builder bilder = new AlertDialog.Builder(QR_Barcode_Read.this);

            // ダイアログの項目
            bilder.setCustomTitle(titleView);

            // メッセージ内容　セット
            String msg_bilder = "入力内容が失われます。「前の画面へ戻りますか」？";
            bilder.setMessage(msg_bilder);

            bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(getApplicationContext(), Work_Choice.class);
                    startActivity(intent);

                    finish();
                }
            });

            AlertDialog dialog = bilder.create();
            dialog.show();

            //******************************************* ボタン　配色　変更
            //********* ボタン はい **********
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B20000"));
            //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

            //********* ボタン いいえ **********
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B20000"));
            //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

            //******************************************* END ボタン　配色　変更

        } else {

            /***
             *  値が　入力　なしで　通常に　バックボタンが押された　場合
             */
            /*
            Intent intent = new Intent(getApplicationContext(), Work_Choice.class);
            startActivity(intent);
             */

            finish();
        }

    }



    /**
     * 作業者名　から　作業者コードを取得する
     */
    private void GET_tantou() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase GET_tantou_db = helper.getReadableDatabase();

        //   String [] arr_item = new String [2];
        ArrayList<String> arr_item = new ArrayList<>();

        Cursor cursor = null;
        try {

            // 部署名　：　作業部署名　セット
            String[] selectionArgs = {ch_busyo_view_Hon_num, ch_busyo_view_num};

            cursor = GET_tantou_db.rawQuery("select BUMF_c_01 from " +
                    "BUMF_table where" +
                    " BUMF_c_02 in (?,?);", selectionArgs);

            if(cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {

                    do {

                        arr_item.add(cursor.getString(cursor.getColumnIndex("BUMF_c_01")));

                        //   System.out.println("rawQuery 出力：：：" + arr_item[0] + arr_item[1]);

                    } while (cursor.moveToNext());

                } // END if moveToFirst

            } else {
                return;
            }

            // 部署コード, 作業部署 を 変数へ格納
            busyo = arr_item.get(0);
            sagyou_busyo = arr_item.get(1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (GET_tantou_db != null) {
                GET_tantou_db.close();
            }
        }

    }


    private void Stop_Edit_01() {

        // 予定数
        input_Yotei_num.setFocusable(false);
        input_Yotei_num.setFocusableInTouchMode(false);
        input_Yotei_num.setEnabled(false);

        // 開始時
        input_starttime.setFocusable(false);
        input_starttime.setFocusableInTouchMode(false);
        input_starttime.setEnabled(false);

        // 終了時
        Input_endtime.setFocusable(false);
        Input_endtime.setFocusableInTouchMode(false);
        Input_endtime.setEnabled(false);

        // 不良品数
        input_furyou.setFocusable(false);
        input_furyou.setFocusableInTouchMode(false);
        input_furyou.setEnabled(false);

        // 生産数
        Input_seisan.setFocusable(false);
        Input_seisan.setFocusableInTouchMode(false);
        Input_seisan.setEnabled(false);

        // 良品数
        Input_ryouhinn.setFocusable(false);
        Input_ryouhinn.setFocusableInTouchMode(false);
        Input_ryouhinn.setEnabled(false);

    }

    /***
     *
     * @param target
     * @param target_02
     * @param target_03
     *
     * 　エディットテキスト　フォーカス開始
     */

    private void Go_Edit_01(EditText target, EditText target_02, EditText target_03) {

        target.setFocusable(true);
        target.setFocusableInTouchMode(true);
        target.setEnabled(true);

        target_02.setFocusable(true);
        target_02.setFocusableInTouchMode(true);
        target_02.setEnabled(true);

        target_03.setFocusable(true);
        target_03.setFocusableInTouchMode(true);
        target_03.setEnabled(true);
    }

    /**
     * @param target
     * @param target_02
     * @param target_03
     *
     * 　　エディットテキスト　フォーカス停止処理
     */
    private void Stop_Edit_02(EditText target, EditText target_02, EditText target_03) {

        target.setFocusable(false);
        target.setFocusableInTouchMode(false);
        target.setEnabled(false);

        target_02.setFocusable(false);
        target_02.setFocusableInTouchMode(false);
        target_02.setEnabled(false);

        target_03.setFocusable(false);
        target_03.setFocusableInTouchMode(false);
        target_03.setEnabled(false);
    }



    private void Resurrection_Edit_01() {

        // 予定数
        input_Yotei_num.setFocusable(true);
        input_Yotei_num.setFocusableInTouchMode(true);
        input_Yotei_num.setEnabled(true);

        // 開始時
        input_starttime.setFocusable(true);
        input_starttime.setFocusableInTouchMode(true);
        input_starttime.setEnabled(true);

        // 終了時
        Input_endtime.setFocusable(true);
        Input_endtime.setFocusableInTouchMode(true);
        Input_endtime.setEnabled(true);

        // 不良品数
        input_furyou.setFocusable(true);
        input_furyou.setFocusableInTouchMode(true);
        input_furyou.setEnabled(true);

        // 生産数
        Input_seisan.setFocusable(true);
        Input_seisan.setFocusableInTouchMode(true);
        Input_seisan.setEnabled(true);

        // 良品数
        Input_ryouhinn.setFocusable(true);
        Input_ryouhinn.setFocusableInTouchMode(true);
        Input_ryouhinn.setEnabled(true);

    }

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
        send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_Get_file_name + "-" + get_TMNF_01 + ".csv";


        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_qr_0130 = helper.getReadableDatabase();

        String[] arr_item = new String[29];

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

            Cursor cursor = db_qr_0130.rawQuery("SELECT * FROM Send_table_01", null);

            if (cursor.moveToFirst()) {

                do {


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

                    // グラフ用　総生産数　格納
                    select_souseisan = arr_item[16];

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_15");
                    arr_item[17] = cursor.getString(idx);

                    //----- 作業場所
                    idx = cursor.getColumnIndex("send_col_16");
                    arr_item[18] = cursor.getString(idx);

                    // 最終工程
                    idx = cursor.getColumnIndex("send_col_17");
                    arr_item[19] = cursor.getString(idx);

                    //******** 送信時間 ********
                    sousin_time = "\"" + Send_time_str + "\"";

                    /**
                     *  追加　加工数
                     */
                    idx = cursor.getColumnIndex("send_col_18");
                    arr_item[20] = cursor.getString(idx);

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

                    //******* 開始時間
                    String tmp_time_01 = arr_item[22];

                    /** 0 埋め
                     *        1:9 ,  10:9  , 1:30  などのデータ
                     */

                    //******　「終了時間」
                    idx = cursor.getColumnIndex("send_col_21");
                    arr_item[23] = cursor.getString(idx);

                    // 終了時間
                    String tmp_time_02 = arr_item[23];

                    /** 0 埋め
                     *        1:9 ,  10:9  , 1:30  などのデータ
                     */
                    // ******* send_col_22 取得  Start ***************
                    idx = cursor.getColumnIndex("send_col_22");
                    arr_item[24] = cursor.getString(idx);

                    // 作業番号 ファイルカラム 2
                    String GET_id = arr_item[24];

                    // 作業 DD ファイルカラム 1
                    String ID_DD_01 = GET_id.substring(0,2);

                    // ******* send_col_22 取得  END ***************

                    idx = cursor.getColumnIndex("send_col_23");
                    arr_item[25] = cursor.getString(idx); //***************** ファイル カラム　03

                    //------------ csv_get_file_item に　INSERT 用に　値を格納  ---------
                    for (int i = 0; i < arr_item.length; i++) {
                        csv_get_file_item.add(arr_item[i]);

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
                    String record = ID_DD_01 + "," + GET_id + "," + arr_item[25] + "," + arr_item[3] + "," + arr_item[4] +
                            "," + arr_item[5] + "," + arr_item[6] + "," + arr_item[7] + "," + Himoku_Name + "," + Himoku_Bikou +
                            "," + arr_item[10] + "," + arr_item[11] + "," + arr_item[12] + "," + arr_item[13] + "," + arr_item[14] + "," + arr_item[15] +
                            "," + arr_item[16] + "," + arr_item[17] + "," + arr_item[18] + "," + arr_item[19] + "," +
                            sousin_time + "," + arr_item[20] + "," + saisou_Flg + "," +
                            tmp_time_01 + "," + tmp_time_02 + "," +
                            arr_item[26] + "," + arr_item[27] + "," + arr_item[28];
                       //     tmp_time_01 + "," + tmp_time_02;


                    csv_itme_list.add(record);
                    // ファイルへ　格納
                    printWriter.println(record);
                    printWriter.flush();

                } while (cursor.moveToNext());

                printWriter.close();

            } //------------ END if

            cursor.close();

            StringBuilder stb = new StringBuilder();
            for (int i = 0; i < csv_itme_list.size(); i++) {
                stb.append(csv_itme_list.get(i) + "\n");
                System.out.println("csvデータ出力：：：" + stb);
            }

            System.out.println("csv_count_list：：：");
            for (String a : csv_get_file_item) {
                System.out.println(a);
            }

            //------- テキストビュー　にセットして　表示　テスト
            // test_csv.setText(stb);

            System.out.println("送信ファイル名:::" + send_csv_file_name);

        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(db_qr_0130 != null) {
                db_qr_0130.close();
            }
        }

        /***
         * --------------------- ファイル　作成処理 END ------------------------->
         */

    }


    /***
     *  CSV ファイル　送信　処理
     */
    private void Send_CSV_POST() {

        Read_Send_File_02();

        System.out.println("send_csv_file_name + 送信用ファイル名 2021/01/16::::" + send_csv_file_name);


        /***
         * ---------------------------------------- POST 送信処理　Start ------------
         */

        String jim = "http://192.168.254.87/tana_phppost_file/UploadToServer.php";  // JIM　社内 OK *****
        //  String jim_02 = "http://192.168.254.51/tana_phppost_file/UploadToServer.php";

        /**
         * 　送信　プロパティ
         */
        String uploadURL = JIM_TEST_URL;  //----- 社内
      //  String uploadURL = TOYAMA_DATA_URL; //********** 本番
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

            // ここで　送信フラグを１に変える　アップデート処理
         //   Send_Flg_ON();

            send_log_Stats = "1";

        }  catch (Exception e) {
            send_log_Stats = "2";

            e.printStackTrace();

        }

        /***
         * ---------------------------------------- POST 送信処理　END ------------>
         */
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

    }

    /**
     *  　　Send_table_01　タスク数　取得
     */
    private void Get_Task_Num() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_03 = helper.getReadableDatabase();

        // トランザクション　開始
        db_03.beginTransaction();
        Cursor cursor = null;

        try {

            cursor = db_03.rawQuery("select count(id) from Send_table_01",null);

            // *********** カーソルが　空　じゃなくて、　0 以上
            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    //*************** タスク　数　取得
                    Task_Count_num = cursor.getString(0);

                    System.out.println("タスク数 COUNT::::" + Task_Count_num);

                    // トランザクション　成功
                    db_03.setTransactionSuccessful();

                } else {
                    Task_Count_num = "1";
                }

            } // end if cursor

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            db_03.endTransaction();

            if(db_03 != null) {
                db_03.close();
            }

        }


    }

    /***
     *  ログ用　テーブル　(Send_table_01_log)  インサート
     */
    private void Send_table_01_log_Insert() {

        /**
         *  Get_Task_Num(); タスク数　取得
         */
        Get_Task_Num();

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_0001 = helper.getWritableDatabase();

        // トランザクション　開始
        db_0001.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            String sousinn_time = time_001 + ":" + time_002 + ":" + time_003;

            values.put("log_send_col_01",send_csv_file_name); //　送信ファイル名
            values.put("log_send_col_02",sousinn_time); // 送信時間
            values.put("log_send_col_03",Sousin_log_Flg_QR); // 0: 未送信  1: 送信ok , 2:送信失敗
            values.put("log_send_col_04",Task_Count_num); // タスク数

            db_0001.insert("Send_table_01_log",null, values);

            // トランザクション　成功
            db_0001.setTransactionSuccessful();

            System.out.println("Send_table_01_log インサート成功:::" + send_csv_file_name + ":" + sousin_time + ":" +
                    send_log_Stats + ":" + Task_Count_num);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            // トランザクション　終了
            db_0001.endTransaction();

            if(db_0001 != null) {
                db_0001.close();
            }
        }

    }

    /**
     * 　グラフ　用　総生産　インサート 処理
     */

    private void Send_Grafu_Table_Insert(int g_num) {

        /**
         * 　タスク数　取得
         */
        Get_Task_Num();


        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Send_Grafu_in_db = helper.getWritableDatabase();

        // トランザクション　開始
        Send_Grafu_in_db.beginTransaction();

        try {
            ContentValues values = new ContentValues();


            /**
             * タスク総数をセット
             */
            String tmp = Task_Count_num;

        //    String tmp_send_id = g_num_str_01 + "-" + "0" + tmp_s_01_g;
            String tmp_send_id = g_num_str_01 + "-" + "0" + tmp;

            System.out.println("tmp_send_id ::: グラフインサート:::" +  tmp);
       //     values.put("Send_id", tmp_send_id); // タスク　番号

            //T_ID_Str   作業番号　 *** 翌日　対応  dd-01 , dd-02
            values.put("Send_id", T_ID_Str); // タスク　番号

            //******** グラフの最初の値を格納する ********

                switch (HH_hikaku_01_i) {
                    case 8:
                        values.put("Sou_Num_01", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_01 インサート完了");
                        System.out.println("Sou_Num_01 インサート完了");
                        break;

                    case 9:
                        values.put("Sou_Num_02", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_02 インサート完了");
                        System.out.println("Sou_Num_02 インサート完了");
                        break;

                    case 10:
                        values.put("Sou_Num_03", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_03 インサート完了");
                        System.out.println("Sou_Num_03 インサート完了");
                        break;

                    case 11:
                        values.put("Sou_Num_04", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_04 インサート完了");
                        System.out.println("Sou_Num_04 インサート完了");
                        break;

                    case 12:
                        values.put("Sou_Num_05", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_05 インサート完了");
                        System.out.println("Sou_Num_05 インサート完了");
                        break;

                    case 13:
                        values.put("Sou_Num_06", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_06 インサート完了");
                        System.out.println("Sou_Num_06 インサート完了");
                        break;

                    case 14:
                        values.put("Sou_Num_07", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_07 インサート完了");
                        System.out.println("Sou_Num_07 インサート完了");
                        break;

                    case 15:
                        values.put("Sou_Num_08", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_08 インサート完了");
                        System.out.println("Sou_Num_08 インサート完了");
                        break;

                    case 16:
                        values.put("Sou_Num_09", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_09 インサート完了");
                        System.out.println("Sou_Num_09 インサート完了");
                        break;

                    case 17:
                        values.put("Sou_Num_10", g_num);// 総生産数 Integer 型
                        Log.v("インサート完了:::", "Sou_Num_10 インサート完了");
                        System.out.println("Sou_Num_10 インサート完了");
                        break;
            }

            Send_Grafu_in_db.insert("Send_Grafu_Table", null,values);
            // トランザクション　成功
            Send_Grafu_in_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            Send_Grafu_in_db.endTransaction();

            if(Send_Grafu_in_db != null) {
                Send_Grafu_in_db.close();
            }

        }

    } // ============== END  function

    /**
     *  Send_Table の id の max 値　を　取得
     */
    private void Send_TB_id_max() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Send_TB_id_max_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Send_TB_id_max_db.rawQuery("SELECT max(id) from Send_table_01", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Send_Table_id_max = cursor.getInt(0);

                    if(Send_Table_id_max == 0) {
                        Send_Table_id_max = 1;
                    } else {
                        Send_Table_id_max += 1;
                    }

                    System.out.println("Send_Table_id_max::: 関数内::" + Send_Table_id_max);
                }

            } else {
                Send_Table_id_max = 1;
                System.out.println("Send_Table_id_max::: 関数内 else ::" + Send_Table_id_max);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Send_TB_id_max_db != null) {
                Send_TB_id_max_db.close();
            }
        }


    } //==================== END function


    /***
     *  ログ用　テーブル　(Send_table_01_log)  インサート
     */
    private void worker_Send_table_01_log_Insert() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_0001_2 = helper.getWritableDatabase();

        // トランザクション　開始
        db_0001_2.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            values.put("log_send_col_01",send_csv_file_name); //　送信ファイル名
            values.put("log_send_col_02",sousin_time); // 送信時間
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
    private void worker_Get_Task_Num() {

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
     * ---------------- 送信　フラグを　１ する　Update -----------------
     *
     */
    public void worker_Send_Flg_ON() {

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

    /**
     * 文字列前後のダブルクォーテーションを削除するFunction
     * @param str 文字列
     * @return 前後のダブルクォーテーションを削除した文字列
     */
    public static String trimDoubleQuot(String str) {
        char c = '"';
        if(str.charAt(0) == c && str.charAt(str.length()-1) == c) {
            return str.substring(1, str.length()-1);
        }else {
            return str;
        }
    }

    /**
     *  　　Send_table_01　日付またぐ用　の　ID 振り分け  DD-01 , DD-02 ,  01 , 02  に count + 1 を入れる
     */
    private void DD_Task_GET_Num() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase DD_Task_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {
            String tmp = Saghou_Code + "%";
            String[] arr_select = {tmp};

            cursor = DD_Task_01.rawQuery("select count(send_col_22) from Send_table_01 " +
                    "where send_col_22 like ?", arr_select);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    //****** send_col_22　に dd が　無いか　検索して なかった場合は 1 , あった場合は + 1 する。
                    DD_id = cursor.getString(0);
                    DD_id_i = Integer.parseInt(DD_id);

                    if(DD_id_i == 0) {
                        DD_id_i = 1;

                    } else {
                        DD_id_i += 1;
                    }

                }

            } else {
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(DD_Task_01 != null) {
                DD_Task_01.close();
            }
        }


    } // *************** END function


    /***
     *
     *     Data_zan を　0 => 1  に　変える  ::: 定期送信用　フラグ
     *
     */
    private void Teiki_Send_Flg() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Teiki_Send_Flg_db_01 = helper.getWritableDatabase();

        // トランザクション開始
        Teiki_Send_Flg_db_01.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            // ******* Data_zan　1 に　する *******

            int On = 1;
            values.put("Data_zan", On);

            Teiki_Send_Flg_db_01.update("Flg_Table",values,null,null);

            Teiki_Send_Flg_db_01.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Teiki_Send_Flg_db_01.endTransaction();

            if(Teiki_Send_Flg_db_01 != null) {
                Teiki_Send_Flg_db_01.close();
            }

        }

    } //----------------- END Syuuzitu_Flg_Update



    /**
     *  　　Send_table_01　日付またぐ用　の　ID 振り分け  DD-01 , DD-02 ,  01 , 02  に max  + 1 を入れる
     */
    private void DD_Task_GET_Num_Max() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase DD_Task_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {
            String tmp = dd_num + "%";
            String[] arr_select = {tmp};

            cursor = DD_Task_01.rawQuery("select max(send_col_22) from Send_table_01 " +
                    "where send_col_22 like ?", arr_select);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    //****** send_col_22　に dd が　無いか　検索して なかった場合は 1 , あった場合は + 1 する。
                    DD_id = cursor.getString(0);

                    //*******  ID　日付　またぎ処理　&  途中で キーが　抜けた時の処理 & 10 桁以上の処理 **********
                    if(DD_id == null) {
                        DD_id_i = 0;
                        System.out.println("DD_id_i:::" + DD_id_i);
                    } else if(DD_id.length() == 6) {

                        String tmp_str_max = DD_id.substring(4,6);
                        DD_id_i = Integer.parseInt(tmp_str_max);
                        System.out.println("DD_id_i:::" + DD_id_i);
                    } else if(DD_id.length() == 5) {
                        //********* 30-01 => 2   30-010 => 11
                        String tmp_str_max = DD_id.substring(3,5);
                        DD_id_i = Integer.parseInt(tmp_str_max);
                        System.out.println("DD_id_i:::" + DD_id_i);
                    }

                    if(DD_id_i == 0) { //********** カウントアップ　処理
                        DD_id_i = 1;   // 29-00 =>   1  , 30-00 => 1

                        System.out.println("DD_id_i 下:::" + DD_id_i);
                    } else {
                        DD_id_i++;   // 29-01 =>  2  ,  30-10 => 11

                        System.out.println("DD_id_i　下:::" + DD_id_i);
                    } //************　カウントアップ　処理 END

                }

            } else {
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(DD_Task_01 != null) {
                DD_Task_01.close();
            }
        }


    } // *************** END function

    private void TousouKa_Edit_TextHandle(EditText ok_e1, EditText no1, EditText no2, EditText no3, EditText no4,
                                EditText no5, EditText no6 ) {

        //********** フォーカス OK
        ok_e1.setFocusable(true);
        ok_e1.setFocusableInTouchMode(true);
        ok_e1.setEnabled(true);

        //********** フォーカス　NG
        no1.setFocusable(false);
        no1.setFocusableInTouchMode(false);
        no1.setEnabled(false);

        no2.setFocusable(false);
        no2.setFocusableInTouchMode(false);
        no2.setEnabled(false);

        no3.setFocusable(false);
        no3.setFocusableInTouchMode(false);
        no3.setEnabled(false);

        no4.setFocusable(false);
        no4.setFocusableInTouchMode(false);
        no4.setEnabled(false);

        no5.setFocusable(false);
        no5.setFocusableInTouchMode(false);
        no5.setEnabled(false);

        no6.setFocusable(false);
        no6.setFocusableInTouchMode(false);
        no6.setEnabled(false);


    } //************************* END function ********************


    /***
     *    作業コード 頭　dd 取得
     */
    private void Sagyou_new_ID_SELECT () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Sagyou_new_ID_SELECT = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Sagyou_new_ID_SELECT.rawQuery("select create_date from Flg_Table", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {


                    //************ 作業コードの　頭を　取得  yyyyMMdd
                    Create_Date_NUM = cursor.getString(0);
                }

            } else {
                return;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }

    } // ---------------- END function   Sagyou_new_ID_SELECT


    /**
     *    //**************** 樹脂成型課　用
     */
    private void Allahto_Dailog_Zyushi_Seikei () {

        //******************** オリジナルアラートログの表示 処理 開始  ********************//
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View bilde_layout_04 = inflater.inflate(R.layout.dialog_04, (ViewGroup) findViewById(R.id.alertdialog_layout_04));

        //*********** コンポーネント　初期化

        d04_label_01 = bilde_layout_04.findViewById(R.id.d04_label_01); // ***** ラベル
        d04_label_02 = bilde_layout_04.findViewById(R.id.d04_label_02); // ***** ラベル

        iro_input = bilde_layout_04.findViewById(R.id.iro_input); // 色段取時間 エディットテキスト
        kata_input = bilde_layout_04.findViewById(R.id.kata_input); // 型段取時間 エディットテキスト
        kikai_input = bilde_layout_04.findViewById(R.id.kikai_input); // 機械コード　エディットテキスト

        iro_input.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字
        kata_input.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字
        kikai_input.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字

        dia_4_touroku_btn_001 = bilde_layout_04.findViewById(R.id.dia_4_touroku_btn_001); // 保存　ボタン
        dia_4_touroku_btn_002 = bilde_layout_04.findViewById(R.id.dia_4_touroku_btn_002); // 保存　ボタン


        //--------------- アラートダイヤログ タイトル　設定 ---------------//
        AlertDialog.Builder bilder = new AlertDialog.Builder(QR_Barcode_Read.this);
        // タイトル
        TextView titleView;
        titleView = new TextView(QR_Barcode_Read.this);
        titleView.setText("樹脂成型課 用 入力");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.colorPinku));
        titleView.setPadding(20, 30, 20, 30);
        titleView.setGravity(Gravity.CENTER);

        // ダイアログに　「タイトル」　を　セット
        bilder.setCustomTitle(titleView);

        // カスタムレイアウト　を　セット
        bilder.setView(bilde_layout_04);

        AlertDialog dialog = bilder.create();
        dialog.show();


        /**
         *  「色段取時間」
         */
        iro_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    //***** エラー　空処理
                    if(iro_input.getText().toString().equals("") == false) {
                        iro_input_tmp = iro_input.getText().toString();
                    }

                }

                return false;
            }
        });


        /**
         *  「型段取時間」
         */
        kata_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    kata_input_tmp = kata_input.getText().toString();
                }

                return false;
            }
        });

        /**
         *  「機械コード」
         */
        kikai_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    kikai_input_tmp = kikai_input.getText().toString();
                }

                return false;
            }
        });

        /**
         *   「保存」　ボタン
         */
        dia_4_touroku_btn_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iro_input_tmp = iro_input.getText().toString(); // 色段取時間
                kata_input_tmp = kata_input.getText().toString(); // 型段取時間
                kikai_input_tmp = kikai_input.getText().toString(); // 機械コード

                //*** 色段取時間セット
                if(iro_input_tmp.length() != 0) {
                    iro_z_edit.setText(iro_input_tmp);
                } else {
                    iro_z_edit.setText("0");
                }

                //*** 型段取時間セット
                if(kata_input_tmp.length() != 0) {
                    Kata_z_edit.setText(kata_input_tmp);
                } else {
                    Kata_z_edit.setText("0");
                }

                //*** 機械コードセット
                if(kikai_input_tmp.length() != 0) {
                    Kikai_z_input.setText(kikai_input_tmp);
                } else {
                    Kikai_z_input.setText("0");
                }

                //******* ダイアログ　非表示
                dialog.dismiss();

            }
        });

        /**
         *   「キャンセル」　ボタン
         */
        dia_4_touroku_btn_002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //************* キャンセル
                dialog.dismiss();

            }
        });



    } //******************** END function


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