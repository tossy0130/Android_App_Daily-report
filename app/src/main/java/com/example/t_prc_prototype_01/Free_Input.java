package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Free_Input extends AppCompatActivity {


    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/UploadToServer.php";

    private EditText free_input_edit, free_input_time_edit;
    private TextView free_input_time_view;
    private MaterialButton free_back_btn;
    private MaterialButton free_save_btn;

    public static String Sousin_log_Flg_Fr = "0";

    //------- Work_Choice.java からの値を受け取り用  getIntent()
    private String get_TMNF_01, get_TMNF_02,dia_edit_01_num,i_dia_chack_01_num,
            ch_user_name_num,ch_busyo_view_Hon_num,ch_busyo_view_num,dia_user_key,dia_user_key_str;

    //------- 作業番号
    private String g_num_str_01;
    private String tmp_s_01;

    // 作業コード用  dd が　入る
    private String Saghou_Code;

    //    // 現在時刻　取得
    private String Sagyou_yymmdd;

    //------------ 送信 Flg
    private String saisou_Flg;
    private String Send_time_str;

    //------------ 送信 Flg
    public static String send_csv_file_name;
    private ArrayList<String> csv_itme_list = new ArrayList<>();
    private ArrayList<String> csv_get_file_item = new ArrayList<>();
    private String csv_file_name_01, csv_file_name_02;

    // 作業番号用  yyyymm
    private String Sagyou_date;

    // インサート用　送信スタッツ
    private String send_log_Stats;

    //------------- CSV ファイル作成 & ファイル　送信用 ------------
    private File target_csv_file;

    private String time_001, time_002, time_003;

    // インサート用　タスク　count 数
    private String Task_Count_num;

    //****** 追加　開始時間 & 終了時間
    private Button free_start_time,free_end_time;
    private TextView free_start_time_view,free_end_time_view;

    private int Send_Table_id_max;

    //********* 開始時間　終了時間　計算結果用
    private int time_01_a,time_01_b,time_02_a,time_02_b;

    // Send_Table インサート用　グラフ ID
    private String G_id_str;

    private String get_Busho_C;

    private String Sagyou_B;

    private String DD_id,T_ID_Str;
    private int DD_id_i;

    private  String free_time_str;

    //********** ファイル ID 作成用
    private String Create_Date_NUM,yy_num,dd_num;

    /**
     *   作業部署（ファイル送信用） 追加 2021/01/16
     */
    private String Sagyou_Get_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free__input);

        // XML コンポーネント初期化
        init();

        if(getIntent() != null) {

            get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
            get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");

            dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");
            i_dia_chack_01_num = getIntent().getStringExtra("i_dia_chack_01_num");

            /**
             * 確実 data
             */

            ch_user_name_num = getIntent().getStringExtra("ch_user_name_num");
            //------ 部署名
            ch_busyo_view_Hon_num = getIntent().getStringExtra("ch_busyo_view_Hon_num");
            //------ 作業部署名
            ch_busyo_view_num = getIntent().getStringExtra("ch_busyo_view_num");
            dia_user_key = getIntent().getStringExtra("dia_user_key");

            System.out.println(get_TMNF_01 + get_TMNF_02 + dia_edit_01_num + i_dia_chack_01_num +
                    ch_user_name_num + ch_busyo_view_Hon_num + ch_busyo_view_num + dia_user_key);

            System.out.println("get_TMNF_01,get_TMNF_02,dia_edit_01_num,i_dia_chack_01_num" +
                    "ch_user_name_num,ch_busyo_view_Hon_num,ch_busyo_view_num,dia_user_key");

            /**
             * 部署コード
             */

            // ******　エディットテキストで選んだ　「作業用 部署コード」
            dia_user_key_str = getIntent().getStringExtra("dia_user_key_str_GO");

            get_Busho_C = getIntent().getStringExtra("SAGYOU_Busyo_Code_SELECT_01_str");

            Sagyou_B = getIntent().getStringExtra("Sagyou_B");

        }

        /**
         *  インサート用　作業番号
         */

        //------------ 現在時刻　取得
        String time_str = getNowDate();

        // DB 格納用
        Saghou_Code = time_str.substring(6, 8); // dd 取得
        Sagyou_date = time_str.substring(0, 6); // yyyymm 取得
        Sagyou_yymmdd = time_str.substring(0, 8); // yyyymmdd


        csv_file_name_01 = time_str.substring(0, 8); // yyyymmdd 取得
        csv_file_name_02 = time_str.substring(8, 14); // HHmmss

        //------ 送信時間作成
        String year = time_str.substring(0, 4); // 年
        String month = time_str.substring(4, 6); // 月

        String hour = time_str.substring(8, 10); //  １時間
        String minute = time_str.substring(10, 12); //　１分
        String second = time_str.substring(12, 14); // 1秒

        //********** 時間取得
        time_001 = time_str.substring(8,10);
        time_002 = time_str.substring(10,12);
        time_003 = time_str.substring(12,14);

        // グラフインサート用
        g_num_str_01 = time_str.substring(6,8);

        // グラフインサート用
        g_num_str_01 = time_str.substring(6,8);
        // DB 格納用
        Saghou_Code = time_str.substring(6, 8); // dd 取得

        Sagyou_yymmdd = time_str.substring(0, 8); // yyyymmdd

        //------ 送信時間
        Send_time_str = year + "/" + month + "/" + Saghou_Code + " " + hour + ":" + minute + ":" + second;
        System.out.println(Send_time_str);
        // ＊＊＊＊＊  CSV 送信ファイル名　作成 　＊＊＊＊＊　ANDROID の前に　作業者コード　が入る。
       // send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_TMNF_01 + ".csv";
       //  send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + ".csv";

        /**
         *  ******** 開始時間　追加 ********
         */
        free_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(Free_Input.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        time_01_a = hourOfDay;
                        time_01_b = minute;

                        free_start_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);

                timePickerDialog.show();

            }
        }); //********************* END free_start_time 開始時間 *************

        /**
         *  ******** 終了時間 ********
         */
        free_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(Free_Input.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        free_end_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
                        time_02_a = hourOfDay;
                        time_02_b = minute;


                        //*** 開始時間  分に　変換
                        int hour_01_sum = time_01_a * 60 + time_01_b; //*** 開始時間
                        int hour_02_sum = time_02_a * 60 + time_02_b; //*** 終了時間

                        int time_sum = hour_02_sum - hour_01_sum;

                        //********* 計算結果を表示 *********
                        free_input_time_edit.setText(String.valueOf(time_sum));

                    }
                }, hour, minute, true);

                timePickerDialog.show();

            }
        });




        /**
         * ------------------ 作業時間　入力　テキストエディット　ソフトキーボードの「決定ボタン」
         */
        free_input_time_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    // ########### ソフトキーボードを非表示
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    if(free_input_time_edit.getText().toString().equals("")) {

                        toastMake("「作業時間」を入力してください。", -200, 0);
                        return false;

                    } else {
                        String tmp_str = free_input_time_edit.getText().toString();
                        int tmp_i = Integer.parseInt(tmp_str);

                        if(tmp_i > 480) {
                            // ************* アラートで　「値」　確認
                            Allahto_Dailog_02_error();
                        } else {

                            // 作業時間（エディットテキスト）　の　値を　格納
                            free_time_str = free_input_time_edit.getText().toString();
                            return true;
                        }

                    }

                }



                return false;
            }
        });


        /**
         *  「保存」　ボタン　を　タップした処理
         */
        free_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(free_input_edit.getText().toString().equals("")  ||
                        free_input_time_edit.getText().toString().equals("")) {

                    toastMake("「入力項目」が「空」です。", 0,-200);
                    return;

                } else {

                    //*********** エラー マイナス時間処理
                    String free_input_time_edit_s = free_input_time_edit.getText().toString();
                    int free_input_time_edit_i = Integer.parseInt(free_input_time_edit_s);

                    if(free_input_time_edit_i < 0) {

                        toastMake("「作業時間」にマイナスの値が入力されてます。値を修正してください。", 0,-200);
                        free_input_time_edit.setText("");
                        return;
                    }

                    //--------------- アラートダイアログ　の表示　開始 ---------------------*/
                    AlertDialog.Builder bilder = new AlertDialog.Builder(Free_Input.this);

                    //-------------- カスタムタイトル　作成
                    TextView titleView;
                    titleView = new TextView(Free_Input.this);
                    titleView.setText("データを保存しますか？");
                    titleView.setTextSize(22);
                    titleView.setTextColor(Color.WHITE);
                    titleView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    titleView.setPadding(20, 30, 20, 30);
                    titleView.setGravity(Gravity.CENTER);
                    //-------------- カスタムタイトル　作成 END
                    // ダイアログの項目
                    bilder.setCustomTitle(titleView);

                    //-------------- ダイアログ　メッセージ内容
                    String msg_bilder = "入力内容を「保存」しますか？";
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

                            /***
                             *   インサート処理　実行
                             */

                            Insert_Send_03_save(); // インサート クラス　function
                          //  Free_input_Insert_03(); // インサート
                            Read_Send_File_02(); // CSV ファイル作成
                            Send_CSV_POST(); // ファイル送信
                            Send_Grafu_Table_Insert(); // グラフ用インサート実行
                            Teiki_Send_Flg(); // ********* 定期送信　フラグを ok に　する。

                            System.out.println("インサート成功");
                            tostMake("「保存」が完了しました。", -200,0);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // 0.5 秒後に　activity　終了
                                    finish();
                                }
                            },400);

                            Send_table_01_log_Insert();

                        }
                    });

                    AlertDialog dialog = bilder.create();
                    dialog.show();

                    //******************************************* ボタン　配色　変更
                    //********* ボタン はい **********
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#0092CA"));
                    //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                    //********* ボタン いいえ **********
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0092CA"));
                    //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

                    //******************************************* END ボタン　配色　変更

                }

            }
        });


        /**
         * 　「戻る」　ボタン　をタップした処理
         */
        free_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // エディットテキスト　free_input_edit, free_input_time_edit が「空」じゃなかった場合
                if(free_input_edit.getText().toString().equals("") == false ||
                        free_input_time_edit.getText().toString().equals("") == false) {

                    //--------------- アラートダイアログ　の表示　開始 ---------------------*/
                    AlertDialog.Builder bilder = new AlertDialog.Builder(Free_Input.this);

                    //-------------- カスタムタイトル　作成
                    TextView titleView;
                    titleView = new TextView(Free_Input.this);
                    titleView.setText("入力内容が破棄されます！？");
                    titleView.setTextSize(22);
                    titleView.setTextColor(Color.WHITE);
                    titleView.setBackgroundColor(getResources().getColor(R.color.red));
                    titleView.setPadding(20, 30, 20, 30);
                    titleView.setGravity(Gravity.CENTER);
                    //-------------- カスタムタイトル　作成 END
                    // ダイアログの項目
                    bilder.setCustomTitle(titleView);

                    //-------------- ダイアログ　メッセージ内容
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
                    // エディットテキストの「値」が空だった場合
                    finish();
                }

            }
        });

    } //------------------------------------------ END onCreate

    /**
     *   トーストメッセージ　表示
     */
    private void toastMake(String msg, int x, int y) {

        Toast toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER, x,y);
        toast.show();

    }

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
     * 端末 「Back Btn」 を押した処理
     */
    @Override
    public void onBackPressed() {

        // エディットテキスト　free_input_edit, free_input_time_edit が「空」じゃなかった場合
        if(free_input_edit.getText().toString().equals("") == false ||
                free_input_time_edit.getText().toString().equals("") == false) {

            //--------------- アラートダイアログ　の表示　開始 ---------------------*/
            AlertDialog.Builder bilder = new AlertDialog.Builder(Free_Input.this);

            //-------------- カスタムタイトル　作成
            TextView titleView;
            titleView = new TextView(Free_Input.this);
            titleView.setText("入力内容が破棄されます！？");
            titleView.setTextSize(22);
            titleView.setTextColor(Color.WHITE);
            titleView.setBackgroundColor(getResources().getColor(R.color.red));
            titleView.setPadding(20, 30, 20, 30);
            titleView.setGravity(Gravity.CENTER);
            //-------------- カスタムタイトル　作成 END
            // ダイアログの項目
            bilder.setCustomTitle(titleView);

            //-------------- ダイアログ　メッセージ内容
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
            // エディットテキストの「値」が空だった場合
            finish();
        }

    } //-------------- onBackPressed END

    /***
     *   Send_table_01 へ　インサート
     */
    private void Free_input_Insert_03() {

        /**
         *  Send_Table max 取得  グラフ id , カラム 22
         */
        Send_TB_id_max();
        String tmp_id_str = String.valueOf(Send_Table_id_max);


        /***
         *    select count(send_col_22)  => 日付　またぎ　対応 ID
         */
        //************* 変動用　id
    //    DD_Task_GET_Num();
    //    String Id_tmp = String.valueOf(DD_id_i);

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Free_input_db_01 = helper.getWritableDatabase();

        // トランザクション　開始
        Free_input_db_01.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            /**
             *  インサート　項目
             *  1: Send_id* 2: 担当者C* 3: 部署C* 4: 現品票C* 5: 品目K
             * 6: 品目C * 7: 品目名* 8: 品備考* 9: 作業場所* 10:段取時間
             * 11:作業時間* 12:予定数量* 13:開始数量* 14:終了数量* 15:総生産数
             * 16:不良品数* 17:良品数* 18:最終工程 19: 加工数  20: 再送フラグ
             */
            //************** インサート用 データ　作成 ***************:
            // free_input_edit => 自由入力　テキスト　取得
            String free_input_edit_str = free_input_edit.getText().toString();
            // 再送フラグ 基本 0
            String saisou_num = "0";
            // 作業時間　取得
            String free_input_time_edit_str = free_input_time_edit.getText().toString();

            //************* END ******************

            // 担当者C : 2
            values.put("send_col_01", get_TMNF_01);
            //  部署C : 3
            values.put("send_col_02", Sagyou_B);
            //　現品票C : 4
            values.put("send_col_03", "");
            // 品目K : 5
            values.put("send_col_04", "");
            // 品目C : 6
            values.put("send_col_05", ""); // 判別用　品目コード 0000

            // 品目名 : 7  free_input_edit => 自由入力　テキスト　取得
            String free_input_edit_tmp = free_input_edit.getText().toString();
            String free_input_edit_str_t = free_input_edit_tmp.trim();
            String hinmei_IN = free_input_edit_str_t.replaceAll("　", "");

            values.put("send_col_06",hinmei_IN);
            // 品備考 : 8
            values.put("send_col_07", "");
            // 作業場所 : 9
            values.put("send_col_08", "");
            // 段取時間 : 10
            values.put("send_col_09", "0");

            //****** 作業時間 : 11 ******
            String free_time_str_tmp = free_input_time_edit.getText().toString();

            values.put("send_col_10", free_time_str_tmp);
            // 予定数量 : 12
            values.put("send_col_11", "");
            // 開始数量 : 13
            values.put("send_col_12", "");
            // 終了数量 : 14
            values.put("send_col_13", "");
            // 総生産数 : 15
            values.put("send_col_14", "");
            // 不良品数 : 16
            values.put("send_col_15", "");
            // 良品数 :  17
            values.put("send_col_16", "");
            // 最終工程
            values.put("send_col_17", "");
            /**
             *  加工数
             */
            // 加工数
            values.put("send_col_18","");
            // 再送フラグ
            values.put("send_col_19",saisou_num);

            //******************* 追加 *******************
            // 開始時間
            String free_start_time_view_in = free_start_time_view.getText().toString();
            values.put("send_col_20", free_start_time_view_in);

            // 終了時間
            String end_time_view_in = free_end_time_view.getText().toString();
            values.put("send_col_21", end_time_view_in);

            // フラフ用　id
            String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用

            //********  dd-0 Send_Table の　max id + 1  or  1
            G_id_str = Saghou_Code_num + "-" + "0" + tmp_id_str;

            //*********** 変動用　ID
         //   T_ID_Str = Saghou_Code_num + "-" + "0" + Id_tmp;

            //************  変動用 id (max) **************************************
            DD_Task_GET_Num_Max();
            //*********** 変動用　ID (max) max(send_col_22) from Send_table_01 where send_col_22 like '26%';

            String Id_tmp_max;
            //******** １　～　９の時は　０をつける
            if(DD_id_i > 0 && DD_id_i < 10) {

                Id_tmp_max  = String.valueOf(DD_id_i);
                T_ID_Str = Saghou_Code_num + "-" + "0" + Id_tmp_max;

            } else {

                //********** １０以上は　０　をつけない
                Id_tmp_max = String.valueOf(DD_id_i);
                T_ID_Str = Saghou_Code_num + "-" + Id_tmp_max;

            }
            System.out.println("Id_tmp:::値:::" + Id_tmp_max);
            //************  変動用 id (max) ************************************** END

            //******* グラフ用　ID  &  作業番号  &  csv ファイル項目 1  挿入
            values.put("send_col_22", T_ID_Str);
            System.out.println("T_ID_Str:::インサート時" + T_ID_Str);

            //******* yyyymmmdd   csv ファイル　項目３
            values.put("send_col_23", Sagyou_yymmdd);

            //******************************************************* END インサート用　データ

            //********** インサート処理 **********
            Free_input_db_01.insert("Send_table_01", null, values);

            // トランザクション成功
            Free_input_db_01.setTransactionSuccessful();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            Free_input_db_01.endTransaction();

            if(Free_input_db_01 != null) {
                Free_input_db_01.close();
            }

        }

    }


    /***
     *  CSV ファイル　送信　処理
     */
    private void Send_CSV_POST() {

        /***
         * ---------------------------------------- POST 送信処理　Start ------------
         */

        String jim = "http://192.168.254.87/tana_phppost_file/UploadToServer.php";  // JIM　社内 OK *****
        //  String jim_02 = "http://192.168.254.51/tana_phppost_file/UploadToServer.php";




        /**
         * 　送信　プロパティ
         */
        String uploadURL = JIM_TEST_URL;  //------- 社内
    //   String uploadURL = TOTAMA_URL;  // *********** 本番
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
        //    Send_Flg_ON();

            send_log_Stats = "1";

        }  catch (Exception e) {
            e.printStackTrace();

            send_log_Stats = "2";
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
        SQLiteDatabase db_002 = helper.getWritableDatabase();

        try {

            // トランザクション　開始
            //  db_001.setTransactionSuccessful();

            String send_Flg = "1";
            String send_Flg_zero = "0";
            ContentValues values = new ContentValues();
            values.put("send_col_19",send_Flg);

            //データベースを更新
            db_002.update("Send_table_01", values,"send_col_19 = ?",new String[]{"0"});

            //　トランザクション　成功
            //     db_001.beginTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            //     db_001.endTransaction();

            if(db_002 != null) {
                db_002.close();
            }

            System.out.println("フラグ 1　変更 OK");
            Log.d("Send_CSV_01.java ログ", "送信フラグ 1 変更　OK");
        }

    } //=============== END function

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


    private void init() {

        // 自由入力　用
        free_input_edit = findViewById(R.id.free_input_edit);
        // 作業時間　入力用
        free_input_time_edit = findViewById(R.id.free_input_time_edit);
        // 数字入力
        free_input_time_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 作業時間表示　ビュー
       // free_input_time_view = findViewById(R.id.free_input_time_view);

        // 保存、 戻るボタン
        free_back_btn = (MaterialButton) findViewById(R.id.free_back_btn);
        free_save_btn = (MaterialButton) findViewById(R.id.free_save_btn);

        //***** 追加　開始時間 & 終了時間
        free_start_time_view = findViewById(R.id.free_start_time_view);
        free_end_time_view = findViewById(R.id.free_end_time_view);

        free_start_time = findViewById(R.id.free_start_time);
        free_end_time = findViewById(R.id.free_end_time);


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
        SQLiteDatabase db = helper.getReadableDatabase();

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

            Cursor cursor = db.rawQuery("SELECT * FROM Send_table_01", null);

            if (cursor.moveToFirst()) {

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

                    //******* 開始時間
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

                    //****** 終了時間
                    String tmp_time_02 = arr_item[23];


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

                    //------------ csv_get_file_item に　INSERT 用に　値を格納  ---------
                    for (int i = 0; i < arr_item.length; i++) {
                        csv_get_file_item.add(arr_item[i]);
                        ;
                    }

                    csv_get_file_item.add(Send_time_str);
                    csv_get_file_item.add(saisou_Flg);
                    //------------ csv_get_file_item に　INSERT 用に　値を格納  END -------

                    /**
                     *   CSV ファイルへ　値を格納
                     *
                     *   ID_DD_01 => dd  ,  GET_id => dd-01 ,dd-02
                     *
                     */


                    /**
                     *  「樹脂成型課用」　追加
                     */
                    idx = cursor.getColumnIndex("send_col_24");
                    arr_item[26] = cursor.getString(idx); //***************** 色段取時間

                    idx = cursor.getColumnIndex("send_col_25");
                    arr_item[27] = cursor.getString(idx); //*****************　型段取時間

                    idx = cursor.getColumnIndex("send_col_26");
                    arr_item[28] = cursor.getString(idx); //*****************  機械コード

                    String record = ID_DD_01 + "," + GET_id + "," + arr_item[25] + "," + arr_item[3] + "," + arr_item[4] +
                            "," + arr_item[5] + "," + arr_item[6] + "," + arr_item[7] + "," + Himoku_Name + "," + arr_item[9] +
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
        }

        /***
         * --------------------- ファイル　作成処理 END ------------------------->
         */
    }



    /**
     * 　グラフ　用　総生産　インサート 処理
     */
    private void Send_Grafu_Table_Insert() {


        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Send_Grafu_in_db = helper.getWritableDatabase();

        // トランザクション　開始
        Send_Grafu_in_db.beginTransaction();

        try {
            ContentValues values = new ContentValues();

            // 総生産数

            /*
            String tmp_send_id = g_num_str_01 + "-" + "0" + tmp_s_01;
            System.out.println("tmp_send_id ::: グラフインサート" +  tmp_send_id);
            values.put("Send_id", tmp_send_id); // タスク　番号
             */

            // T_ID_Str  作業番号 dd-01 , dd-02 , 日付またぎ対応
            values.put("Send_id", T_ID_Str); // タスク　番号

            values.put("Sou_Num_01", 0);// 総生産数 Integer 型
            values.put("Sou_Num_02", 0);// 総生産数 Integer
            values.put("Sou_Num_03", 0);// 総生産数 Integer 型
            values.put("Sou_Num_04", 0);// 総生産数 Integer 型
            values.put("Sou_Num_05", 0);// 総生産数 Integer 型
            values.put("Sou_Num_06", 0);// 総生産数 Integer 型
            values.put("Sou_Num_07", 0);// 総生産数 Integer 型
            values.put("Sou_Num_08", 0);// 総生産数 Integer 型
            values.put("Sou_Num_09", 0);// 総生産数 Integer 型
            values.put("Sou_Num_10", 0);// 総生産数 Integer 型


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

    } //*********************************** END function

    /***
     *  ログ用　テーブル　(Send_table_01_log)  インサート
     */
    private void Send_table_01_log_Insert() {

        worker_Get_Task_Num();

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_0001 = helper.getWritableDatabase();

        // トランザクション　開始
        db_0001.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            String sousinn_time = time_001 + ":" + time_002 + ":" + time_003;

            values.put("log_send_col_01",send_csv_file_name); //　送信ファイル名
            values.put("log_send_col_02",sousinn_time); // 送信時間
            values.put("log_send_col_03",Sousin_log_Flg_Fr); // 送信スタッツ 0: 未送信, 1: 送信ok , 2:送信失敗
            values.put("log_send_col_04",Task_Count_num); // タスク数

            db_0001.insert("Send_table_01_log",null, values);

            // トランザクション　成功
            db_0001.setTransactionSuccessful();

            System.out.println("Send_table_01_log インサート成功:::" + send_csv_file_name + ":" + sousinn_time + ":" +
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
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_02_error() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Free_Input.this);
        titleView.setText("入力された値が「480」以上の値です。");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.red));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(Free_Input.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        //------- メッセージセット
        bilder.setMessage("作業時間の合計が「480分」を超えています。大丈夫ですか？");

        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                free_input_time_edit.setText("0");

                toastMake("もう一度「値」を入力してください。", 0,-200);
                return;
            }
        });


        android.app.AlertDialog dialog = bilder.create();
        dialog.show();

        //******************************************* ボタン　配色　変更
        //********* ボタン はい **********
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B20000"));
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //********* ボタン いいえ **********
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B20000"));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //******************************************* END ボタン　配色　変更


    } //-------------- Allahto_Dailog_01  END


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

    private void tostMake(String msg, int x, int y) {
        Toast toast = Toast.makeText(this,msg, Toast.LENGTH_LONG);

        // 表示位置　調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    } //----------------- END tostMake


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

    /**
     *  インサート　処理
     */
    private void Insert_Send_03_save() {


        /**
         *   ログイン時　作業日　から　取得  yyyyMMdd
         */
        Sagyou_new_ID_SELECT();

        String date_num = Create_Date_NUM; // yyyyMMdd
        yy_num = date_num.substring(0,4); // yyyy
        dd_num = date_num.substring(6,8); // dd , dd-01 , dd-02  ,  Saghou_Code_num と　変更


        /***
         * ------------------- INSERT 用　データ -----------------------
         */
        String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用
        String Sagyou_date_num = Sagyou_date; // yyyymm 取得  => 作業日　用

        //------- 品名
        // 品目名 : 7  free_input_edit => 自由入力　テキスト　取得

        String free_input_edit_tmp = free_input_edit.getText().toString();
        String free_input_edit_str_t = free_input_edit_tmp.trim();

        System.out.println(free_input_edit_str_t);

        String hinmei = free_input_edit_str_t.replaceAll("\n", "");
        System.out.println(hinmei);
        //------- END 品名

        //------ 作業時間
        String Sagyou_Time = free_input_time_edit.getText().toString();
        System.out.println(Sagyou_Time);

        // 最終チェック　0 にする
        String last_check = "0";

        String kakou_num = null;

        // 再送フラグ 基本 0
        String saisou_num = "0";
        //**************************** 追加
        // 開始時間
        String start_time_view_in = free_start_time_view.getText().toString();
        // 終了時間
        String end_time_view_in = free_end_time_view.getText().toString();


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

        // 品目区分
        String Hinmoku_C_str = "9";

        // 品目
        String  GET_Hinmoku_C = "SG00";

        /**
         * 　＊＊＊＊＊＊ 「樹脂成型課」　３項目　追加　＊＊＊＊＊＊＊
         */
        String Color_Time = "0"; // 色段取時間

        String Kata_Time = "0"; // 型段取時間

        String Kikai_Code = "0"; // 機械コード

        //**********************  END インサート用　データ　作成

        // DB への　登録
        DBAdapter dbAdapter = new DBAdapter(this);

        dbAdapter.openDB();

        // dia_user_key => 作業部署コード
        dbAdapter.saveDB(get_TMNF_01,Sagyou_B,"",Hinmoku_C_str,
                GET_Hinmoku_C,hinmei,"","","0",Sagyou_Time,
                "","","",
                "","","",
                last_check,"",saisou_num,
                start_time_view_in,end_time_view_in,T_ID_Str,date_num,
                Color_Time,Kata_Time,Kikai_Code);

        dbAdapter.closeDB();

        init();

    }


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