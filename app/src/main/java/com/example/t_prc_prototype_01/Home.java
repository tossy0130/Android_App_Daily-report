package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.function.IntUnaryOperator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.ConnectionShutdownException;

import static com.example.t_prc_prototype_01.Free_Input.Sousin_log_Flg_Fr;
import static com.example.t_prc_prototype_01.MainActivity.app_finish_flag;
import static com.example.t_prc_prototype_01.Operation_Input.Sousin_log_Flg_Op;
import static com.example.t_prc_prototype_01.QR_Barcode_Read.Sousin_log_Flg_QR;

public class Home extends AppCompatActivity {

    private static final String [] JIM_Test = {
            "http://192.168.254.226/tana_phppost_file/view_pdf/new_toyama.pdf", //0
            "http://192.168.254.226/tana_phppost_file/get_csv/SHMF.csv", // 1
    };



    //**********　「遅刻」「早退」「残業」用　スピナー
    private final String [] arr_time_01 = {
        "0","15","30","45","60","75","90","105", "120", "135", "150", "165", "180",
                "195", "210", "225", "240", "255", "270", "285", "300"};

    private static final int REQUESTCODE_TEST = 1;
    private static final int REQUESTCODE_SECOND = 100;

    public String L_TAG = "Home.java";

    public static boolean Icon_Send_Flg = false;

    // コールバック
    private int call_chk;

    //----------------- ヘッダー部分
    // ユーザー名
    private TextView user_name;

    // 部署コード用　テキストビュー
    //  private TextView busyo_text_box;
    private TextView busyo_view;
    // スナックバー
    private Snackbar snackbar;

    // webView
    private WebView webview_01;

    //----------DB 接続用 ----------
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    //--------- DB count用　判別　変数
    private int db_count;

    //--------- 商品マスター CSV　ダウンロード用　ボタン
    private Button csv_btn_02;
    private Button work_btn_01;

    private TextView date_time_text, di1_start_time_view, di1_end_time_view;

    //--------- 部署コード 判定用　
    private Map<String, String> spi_map_busyo = new HashMap<>();
    private Map<String, String> spi_map_busyo_02 = new HashMap<>();
    private Map<String, String> map_busyo_03 = new HashMap<>();

    //--------- 部署コード　スピナー　データ
    private Spinner spinner_busyo;

    private  ArrayList<String> spinner_item_01 = new ArrayList<>();
    private  ArrayList<String> spinner_item_02 = new ArrayList<>();

    //-------- スピナー　取得用
    private String item_spinner;
    private int spinner_idx;

    //------- テスト用　リストビュー
    private ListView listView_01;


    public List<Send_Item> send_item_list = new ArrayList<Send_Item>();

    //------- 遅刻時間
    private EditText input_late_time;
    private TextView view_late_time;

    //------- 残業時間
    private EditText input_over_time;
    private TextView view_over_time;

    //------- カードビュー
    private CardView list_layout_01;

    //------- MainActivety.java から　の取得用 getIntent()
    private String get_TMNF_01;
    private String get_TMNF_02;
    private String get_TMNF_03;

    private String get_BUMF_01;

    //-------- 部署 String
    private String busho_str;

    //----------- スタッツ　アイコン用
    private TextView statu_01;
    private TextView statu_02;

    //----------- アラートダイアログ用　　アダプター作成
    private ArrayAdapter<String> MenuAdapter;
    // 値取得用
    private int buttonSelectedIndex = 0;

    private List<String> dataList;

    private String dia_spinner_01_num, dia_chack_01_num, dia_edit_01_num,dia_user_key;
    public static int i_dia_chack_01_num;

    private EditText dia_edit_01;

    //----------------------- フラグ
    // PDF 最新表示　スナックバー　フラグ
    public boolean Snac_Flg;

    //------------------------ スタッツ Image
    private ImageButton stuatu_img_ok;
    private ImageButton stuatu_img_ng;

    //**** 戻るボタン
    private ImageButton back_btn_y;

    public int get_result_num,i_dia_chack_01_num_result;

    //------------------------- 返ってきた値　受け取り
    private String get_TMNF_01_result,get_TMNF_02_result,dia_edit_01_num_result,dia_user_key_result;
    private String s_tmp_01, s_tmp_02;

    private String Busho_Code,Saghou_Code,Sagyou_date;

    //--------------- 送信画面へ -------->
    private ImageButton send_view_Move;

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<MyListItem> items;
    private MyListItem myListItem; // マイリストアイテム　の　オブジェクト

    // 参照する DB のカラムを入れる
    private String[] columns = null;
    private SendAdapter sendAdapter;
    private TextView tasuku_max;

    // ------------------ 段取り時間合計　、　作業時間合計
    private String Dandori_Time_Sum, Sagyou_Time_Sum,Time_Sum,Iro_Time_Sum,Kata_Time_Sum;
    private int Dandori_Time_Sum_i, Sagyou_Time_Sum_i,Iro_Time_Sum_i,Kata_Time_Sum_i;
    private TextView pro_time;

    private String hinmok_k;

    // 終日フラグ　SELECT 結果格納
    private int Syuuzitu_Flg;

    //******************* 遅早　ダイアログ用
    private Button late_time,over_time;
    private String Tikoku_Flg, Soutai_Flg,Sagyou_yymmdd
    ;  // チェックボックス用　遅刻・早退　フラグ

    private int Send_Table_id_max,Send_Table_id_count;
    // Send_Table インサート用　グラフ ID
    private String G_id_str;
    // 時間入力
    private EditText dia_chack_01_01_edit;
    private String Ti_So;
    private int Ti_So_Time;
    private String Ti_So_str;

    //******************* 残業　用
    private Button dia_2_touroku_btn_001,dia_2_touroku_btn_002; // 登録　キャンセル　ボタン
    private EditText dia_2_chack_01_01_edit; // 時間入力
    private int Zangyou_Time;
    private String Zangyou_str;

    //********* 翌日　id 用
    private String DD_id, T_ID_Str;
    private int DD_id_i;

    private int time_01_a, time_01_b, time_02_a, time_02_b;
    private int time_03_a, time_03_b, time_04_a, time_04_b;
    private long a_time, b_time, C_time, D_time;
    private TextView di2_start_time_view, di2_end_time_view;

    private boolean check_Tikoku, check_Soutai;

    private List<Integer> Tikoku_Soutai_list = new ArrayList<>();
    private int result_sum = 0;
    private int soutai_str_i = 0;
    private int tikoku_str_i = 0;

    private String Tikoku_SOUtai_C;

    private String HinMoku_C;

    //******* ID 作成
    private String Create_Date_NUM,yy_num,dd_num;

    //****** 「遅刻」「早退」「残業」
    private Spinner Tkoku_spi,Zangyou_spi;

    //***** ログアウト判定用　Flg
    private String Logout_GO_str;

    //---- エラー表示用
    private TextView err_view;

    //----- 送信用 Flg_Table から取得　作業部署コード
    private String Flg_send_sagyou_C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //------- コンポーネント 初期化 処理 -------
        init();

        dataList = mkDataList(50);

        // スタッツ 200 OK Image btn
        stuatu_img_ok.setVisibility(View.VISIBLE);

        //-------- スタッツ非表示
        /*
        statu_01.setVisibility(View.GONE);
        statu_02.setVisibility(View.GONE);
         */

        //------------ 現在時刻　取得
        String time_str = getNowDate_02();
        // DB 格納用
        Saghou_Code = time_str.substring(6,8); // dd 取得
        Sagyou_date = time_str.substring(0, 6); // yyyymm 取得
        Sagyou_yymmdd = time_str.substring(0, 8); // yyyymmdd

        date_time_text.setText(getNowDate());

        //------ 部署コード　スピナーセット
        //***   getSpinner_01();

        // リスト表示用
        //  Get_LIST();


        //--------------------- MainActivity.java から　値を受け取る
        if(getIntent() != null) {
            // 担当者コード
            get_TMNF_01 = getIntent().getStringExtra("TNMF_01_val");
            // 担当者名
            get_TMNF_02 = getIntent().getStringExtra("TNMF_02_val");

            if(get_TMNF_02.length() != 0) {
                user_name.setText(get_TMNF_02);
            } else {
                user_name.setText("ユーザー名がデータにありません。");
            }

            // 部署コード
            get_TMNF_03 = getIntent().getStringExtra("TNMF_03_val");
            // 部署名
            get_BUMF_01 = getIntent().getStringExtra("BUMF_01_val");

            //************** テキストビューに　部署コードをセットする コード修正 2020/12/25 *****************
            if(get_BUMF_01.length() != 0) {
                busyo_view.setText(get_BUMF_01);
                busho_str = busyo_view.getText().toString();

            } else {

                return;

            }


        } //---------------- getIntent()

        // DBAdapter の　コントラクタを呼ぶ
        dbAdapter = new DBAdapter(this);
        // Mylist items の list 作成
        items = new ArrayList<>();
        //  MyBaseAdapte の　コンストラクタを呼び出す
        myBaseAdapter = new MyBaseAdapter(this, items);
        // ListVIew の結び付け


        input_late_time.setText("0");
        /**
         *  *************** 遅刻・早退　残業 SELECT ***************
         *  Tikoku_Soutai_SELECT -> 遅刻　・　早退
         *
         *  Zangyou_SELECT -> 残業
         *
         */

        /*
        Tikoku_Soutai_SELECT();
        Tikoku_Soutai_SELECT_02();

         */
        Tikoku_Soutai_SELECT_03();

        Zangyou_SELECT();



        /**
         *  一覧　読み出し
         */
        loadMyList();


        /**
         *  定期送信　ができるか　どうかの　フラグ　確認  Send_TB_id_COUNT()
         */
        Send_TB_id_COUNT();



        /**
         *  user_name　テキストエディットの値　取得
         */
        if(busyo_view.getText().toString().equals("") == false) {
            Busho_Code = busyo_view.getText().toString();
        }


        /**
         *  ------------------ 段取り時間　、　作業時間　の　合計値を取得
         *   Send_table_01_Time_Sum();
         */
        Send_table_01_Time_Sum();

        /**
         *  Syuuzitu_Flg_SELECT   終日チェック　値取得
         */
        Syuuzitu_Flg_SELECT();


