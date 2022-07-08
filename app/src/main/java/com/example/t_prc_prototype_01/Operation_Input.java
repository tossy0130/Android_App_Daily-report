package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Operation_Input extends AppCompatActivity{

    /**
     *   URL
     */

    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/UploadToServer.php";

    public static String Sousin_log_Flg_Op = "0";

    // ログ用　タグ
    private String TAG = "アクティビティ:Operation_Input.java";

    //------- スピナー　選択　アイテム表示　テキストビュー
    private TextView get_spiiner_text,start_time_view,end_time_view;

    //------- スピナー
    private Spinner spinner;
    private Spinner spinner_02;
    private Spinner spinner_03;
    private Spinner spinner_04;
    private Spinner spinner_Tosou;

    private String item;
    private String item_02;
    private String item_03;
    private String item_04;

    private ArrayList<String> spinner_item_A = new ArrayList<>();
    private ArrayList<String> spinner_item_B = new ArrayList<>();
    private ArrayList<String> spinner_item_C = new ArrayList<>();
    private ArrayList<String> spinner_item_D = new ArrayList<>();
    private ArrayList<String> spinner_item_Tosou = new ArrayList<>();

    private HashMap<String,String> spinner_item_A_Hash = new HashMap<>();
    private HashMap<String,String> spinner_item_B_Hash = new HashMap<>();
    private HashMap<String,String> spinner_item_C_Hash = new HashMap<>();
    private HashMap<String,String> spinner_item_D_Hash = new HashMap<>();
    private HashMap<String,String> spinner_item_Tosou_Hash = new HashMap<>();

    private String Hinmoku_C_str;
    private String GET_Hinmoku_C;

    //------- ボタン
    private Button select_back_btn,start_time,end_time;

    //------- 作業時間　エディットテキスト
    private EditText select_input_01;

    //------- 「保存」　ボタン　
    private MaterialButton select_save_btn;

    //------- 画面外　タップ　用　オブジェクト
    private InputMethodManager inputMethodManager;

    //------- ConstLayout 全体　オブジェクト
    private ScrollView operation_con_view;

    //------- スピナー選択用　& テキストビュー　値取得用
    private String get_spiiner_text_insert;

    //------- 作業時間　エディットテキスト　値取得用
    private String get_select_input_01;

    //------- Work_Choice.java からの値を受け取り用
    private String get_TMNF_01, get_TMNF_02,dia_edit_01_num,i_dia_chack_01_num,
            ch_user_name_num,ch_busyo_view_Hon_num,ch_busyo_view_num,dia_user_key;

    //------------- CSV ファイル作成 & ファイル　送信用 ------------
    private File target_csv_file;
    //------------ 送信 Flg
    private String saisou_Flg;
    private String Send_time_str;
    public static String send_csv_file_name;
    private ArrayList<String> csv_itme_list = new ArrayList<>();
    private ArrayList<String> csv_get_file_item = new ArrayList<>();
    private String csv_file_name_01, csv_file_name_02;

    // 作業コード用  dd が　入る
    private String Saghou_Code;
    // 作業番号用  yyyymm
    private String Sagyou_date;

    //    // 現在時刻　取得
    private String Sagyou_yymmdd;

    // インサート用　送信スタッツ
    private String send_log_Stats;

    private String g_num_str_01;

    private String tmp_s_01;

    private String time_001, time_002, time_003;

    private String Task_Count_num;
    // インサート用　送信時間
    private String sousin_time;

    // Send_Table max 値　取得用　
    private int Send_Table_id_max;
    private String G_id_str,T_ID_Str;

    private long a_time, b_time;

    private int time_01_a, time_01_b, time_02_a, time_02_b;

    private String get_Sagayou_B;

    private String Sagyou_B;

    //********** id用
    private String DD_id;
    private int DD_id_i;

    private String Create_Date_NUM,yy_num,dd_num;

    /**
     *   作業部署（ファイル送信用） 追加 2021/01/16
     */
    private String Sagyou_Get_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation__input);

        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
        /***
         *   Work_Choice.java から 値を受け取る
         */

        if(getIntent() != null) {

            get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
            get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");

            dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");


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
             *  作業部署　取得
             */
            get_Sagayou_B = getIntent().getStringExtra("SAGYOU_Busyo_Code_SELECT_01_str");

            Sagyou_B = getIntent().getStringExtra("Sagyou_B");

        }


        //-------- コンポーネント　初期化
        init();

        /**　
         *   「塗装課」　だったら　スピナー　追加
         */
        spinner_Tosou.setVisibility(View.GONE);

        if(Sagyou_B.equals("B0887")) {
            spinner_Tosou.setVisibility(View.VISIBLE);

            start_time_view.setTextColor(Color.parseColor("#333333"));
            end_time_view.setTextColor(Color.parseColor("#333333"));
            System.out.println("塗装課 表示　OK");
        } else {
            spinner_Tosou.setVisibility(View.GONE);
        }




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

        //********** 時間取得
        time_001 = time_str.substring(8,10);
        time_002 = time_str.substring(10,12);
        time_003 = time_str.substring(12,14);


        csv_file_name_01 = time_str.substring(0, 8); // yyyymmdd 取得
        csv_file_name_02 = time_str.substring(8, 14); // HHmmss

        //------ 送信時間作成
        String year = time_str.substring(0, 4); // 年
        String month = time_str.substring(4, 6); // 月

        String hour_f = time_str.substring(8, 10); //  １時間
        String minute_f = time_str.substring(10, 12); //　１分
        String second_f = time_str.substring(12, 14); // 1秒

        // グラフインサート用
        g_num_str_01 = time_str.substring(6,8); // dd 取得

        //------ 送信時間
        Send_time_str = year + "/" + month + "/" + Saghou_Code + " " + hour_f + ":" + minute_f + ":" + second_f;
        System.out.println(Send_time_str);
        // ＊＊＊＊＊  CSV 送信ファイル名　作成 　＊＊＊＊＊　ANDROID の前に　作業者コード　が入る。
       //  send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-ANDROID-" + get_TMNF_01 + ".csv";
       // 変更 2021/01/16 send_csv_file_name = "SG-" + csv_file_name_01 + "-" + csv_file_name_02 + "-" + Sagyou_B + "-" + get_TMNF_01 + ".csv";

        //------------- 画面外　タップしたら　ソフトキーボード　非表示処理
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        operation_con_view = (ScrollView) findViewById(R.id.operation_con_view);

        /**
         *  ----------- リスト　（スピナー） A　最上段 部分 -----------
         */


        /***
         *   開始時間　タップ
         */
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Calendar calendar = Calendar.getInstance();
                 int hour = calendar.get(Calendar.HOUR_OF_DAY);
                 int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(Operation_Input.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time_01_a = hourOfDay;
                        time_01_b = minute;

                        a_time = calendar.getTimeInMillis();

                        start_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });


        /***
         *   終了時間　タップ
         */
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Calendar calendar1 = Calendar.getInstance();
                 int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                 int minute = calendar1.get(Calendar.MINUTE);


                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(Operation_Input.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        b_time = calendar1.getTimeInMillis();

                        time_02_a = hourOfDay;
                        time_02_b = minute;

                        //*** 開始時間  分に　変換
                        int hour_01_sum = time_01_a * 60 + time_01_b; //*** 開始時間
                        int hour_02_sum = time_02_a * 60 + time_02_b; //*** 終了時間

                        int time_sum = hour_02_sum - hour_01_sum;

                     //   int result_hour = time_sum / 60;  時間　算出
                     //   int result_miute = time_sum % 60; 分　　算出

                        System.out.println("time_sum:::値:::" + time_sum);

                        end_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));

                        //******* 時間計算テスト *********

                        select_input_01.setText(String.valueOf(time_sum));

                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });


        // 初回動作の対応
        spinner.setFocusable(false);
        //--------------------------------- スピナー　変更イベント 01
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //******* フォーカス　を　true に　する
                if(spinner.isFocusable() == false) {
                    spinner.setFocusable(true);
                    return;
                }

                //******* 選択されているアイテムを取得

             //   spinner.setSelection(position);

            //    final String item = (String)spinner.getSelectedItem();
                String item =(String) parent.getItemAtPosition(position);
                get_spiiner_text.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });//---------------------------- setOnItemSelectedListener END ----------

        /**
         * *----------- リスト　（スピナー） B　中段 部分 -----------
         */

        // 初回動作の対応
        spinner_02.setFocusable(false);
        //--------------------------------- スピナー　変更イベント 02
        spinner_02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //******* フォーカス　を　true に　する
                if(spinner_02.isFocusable() == false) {
                    spinner_02.setFocusable(true);
                    return;
                }


                //******* 選択されているアイテムを取得

                // spinner_02.setSelection(position);

            //    final String item_02 = (String)spinner_02.getSelectedItem();
                String item_02 =(String) parent.getItemAtPosition(position);
                get_spiiner_text.setText(item_02);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });//---------------------------- setOnItemSelectedListener END ----------


        /**
         * ----------- リスト　（スピナー） C　下段 部分 -----------
         */

        // 初回動作の対応
        spinner_03.setFocusable(false);
        //--------------------------------- スピナー　変更イベント 03
        spinner_03.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //******* フォーカス　を　true に　する
                if(spinner_03.isFocusable() == false) {
                    spinner_03.setFocusable(true);
                    return;
                }

                //******* 選択されているアイテムを取得

             //   final String item_03 = (String)spinner_03.getSelectedItem();
                String item_03 =(String) parent.getItemAtPosition(position);
                get_spiiner_text.setText(item_03);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });//---------------------------- setOnItemSelectedListener END ----------


        /**
         * ----------- リスト　（スピナー） D　下段 部分 -----------
         */

        // 初回動作の対応
        spinner_04.setFocusable(false);
        //--------------------------------- スピナー　変更イベント 04
        spinner_04.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //******* フォーカス　を　true に　する
                if(spinner_04.isFocusable() == false) {
                    spinner_04.setFocusable(true);
                    return;
                }

                //******* 選択されているアイテムを取得

                String item_04 =(String) parent.getItemAtPosition(position);
                get_spiiner_text.setText(item_04);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//---------------------------- setOnItemSelectedListener END ----------

        /**
         *  「塗装課」　用　スピナー
         */
        spinner_Tosou.setFocusable(false);
        spinner_Tosou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //******* フォーカス　を　true に　する
                if(spinner_Tosou.isFocusable() == false) {
                    spinner_Tosou.setFocusable(true);
                    return;
                }

                String item_05 =(String) parent.getItemAtPosition(position);
                get_spiiner_text.setText(item_05);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         *  「保存」ボタン タップ処理
         */
        select_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(get_spiiner_text.length() == 0) {
                    // コンボボックスが選択されていない場合
                    toastMake("項目を選択してください。", -200,0);
                    return;

                } else if(select_input_01.length() == 0) {
                    // コンボボックスが選択されていない場合
                    toastMake("「作業時間」が入力されていません。", -200,0);
                    return;

                } else {

                    //********* 作業時間に　マイナスの値が入っていたら
                    String end_time_view_s = select_input_01.getText().toString();
                    int end_time_view_i = Integer.parseInt(end_time_view_s);

                    if(end_time_view_i < 0) {
                        toastMake("「作業時間」にマイナスの値が入力されています。値を修正してください。", -200,0);
                        end_time_view.setText("");
                        return;
                    }

                    // ######## 保存ボタンを押された処理 insert 処理

                    /**
                     *  インサート処理
                     */
                    // Insert_Send01();
                    Insert_Send_02_save();
                    Read_Send_File_02(); // CSVファイル作成
                    Send_CSV_POST(); // ファイル送信
                    Send_Grafu_Table_Insert(); // グラフ連携ファイル　インサート
                    Teiki_Send_Flg(); //******** 定期送信　フラグ ok にする

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

                } // ############################## Insert 処理　END

            }
        });

        /**
         *   「戻る」　ボタン　タップ処理
         */
        select_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // エディットテキスト　free_input_edit, free_input_time_edit が「空」じゃなかった場合
                if(select_input_01.getText().toString().equals("") == false) {

                    //--------------- アラートダイアログ　の表示　開始 ---------------------
                    AlertDialog.Builder bilder = new AlertDialog.Builder(Operation_Input.this);

                    //-------------- カスタムタイトル　作成
                    TextView titleView;
                    titleView = new TextView(Operation_Input.this);
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
        }); //-------------------------------　戻る　ボタン　タップ処理 END -------------

        /**
         *   ------------------ 「作業時間」　エディットテキスト　が　ソフトキーボードが押された処理
         * */
        //
        select_input_01.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    // エディターテキストの値が　「空」　じゃなかった場合
                    if(select_input_01.length() != 0) {
                        get_select_input_01 = select_input_01.getText().toString();
                    } else {
                        //----- 「空」 だった場合
                        toastMake("「作業時間」が空です。",-200,0);
                    }
                }

                return false;
            }
        });

    } //----------------------------------------------------------------- on create END

    private void NP_data_SELECT() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_NP_01 = helper.getReadableDatabase();

        try {

            //      Cursor cursor = db_NP_01.rawQuery("");

        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
        }

    }


    private void init() {

        // スピナー　値取得　テキストボックス
        get_spiiner_text = findViewById(R.id.get_spiiner_text);

        // 戻るボタン
        select_back_btn = findViewById(R.id.select_back_btn);

        // 作業時間　入力　テキストエディット
        select_input_01 = findViewById(R.id.select_input_01);
        //------ 数字入力
        select_input_01.setInputType(InputType.TYPE_CLASS_NUMBER);

        //------ 「保存」　ボタン
        select_save_btn = findViewById(R.id.select_save_btn);

        //----------------------------- 空にする
        // -------- スピナー取得　textview
        get_spiiner_text.setText("");
        // -------- 時間エディットテキスト
        select_input_01.setText("");

        //******* 開始時間 & 終了時間　取得用　テキストビュー
        start_time_view = findViewById(R.id.start_time_view);
        end_time_view = findViewById(R.id.end_time_view);

        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);

        /***
         * 　＊＊＊＊＊＊＊＊＊＊＊＊＊　スピナー　値セット ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
         */

        GET_Spinner_A();

        spinner = findViewById(R.id.spinner_01);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_A
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);

        //**************** スピナー　02
        GET_Spinner_B();

        spinner_02 = findViewById(R.id.spinner_02);

        ArrayAdapter<String> adapter_02 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_B
        );
        adapter_02.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner_02.setAdapter(adapter_02);

        //**************** スピナー　03
        GET_Spinner_C();

        spinner_03 = findViewById(R.id.spinner_03);

        ArrayAdapter<String> adapter_03 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_C
        );
        adapter_03.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner_03.setAdapter(adapter_03);

        //****************** スピナー　04
        GET_Spinner_D();

        spinner_04 = findViewById(R.id.spinner_04);

        ArrayAdapter<String> adapter_04 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_D
        );
        adapter_04.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner_04.setAdapter(adapter_04);

        //******************* 塗装課　スピナー
        GET_Spinner_Tosou();

        spinner_Tosou = findViewById(R.id.spinner_Tosou);

        ArrayAdapter<String> adapter_tosou = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_Tosou
        );

        adapter_tosou.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner_Tosou.setAdapter(adapter_tosou);
    //    spinner_Tosou.setVisibility(View.GONE);

        System.out.println("Sagyou_B:::init" + Sagyou_B);

    }

    @Override
    public void onBackPressed() {

        // エディットテキスト　free_input_edit, free_input_time_edit が「空」じゃなかった場合
        if(select_input_01.getText().toString().equals("") == false) {

            //--------------- アラートダイアログ　の表示　開始 ---------------------*/
            AlertDialog.Builder bilder = new AlertDialog.Builder(Operation_Input.this);

            //-------------- カスタムタイトル　作成
            TextView titleView;
            titleView = new TextView(Operation_Input.this);
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

    /**
     * -------------------- トースト作成 ----------------------
     */
    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    /**
     *  現在時刻 取得
     */
    private String getToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム A 取得
     */
    private void GET_Spinner_A() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;

        //---- リストを空にする
        spinner_item_A.clear();

        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("select * from NP_data_table" +
                    " where NP_data_c_02 " +
                    "like 'A%' order by NP_data_c_02 ASC;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("NP_data_c_01");
                Hinmoku_C_str = cursor.getString(idx);

                //------- 朝礼・ミーティング・課内打合せ
                idx = cursor.getColumnIndex("NP_data_c_03");
                arr_item[0] = cursor.getString(idx);

                //------- A1
                idx = cursor.getColumnIndex("NP_data_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_A.add(arr_item[0]);

                // 比較用にハッシュマップに挿入
                spinner_item_A_Hash.put(arr_item[0], arr_item[1]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }

        spinner_item_A.add(0, "リスト（ A1 ～ A10 ）");

    } //------------ GET_Spinner_A END ---------------->

    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム B 取得
     */
    private void GET_Spinner_B() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;

        //---- リストを空にする
        spinner_item_B.clear();

        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("select * from NP_data_table" +
                    " where NP_data_c_02 " +
                    "like 'B%' order by NP_data_c_02 ASC;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("NP_data_c_01");
                Hinmoku_C_str = cursor.getString(idx);

                //------- 朝礼・ミーティング・課内打合せ
                idx = cursor.getColumnIndex("NP_data_c_03");
                arr_item[0] = cursor.getString(idx);

                //------- A1
                idx = cursor.getColumnIndex("NP_data_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_B.add(arr_item[0]);

                // 比較用にハッシュマップに挿入
                spinner_item_B_Hash.put(arr_item[0], arr_item[1]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }

        spinner_item_B.add(0, "リスト（ B1 ～ B5 ）");

    } //------------ GET_Spinner_B END ---------------->

    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム C 取得
     */
    private void GET_Spinner_C() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        //---- リストを空にする
        spinner_item_C.clear();

        int num = 0;
        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("select * from NP_data_table" +
                    " where NP_data_c_02 " +
                    "like 'C%' order by NP_data_c_02 ASC;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("NP_data_c_01");
                Hinmoku_C_str = cursor.getString(idx);

                //------- 朝礼・ミーティング・課内打合せ
                idx = cursor.getColumnIndex("NP_data_c_03");
                arr_item[0] = cursor.getString(idx);

                //------- A1
                idx = cursor.getColumnIndex("NP_data_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_C.add(arr_item[0]);

                // 比較用にハッシュマップに挿入
                spinner_item_C_Hash.put(arr_item[0], arr_item[1]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }

        spinner_item_C.add(0, "リスト（ C1 ～ C4 ）");

    } //------------ GET_Spinner_C END ---------------->


    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム C 取得
     */

    private void GET_Spinner_D() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;

        //---- リストを空にする
        spinner_item_D.clear();

        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("select * from NP_data_table" +
                    " where NP_data_c_02 " +
                    "like 'D%' order by NP_data_c_02 ASC;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("NP_data_c_01");
                Hinmoku_C_str = cursor.getString(idx);

                //------- 朝礼・ミーティング・課内打合せ
                idx = cursor.getColumnIndex("NP_data_c_03");
                arr_item[0] = cursor.getString(idx);

                //------- A1
                idx = cursor.getColumnIndex("NP_data_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_D.add(arr_item[0]);

                // 比較用にハッシュマップに挿入
                spinner_item_D_Hash.put(arr_item[0], arr_item[1]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }

        spinner_item_D.add(0, "リスト（ D1 ～ D6, その他 ）");

    } //------------ GET_Spinner_D END ---------------->

    /**
     *   「塗装課」
     */
    private void GET_Spinner_Tosou () {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_tosou = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;

        //****** リストを空にする
        spinner_item_Tosou.clear();

        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db_tosou.rawQuery("select * from Tosou_Data" +
                    " order by Tosou_Data_c_02 asc;", null);

            while (cursor.moveToNext()) {

                //------- 9
                int idx = cursor.getColumnIndex("Tosou_Data_c_01");
                Hinmoku_C_str = cursor.getString(idx);

                //------- 朝礼・ミーティング・課内打合せ
                idx = cursor.getColumnIndex("Tosou_Data_c_03");
                arr_item[0] = cursor.getString(idx);

                //------- A1
                idx = cursor.getColumnIndex("Tosou_Data_c_02");
                arr_item[1] = cursor.getString(idx);

                // ArrayList に　挿入　
                spinner_item_Tosou.add(arr_item[0]);

                // 比較用にハッシュマップに挿入
                spinner_item_Tosou_Hash.put(arr_item[0], arr_item[1]);

                num++;

            } //-------- while END

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db_tosou != null) {
                db_tosou.close();
            }
        }

        spinner_item_Tosou.add(0, "（ 塗装課用 ）リスト");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(operation_con_view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // 背景にフォーカスを移す
        operation_con_view.requestFocus();

        return true;
    }


    private void Insert_Send_02_save() {


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
        String hinmei = get_spiiner_text.getText().toString();
        System.out.println(hinmei);

        //------ 作業時間
        String Sagyou_Time = select_input_01.getText().toString();
        System.out.println(Sagyou_Time);

        // 最終チェック　0 にする
        String last_check = "0";

        String kakou_num = null;

        // 再送フラグ 基本 0
        String saisou_num = "0";
        //**************************** 追加
        // 開始時間
        String start_time_view_in = start_time_view.getText().toString();
        // 終了時間
        String end_time_view_in = end_time_view.getText().toString();


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



        //********** 品目区分　9 を　入れる
       if(spinner_item_A_Hash.containsKey(hinmei)) {
           GET_Hinmoku_C = spinner_item_A_Hash.get(hinmei);

       } else if (spinner_item_B_Hash.containsKey(hinmei)) {
           GET_Hinmoku_C = spinner_item_B_Hash.get(hinmei);

       } else if (spinner_item_C_Hash.containsKey(hinmei)) {
           GET_Hinmoku_C = spinner_item_C_Hash.get(hinmei);

       } else if (spinner_item_D_Hash.containsKey(hinmei)) {
           GET_Hinmoku_C = spinner_item_D_Hash.get(hinmei);

           //*************** 塗装課　取得
       } else if(spinner_item_Tosou_Hash.containsKey(hinmei)) {
           GET_Hinmoku_C = spinner_item_Tosou_Hash.get(hinmei);
       }

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

    /**
     *  トースト表示
     * @param msg
     * @param x
     * @param y
     */
    private void tostMake(String msg, int x, int y) {
        Toast toast = Toast.makeText(this,msg, Toast.LENGTH_LONG);

        // 表示位置　調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    } //----------------- END tostMake




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
            if(db != null) {
                db.close();
            }
        }

        /***
         * --------------------- ファイル　作成処理 END ------------------------->
         */
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
        String uploadURL = JIM_TEST_URL; //------ 社内
      //  String uploadURL = TOYAMA_URL;  //*********** 本番
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



            String tmp_send_id = g_num_str_01 + "-" + "0" + tmp_s_01;

            //*********  Send_table_01 の　グラフ ID を　挿入

          //  values.put("Send_id", tmp_send_id); // タスク　番号

            values.put("Send_id", T_ID_Str); // タスク　番号
            System.out.println("tmp_send_id ::: グラフインサート" +  T_ID_Str);

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

    }

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
            values.put("log_send_col_03",Sousin_log_Flg_Op); // 送信スタッツ 0: 未送信, 1: 送信ok , 2:送信失敗
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
     *  　　Send_table_01　タスク数　取得
     */
    private void worker_Get_Task_Num() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Worker_db_03 = helper.getReadableDatabase();

        // トランザクション　開始
        Worker_db_03.beginTransaction();
        Cursor cursor = null;

        try {

            cursor = Worker_db_03.rawQuery("select count(id) from Send_table_01", null);

            // *********** カーソルが　空　じゃなくて、　0 以上
            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {

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

            if (Worker_db_03 != null) {
                Worker_db_03.close();
            }

        }

    } // *************** END function


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

                    //**********************************************
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

                    if(DD_id_i == 0) {
                        DD_id_i = 1;   // 29-00 =>   1  , 30-00 => 1

                        System.out.println("DD_id_i 下:::" + DD_id_i);
                    } else {
                        DD_id_i++;   // 29-01 =>  2  ,  30-10 => 11

                        System.out.println("DD_id_i　下:::" + DD_id_i);
                    }
                    //********************************************** END

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