        /**
         *   late_time => ボタン　遅刻・早退用
         */
        late_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //******* アラートダイアログ　遅刻・早退　用
                Allahto_Dailog_Tikoku_Soutai ();

            }
        });

        /**
         *   over_time => ボタン  残業
         */
        over_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*******  残業　ダイアログ
                Allahto_Dailog_Zangyou();

            }
        });




        /**
         * スタッツ　oK ImageBtn 処理
         */
        stuatu_img_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMake("最新PDFファイルを表示します。", 0,-200);
                Snac_Flg = true;

                /*------ HttpGetTask クラス ------*/
                HttpGetTask httpGetTask = new HttpGetTask();

                // 社内テスト
                String jim = "http://192.168.254.87/tana_phppost_file/view_pdf/new.pdf";


                try {  //********************* URL *****************************************
                     httpGetTask.execute(new URL(JIM_Test[0]));  //--- 社内
                  // httpGetTask.execute(new URL(TOYAMA_DATA[0])); //******** 本番
                } catch (MalformedURLException e) {

                    e.printStackTrace();
                }

                /*----- コールバック ------*/
                httpGetTask.setOnCallBack(new HttpGetTask.CallBackTask() {

                    @Override
                    public void CallBack(String result) {
                        super.CallBack(result);

                        // コールバック出力テスト
                        System.out.println("コールバック result:" + result);


                        // ------------- result の　値を　int 型にパース　処理 --------------
                        try {

                            // result の　値を　パース
                            call_chk = Integer.parseInt(result);
                            System.out.println("result パース完了");

                        } catch (NumberFormatException e) {
                            System.out.println("result 型　エラー　パースができません。");
                            e.printStackTrace();
                        }

                        //-------------- result の　返却スタッツで　分岐処理 ----------------
                        if(call_chk != 200) {
                            System.out.println("コールバックエラー スタッツ 200 以外です。" + call_chk);

                            Snac_Flg = false;

                            //************　スタッツ アイコン表示　NG 用 **************
                            //      statu_02.setVisibility(View.VISIBLE);
                            stuatu_img_ng.setVisibility(View.VISIBLE);

                            return;

                        } else {

                            System.out.println("コールバック　result OK 200　取得" + call_chk);

                            if(Snac_Flg) {

                                //------ スナックバー表示 -------
                                snackbar = Snackbar.make(findViewById(android.R.id.content), "【 更新されたPDFファイル 】があります。クリックしてください。", Snackbar.LENGTH_INDEFINITE);
                           //     snackbar.getView().setBackgroundColor(Color.argb(133,213, 40, 83));
                                snackbar.getView().setBackgroundColor(Color.argb(133,0, 0, 0));
                                snackbar.setTextColor(Color.rgb(255,255,255)); // 説明文 テキスト
                                snackbar.setActionTextColor(Color.rgb(87,215,141)); // クリック部分　テキスト
                                // snackbar.setDuration(6000);
                                snackbar.show();

                                snackbar.setAction("PDF表示", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //*************** 最新PDF を　webview　で　表示 ********************
                                        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                                        startActivity(intent);

                                        // スナックバー　切断
                                        snackbar.dismiss();
                                    }

                                }); //-----------------------  snackbar.setAction("PDF表示", new View.OnClickListener() END

                            } else  {
                                System.out.println("スナックバー １度表示 OK");
                                Log.d(L_TAG, "スナックバー　非表示 False OK");
                            }

                            //************ スタッツ アイコン表示 OK用 *********
                            //     statu_01.setVisibility(View.VISIBLE);


                        }//------------- else END


                    }
                });
            }
        });

        /**
         *  スタッツ　NG ImageBtn 処理
         */
        stuatu_img_ng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMake("最新PDFを取得できませんでした。サーバー接続が不正です。",0,-200);
            }
        });

        /**
         *  ←　戻る　ボタン
         */
        back_btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------- 最新PDF を　表示させない
                Snac_Flg = false;

                //--------------- アラートダイヤログ　タイトル　設定 ---------------//
                // タイトル
                TextView titleView;
                titleView = new TextView(Home.this);
                titleView.setText("ログアウト確認");
                titleView.setTextSize(22);
                titleView.setTextColor(Color.WHITE);
                titleView.setBackgroundColor(getResources().getColor(R.color.red));
                titleView.setPadding(20, 30, 20, 30);
                titleView.setGravity(Gravity.CENTER);

                //-------------- アラートログの表示 開始 -------------- //
               AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                // ダイアログの項目
                bilder.setCustomTitle(titleView);

                // メッセージ内容　セット
                String msg_bilder = "ログアウトしますか？";
                bilder.setMessage(msg_bilder);

                bilder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                });



                bilder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });

                AlertDialog dialog = bilder.create();
                dialog.show();

                //********* ボタン はい **********
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B20000"));
             //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                //********* ボタン いいえ **********
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B20000"));
             //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

            }
        });


        //******************* 商品マスター　読み込み ***********************
        csv_btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                err_view.setVisibility(View.GONE);
                err_view.setText("");

                toastMake("商品マスターファイル　ダウンロード開始します。\n\nダウンロードが完了するまで「＊＊＊＊ このままお待ちください ＊＊＊＊＊」。",0,-200);

                        // CSV SHMF インサート
                        SHMF_CSV_IN();

            }
        });

        /**
         *  送信画面　へ　移動 （ImageButoon が押されたら）
         */

        send_view_Move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  送信画面へ　遷移する　アラートダイアログ
                 */
                Allahto_Dailog_Shot_Send();

            }
        });

        /**
         * 　「残業　エディットテキスト」
         */
        input_late_time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // ###### エンターボタンが押された時の処理
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    // ########### ソフトキーボードを非表示
                    if(getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    // ####  input_late_time エディットテキストの　値をセットする  ####
                    String view_late_time_str = input_late_time.getText().toString();

                    if (view_late_time_str.length() != 0) {

                        view_late_time.setText(view_late_time_str + " 分");

                    } else {
                        //************* テキストエディットの値が「空」だった場合は、 "0" を　セットする。

                        // エディットテキストに 0 をセット
                        int err_num = 0;

                        input_late_time.setText("");

                        // 遅刻時間　入力エラー
                        toastMake("「遅刻時間」入力が空になっています。",0,-200);
                        view_late_time.setText("0");

                    }

                } //-------- END if

                return false;
            }
        }); //---------------------- （遅刻時間入力　END） input_late_time.setOnEditorActionListener END



        //------------- (残業時間) エディットテキスト　ソフトキーボード「決定」ボタンが押されたら
        input_over_time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // ###### エンターボタンが押された時の処理
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    // ########### ソフトキーボードを非表示
                    if(getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    // ####  input_over_time エディットテキストの　値をセットする  ####
                    String input_over_time_str = input_over_time.getText().toString();

                    if (input_over_time_str.length() != 0) {


                        //--------- 残業時間をセットする。
                        view_over_time.setText(input_over_time_str + " 分");

                    } else {
                        //************* テキストエディットの値が「空」だった場合は、 "0" を　セットする。

                        // エラーメッセージ　表示
                        toastMake("「残業時間」が空になっています。",0,-200);


                        // エディットテキストを　　空に する。
                        int err_num = 0;
                        input_over_time.setText("");

                        // エラー　遅刻時間 0 をセットする。
                        view_over_time.setText("0");

                    }

                } //-------- END if

                return false;
            }
        }); //---------------------- （遅刻時間 入力　END） input_over_time.setOnEditorActionListener END


        /***
         *
         *  ----------- list view タップ処理 --------　「詳細画面」　へ　遷移
         *
         */

        listView_01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ID を取得する
                myListItem = items.get(position);
                int listId = myListItem.getId();


                Intent intent = new Intent(getApplicationContext(), List_Details.class);
                intent.putExtra("listId", listId);
                startActivity(intent);

                }

        });


        /***
         *  ------ list view ロングタップ ----- 「タスク　削除処理」
         */
        listView_01.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * @param parent ListView
                 * @param view 選択した項目
                 * @param position 選択した項目の添え字
                 * @param id 選択した項目のID
                 */

                //-------------- アラートログ　削除　-----------
                // タイトル
                TextView textView;
                textView = new TextView(Home.this);
                textView.setText("タスクの削除");
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                textView.setPadding(20, 20, 20, 20);
                textView.setGravity(Gravity.CENTER);

                //--------------　アラートログの表示　開始
                AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                //--------- カスタムタイトル　セット
                bilder.setCustomTitle(textView);
                //--------- メッセージのセット
                bilder.setMessage("選択されたタスクを削除しますか？");

                bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //------------------　「いいえ」　の処理
                        return;
                    }
                });

                bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //------------ 「はい」　タスク　削除処理

                        myListItem = items.get(position);
                        int listId = myListItem.getId();

                        dbAdapter.openDB();

                        dbAdapter.selectDelete(String.valueOf(listId));

                        //********* グラフ　テーブル削除
                        dbAdapter.selectDelete_02(String.valueOf(listId));

                        Log.d("ログ： アイテムクリック削除　OK",String.valueOf(listId));

                        dbAdapter.closeDB();

                        /*
                        Tikoku_Soutai_SELECT();
                        Tikoku_Soutai_SELECT_02();

                         */
                        Tikoku_Soutai_SELECT_03();

                        Zangyou_SELECT();

                        Send_TB_id_COUNT(); //****** 定期送信　フラグ　確認


                        loadMyList();

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



                return true; //　***** false を返すと　onItemLongClick()後、onItemClick()が動いてしまうので注意 *******
            }

        });


    }//--------------------------------------------- onCreate END



    /**
     *    ******　リスタート　
     */
    @Override
    public void onStart(){

        /**
         *  //**************  終日フラグ
         */
        Syuuzitu_Flg_SELECT();

        /**
         *  合計時間が　480 分　以上の場合は 1:  480以下は　0
         */

        Send_table_01_Time_Sum();

        /**
         *  *************** 遅刻・早退　残業 SELECT ***************
         *  Tikoku_Soutai_SELECT -> 遅刻　・　早退
         *
         *  Zangyou_SELECT -> 残業
         *
         */

        /*
        Tikoku_Soutai_SELECT();
        Tikoku_Soutai_SELECT_02();
           */

        Tikoku_Soutai_SELECT_03();

        Zangyou_SELECT();

        Send_TB_id_COUNT(); //****** 定期送信　フラグ　確認

        super.onStart();
        Log.v("LifeCycle", "onStart");
    }


    //----------------- メニュー　追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    /**
     *    // メニューボタン　が　押された時の　処理
     *     //******************* 作業追加 画面へ　遷移 ***********************
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            case R.id.menubtn_01 :

                if(pro_time.getText().toString().equals("000") || pro_time.getText().toString().equals("")) {

                    //********* 作業時間に「Error」 が入っている場合は　画面遷移させない
                    Allahto_Dailog_02_error();

                } else {

                    // 終日チェック　が　0 だった場合
                    if(Syuuzitu_Flg == 0) {

                        //-----------------------　アダプター作成 （アラートダイアログ用）

                        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
                        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

                        String[] arr_item = new String[2];

                        ArrayList<String> spinner_item_01 = new ArrayList<>();

                        int num = 0;

                        // ******** リストを空にする
                        spinner_item_02.clear();

                        //------------- スピナー　アイテム取得
                        try {

                            Cursor cursor = db.rawQuery("SELECT BUMF_c_01, BUMF_c_02 FROM BUMF_table;", null);

                            while (cursor.moveToNext()) {

                                int idx = cursor.getColumnIndex("BUMF_c_01");
                                arr_item[0] = cursor.getString(idx);

                                idx = cursor.getColumnIndex("BUMF_c_02");
                                arr_item[1] = cursor.getString(idx);

                                // ArrayList に　挿入　
                                spinner_item_02.add(arr_item[1]);

                                // 比較用にハッシュマップに挿入
                                spi_map_busyo_02.put(arr_item[1], arr_item[0]);

                                num++;

                            } //-------- while END

                        } catch (SQLException e) {
                            e.printStackTrace();

                        } finally {
                            if (db != null) {
                                db.close();
                            }
                        }

                        //------------------------　アダプター作成 （アラートダイアログ用） END ---->

                        //******************** アラートログの表示 処理 開始  ********************//
                        // カスタムビューを設定
                        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        final View bilde_layout = inflater.inflate(R.layout.dialog,(ViewGroup)findViewById(R.id.alertdialog_layout));

                        // dialog.xml コンポーネント
                        Spinner dia_spinner_01 = (Spinner) bilde_layout.findViewById(R.id.dia_spinner_01);
                        CheckBox dia_chack_01 = (CheckBox) bilde_layout.findViewById(R.id.dia_chack_01);
                        dia_edit_01 = (EditText) bilde_layout.findViewById(R.id.dia_edit_01);

                        // エディットテキスト入力不可
                        dia_edit_01.setFocusable(false);

                        //　スピナー　部署コード

                        //---------- ArrayAdapter -------------//
                        ArrayAdapter<String> spi_3adapter_02 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_item_02);
                        spi_3adapter_02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // スピナー に　アダプターを　セット
                        dia_spinner_01.setAdapter(spi_3adapter_02);

                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------------- アラートダイヤログ タイトル　設定 ---------------//
                        // タイトル
                        TextView titleView;
                        titleView = new TextView(Home.this);
                        titleView.setText("作業部署確認");
                        titleView.setTextSize(22);
                        titleView.setTextColor(Color.WHITE);
                        titleView.setBackgroundColor(getResources().getColor(R.color.colorPinku));
                        titleView.setPadding(20, 30, 20, 30);
                        titleView.setGravity(Gravity.CENTER);

                        // ダイアログに　「タイトル」　を　セット
                        bilder.setCustomTitle(titleView);

                        // カスタムレイアウト　を　セット
                        bilder.setView(bilde_layout);

                        /**
                         * ダイアログ内　スピナー　イベント
                         */
                        dia_spinner_01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // String で　スピナーの値取得
                                dia_edit_01_num = (String)dia_spinner_01.getSelectedItem();

                                // スピナーの値をエディットテキストにセットする。
                                dia_edit_01.setText(dia_edit_01_num);

                                System.out.println("dia_edit_01_num:::" + dia_edit_01_num);
                                // 作業確認 担当者　コード取得
                                if(spi_map_busyo_02.containsKey(dia_edit_01_num)) {
                                    dia_user_key = spi_map_busyo_02.get(dia_edit_01_num);

                                    System.out.println("dia_user_key:::" + dia_user_key);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        /**
                         *  //------------------ 終日チェック　ボックス　値取得
                         */
                        dia_chack_01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                boolean check = dia_chack_01.isChecked();

                                //----------- 終日にチェックが入っていたら　1, チェックなし： 0
                                if(check) {
                                    i_dia_chack_01_num = 1;

                                } else {
                                    i_dia_chack_01_num = 0;
                                }

                            }
                        });


                        //------- OK の時の処理 ----------//
                        bilder.setPositiveButton("登録", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 作業項目　選択画面　へ　遷移
                                Intent intent = new Intent(getApplication(),Work_Choice.class);

                                //---------------- 担当者コード, 担当者名
                                // 担当者コード
                                intent.putExtra("get_TMNF_01", get_TMNF_01);
                                // 担当者名
                                intent.putExtra("get_TMNF_02", get_TMNF_02);

                                //---------------- アラートダイアログ　取得　値
                                // 「スピナー」　の　値
                                intent.putExtra("dia_edit_01_num", dia_edit_01_num);
                                // 「終日：　チェックボックス値：  1 => チェックあり 0 => チェックなし」
                                intent.putExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                                // 「担当者コード」　取得
                                intent.putExtra("dia_user_key", dia_user_key);

                                System.out.println("画面遷移時:::dia_user_key" + dia_user_key);

                                // 所属の　担当部署名
                                intent.putExtra("Busho_Code", Busho_Code);

                                // 所属部署　コード
                                intent.putExtra("get_TMNF_03",get_TMNF_03);
                                System.out.println("画面遷移時:::get_TMNF_03" + get_TMNF_03);

                                // ************* 大本　部署名　例；〇〇課
                                intent.putExtra("get_BUMF_01", get_BUMF_01);

                                if(i_dia_chack_01_num == 1) {
                                    //******* 作業フラグ 1:　アップデート *******
                                    Syuuzitu_Flg_Update(i_dia_chack_01_num, dia_user_key);

                                } else {
                                    //******** 作業担当者　アップデート
                                    Sagyou_busyo_code_Update(dia_user_key);
                                }


                                startActivity(intent);
                                //  startActivityForResult(intent, REQUESTCODE_TEST);

                            }

                        });

                        bilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // ダイアログの表示
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

                        //******************** アラートログの表示 処理 終了  ********************//

                        //    } //--------- END IF



                        //     finish();

                    } else {

                        /**
                         *  返却された　値   REQUESTCODE_SECOND = 100
                         */

                        get_result_num = 1;

                        // 作業項目　選択画面　へ　遷移
                        Intent intent = new Intent(getApplication(),Work_Choice.class);

                        //---------------- 担当者コード, 担当者名
                        // 担当者コード
                        intent.putExtra("get_TMNF_01_result", get_TMNF_01_result);
                        // 担当者名
                        intent.putExtra("get_TMNF_02_result", get_TMNF_02_result);

                        //---------------- アラートダイアログ　取得　値
                        // 「スピナー」　の　値
                        intent.putExtra("dia_edit_01_num_result", dia_edit_01_num_result);
                        // 「終日：　チェックボックス値：  1 => チェックあり 0 => チェックなし」

                        // 終日の場合は終日を入れる。
                        intent.putExtra("i_dia_chack_01_num_result", i_dia_chack_01_num_result);

                        intent.putExtra("get_result_num", get_result_num);


                        // 「担当者コード」　取得
                        //   intent.putExtra("dia_user_key_result", dia_user_key_result);

                        //-----------
                        intent.putExtra("get_TMNF_01",get_TMNF_01);
                        intent.putExtra("get_TMNF_02",get_TMNF_02);
                        //       intent.putExtra("dia_edit_01_num",dia_edit_01_num);
                        //       intent.putExtra("dia_user_key",dia_user_key);

                        intent.putExtra("dia_edit_01_num",s_tmp_01);
                        intent.putExtra("dia_user_key",s_tmp_02);

                        // ************* 大本　部署名　例；〇〇課
                        intent.putExtra("get_BUMF_01", get_BUMF_01);

                        startActivity(intent);
                        // startActivityForResult(intent, REQUESTCODE_TEST);

                    }

                }

                break;

            /**
             *   「？」　説明動画　へ　遷移
             */
            case R.id.home_lesson_btn_01 :

                Intent intent = new Intent(getApplicationContext(), WebViewActivity_04.class);
                startActivity(intent);

                break;

            case R.id.the_day_button:

                toastMake("⊕ ボタンをタップして「作業追加」を行ってください。",0,-200);

                break;



        }

        return true;

    }



    private void SHMF_CSV_IN() {

        err_view.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                String jim = "http://192.168.254.87/tana_phppost_file/get_csv/SHMF.csv"; // 社内

                // リクエスト　作成  // ****************** 商品マスター *********************
                Request request = new Request.Builder()
                        .url(JIM_Test[1])
                       // .url(TOYAMA_DATA[1])
                        .get()
                        .build();

                //　クライアント　作成
                OkHttpClient client = new OkHttpClient();

                // リクエストして　結果を受け取る
                try {

                    Response response = client.newCall(request).execute();

                    result = response.body().string();

                } catch(SocketTimeoutException e) {
             //       toastMake("ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください", 0, -200);
                    e.printStackTrace();
                    result = "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください";

                    System.out.println("1:" + result);

                } catch(RuntimeException e) {
                //    toastMake("ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください", 0, -200);
                    e.printStackTrace();
                    result = "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください";

                    System.out.println("2:" + result);

                } catch (ConnectionShutdownException e) {
               //     toastMake("ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください",0,-200);
                    e.printStackTrace();
                    result = "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください";

                    System.out.println("3:" + result);

                } catch(BindException e) {
               //     toastMake("ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください",0,-200);
                    e.printStackTrace();
                    result = "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください";

                    System.out.println("4:" + result);

                } catch (IOException e) {
                //    toastMake("ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください",0,-200);
                    e.printStackTrace();
                    result = "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください";

                    System.out.println("5:" + result);

                }


                return result;


            } //---------- END doInBackground

            @Override
            protected void onPostExecute(String result) {

                TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
                SQLiteDatabase db_shmf = helper.getReadableDatabase();

                //----------- 削除処理 ----------------
                // トランザクション　開始
                db_shmf.beginTransaction();
                try {

                    db_shmf.delete("SHMF_table", null, null);
                    // トランザクション　成功
                    db_shmf.setTransactionSuccessful();

                    Log.v("SHMF 商品マスターダウンロード", "商品マスターダウンロード完了");

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    //------------- トランザクション　完了
                    db_shmf.endTransaction();

                }

                String line = null;
                String[] RowData = null;

                // トランザクション　開始
                db_shmf.beginTransaction();

                try {

                    int count = 1;

                    BufferedReader reader = new BufferedReader(new StringReader(result));

                    while ((line = reader.readLine()) != null) {

                        RowData = line.split(",", -1);

                        ContentValues values = new ContentValues();

                        /*
                        values.put(TestOpenHelper.COLUMN_01, RowData[0]);
                        values.put(TestOpenHelper.COLUMN_02, RowData[1]);
                        values.put(TestOpenHelper.COLUMN_03, RowData[2]);
                        values.put(TestOpenHelper.COLUMN_04, RowData[3]);
                        values.put(TestOpenHelper.COLUMN_05, RowData[4]);
                        values.put(TestOpenHelper.COLUMN_06, RowData[5]);
                        values.put(TestOpenHelper.COLUMN_07, RowData[6]);
                        values.put(TestOpenHelper.COLUMN_08, RowData[7]);
                        values.put(TestOpenHelper.COLUMN_09, RowData[8]);

                         */

                        values.put("SHMF_c_01", RowData[0]);
                        values.put("SHMF_c_02", RowData[1]);
                        values.put("SHMF_c_03", RowData[2]);
                        values.put("SHMF_c_04", RowData[3]);
                        values.put("SHMF_c_05", RowData[4]);
                        values.put("SHMF_c_06", RowData[5]);
                        values.put("SHMF_c_07", RowData[6]);
                        values.put("SHMF_c_08", RowData[7]);
                        values.put("SHMF_c_09", RowData[8]);


                        db_shmf.insert("SHMF_table", null, values);

                        System.out.println("商品マスター テーブル DB １行　インサート　成功" + count + "件");

                        for (int i = 0; i <= 8; i++) {
                            System.out.println("項目" + i + "番" + RowData[i]);
                        }

                    }

                    // トランザクション　成功
                    db_shmf.setTransactionSuccessful();

                    toastMake("商品マスターファイル　ダウンロード「100%完了」 しました。」",0,-200);

                    csv_btn_02.setBackgroundColor(Color.parseColor("#FF4081"));
                    csv_btn_02.setTextColor(Color.parseColor("#ffffff"));

                } catch(SocketTimeoutException e) {
                    e.printStackTrace();
                    err_view.setVisibility(View.VISIBLE);
                    err_view.setText("ダウンロードエラー：：：" + "SocketTimeoutException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    return;

                } catch (ConnectionShutdownException e) {
                    e.printStackTrace();
                    err_view.setVisibility(View.VISIBLE);
                    err_view.setText("ダウンロードエラー：：：" + "ConnectionShutdownException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    return;

                } catch (IOException e) {
                    err_view.setVisibility(View.VISIBLE);
                    err_view.setText("ダウンロードエラー：：：" + "IOException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();
                    return;

                } catch (SQLException e) {
                    err_view.setVisibility(View.VISIBLE);
                    err_view.setText("ダウンロードエラー：：：" + "SQLException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();
                    return;

                } catch(ArrayIndexOutOfBoundsException e) {
                    err_view.setVisibility(View.VISIBLE);
                    err_view.setText("ダウンロードエラー：：：" + "ArrayIndexOutOfBoundsException\n" +
                            "ダウンロードエラーです。Wi-Fi環境を確認してダウンロードを開始してください。");
                    e.printStackTrace();
                    return;

                } finally {
                    // トランザクション　終了
                    db_shmf.endTransaction();
                    if(db_shmf != null) {
                        db_shmf.close();

                    }


                } //========== END finally


            } //----------- END onPostExecute


        }.execute();


    }  // --------------- ********** SHMF.csv インサート処理 ********** 終了 ------------


    private void SHMF_Null_Check() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        // SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteDatabase db_shmf_ch = helper.getReadableDatabase();

        //  String count = "SELECT count(*) FROM TNMF_table";

        //  Cursor cursor = db.rawQuery(count, null);

        // トランザクション　開始
        db_shmf_ch.beginTransaction();
        try {

            Cursor cursor = db_shmf_ch.rawQuery("SELECT count(*) FROM SHKB_table", null);
            cursor.moveToFirst();

            db_count = cursor.getInt(0);

            // トランザクション　完了
            db_shmf_ch.setTransactionSuccessful();

        } catch (SQLiteDatabaseLockedException e) {
            //SQLiteDatabaseLockedException

        } finally {
            // トランザクション完了
            db_shmf_ch.endTransaction();
            db_shmf_ch.close();
        }


    } //------------------ SHMF_Null_Check ------------>


    //-------------------- トースト作成 ----------------------
    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    //-------------------- トースト作成 02 ----------------------
    private void toastMake_02(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate(){
        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("MM 月 dd日 （E）");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }


    /**
     * 現在日時を yyyyMMddHHmmss 取得する
     */
    public static String getNowDate_02(){
        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
    


    /**
     *  部署　スピナーに　入れる「アイテム」を取得
     */

    public void getSpinner_01() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        ArrayList<String> spinner_item_01 = new ArrayList<>();

        int num = 0;
        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db.rawQuery("SELECT BUMF_c_01, BUMF_c_02 FROM BUMF_table;", null);

            while (cursor.moveToNext()) {

                if (num == 0) {
                    arr_item[1] = "「部署」選択";
                    spinner_item_01.add(arr_item[1]);
                } else {

                    int idx = cursor.getColumnIndex("BUMF_c_01");
                    arr_item[0] = cursor.getString(idx);

                    idx = cursor.getColumnIndex("BUMF_c_02");
                    arr_item[1] = cursor.getString(idx);

                    // ArrayList に　挿入　
                    spinner_item_01.add(arr_item[1]);

                    // 比較用にハッシュマップに挿入
                    spi_map_busyo.put(arr_item[0], arr_item[1]);

                }

                num++;

            } //-------- while END

            for(String str : spinner_item_01) {
                System.out.println("スピナーアイテム：" + str);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
            }
        }

        //　スピナー　部署コード
        //***
        /*
        spinner_busyo = findViewById(R.id.spinner_busyo);

        //---------- ArrayAdapter -------------//
        ArrayAdapter<String> spi_3adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_item_01);
        spi_3adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // スピナーセット　
        spinner_busyo.setAdapter(spi_3adapter);

         */

    } //-------------- getSpinner_01 END



    /**
     * 　端末 Back Btn を　押された処理
     */
    @Override
    public void onBackPressed() {

        //------- 最新PDF を　表示させない
        Snac_Flg = false;

        //***** エラーメッセージ　非表示
        err_view.setVisibility(View.GONE);
        err_view.setText("");

        //------- ログアウト判定用 function　Send_data_Flg_SELECT ()　　0:未送信  1:送信済み
        Send_data_Flg_SELECT();

        //--------------- アラートダイヤログ　タイトル　設定 ---------------//
        // タイトル
        TextView titleView;
        titleView = new TextView(Home.this);
        titleView.setText("ログアウト確認");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.red));
        titleView.setPadding(20, 30, 20, 30);
        titleView.setGravity(Gravity.CENTER);

        //-------------- アラートログの表示 開始 -------------- //
        android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(Home.this);

        // ダイアログの項目
        bilder.setCustomTitle(titleView);

        // メッセージ内容　セット
        String msg_bilder = "ログアウトしますか？";
        bilder.setMessage(msg_bilder);

        bilder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(Logout_GO_str.equals("1")) {

                    //************ ファイル送信済みの場合は　ログアウト OK
                    finish();

                } else {

                    //************ ファイル未送信の場合は ログアウトNG

                    toastMake("「ログアウト」を行うと作業部署がリセットされます。\n\n" +
                            "ファイル送信を行ってから、ログアウトをしてください。", 0, -200);

                    return;
                }

            }
        });

        bilder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    }

    /**
     *  リスタートした時の処理
     */
    @Override
    protected void onRestart() {

        //------- 最新PDF を　表示させない
        Snac_Flg = false;
        //  snackbar.dismiss();

        //------------ ダイアログ の　値を　クリア
        i_dia_chack_01_num = 0;
        dia_edit_01_num = "";
        //   dia_edit_01.setText("");
        dia_user_key = "";
        //------------ ダイアログ の　値を　クリア END

        //------------ ダイアログスピナー　値　クリア
        spinner_item_02.clear();

        //***** エラーメッセージ　非表示
        err_view.setVisibility(View.GONE);
        err_view.setText("");

        /**
         *  Syuuzitu_Flg_SELECT   終日チェック　値取得
         */
        Syuuzitu_Flg_SELECT();

        /**
         *  *************** 遅刻・早退　残業 SELECT ***************
         *  Tikoku_Soutai_SELECT -> 遅刻　・　早退
         *
         *  Zangyou_SELECT -> 残業
         *
         */
        /*
        Tikoku_Soutai_SELECT();
        Tikoku_Soutai_SELECT_02();

         */

        Tikoku_Soutai_SELECT_03();

        Zangyou_SELECT();

        loadMyList();

        Send_TB_id_COUNT(); //****** 定期送信　フラグ　確認


        //------------ 一旦　時間オーバー　判定 ************
        String tmp_pro_time = pro_time.getText().toString();
        int tmp_pro_time_i = Integer.parseInt(tmp_pro_time);

        if(tmp_pro_time_i <= 480) {
          //  pro_time.setTextColor(Color.parseColor("#3eaad4")); // テキスト「青」
            pro_time.setBackgroundColor(Color.parseColor("#00ffffff")); // 透明
            pro_time.setTextColor(Color.parseColor("#ffffff"));
            pro_time.setText(tmp_pro_time);

            /**
             *  フラグを　０　に戻す
             */
            Time_Sum_Flg_Off();

        } else {
            pro_time.setTextColor(Color.parseColor("#B20000"));
            pro_time.setText(tmp_pro_time);

            /**
             *  合計時間が　480 分　以上の場合は 1:  480以下は　0
             */
            Time_Sum_Flg_On();
        }


        //******** 時間　合計に加算
        Send_table_01_Time_Sum();



        super.onRestart();
    }


    /**
     *   app_finish_flag が ture なら　アクティビティを　終了させる
     */
    @Override
    protected void onResume()
    {

        /**
         *  *************** 遅刻・早退　残業 SELECT ***************
         *  Tikoku_Soutai_SELECT -> 遅刻　・　早退
         *
         *  Zangyou_SELECT -> 残業
         *
         */
        /*
        Tikoku_Soutai_SELECT();
        Tikoku_Soutai_SELECT_02();

         */

        //***** エラーメッセージ　非表示
        err_view.setVisibility(View.GONE);
        err_view.setText("");

        Tikoku_Soutai_SELECT_03();

        Zangyou_SELECT();

        Send_TB_id_COUNT(); //****** 定期送信　フラグ　確認

        super.onResume();
        if ( app_finish_flag ) {
            finish();
        } else {
            super.onResume();
        }
    }


    /**
     * 指定した数のList<String>を生成
     *
     * @param cnt
     * @return　文字列リスト
     */
    private List<String> mkDataList(int cnt) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < cnt; i++) {
            list.add("選択" + (int) (i + 1));
        }
        return list;
    }


    private void init() {

        send_view_Move = findViewById(R.id.send_view_Move);

        // ユーザー名　格納用 textview
        user_name = findViewById(R.id.user_name);
        // 部署コード　格納用 textview
        //    busyo_text_box = findViewById(R.id.busyo_text_box);
        busyo_view = findViewById(R.id.busyo_view);

        // 戻るボタン
        back_btn_y = findViewById(R.id.back_btn_y);

        //     webview_01 = findViewById(R.id.webview_01);

        // CSV ダウンロード用ボタン
        csv_btn_02 = (MaterialButton) findViewById(R.id.csv_btn_02);

        // 時間表示用
        date_time_text = findViewById(R.id.date_time_text);

        // テスト用　リストビュー
        listView_01 = findViewById(R.id.listView_01);

        // 遅刻時間
        input_late_time = findViewById(R.id.input_late_time);
   //     view_late_time = findViewById(R.id.view_late_time);

        //********* 遅刻時間　を　入力　不可
        input_late_time.setFocusableInTouchMode(false);
        input_late_time.setFocusable(false);
        input_late_time.setEnabled(false);

        //------ 数字入力　設定
        input_late_time.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 残業時間
        input_over_time = findViewById(R.id.input_over_time);

        //****** 残業時間　入力　不可
        input_over_time.setEnabled(false);
        input_over_time.setFocusable(false);
        input_over_time.setFocusableInTouchMode(false);

        //------ 数字入力　設定
        input_over_time.setInputType(InputType.TYPE_CLASS_NUMBER);

        //-------- スタッツ表示用
        /*
        statu_01 = findViewById(R.id.statu_01);
        statu_02 = findViewById(R.id.statu_02);
         */
        stuatu_img_ok = (ImageButton) findViewById(R.id.stuatu_img_ok);
        stuatu_img_ng = (ImageButton) findViewById(R.id.stuatu_img_ng);

        stuatu_img_ok.setVisibility(View.GONE);
        stuatu_img_ng.setVisibility(View.GONE);

        tasuku_max = findViewById(R.id.tasuku_max);

        //******** 遅刻・早退　用
        late_time = (Button) findViewById(R.id.late_time);
        //********
        over_time = (Button) findViewById(R.id.over_time);

        //***** エラー表示用
        err_view = (TextView) findViewById(R.id.err_view);
        err_view.setText("");
        err_view.setVisibility(View.GONE);


    }


    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */
    private void loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB();

        // DB データを取得
        Cursor c = dbAdapter.getDB(columns);

        if(c.moveToFirst()) {

            do {

                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem = new MyListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8),
                        c.getString(9),
                        c.getString(10),
                        c.getString(11),
                        c.getString(12),
                        c.getString(13),
                        c.getString(14),
                        c.getString(15),
                        c.getString(16),
                        c.getString(17),
                        c.getString(18),
                        c.getString(19),
                        c.getString(20), //  開始時間
                        c.getString(21), //　終了時間
                        c.getString(22),  // *******　グラフID && 作業 ID
                        c.getString(23), // yyyyMMdd
                        c.getString(24), // 色段取時間
                        c.getString(25), // 型段取時間
                        c.getString(26)  // 機械コード
                );


                items.add(myListItem);

            } while(c.moveToNext());


            // タスク合計
            int size = items.size();
            tasuku_max.setText("タスク数：" + String.valueOf(size));

        }

        c.close();
        dbAdapter.closeDB();

        listView_01.setAdapter(myBaseAdapter);

        //****************** 　合計　表示 ***************//

        int size_num = items.size();
        tasuku_max.setText("タスク数：" + String.valueOf(size_num));


        myBaseAdapter.notifyDataSetChanged();   // Viewの更新

    }


    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */

    public class MyBaseAdapter extends BaseAdapter {

        private Context context;
        private List<MyListItem> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {

            FrameLayout frameLayout;

            // スタッツ確認　左側　アイコン
            ImageButton list_image_icon;
            // 削除ボタン　バケツアイコン
            ImageButton list_image_icon_delete;

            TextView item_01;
            TextView item_02;
            TextView item_03;
            TextView item_04;
            TextView item_05;
            TextView item_06;
            // 追加　商品備考
            TextView item_07;

            TextView item_001;
            TextView item_002;
            TextView item_003;
            TextView item_004;
            TextView item_005;
            TextView item_006;
            // 追加　商品備考
            TextView item_007;

        //    ImageView imageView;

        }

        public MyBaseAdapter(Context context,List<MyListItem>items) {
            this.context = context;
            this.items = items;
        }

        // Listの数
        @Override
        public int getCount() {
            return items.size();
        }

        // index or オブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        // index を　他の index に返す
        @Override
        public long getItemId(int position) {
            return position;
        }

        //------- 新しいデータが表示されるタイミングで呼び出される -------
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;

            // リスト 降順   int item_last = items.size() - 1  - position;

            // データ取得
            myListItem = items.get(position);

            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_listview_02,parent,false);

                // フレームレイアウト
                FrameLayout list_flame = (FrameLayout) view.findViewById(R.id.fl_left);

                // イメージボタン 左側　アイコン　（送信　スタッツ　確認用）
                ImageButton list_image_btn = (ImageButton) view.findViewById(R.id.list_image_icon);
                // イメージボタン　削除用　バケツアイコン
                ImageButton list_image_btn_delete = (ImageButton) view.findViewById(R.id.list_image_icon_delete);

                TextView list_text_view_01 = (TextView) view.findViewById(R.id.list_item_001);
                TextView list_text_view_02 = (TextView) view.findViewById(R.id.list_item_002);
                TextView list_text_view_03 = (TextView) view.findViewById(R.id.list_item_003);
                TextView list_text_view_04 = (TextView) view.findViewById(R.id.list_item_004);
                TextView list_text_view_05 = (TextView) view.findViewById(R.id.list_item_005);
                TextView list_text_view_06 = (TextView) view.findViewById(R.id.list_item_006);
                // 品目備考　追加
                TextView list_text_view_07 = (TextView) view.findViewById(R.id.list_item_007);

                // 作業番号　表示
                TextView list_text_view_001 = (TextView) view.findViewById(R.id.list_item_0001);
                // 商品名
                TextView list_text_view_002 = (TextView) view.findViewById(R.id.list_item_0002);
                // 予定数
                TextView list_text_view_003 = (TextView) view.findViewById(R.id.list_item_0003);
                // 総生産数
                TextView list_text_view_004 = (TextView) view.findViewById(R.id.list_item_0004);
                // 段取時間
                TextView list_text_view_005 = (TextView) view.findViewById(R.id.list_item_0005);
                // 作業時間
                TextView list_text_view_006 = (TextView) view.findViewById(R.id.list_item_0006);

                //***** 品目備考　追加
                TextView list_text_view_007 = (TextView) view.findViewById(R.id.list_item_0007);


         //       ImageView imageView = (ImageView) view.findViewById(R.id.list_icon);


                // holder に　view を持たせておく
                holder = new ViewHolder();


                holder.item_01 = list_text_view_01;
                holder.item_02 = list_text_view_02;
                holder.item_03 = list_text_view_03;
                holder.item_04 = list_text_view_04;
                holder.item_05 = list_text_view_05;
                holder.item_06 = list_text_view_06;
                // 品目備考　追加
                holder.item_07 = list_text_view_07;

                // 作業番号
                holder.item_001 = list_text_view_001;
                // 商品名
                holder.item_002 = list_text_view_002;
                // 予定数
                holder.item_003 = list_text_view_003;
                // 総生産数
                holder.item_004 = list_text_view_004;
                // 段取時間
                holder.item_005 = list_text_view_005;
                // 作業時間
                holder.item_006 = list_text_view_006;

                //************* 追加　品目備考
                holder.item_007 = list_text_view_007;

                // フレームレイアウト
                holder.frameLayout = list_flame;

                // Image Buttton
                holder.list_image_icon = list_image_btn;
                // 削除用　バケツ　イメージボタン
                holder.list_image_icon_delete = list_image_btn_delete;

        //      holder.imageView = imageView;


                view.setTag(holder);

            } else {

                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
                //     convertView.setBackgroundColor(getResources().getColor(R.color.back_color_02));
            }

            //------- holder の　コンポーネント　と　データ　を紐づける

            //------- ID

            // ------ 品目区分　取得
            hinmok_k = myListItem.getHinmoku_k();


            // ------ 品目コード 取得  A-00  SG-00 など
            HinMoku_C = myListItem.getHinmoku_c();

            System.out.println("********* HinMoku_C *****************" + HinMoku_C);

            if(!(HinMoku_C.contains("SG")) && hinmok_k.contains("9")) {

                String tmp = String.valueOf(myListItem.getSend_id());

                //********** 作業番号 セット ***********
              //  holder.item_001.setText(Saghou_Code + "-0" + tmp);
                String S_id = myListItem.getGR_id();
                holder.item_001.setText(S_id);

                holder.frameLayout.setBackgroundColor(Color.parseColor("#FF4081"));

                //--------  品名
                holder.item_002.setText(myListItem.getHinmoku_name());
                holder.item_002.setTextColor(Color.parseColor("#FF4081"));
                //------ 予定数量
                holder.item_003.setVisibility(View.GONE);
                //------- 総生産数
                holder.item_004.setVisibility(View.GONE);
                //------ 段取時間
                holder.item_005.setText(myListItem.getDandori_time());
                holder.item_005.setTextColor(Color.parseColor("#FF4081"));
                //------ 作業時間
                holder.item_006.setText(myListItem.getSagyou_time());
                holder.item_006.setTextColor(Color.parseColor("#FF4081"));

                holder.item_03.setVisibility(View.GONE);
                holder.item_04.setVisibility(View.GONE);

                //****** 品目備考　追加
                holder.item_007.setVisibility(View.GONE);
                holder.item_07.setVisibility(View.GONE);

                //------------------ Image Button 用 ---------------------

                // 最終フラグ 1 :送信済み 0:未送信
                String Send_flg = String.valueOf(myListItem.getSaisou_Flg());
                int Send_flg_i = Integer.parseInt(Send_flg);

                String msg;
                if(Send_flg_i == 0) {
                    msg = "ファイル送信状況：：：「未送信」" + "\n" + "作業番号:::" + S_id;
                } else {
                    msg = "ファイル送信状況：：：「送信済み」" + "\n" + "作業番号:::" + S_id;
                }

                /**
                 *  送信　スタッツ　確認
                 */
                holder.list_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastMake_02(msg, 0, -200);
                    }
                });

                /**
                 * --------- バケツ　削除ボタン
                 */
                holder.list_image_icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //-------------- アラートログ　削除　-----------
                        // タイトル
                        TextView textView;
                        textView = new TextView(Home.this);
                        textView.setText("タスクの削除");
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);

                        //--------------　アラートログの表示　開始
                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------- カスタムタイトル　セット
                        bilder.setCustomTitle(textView);
                        //--------- メッセージのセット
                        bilder.setMessage("選択されたタスクを削除しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------------　「いいえ」　の処理
                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------ 「はい」　タスク　削除処理

                                myListItem = items.get(position);
                                int listId = myListItem.getId();

                                dbAdapter.openDB();

                                dbAdapter.selectDelete(String.valueOf(listId));
                                //********* グラフ　テーブル削除
                                dbAdapter.selectDelete_02(String.valueOf(listId));

                                Log.d("ログ： アイテムクリック削除　OK",String.valueOf(listId));

                                dbAdapter.closeDB();


                                //******* 遅刻・早退 select
                                /*
                                Tikoku_Soutai_SELECT();
                                Tikoku_Soutai_SELECT_02();

                                 */

                                Tikoku_Soutai_SELECT_03();
                                //******* 残業 select
                                Zangyou_SELECT();

                                loadMyList();

                                Send_table_01_Time_Sum();


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



                    }
                });

                /***
                 * **********************　手入力　画面 **************************
                 */
            } else if(HinMoku_C.contains("SG00")) {

                String tmp = String.valueOf(myListItem.getSend_id());

                //********** 作業番号 セット ***********
                //  holder.item_001.setText(Saghou_Code + "-0" + tmp);
                String S_id = myListItem.getGR_id();
                holder.item_001.setText(S_id);

                holder.frameLayout.setBackgroundColor(Color.parseColor("#F0BC08"));

                //--------  品名
                holder.item_002.setText(myListItem.getHinmoku_name());
                holder.item_002.setTextColor(Color.parseColor("#F0BC08"));
                //------ 予定数量
                holder.item_003.setVisibility(View.GONE);
                //------- 総生産数
                holder.item_004.setVisibility(View.GONE);
                //------ 段取時間
                holder.item_005.setText(myListItem.getDandori_time());
                holder.item_005.setTextColor(Color.parseColor("#F0BC08"));
                //------ 作業時間
                holder.item_006.setText(myListItem.getSagyou_time());
                holder.item_006.setTextColor(Color.parseColor("#F0BC08"));

                holder.item_03.setVisibility(View.GONE);
                holder.item_04.setVisibility(View.GONE);

                //****** 品目備考　追加
                holder.item_007.setVisibility(View.GONE);
                holder.item_07.setVisibility(View.GONE);


                //------------------ Image Button 用 ---------------------


                // 最終フラグ 1 :送信済み 0:未送信
                String Send_flg = String.valueOf(myListItem.getSaisou_Flg());
                int Send_flg_i = Integer.parseInt(Send_flg);

                String msg;
                if (Send_flg_i != 0) {
                    msg = "ファイル送信状況：：：「送信済み」" + "\n" + "作業番号:::" + S_id;
                } else {
                    msg = "ファイル送信状況：：：「未送信」" + "\n" + "作業番号:::" + S_id;
                }

                /**
                 *  送信　スタッツ　確認
                 */
                holder.list_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastMake_02(msg, 0, -200);
                    }
                });

                /**
                 * --------- バケツ　削除ボタン
                 */
                holder.list_image_icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //-------------- アラートログ　削除　-----------
                        // タイトル
                        TextView textView;
                        textView = new TextView(Home.this);
                        textView.setText("タスクの削除");
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);

                        //--------------　アラートログの表示　開始
                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------- カスタムタイトル　セット
                        bilder.setCustomTitle(textView);
                        //--------- メッセージのセット
                        bilder.setMessage("選択されたタスクを削除しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------------　「いいえ」　の処理
                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------ 「はい」　タスク　削除処理

                                myListItem = items.get(position);
                                int listId = myListItem.getId();

                                dbAdapter.openDB();

                                dbAdapter.selectDelete(String.valueOf(listId));

                                //********* グラフ　テーブル削除
                                dbAdapter.selectDelete_02(String.valueOf(listId));

                                Log.d("ログ： アイテムクリック削除　OK", String.valueOf(listId));

                                dbAdapter.closeDB();

                                //******* 遅刻・早退 select
                                /*
                                Tikoku_Soutai_SELECT();
                                Tikoku_Soutai_SELECT_02();
                                */

                                Tikoku_Soutai_SELECT_03();
                                //******* 残業 select
                                Zangyou_SELECT();

                                loadMyList();

                                //**************** 合計時間　計算 遅延 0.77 秒
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Send_table_01_Time_Sum();
                                    }
                                }, 770);

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

                    }
                });

                /**
                 * 　　「手打ち」　で　入力された場合
                 */


            }else if(HinMoku_C.contains("SG01") || HinMoku_C.contains("SG02") ) {

                String tmp = String.valueOf(myListItem.getSend_id());

                //********** 作業番号 セット ***********
                //  holder.item_001.setText(Saghou_Code + "-0" + tmp);
                String S_id = myListItem.getGR_id();
                holder.item_001.setText(S_id);

                holder.frameLayout.setBackgroundColor(Color.parseColor("#0F9856"));

                //--------  品名
                holder.item_002.setText(myListItem.getHinmoku_name());
                holder.item_002.setTextColor(Color.parseColor("#0F9856"));
                //------ 予定数量
                holder.item_003.setVisibility(View.GONE);
                //------- 総生産数
                holder.item_004.setVisibility(View.GONE);
                //------ 段取時間
                holder.item_005.setText(myListItem.getDandori_time());
                holder.item_005.setTextColor(Color.parseColor("#0F9856"));
                //------ 作業時間
                holder.item_006.setText(myListItem.getSagyou_time());
                holder.item_006.setTextColor(Color.parseColor("#0F9856"));

                holder.item_03.setVisibility(View.GONE);
                holder.item_04.setVisibility(View.GONE);

                //****** 品目備考　追加
                holder.item_007.setVisibility(View.GONE);
                holder.item_07.setVisibility(View.GONE);

                //------------------ Image Button 用 ---------------------


                // 最終フラグ 1 :送信済み 0:未送信
                String Send_flg = String.valueOf(myListItem.getSaisou_Flg());
                int Send_flg_i = Integer.parseInt(Send_flg);

                String msg;
                if (Send_flg_i != 0) {
                    msg = "ファイル送信状況：：：「送信済み」" + "\n" + "作業番号:::" + S_id;
                } else {
                    msg = "ファイル送信状況：：：「未送信」" + "\n" + "作業番号:::" + S_id;
                }

                /**
                 *  送信　スタッツ　確認
                 */
                holder.list_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastMake_02(msg, 0, -200);
                    }
                });

                /**
                 * --------- バケツ　削除ボタン
                 */
                holder.list_image_icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //-------------- アラートログ　削除　-----------
                        // タイトル
                        TextView textView;
                        textView = new TextView(Home.this);
                        textView.setText("タスクの削除");
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);

                        //--------------　アラートログの表示　開始
                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------- カスタムタイトル　セット
                        bilder.setCustomTitle(textView);
                        //--------- メッセージのセット
                        bilder.setMessage("選択されたタスクを削除しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------------　「いいえ」　の処理
                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------ 「はい」　タスク　削除処理

                                myListItem = items.get(position);
                                int listId = myListItem.getId();

                                dbAdapter.openDB();

                                dbAdapter.selectDelete(String.valueOf(listId));

                                //********* グラフ　テーブル削除
                                dbAdapter.selectDelete_02(String.valueOf(listId));

                                Log.d("ログ： アイテムクリック削除　OK", String.valueOf(listId));

                                dbAdapter.closeDB();

                                //******* 遅刻・早退 select
                                /*
                                Tikoku_Soutai_SELECT();
                                Tikoku_Soutai_SELECT_02();


                                 */
                                Tikoku_Soutai_SELECT_03();

                                //******* 残業 select
                                Zangyou_SELECT();

                                loadMyList();

                                //**************** 合計時間　計算 遅延 0.77 秒
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Send_table_01_Time_Sum();
                                    }
                                }, 770);

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

                    }
                });

                /**
                 * 　　「遅刻」　「早退」
                 */

            } else if(HinMoku_C.contains("SG03")) { //******************* 残業

                String tmp = String.valueOf(myListItem.getSend_id());

                //********** 作業番号 セット ***********
                //  holder.item_001.setText(Saghou_Code + "-0" + tmp);
                String S_id = myListItem.getGR_id();
                holder.item_001.setText(S_id);

                holder.frameLayout.setBackgroundColor(Color.parseColor("#671fa2"));

                //--------  品名
                holder.item_002.setText(myListItem.getHinmoku_name());
                holder.item_002.setTextColor(Color.parseColor("#671fa2"));
                //------ 予定数量
                holder.item_003.setVisibility(View.GONE);
                //------- 総生産数
                holder.item_004.setVisibility(View.GONE);
                //------ 段取時間
                holder.item_005.setText(myListItem.getDandori_time());
                holder.item_005.setTextColor(Color.parseColor("#671fa2"));
                //------ 作業時間
                holder.item_006.setText(myListItem.getSagyou_time());
                holder.item_006.setTextColor(Color.parseColor("#671fa2"));

                holder.item_03.setVisibility(View.GONE);
                holder.item_04.setVisibility(View.GONE);

                //****** 品目備考　追加
                holder.item_007.setVisibility(View.GONE);
                holder.item_07.setVisibility(View.GONE);

                //------------------ Image Button 用 ---------------------


                // 最終フラグ 1 :送信済み 0:未送信
                String Send_flg = String.valueOf(myListItem.getSaisou_Flg());
                int Send_flg_i = Integer.parseInt(Send_flg);

                String msg;
                if (Send_flg_i != 0) {
                    msg = "ファイル送信状況：：：「送信済み」" + "\n" + "作業番号:::" + S_id;
                } else {
                    msg = "ファイル送信状況：：：「未送信」" + "\n" + "作業番号:::" + S_id;
                }

                /**
                 *  送信　スタッツ　確認
                 */
                holder.list_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastMake_02(msg, 0, -200);
                    }
                });

                /**
                 * --------- バケツ　削除ボタン
                 */
                holder.list_image_icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //-------------- アラートログ　削除　-----------
                        // タイトル
                        TextView textView;
                        textView = new TextView(Home.this);
                        textView.setText("タスクの削除");
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);

                        //--------------　アラートログの表示　開始
                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------- カスタムタイトル　セット
                        bilder.setCustomTitle(textView);
                        //--------- メッセージのセット
                        bilder.setMessage("選択されたタスクを削除しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------------　「いいえ」　の処理
                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------ 「はい」　タスク　削除処理

                                myListItem = items.get(position);
                                int listId = myListItem.getId();

                                dbAdapter.openDB();

                                dbAdapter.selectDelete(String.valueOf(listId));

                                //********* グラフ　テーブル削除
                                dbAdapter.selectDelete_02(String.valueOf(listId));

                                Log.d("ログ： アイテムクリック削除　OK", String.valueOf(listId));

                                dbAdapter.closeDB();

                                //******* 遅刻・早退 select
                                /*
                                Tikoku_Soutai_SELECT();
                                Tikoku_Soutai_SELECT_02();

                                 */

                                Tikoku_Soutai_SELECT_03();
                                //******* 残業 select
                                Zangyou_SELECT();

                                loadMyList();

                                //**************** 合計時間　計算 遅延 0.77 秒
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Send_table_01_Time_Sum();
                                    }
                                }, 770);

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

                    }
                });

                /**
                 * 　END *****　残業  *******
                 */


            } else {

                /**
                 *  現品票　バーコード、QRコード　など
                 */

                String tmp = String.valueOf(myListItem.getSend_id());

                //********** 作業番号 セット ***********
                //  holder.item_001.setText(Saghou_Code + "-0" + tmp);
                String S_id = myListItem.getGR_id();
                holder.item_001.setText(S_id);

                holder.frameLayout.setBackgroundColor(Color.parseColor("#3EAAD4"));

                holder.item_03.setVisibility(View.VISIBLE);
                holder.item_04.setVisibility(View.VISIBLE);
                holder.item_003.setVisibility(View.VISIBLE);
                holder.item_004.setVisibility(View.VISIBLE);
                //****** 品目備考　追加
                holder.item_007.setVisibility(View.VISIBLE);
                holder.item_07.setVisibility(View.VISIBLE);

                //--------  品名
                holder.item_002.setText(myListItem.getHinmoku_name());
                holder.item_002.setTextColor(Color.parseColor("#3EAAD4"));
                //------ 予定数量
                holder.item_003.setText(myListItem.getYotei_num());
                holder.item_003.setTextColor(Color.parseColor("#3EAAD4"));
                //------- 総生産数
                holder.item_004.setText(myListItem.getSouseisan_num());
                holder.item_004.setTextColor(Color.parseColor("#3EAAD4"));
                //------ 段取時間
                holder.item_005.setText(myListItem.getDandori_time());
                holder.item_005.setTextColor(Color.parseColor("#3EAAD4"));
                //------ 作業時間
                holder.item_006.setText(myListItem.getSagyou_time());
                holder.item_006.setTextColor(Color.parseColor("#3EAAD4"));

                //****** 品目備考　追加
                holder.item_007.setText(myListItem.getHinmoku_bikou());
                holder.item_007.setTextColor(Color.parseColor("#3EAAD4"));


                //------------------ Image Button 用 ---------------------

                // 作業番号
                String sagyou_C = Saghou_Code + "-0" + tmp;

                // 最終フラグ 1 :送信済み 0:未送信
                String Send_flg = String.valueOf(myListItem.getSaisou_Flg());
                int Send_flg_i = Integer.parseInt(Send_flg);

                String msg;
                if(Send_flg_i != 0) {
                    msg = "ファイル送信状況：：：「送信済み」" + "\n" + "作業番号:::" + sagyou_C;
                } else {
                    msg = "ファイル送信状況：：：「未送信」" + "\n" + "作業番号:::" + sagyou_C;
                }

                /**
                 *  送信　スタッツ　確認
                 */
                holder.list_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastMake_02(msg, 0, -200);
                    }
                });

                /**
                 * --------- バケツ　削除ボタン
                 */
                holder.list_image_icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //-------------- アラートログ　削除　-----------
                        // タイトル
                        TextView textView;
                        textView = new TextView(Home.this);
                        textView.setText("タスクの削除");
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);

                        //--------------　アラートログの表示　開始
                        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);

                        //--------- カスタムタイトル　セット
                        bilder.setCustomTitle(textView);
                        //--------- メッセージのセット
                        bilder.setMessage("選択されたタスクを削除しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------------　「いいえ」　の処理
                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //------------ 「はい」　タスク　削除処理

                                myListItem = items.get(position);
                                int listId = myListItem.getId();

                                dbAdapter.openDB();

                                dbAdapter.selectDelete(String.valueOf(listId));

                                //********* グラフ　テーブル削除
                                dbAdapter.selectDelete_02(String.valueOf(listId));

                                Log.d("ログ： アイテムクリック削除　OK",String.valueOf(listId));

                                dbAdapter.closeDB();


                                //******* 遅刻・早退 select
                                /*
                                Tikoku_Soutai_SELECT();
                                Tikoku_Soutai_SELECT_02();

                                 */

                                Tikoku_Soutai_SELECT_03();

                                //******* 残業 select
                                Zangyou_SELECT();

                                loadMyList();

                                //**************** 合計時間　計算 遅延 0.77 秒
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Send_table_01_Time_Sum();
                                    }
                                }, 770);

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

                    }
                });


            }

            return view;
        }

    } //---------- MyBaseAdapter END

    /**
     * 　--------------　Send_table_01　 作業時間、　段取り時間　の　合計値を　取り出す　
     */

    private void Send_table_01_Time_Sum() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_sum = helper.getReadableDatabase();

        //--------- arr_item[0] = 段取り時間  arr_item[1] = 作業時間
        String [] arr_item = new String[4];
        Cursor cursor = null;

         try {

             cursor = db_sum.rawQuery("select  sum(send_col_09) , sum(send_col_10) ,sum(send_col_24), sum(send_col_25)" +
                     "from Send_table_01 where send_col_06 != '残業';", null);
             //------ カーソル null && 0 対策
             if(cursor!=null && cursor.getCount() > 0) {

                 if (cursor.moveToFirst()) {
                     do {
                         //------ 段取り時間
                         //    int idx = cursor.getColumnIndex(0);
                         arr_item[0] = cursor.getString(0); // 段取り時間　合計
                         arr_item[1] = cursor.getString(1); // 作業時間　合計
                         arr_item[2] = cursor.getString(2); // 色段取時間　合計
                         arr_item[3] = cursor.getString(3); // 型段取時間　合計

                         Dandori_Time_Sum = arr_item[0];
                         Sagyou_Time_Sum = arr_item[1];
                         Iro_Time_Sum = arr_item[2];
                         Kata_Time_Sum = arr_item[3];

                         System.out.println("時間集計:::" + "作業時間:::" + arr_item[0] +
                                 "段取り時間:::" + arr_item[1]);

                     } while (cursor.moveToNext()); //------------- END while

                 } //------ END if moveToFirst

                 // ********** 合計時間　表示　TextView
                 pro_time = (TextView) findViewById(R.id.pro_time);

                 // ********** 　合計値　計算 ********
                 if(Dandori_Time_Sum != null) {
                     Dandori_Time_Sum_i = Integer.parseInt(Dandori_Time_Sum); // 段取り時間
                 } else {
                     Dandori_Time_Sum_i = 0;
                 }

                 if(Sagyou_Time_Sum != null) {
                     Sagyou_Time_Sum_i = Integer.parseInt(Sagyou_Time_Sum); // 作業時間
                 } else {
                     Sagyou_Time_Sum_i = 0;
                 }

                 if(Iro_Time_Sum != null) {
                     Iro_Time_Sum_i = Integer.parseInt(Iro_Time_Sum); // 色段取時間
                 } else {
                     Iro_Time_Sum_i = 0;
                 }

                 if(Kata_Time_Sum != null) {
                     Kata_Time_Sum_i = Integer.parseInt(Kata_Time_Sum); // 型段取時間
                 } else {
                     Kata_Time_Sum_i = 0;
                 }

                 //********************** 合計時間　取得 「段取り時間」「作業時間」「色段取時間」「型段取時間」
                 int Time_Sum_i = Dandori_Time_Sum_i + Sagyou_Time_Sum_i + Iro_Time_Sum_i + Kata_Time_Sum_i;

                 String Time_Sum = String.valueOf(Time_Sum_i);

                 //-------- 値が　「空」　だった場合
                 if (Time_Sum_i < 0) {
                     pro_time.setText("0");

                     //------- 「空」　じゃなかった場合
                 } else if (Time_Sum_i > 480) {

                     /**
                      *  合計時間　が　480 分　を　超えていた場合は　ダイアログを出して　確認する
                      */

                     //-------　タイトル
                     TextView titleView;
                     // アクティビティ名を入れる
                     titleView = new TextView(Home.this);
                     titleView.setText("合計時間オーバー");
                     titleView.setTextSize(20);
                     titleView.setTextColor(Color.WHITE);
                     titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                     titleView.setPadding(20, 20, 20, 20);
                     titleView.setGravity(Gravity.CENTER);

                     //-------- アラートログの表示 開始 ----------
                     android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(Home.this);

                     /**
                      *  ダイアログの項目セット
                      */
                     //------- タイトルセット
                     bilder.setCustomTitle(titleView);

                     //------- メッセージセット
                     bilder.setMessage("合計時間が 「480分」　を超えています。このままの値で大丈夫ですか？");

                     bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             //------------ 時間　オーバー　しているけど OK ************
                             pro_time.setTextColor(Color.parseColor("#B20000"));
                             pro_time.setBackgroundColor(Color.parseColor("#F0BC08"));
                             pro_time.setPadding(5,5,5,5);
                             pro_time.setTextSize(26);
                             pro_time.setText(Time_Sum);

                             //******* 480 分　以上のフラグ　ON => Time_Sum_Flg 1:
                             Time_Sum_Flg_On();

                         }
                     });

                             bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {

                                     //----------- 時間オーバー NG　
                                     pro_time.setTextColor(Color.parseColor("#B20000"));
                                    // pro_time.setText("Error");
                                     pro_time.setText(Time_Sum);

                                     toastMake("タスクを確認して、「作業時間」を" +
                                             "修正してください。",0,-200);

                                     //******* 480 分　以上のフラグ　ON => Time_Sum_Flg 1:
                                     Time_Sum_Flg_On();


                                 }
                             });

                             android.app.AlertDialog dialog = bilder.create();
                             dialog.show();

                             //******************************************* ボタン　配色　変更　FF4081　＝＞　ピンク系
                             //********* ボタン はい **********
                             dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
                             //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

                             //********* ボタン いいえ **********
                             dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
                             //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));


                 } else {

                     /**
                      * ＊＊＊＊＊＊＊＊＊ 合計時間　が 480分を　超えていない場合　＊＊＊＊＊＊＊＊＊
　                      */
                     //******* 文字色を　「青色」　にする。
                     pro_time.setTextColor(Color.parseColor("#ffffff"));
                     pro_time.setBackgroundColor(Color.parseColor("#00ffffff")); // 背景　透明色
                     pro_time.setPadding(5,5,5,5);
                     pro_time.setTextSize(26);
                     pro_time.setText(Time_Sum);

                     /**
                      *  合計時間　フラグを　0 に　戻す
                      */
                     Time_Sum_Flg_Off();

                 }

             }
         } catch (SQLException e) {
             e.printStackTrace();

         } finally {
             //------- カーソルを閉じる
             if(cursor != null) {
                 cursor.close();
             }
         }

    }


    /***
     *    終日　フラグ　を　１にする
     *
     *    作業部署コード　アップデート
     */
    private void Syuuzitu_Flg_Update(int target, String target_02) {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Syuuzitu_Flg_db_01 = helper.getWritableDatabase();

        // トランザクション開始
        Syuuzitu_Flg_db_01.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            // ******* 送信フラグを　1 に　する *******
            values.put("Syuuzitu_Flg", target);

            values.put("Sagyou_busyo_code", target_02);

            Syuuzitu_Flg_db_01.update("Flg_Table",values,"Syuuzitu_Flg = ?",new String[]{"0"});

            Syuuzitu_Flg_db_01.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Syuuzitu_Flg_db_01.endTransaction();

            if(Syuuzitu_Flg_db_01 != null) {
                Syuuzitu_Flg_db_01.close();
            }

        }

    } //----------------- END Syuuzitu_Flg_Update

    /**
     * 作業部署コード　アップデート  ,  終日チェックがない場合
     * @param target
     */
    private void Sagyou_busyo_code_Update(String target) {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Syuuzitu_Flg_db = helper.getWritableDatabase();

        // トランザクション開始
        Syuuzitu_Flg_db.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            // ******* 送信フラグを　1 に　する *******
            values.put("Sagyou_busyo_code", target);

            Syuuzitu_Flg_db.update("Flg_Table",values,null,null);

            Syuuzitu_Flg_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Syuuzitu_Flg_db.endTransaction();

            if(Syuuzitu_Flg_db != null) {
                Syuuzitu_Flg_db.close();
            }

        }

    }//********************* END Sagyou_busyo_code_Update

    /**
     *   終日チェック　フラグ SELECT 取得
     */

    private void Syuuzitu_Flg_SELECT() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Syuu_S_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

             cursor = Syuu_S_db.rawQuery("select Syuuzitu_Flg from Flg_Table",null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    //*************** 終日　値取得  0: 終日チェックなし, 1: 終日チェックあり
                    Syuuzitu_Flg = cursor.getInt(0);
                    System.out.println("終日フラグ値：：：" + Syuuzitu_Flg);
                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    } //*****************************************  END Syuuzitu_Flg_SELECT

    /***
     *  合計時間が　480 分　以上の場合は 1:  480以下は　0
     */
    private void Time_Sum_Flg_On() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Time_SUM_db = helper.getWritableDatabase();

        //******* トランザクション開始
        Time_SUM_db.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            values.put("Time_Sum_Flg", 1);

            Time_SUM_db.update("Flg_Table", values, null, null);

            //******* トランザクション完了
            Time_SUM_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Time_SUM_db.endTransaction();

            if(Time_SUM_db != null) {
                Time_SUM_db.close();
            }
        }

    }// ***************** END Time_Sum_Flg_On


    /***
     *  合計時間が　480 分　以上の場合は 1:  480以下は　0
     */
    private void Time_Sum_Flg_Off() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Time_SUM_off_db = helper.getWritableDatabase();

        //******* トランザクション開始
        Time_SUM_off_db.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            values.put("Time_Sum_Flg", 0);

            Time_SUM_off_db.update("Flg_Table", values, null, null);

            //******* トランザクション完了
            Time_SUM_off_db.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Time_SUM_off_db.endTransaction();

            if(Time_SUM_off_db != null) {
                Time_SUM_off_db.close();
            }
        }

    }// ***************** END Time_Sum_Flg_Off


    /**
     *  アラートダイアログ　表示  エラー　01
     */
    private void Allahto_Dailog_02_error() {

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Home.this);
        titleView.setText("作業時間を修正してください。");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.red));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(Home.this);

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
     *  アラートダイアログ　表示
     */
    private void Allahto_Dailog_Shot_Send() {

        // 部署コード　から　部署名　取得用
        Busyo_get_02();
        // 作業コード取得
        Flg_Table_Sagyou_SELECT();

        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Home.this);
        titleView.setText("ファイルの送信画面へ遷移します。");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(Home.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);

        String Get_Busyo_Mei;
        if(map_busyo_03.containsKey(Flg_send_sagyou_C)) {
            Get_Busyo_Mei = map_busyo_03.get(Flg_send_sagyou_C);

        } else {
            Get_Busyo_Mei = "作業部署 未選択（所属部署コード）" + get_TMNF_03;
        }

        //------- メッセージセット
        bilder.setMessage("担当者名" + "\n" + get_TMNF_02 + "\n" + "\n\n" +
                "作業部署" + "\n" + Get_Busyo_Mei + "\n\n" + "上記のデータで、送信処理を行いますか？");

        bilder.setNegativeButton("送信画面へ移動する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getApplicationContext(), Send_CSV_01.class);

                intent.putExtra("icon_get_TMNF_01", get_TMNF_01); // 担当者コード
                intent.putExtra("icon_get_TMNF_02", get_TMNF_02); // 担当者名

        //        intent.putExtra("icon_get_TMNF_03",Flg_send_sagyou_C); // 部署コード
        //        intent.putExtra("icon_get_BUMF_01",get_BUMF_01); // 部署名
                
                //**********  作業部署が null なら　デフォルトの部署コードを入れる
                if(Flg_send_sagyou_C == null || Flg_send_sagyou_C.length() == 0) {
                    Flg_send_sagyou_C = get_TMNF_03;
                }


                intent.putExtra("icon_get_TMNF_03",Flg_send_sagyou_C); // 部署コード
                intent.putExtra("icon_get_BUMF_01",Get_Busyo_Mei); // 部署名

                /**
                 *  アイコン　からの　フラグを　ture にする
                 */
                Icon_Send_Flg = true;

                startActivity(intent);

            }
        });

        bilder.setPositiveButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;
            }
        });

        android.app.AlertDialog dialog = bilder.create();
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

    //**************** 遅刻　・　早退　用
    private void Allahto_Dailog_Tikoku_Soutai ()  {

        //******************** オリジナルアラートログの表示 処理 開始  ********************//
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View bilde_layout_02 = inflater.inflate(R.layout.dialog_02,(ViewGroup)findViewById(R.id.alertdialog_layout_02));

        //*********** コンポーネント　初期化

        CheckBox dia_chack_01_01 = bilde_layout_02.findViewById(R.id.dia_chack_01_01); // 遅刻
        CheckBox dia_chack_01_02 = bilde_layout_02.findViewById(R.id.dia_chack_01_02); // 早退

        dia_chack_01_01_edit = bilde_layout_02.findViewById(R.id.dia_chack_01_01_edit); // 時間入力
        dia_chack_01_01_edit.setFocusable(false);
        dia_chack_01_01_edit.setFocusableInTouchMode(false);
        dia_chack_01_01_edit.setEnabled(false);

        MaterialButton dia_touroku_btn_001 = bilde_layout_02.findViewById(R.id.dia_touroku_btn_001); // 登録ボタン
        MaterialButton dia_touroku_btn_002 = bilde_layout_02.findViewById(R.id.dia_touroku_btn_002); // キャンセルボタン

        //****** エディットテキスト　ナンバー入力設定
        dia_chack_01_01_edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        /**
         *  スピナー 「遅刻」「早退」　時間　処理
         */
        Tkoku_spi = bilde_layout_02.findViewById(R.id.Tkoku_spi); // スピナー
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                arr_time_01
        );

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        Tkoku_spi.setAdapter(adapter);

        Tkoku_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    String get_TI_SOU_TIME = (String)Tkoku_spi.getSelectedItem();
                    dia_chack_01_01_edit.setText(get_TI_SOU_TIME);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //--------------- アラートダイヤログ タイトル　設定 ---------------//
        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);
        // タイトル
        TextView titleView;
        titleView = new TextView(Home.this);
        titleView.setText("遅刻・早退 入力");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.colorPinku));
        titleView.setPadding(20, 30, 20, 30);
        titleView.setGravity(Gravity.CENTER);

        // ダイアログに　「タイトル」　を　セット
        bilder.setCustomTitle(titleView);

        // カスタムレイアウト　を　セット
        bilder.setView(bilde_layout_02);

        AlertDialog dialog = bilder.create();
        dialog.show();


        //  ************  「遅刻」　チェックボックス
        dia_chack_01_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check_Tikoku = dia_chack_01_01.isChecked();

                if(check_Tikoku) {
                    dia_chack_01_02.setChecked(false);
                    Ti_So = "遅刻";
                }

            }
        });

        //  ************  「早退」　チェックボックス
        dia_chack_01_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check_Soutai = dia_chack_01_02.isChecked();

                if(check_Soutai) {
                    dia_chack_01_01.setChecked(false);
                    Ti_So = "早退";
                }

            }
        });



        // ************** 「時間入力　エディットテキスト」
        dia_chack_01_01_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //****** ソフトキーボードが押されたら
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //********* 時間　エディットテキストが　「空」　じゃなかったら
                    if(dia_chack_01_01_edit.getText().toString().equals("") == false) {

                        String dia_chack_01_01_edit_tmp = dia_chack_01_01_edit.getText().toString();
                        int dia_chack_01_01_edit_i = Integer.parseInt(dia_chack_01_01_edit_tmp);

                        //********* 480分以上の　時間入力　を　エラーにする
                        if(dia_chack_01_01_edit_i > 480 || dia_chack_01_01_edit_i < 0) {
                            toastMake("入力された値が不正な「値」です。", 0,-200);

                            dia_chack_01_01_edit.setText("");
                            return false;

                        }  else {

                            //************* 時間の値を格納
                            dia_chack_01_01_edit_tmp = dia_chack_01_01_edit.getText().toString();
                          //  Ti_So_Time = Integer.parseInt(dia_chack_01_01_edit_tmp);

                        }

                    } else {
                        //************* エラー　「空」の場合
                        toastMake("「時間」の入力項目が「空」です。",0,-200);
                        return false;
                    }

                }

                return false;
            }
        }); //******************** END



        /**
         * 　「登録ボタン」
         */
        dia_touroku_btn_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //******* 手入力　と　同じ方法で　タスクを　追加する *********

                //***** エディットテキストが「空」だった場合
                if(dia_chack_01_01_edit.getText().toString().equals("")) {
                    toastMake("「入力時間」が「空」です。", 0,-200);
                    return;
                }

                if(dia_chack_01_01.isChecked() == false && dia_chack_01_02.isChecked() == false) {
                    toastMake("「遅刻」「早退」どちらかにチェックを入れてください。", 0,-200);
                    return;
                }

                //******* エディットテキスト　値　Int型で　取得
                String dia_chack_01_01_edit_tmp = dia_chack_01_01_edit.getText().toString();
                int dia_chack_01_01_edit_i = Integer.parseInt(dia_chack_01_01_edit_tmp);

                //***** 「エディットテキスト」　の値チェック
                if(dia_chack_01_01_edit_i > 480 || dia_chack_01_01_edit_i < 0) {
                    toastMake("「入力時間」の値が不正な値です。", 0,-200);
                    return;
                }

                //************** インサート処理 *****************
                Insert_Tikoku_Soutai_save(); // インサート　クラス　メソッド
               // Free_input_Insert_01();

                Send_Grafu_Table_Insert(); // グラフ用　インサート


                        //******* ダイアログ　非表示
                        dialog.hide();

                        /*
                        Tikoku_Soutai_SELECT();
                        Tikoku_Soutai_SELECT_02();

                         */
                Tikoku_Soutai_SELECT_03();

                loadMyList();

                        //***** 表示用　テキストビューに　値セット
                     //   input_late_time.setText(String.valueOf(Ti_So_str));

                        //******　合計値　加算
                        Send_table_01_Time_Sum();

            }
        });


        /**
         *   「キャンセル」　ボタン
         */
        dia_touroku_btn_002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //************* キャンセル
                dialog.dismiss();
            }
        });

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


    private void Free_input_Insert_01() {

        /**
         *  Send_Table max 取得  グラフ id , カラム 22
         */
        Send_TB_id_max(); //********** send_table の　マックス取得　関数
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


            String free_input_edit_str = "";
            // 再送フラグ 基本 0
            String saisou_num = "0";

            // ******** ダイアログ　「遅刻」「早退」 作業時間　取得
            String free_input_time_edit_str = dia_chack_01_01_edit.getText().toString();


            //************* END ******************

            // 担当者C : 2
            values.put("send_col_01", get_TMNF_01);
            //  部署C : 3
         //   values.put("send_col_02", Sagyou_B); // get_TMNF_03

            //  部署C : 3  ****** フラグテーブルから　SELECT して　作業部署を取得する
            values.put("send_col_02", get_TMNF_03); // get_TMNF_03

            //　現品票C : 4
            values.put("send_col_03", "");
            // 品目K : 5
            values.put("send_col_04", "");
            // 品目C : 6
            values.put("send_col_05", ""); // 判別用　品目コード 0000

            //*********** 「遅刻」　「早退」　などを入れる
            // 品目名 : 7  free_input_edit => 自由入力　テキスト　取得

            String hinmei_IN = Ti_So;  // 遅刻・早退

            values.put("send_col_06",hinmei_IN);
            // 品備考 : 8
            values.put("send_col_07", "");
            // 作業場所 : 9
            values.put("send_col_08", "");
            // 段取時間 : 10
            values.put("send_col_09", "0");

            String dia_chack_01_01_edit_tmp = dia_chack_01_01_edit.getText().toString();
            // 作業時間 : 11
            values.put("send_col_10", dia_chack_01_01_edit_tmp);
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
            String free_start_time_view_in = di1_start_time_view.getText().toString();
            values.put("send_col_20", free_start_time_view_in);

            // 終了時間
            String end_time_view_in = di1_end_time_view.getText().toString();
            values.put("send_col_21", end_time_view_in);

            // グラフ用　id
            String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用

            //********  dd-0 Send_Table の　max id + 1  or  1
            G_id_str = Saghou_Code_num + "-" + "0" + tmp_id_str;


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

            //******* グラフ用　ID 挿入
            values.put("send_col_22", T_ID_Str);

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

    } // ************************** END function

    /**
     *    「残業入力」用　アラートダイアログ
     */
    private void Allahto_Dailog_Zangyou ()  {

        //******************** オリジナルアラートログの表示 処理 開始  ********************//
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View bilde_layout_03 = inflater.inflate(R.layout.dialog_03,(ViewGroup)findViewById(R.id.alertdialog_layout_03));

        //*********** コンポーネント　初期化
        dia_2_chack_01_01_edit = bilde_layout_03.findViewById(R.id.dia_2_chack_01_01_edit);

        //********* 残業時間　スピナー
        Zangyou_spi = bilde_layout_03.findViewById(R.id.Zangyou_spi);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                arr_time_01
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        Zangyou_spi.setAdapter(adapter);

        Zangyou_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    String Zangyou_spi_s = (String)Zangyou_spi.getSelectedItem();
                    dia_2_chack_01_01_edit.setText(Zangyou_spi_s);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //******* 時間代入　エディット
        dia_2_chack_01_01_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        dia_2_chack_01_01_edit.setFocusable(false);
        dia_2_chack_01_01_edit.setFocusableInTouchMode(false);
        dia_2_chack_01_01_edit.setEnabled(false);

        dia_2_touroku_btn_001 = bilde_layout_03.findViewById(R.id.dia_2_touroku_btn_001);
        dia_2_touroku_btn_002 = bilde_layout_03.findViewById(R.id.dia_2_touroku_btn_002);

        //--------------- アラートダイヤログ タイトル　設定 ---------------//
        AlertDialog.Builder bilder = new AlertDialog.Builder(Home.this);
        // タイトル
        TextView titleView;
        titleView = new TextView(Home.this);
        titleView.setText("残業時間 入力");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.colorPinku));
        titleView.setPadding(20, 30, 20, 30);
        titleView.setGravity(Gravity.CENTER);

        // ダイアログに　「タイトル」　を　セット
        bilder.setCustomTitle(titleView);

        // カスタムレイアウト　を　セット
        bilder.setView(bilde_layout_03);

        AlertDialog dialog = bilder.create();
        dialog.show();

        //*************** 時間エディット
        // ************** 「時間入力　エディットテキスト」
        dia_2_chack_01_01_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //****** ソフトキーボードが押されたら
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //********* 時間　エディットテキストが　「空」　じゃなかったら
                    if(dia_2_chack_01_01_edit.getText().toString().equals("") == false) {

                        String dia_02_chack_01_01_edit_tmp = dia_2_chack_01_01_edit.getText().toString();
                        int dia_chack_01_01_edit_i = Integer.parseInt(dia_02_chack_01_01_edit_tmp);

                        //********* 480分以上の　時間入力　を　エラーにする
                        if(dia_chack_01_01_edit_i > 480 || dia_chack_01_01_edit_i < 0) {
                            toastMake("入力された値が不正な「値」です。値を修正してください", 0,-200);

                            dia_2_chack_01_01_edit.setText("");
                            return false;

                        }  else {

                            //************* 時間の値を格納
                            dia_02_chack_01_01_edit_tmp = dia_2_chack_01_01_edit.getText().toString();
                         //   Zangyou_Time = Integer.parseInt(dia_02_chack_01_01_edit_tmp);

                        }

                    } else {
                        //************* エラー　「空」の場合
                        toastMake("「時間」の入力項目が「空」です。",0,-200);
                        return false;
                    }

                }

                return false;
            }
        });  //***************** END エディット



        /**
         *   「残業」　登録　ボタン
         */
        dia_2_touroku_btn_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // エラーチェック
                if(dia_2_chack_01_01_edit.getText().toString().equals("")) {
                    toastMake("「時間」の値が空欄です。", 0,-200);
                    dia_2_chack_01_01_edit.setText("");
                    return;

                } else {

                    String H_tmp_str = dia_2_chack_01_01_edit.getText().toString();
                    int H_tmp_i = Integer.parseInt(H_tmp_str);

                    if(H_tmp_i > 480 || H_tmp_i < 0) {
                        toastMake("時間の値が不正な値です。値を修正してください。", 0, -200);
                        dia_2_chack_01_01_edit.setText("");
                        return;

                    } else {

                        //************** インサート処理 *****************

                        Insert_Zangyou_save(); // インサート　クラス　メソッド使用
                      //  Free_input_Insert_02(); // インサート

                        Send_Grafu_Table_Insert(); // グラフ用　インサート

                        loadMyList();
                        //******* ダイアログ　非表示
                        dialog.hide();

                        Zangyou_SELECT();

                        //***** 表示用　テキストビューに　値セット
                        input_over_time.setText(Zangyou_str);

                        //******　合計値　加算
                        Send_table_01_Time_Sum();

                    }

                }

            }
        });

        /**
         *  「残業」キャンセル　ボタン
         */
        dia_2_touroku_btn_002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*********** キャンセル
                dialog.dismiss();
            }
        });


    } //********************************** END funtion

    /**
     *    「残業時間」　インサート処理
     */
    private void Free_input_Insert_02() {

        /**
         *  Send_Table max 取得  グラフ id , カラム 22
         */
        Send_TB_id_max(); //********** send_table の　マックス取得　関数
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


            String free_input_edit_str = "";
            // 再送フラグ 基本 0
            String saisou_num = "0";

            // ******** ダイアログ　「遅刻」「早退」 作業時間　取得

            //************* END ******************

            // 担当者C : 2
            values.put("send_col_01", get_TMNF_01);
            //  部署C : 3
            //   values.put("send_col_02", Sagyou_B); // get_TMNF_03

            //  部署C : 3  ****** フラグテーブルから　SELECT して　作業部署を取得する
            values.put("send_col_02", get_TMNF_03); // get_TMNF_03

            //　現品票C : 4
            values.put("send_col_03", "");
            // 品目K : 5
            values.put("send_col_04", "");
            // 品目C : 6
            values.put("send_col_05", ""); // 判別用　品目コード 0000

            //*********** 「遅刻」　「早退」　などを入れる
            // 品目名 : 7  free_input_edit => 自由入力　テキスト　取得

            String hinmei_IN = "残業";  // 残業時間

            values.put("send_col_06",hinmei_IN);
            // 品備考 : 8
            values.put("send_col_07", "");
            // 作業場所 : 9
            values.put("send_col_08", "");
            // 段取時間 : 10
            values.put("send_col_09", "0");

            // 作業時間 : 11
      //      values.put("send_col_10", String.valueOf(Zangyou_Time));

            String Zangyou_ZIkan = dia_2_chack_01_01_edit.getText().toString();
            values.put("send_col_10", Zangyou_ZIkan);


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
            String free_start_time_view_in = di2_start_time_view.getText().toString();
            values.put("send_col_20", free_start_time_view_in);

            // 終了時間
            String end_time_view_in = di2_end_time_view.getText().toString();
            values.put("send_col_21", end_time_view_in);

            // グラフ用　id
            String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用

            //********  dd-0 Send_Table の　max id + 1  or  1
            G_id_str = Saghou_Code_num + "-" + "0" + tmp_id_str;

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

            //******* グラフ用　ID 挿入
            values.put("send_col_22", T_ID_Str);

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

    } // ************************** END function

    /**
     *   遅刻　SELECT 取得
     */
    private void Tikoku_Soutai_SELECT() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Tikoku_S_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Tikoku_S_01.rawQuery("select sum(send_col_10) from Send_table_01" +
                    " where send_col_06 = '遅刻' "  , null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    String tikoku_str = cursor.getString(0);

                    if(tikoku_str == null) {
                        return;

                    } else {

                        tikoku_str_i = Integer.parseInt(tikoku_str);

                        result_sum = tikoku_str_i + soutai_str_i;

                        input_late_time.setText(String.valueOf(result_sum));
                    }
                }

            }  else {
                //********** 遅刻・早退がなかった場合は 何もしない。
                result_sum = 0;
                result_sum = soutai_str_i + 0;
                input_late_time.setText(String.valueOf(result_sum));
                return;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Tikoku_S_01 != null) {
                Tikoku_S_01.close();
            }
        }

    } //************** END function


    /**
     *   早退　SELECT 取得
     */
    private void Tikoku_Soutai_SELECT_02() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Tikoku_S_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Tikoku_S_01.rawQuery("select sum(send_col_10) from Send_table_01" +
                    " where send_col_06 = '早退' "  , null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    String soutai_str = cursor.getString(0);

                    if(soutai_str == null) {
                        return;

                    } else {

                        soutai_str_i = Integer.parseInt(soutai_str);

                        result_sum = soutai_str_i + tikoku_str_i;

                        input_late_time.setText(String.valueOf(result_sum));
                    }
                }

            }  else {
                //********** 遅刻・早退がなかった場合は 何もしない。
                result_sum = 0;
                result_sum = 0 + tikoku_str_i;
                input_late_time.setText(String.valueOf(result_sum));
                return;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Tikoku_S_01 != null) {
                Tikoku_S_01.close();
            }
        }

    } //************** END function


    /**
     *   早退　SELECT 取得
     */
    private void Tikoku_Soutai_SELECT_03() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Tikoku_S_03 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Tikoku_S_03.rawQuery("SELECT sum(send_col_10) from Send_Table_01 where send_col_06 = \"早退\" or send_col_06 = \"遅刻\""  , null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    String soutai_str = cursor.getString(0);

                    if(soutai_str == null) {
                        input_late_time.setText("0");
                        return;

                    } else {

                        soutai_str_i = Integer.parseInt(soutai_str);

                      //  result_sum = soutai_str_i + tikoku_str_i;

                        input_late_time.setText(String.valueOf(soutai_str_i));
                    }
                }

            }  else {
                //********** 遅刻・早退がなかった場合は 何もしない。

                input_late_time.setText("0");
                return;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Tikoku_S_03 != null) {
                Tikoku_S_03.close();
            }
        }

    } //************** END function


    /**
     *  残業　SELECT 取得
     */
    private void Zangyou_SELECT() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Zangyou_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Zangyou_01.rawQuery("select sum(send_col_10) from Send_table_01 where send_col_06" +
                    " like '%残業%' ", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Zangyou_str = cursor.getString(0);

                    int Zangyou_str_i = 0;

                    if(Zangyou_str == null) {
                        input_over_time.setText("0");
                    } else {
                        Zangyou_str_i = Integer.parseInt(Zangyou_str);
                    }

                    if(Zangyou_str_i > 0) {
                        Zangyou_str_i = Integer.parseInt(Zangyou_str);

                        input_over_time.setText(String.valueOf(Zangyou_str_i));
                    } else {
                        input_over_time.setText("0");
                    }
                }

            }  else {
                //********** 遅刻・早退がなかった場合は 何もしない。
                input_over_time.setText("0");
                return;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Zangyou_01 != null) {
                Zangyou_01.close();
            }
        }

    } //************** END function

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


    private  void Send_TB_id_COUNT () {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase Send_TB_id_count_db = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Send_TB_id_count_db.rawQuery("SELECT count(*) from Send_table_01", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Send_Table_id_count = cursor.getInt(0);

                    if(Send_Table_id_count == 0) {
                        System.out.println("定期送信 判別 SELECT フラグ:::" + Send_Table_id_count);
                        return;
                    } else {
                        /**
                         *   //****************** 作業があれば　定期送信フラグ　を　1 にする
                         */
                        Teiki_Send_Flg();
                        System.out.println("定期送信 判別 SELECT フラグ:::" + Send_Table_id_count);
                    }
                }

            } else {

                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Send_TB_id_count_db != null) {
                Send_TB_id_count_db.close();
            }
        }

    } //************************ END function


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

    /**
     *  インサート　処理  「遅刻」　「早退」
     */
    private void Insert_Tikoku_Soutai_save() {

        /**
         *   ログイン時　作業日　から　取得  yyyyMMdd
         */
        Sagyou_new_ID_SELECT();

        // 作業コード取得
        Flg_Table_Sagyou_SELECT();

        String date_num = Create_Date_NUM; // yyyyMMdd
        yy_num = date_num.substring(0,4); // yyyy
        dd_num = date_num.substring(6,8); // dd , dd-01 , dd-02  ,  Saghou_Code_num と　変更

        /***
         * ------------------- INSERT 用　データ -----------------------
         */
        String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用
        String Sagyou_date_num = Sagyou_date; // yyyymm 取得  => 作業日　用

        //------- 品名
        String hinmei =  Ti_So; // 遅刻・早退
        System.out.println(hinmei);

        if(hinmei.equals("遅刻")) {
            Tikoku_SOUtai_C = "SG01";

        } else if(hinmei.equals("早退")) {
            Tikoku_SOUtai_C = "SG02";

        }

        // 作業時間 : 11
        String dia_chack_01_01_edit_tmp = dia_chack_01_01_edit.getText().toString();

        //------ 作業時間
        String Sagyou_Time = dia_chack_01_01_edit_tmp;
        System.out.println(Sagyou_Time);

        // 最終チェック　0 にする
        String last_check = "0";

        String kakou_num = null;

        // 再送フラグ 基本 0
        String saisou_num = "0";
        //**************************** 追加
        // 開始時間
  //      String start_time_view_in = di1_start_time_view.getText().toString();
        String start_time_view_in = "0";
        // 終了時間
   //     String end_time_view_in = di1_end_time_view.getText().toString();
        String end_time_view_in = "0";


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
        String  GET_Hinmoku_C = "";

        /**
         * 　＊＊＊＊＊＊ 「樹脂成型課」　３項目　追加　＊＊＊＊＊＊＊
         */
        String Color_Time = "0"; // 色段取時間

        String Kata_Time = "0"; // 型段取時間

        String Kikai_Code = "0"; // 機械コード

        //****** 作業部署が　「空」だったら、デフォルトの部署を入れる
        if(Flg_send_sagyou_C == null) {
            Flg_send_sagyou_C = get_TMNF_03;
        }

        //**********************  END インサート用　データ　作成

        // DB への　登録
        DBAdapter dbAdapter = new DBAdapter(this);

        dbAdapter.openDB();

        // dia_user_key => 作業部署コード
        dbAdapter.saveDB(get_TMNF_01,Flg_send_sagyou_C,"",Hinmoku_C_str,
                Tikoku_SOUtai_C,hinmei,"","","0",Sagyou_Time,
                "","","",
                "","","",
                last_check,"",saisou_num,
                start_time_view_in,end_time_view_in,T_ID_Str,date_num,
                Color_Time,Kata_Time,Kikai_Code);

        dbAdapter.closeDB();

    } //*********** end function


    /**
     *  インサート　処理  「残業」
     */
    private void Insert_Zangyou_save() {

        /**
         *   ログイン時　作業日　から　取得  yyyyMMdd
         */
        Sagyou_new_ID_SELECT();

        // 作業コード取得
        Flg_Table_Sagyou_SELECT();

        String date_num = Create_Date_NUM; // yyyyMMdd
        yy_num = date_num.substring(0,4); // yyyy
        dd_num = date_num.substring(6,8); // dd , dd-01 , dd-02  ,  Saghou_Code_num と　変更


        /***
         * ------------------- INSERT 用　データ -----------------------
         */
        String Saghou_Code_num = Saghou_Code; // dd 取得  =>  作業コード 用
        String Sagyou_date_num = Sagyou_date; // yyyymm 取得  => 作業日　用

        //------- 品名
        String hinmei_IN = "残業";  // 残業時間
        System.out.println(hinmei_IN);

        // 作業時間 : 11
        String dia_chack_01_01_edit_tmp = dia_2_chack_01_01_edit.getText().toString();

        //------ 作業時間
        String Sagyou_Time = dia_chack_01_01_edit_tmp;
        System.out.println(Sagyou_Time);

        // 最終チェック　0 にする
        String last_check = "0";

        String kakou_num = null;

        // 再送フラグ 基本 0
        String saisou_num = "0";
        //**************************** 追加
        // 開始時間
     //   String start_time_view_in = di2_start_time_view.getText().toString();
        String start_time_view_in = "0";
        // 終了時間
   //     String end_time_view_in = di2_end_time_view.getText().toString();
        String end_time_view_in = "0";


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
        String  GET_Hinmoku_C = "SG03";

        /**
         * 　＊＊＊＊＊＊ 「樹脂成型課」　３項目　追加　＊＊＊＊＊＊＊
         */
        String Color_Time = "0"; // 色段取時間

        String Kata_Time = "0"; // 型段取時間

        String Kikai_Code = "0"; // 機械コード

        //****** 作業部署が　「空」だったら、デフォルトの部署を入れる
        if(Flg_send_sagyou_C == null) {
            Flg_send_sagyou_C = get_TMNF_03;
        }

        //**********************  END インサート用　データ　作成

        // DB への　登録
        DBAdapter dbAdapter = new DBAdapter(this);

        dbAdapter.openDB();

        // dia_user_key => 作業部署コード
        dbAdapter.saveDB(get_TMNF_01,Flg_send_sagyou_C,"",Hinmoku_C_str,
                GET_Hinmoku_C,hinmei_IN,"","","0",Sagyou_Time,
                "","","",
                "","","",
                last_check,"",saisou_num,
                start_time_view_in,end_time_view_in,T_ID_Str,date_num,
                Color_Time,Kata_Time,Kikai_Code);

        dbAdapter.closeDB();


    } //*********** end function


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


    /****
     *       Send_data_Flg が 0：　データ送信されていない  1: 送信完了
     */
    private void Send_data_Flg_SELECT () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Send_S_DB = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            //*************** 送信済みかどうか　取得
            cursor = Send_S_DB.rawQuery("SELECT Send_data_Flg FROM Flg_Table", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    Logout_GO_str = cursor.getString(0);

                } else {
                    return;
                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Send_S_DB != null) {
                Send_S_DB.close();
            }
        }

    }

    /***
     *  Flg テーブル　から　作業用　部署を取得
     */
    private void Flg_Table_Sagyou_SELECT () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase FLG_S_DB = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            //**********  作業部署コード　取得
            cursor = FLG_S_DB.rawQuery("SELECT Sagyou_busyo_code FROM Flg_Table", null);

            if(cursor != null || cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    Flg_send_sagyou_C = cursor.getString(0);
                }

                //**********  作業部署コード がない場合　
            } else {
                Flg_send_sagyou_C = get_TMNF_03;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }  finally {
            if(FLG_S_DB != null) {
                FLG_S_DB.close();
            }
        }

    } //------------ end function


    private void Busyo_get_02 () {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Busyo_get_DB = helper.getReadableDatabase();

        Cursor cursor = null;

        String [] arr_item = new String [2];

        try {

            //**********  作業部署コード　取得
            cursor = Busyo_get_DB.rawQuery("SELECT * FROM BUMF_table", null);

            if(cursor != null || cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    do{

                        arr_item[0] = cursor.getString(0);
                        arr_item[1] = cursor.getString(1);

                        map_busyo_03.put(arr_item[0], arr_item[1]);


                    } while (cursor.moveToNext());
                }


                //**********  作業部署コード がない場合　
            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }  finally {
            if(Busyo_get_DB != null) {
                Busyo_get_DB.close();
            }
        }


    } //--------------------- end function


}