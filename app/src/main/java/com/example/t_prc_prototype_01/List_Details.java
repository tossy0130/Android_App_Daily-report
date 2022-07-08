package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.impl.utils.WorkTimer;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class List_Details extends AppCompatActivity {

    private DBAdapter dbAdapter;

    private LineChart mChart;

    //---------- コンポーネント一覧
    private TextView d_list_id,d_list_name,d_list_souseisan_num,d_list_yotei_num,d_list_id_label,d_list_name_label;
    private TextView d_list_yotei_num_label,d_list_souseisan_label,d_list_ryouhinn_l,d_list_kaishi_l,d_list_syuuryou_l;
    private TextView d_list_kakou,d_list_furyou_l,last_koutei_label;
    private CardView content_back_01;

    //---------- 値受け取り用
    private String d_list_id_str, d_list_name_str,date_DD,d_list_yotei_num_str,d_list_souseisan_num_str;
    private String d_list_start_num,d_list_end_num,d_list_furyou_num,d_list_ryouhin_num,d_list_sagyou_time_num_str;
    private String d_list_dandori_time_num_str;
    /**
     * 加工数　追加
     */
    private String Input_kakousuu_list_d_str;

    private EditText input_starttime_list_d,Input_endtime_list_d,input_furyou_list_d,Input_ryouhinn_list_d,d_list_sagyou_time_num;
    private EditText d_list_dandori_time_num;

    private int d_list_start_num_i, d_list_end_num_i, d_list_furyou_num_i;
    private int d_list_souseisan_num_str_i, d_list_ryouhin_num_i;
    private int d_list_dandori_time_num_i, d_list_sagyou_time_num_i;

    //---------- 値受け取り用 END

    private MaterialButton d_list_back_btn,d_list_save_btn;

    private ArrayList<String> list_graf_num = new ArrayList<>();

    // 受け取ったリスト ID
    private int listId;
    private String list_id;

    // 品目区分 db から　受け取り
    private String Hinmoku_K, Hinmoku_C;
    private int Hinmoku_K_i;

    private String get_end_t;
    private int get_end_t_i;

    // ---- 最終工程　チェック判別
    private CheckBox list_last_check;
    private String check_str;

    private LineData lineData;

    //------------- タイマー関係 & グラフ　値セット処理
   // private int timer_count;
    private int[] arr_Grafu_Num = new int [10];

    private int get_souseisan_i;

    private int Sou_Num_01_i;

    private ArrayList<Integer> List_Grafu_Num = new ArrayList<>();

    private String Send_Grafu_Send_id;
    private int [] S_Gurafu_Num = new int [10];

    // 比較用時間　
    private String HH_hikaku_01;
    private int HH_hikaku_01_i;

    // アップデート　グラフ用
    private int update_souseisan_t_i;

    // 総生産
    private int Souseisan_NUM;

    /**
     *  　加工数
     */
    private EditText Input_kakousuu_list_d;

    /**
     *   合計数（小計）
     */
    private EditText input_syoukei_list_d;

    // フレームレイアウト
    private FrameLayout list_select_header_view_02;

    //******* 開始時間 & 終了時間　追加
    private Button list_start_time,list_end_time;
    private TextView list_start_time_view,list_end_time_view;
    private int time_01_a,time_01_b,time_02_a,time_02_b;

    private String Start_time_str,End_time_str,G_id;

    //****** スピナー
    private Spinner list_spinner_001,list_spinner_002,list_spinner_003,list_spinner_004,list_spinner_Tosou;
    private TextView list_get_spiiner_text_01,focusView_02;
    private String spiiner_item_01, spiiner_item_02, spiiner_item_03, spiiner_item_04;

    private String Hinmoku_C_str;

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

    private String Get_spi_set_str;

    private String Get_Sgayou_B;

    /**
     *  「樹脂成型課」　用　
     */
    private int Iro_Time_i, Kata_Time_i;
    private EditText iro_input_a,kata_input_a,kikai_input_a;
    private MaterialButton dia_5_touroku_btn_001,dia_5_touroku_btn_002;
    private String tmp_iro_input_a, tmp_kata_input_a, tmp_kikai_input_a, Kikai_Code;
    private EditText iro_z_edit_a,Kata_z_edit_a,Kikai_z_input_a;
    private Button Zyushi_Seikei_btn_a;

    private TextInputLayout iro_z_box_a, Kata_z_box_a, Kikai_z_box_a;

    // 塗装課　フラグ
    private boolean Tosou_Flg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__details);

        //------------ 現在時刻　取得
        String time_str = getNowDate();

        // 比較用　HH 取得 	一日における時(0～23)
        HH_hikaku_01 = time_str.substring(8, 10);
        HH_hikaku_01_i = Integer.parseInt(HH_hikaku_01);

        // yyyy/MM/dd HH:mm:ss
        String time_str_02 = getNowDate_02();

        // DB 格納用
        date_DD = time_str.substring(6,8); // dd 取得

        // DBAdapter の　コントラクタを呼ぶ
        dbAdapter = new DBAdapter(this);


        if(getIntent() != null) {
            listId = getIntent().getIntExtra("listId",listId);
            System.out.println("リストID" + listId);
            list_id = String.valueOf(listId);
        }

        /**
         *  GET_List_data => DB から　値を　SELECT
         */

        // **************** 塗装課　フラグ　セット
        Tosou_Flg = false;

        GET_List_data();

        System.out.println("Get_Sgayou_B:::" + Get_Sgayou_B);

        list_select_header_view_02 = findViewById(R.id.list_select_header_view_02);
        list_select_header_view_02.setVisibility(View.GONE);


        init();

        // コンポーネント初期化

        // コンポーネントに値をセット
      //  set_num();

        //　エディットテキストのデータを　Int 型に　パース
        Edit_Parse_01();

        //------------- グラフ用　総生産 SELCT
        GET_Souseisan();

        // グラフ描画処理
        addEntry();

        /**
         *  樹脂成型課　用　button
         */
        Zyushi_Seikei_btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Allahto_Dailog_Zyushi_Seikei_A();

            }
        });

        /**
         *  開始時間　追加　Timepikker
         */
        list_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(List_Details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        time_01_a = hourOfDay;
                        time_01_b = minute;

                        list_start_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);

                timePickerDialog.show();

            }
        }); //********** END list_start_time 開始時間

        /**
         *  終了時間　追加
         */
        list_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                //DatePickerDialogインスタンスを取得
                TimePickerDialog timePickerDialog = new TimePickerDialog(List_Details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        list_end_time_view.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
                        time_02_a = hourOfDay;
                        time_02_b = minute;


                        //*** 開始時間  分に　変換
                        int hour_01_sum = time_01_a * 60 + time_01_b; //*** 開始時間
                        int hour_02_sum = time_02_a * 60 + time_02_b; //*** 終了時間

                        int time_sum = hour_02_sum - hour_01_sum;

                        //********* 計算結果を表示 *********
                        d_list_sagyou_time_num.setText(String.valueOf(time_sum));

                    }
                }, hour, minute, true);

                timePickerDialog.show();

            }
        });


        /**
         *  最終工程　チェック
         */
        list_last_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check = list_last_check.isChecked();

                if(check) {
                    check_str = "1";
                } else {
                    check_str = "0";
                }

            }
        });

        /**
         *  戻るボタン
         */
        d_list_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        /***
         *  *********************　スピナー　アイテム 01 　～　　04 イベント　*******************************
         */

        /**
         *  01
         */
        list_spinner_001.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    spiiner_item_01 = (String)list_spinner_001.getSelectedItem();
                    list_get_spiiner_text_01.setText(spiiner_item_01);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                list_get_spiiner_text_01.setText("");
            }
        });

         /**
         *  02
         */

        list_spinner_002.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    spiiner_item_02 = (String)list_spinner_002.getSelectedItem();
                    list_get_spiiner_text_01.setText(spiiner_item_02);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                list_get_spiiner_text_01.setText("");
            }
        });

        /**
         *  03
         */

        list_spinner_003.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    spiiner_item_03 = (String)list_spinner_003.getSelectedItem();
                    list_get_spiiner_text_01.setText(spiiner_item_03);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                list_get_spiiner_text_01.setText("");
            }
        });

        /**
         *  04
         */
        list_spinner_004.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0) {
                    spiiner_item_04 = (String)list_spinner_004.getSelectedItem();
                    list_get_spiiner_text_01.setText(spiiner_item_04);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                list_get_spiiner_text_01.setText("");
            }
        });




        /**
         *  更新 ボタン
         */
        d_list_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(Hinmoku_K.contains("9")) && !(Hinmoku_C.contains("SG"))) {

                    //--------- 「開始時」　「終了時」　「不良品数」　「作業時間」 「段取時間」　の　どれかの値が　「空」　だったら
                    if (input_starttime_list_d.getText().toString().equals("") || // 「開始時」
                            Input_endtime_list_d.getText().toString().equals("") || // 「終了時」
                            input_furyou_list_d.getText().toString().equals("") || // 「不良品数」
                            d_list_sagyou_time_num.getText().toString().equals("")) { // 「作業時間」

                        toastMake("「入力項目」の値が「空」です。", -200, 0);
                        return;

                    } else {

                        /**
                         * ------------------------ 値　取得 ----------------------------
                         */
                        // 開始時
                        String tmp_kaishi = input_starttime_list_d.getText().toString();
                        int tmp_kaishi_i = Integer.parseInt(tmp_kaishi);

                        // 終了時
                        String tmp_syuuryou = Input_endtime_list_d.getText().toString();
                        int tmp_syuuryou_i = Integer.parseInt(tmp_syuuryou);

                        // 総生産数　取得
                        String tmp_souseisan = d_list_souseisan_num.getText().toString();
                        int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                        // 不良品数　取得
                        String tmp_furyouhin = input_furyou_list_d.getText().toString();
                        int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                        // 良品数 取得
                        String tmp_ryouhin = Input_ryouhinn_list_d.getText().toString();
                        int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);

                        //--------- エラー 「開始時」 > 「終了時」
                        if (tmp_syuuryou_i < tmp_kaishi_i) {
                            toastMake("「開始時」 「終了時」　の　値が不正な値です。", -200, 0);
                            return;

                            //---------　エラー 「不良品数」 > 「総生産数」
                        } else if (tmp_furyouhin_i > tmp_souseisan_i) {
                            toastMake("「不良品数」 「総生産数」　の 値が不正な値です。", -200, 0);
                            return;

                            //---------　エラー 「良品数」 < 0   だった場合　
                        } else if (tmp_ryouhin_i < 0) {
                            toastMake("「良品数」 の計算結果が　マイナスの値になっています。", -200, 0);
                            return;
                        }

                        // ------------- エラー　作業時間がマイナスだったら
                        String d_list_sagyou_time_num_s = d_list_sagyou_time_num.getText().toString();
                        int d_list_sagyou_time_num_i = Integer.parseInt(d_list_sagyou_time_num_s);

                        if (d_list_sagyou_time_num_i < 0) {
                            toastMake("「作業時間」にマイナスの値が入力されています。", -200, 0);
                            d_list_sagyou_time_num.setText("");
                            return;
                        }

                        /**
                         *  ********* 　「品目区分」 != 9 での　アップデート *********
                         */

                        //******************* アラート　表示 *******************
                        //-------　タイトル
                        TextView titleView;
                        // アクティビティ名を入れる
                        titleView = new TextView(List_Details.this);
                        titleView.setText("タスクデータ　更新確認");
                        titleView.setTextSize(20);
                        titleView.setTextColor(Color.WHITE);
                        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        titleView.setPadding(20, 20, 20, 20);
                        titleView.setGravity(Gravity.CENTER);

                        //-------- アラートログの表示 開始 ----------
                        AlertDialog.Builder bilder = new AlertDialog.Builder(List_Details.this);

                        /**
                         *  ダイアログの項目セット
                         */
                        //------- タイトルセット
                        bilder.setCustomTitle(titleView);
                        //------- メッセージセット
                        bilder.setMessage("タスクを更新しますか？");


                        //******************************************* END ボタン　配色　変更

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /**
                                 *  アップデート処理
                                 */

                                //******************** エディットテキスト　値　格納 ***************
                                // 段取時間
                                String update_dandori_t = d_list_dandori_time_num.getText().toString();
                                //　作業時間　を　格納
                                String update_sagyou_t = d_list_sagyou_time_num.getText().toString();
                                // 開始数量
                                String update_kaisi_t = input_starttime_list_d.getText().toString();

                                // 終了時数量
                                String update_syuryou_t = Input_endtime_list_d.getText().toString();
                                int update_syuryou_t_i = Integer.parseInt(update_syuryou_t); // ******* 新しい「終了時」
                                int d_list_end_num_i = Integer.parseInt(d_list_end_num); // ******** 前の　「終了時」

                                int Update_G_SUM = update_syuryou_t_i - d_list_end_num_i;


                                // 総生産数
                                String update_souseisan_t = d_list_souseisan_num.getText().toString();
                                // 不良品数
                                String update_furyou_t = input_furyou_list_d.getText().toString();
                                // 良品数
                                String update_ryouhin_t = Input_ryouhinn_list_d.getText().toString();

                                //********************* 最終工程チェック
                                String update_check_str = check_str;

                                /**
                                 *  生産数（小計）
                                 */
                                String input_syoukei_list_d_tmp = input_syoukei_list_d.getText().toString();

                                /**
                                 *  加工数　追加
                                 */
                                String Input_kakousuu_list_d_t = Input_kakousuu_list_d.getText().toString();
                                int Input_kakousuu_list_d_t_i = Integer.parseInt(Input_kakousuu_list_d_t);

                                /**
                                 *  開始時間　、　終了時間　追加
                                 *
                                 */

                                String list_start_time_view_num = list_start_time_view.getText().toString();
                                String list_end_time_view_num = list_end_time_view.getText().toString();


                                /**
                                 *  予定数量　変更　追加
                                 */
                                String d_list_yotei_num_s = d_list_yotei_num.getText().toString();

                                /**
                                 *  　樹脂成型課　項目　追加
                                 */

                                String iro_z_edit_a_tmp = iro_z_edit_a.getText().toString(); // 色段取時間
                                String Kata_z_edit_a_tmp = Kata_z_edit_a.getText().toString(); // 型段取時間
                                String Kikai_z_input_a_tmp = Kikai_z_input_a.getText().toString(); // 機械コード




                                //******************** END  アップデートデータ　値　格納 ***************

                                //******** SQLlite へ　アップデート処理
                                DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
                                SQLiteDatabase update_db = helper.getWritableDatabase();

                                try {
                                    // ****** トランザクション　開始
                                    update_db.beginTransaction();

                                    ContentValues values = new ContentValues();

                                    values.put("send_col_09", update_dandori_t); // 段取時間
                                    values.put("send_col_10", update_sagyou_t); // 作業時間
                                    /**
                                     *   予定数　変更　追加  d_list_yotei_num_s
                                     */
                                    values.put("send_col_11", d_list_yotei_num_s); //**** 予定数量

                                    values.put("send_col_12", update_kaisi_t); // 開始数量
                                    values.put("send_col_13", update_syuryou_t); // 終了時数量
                                    values.put("send_col_14", update_souseisan_t); // 総生産数
                                    values.put("send_col_15", update_furyou_t); // 不良品数
                                    values.put("send_col_16", update_ryouhin_t); // 良品数

                                    values.put("send_col_17", update_check_str); // 最終工程チェック
                                    /**
                                     *  加工数　追加
                                     */
                                    values.put("send_col_18", Input_kakousuu_list_d_t); // 加工数

                                    /**
                                     *  開始時間　& 終了時間　追加
                                     */
                                    values.put("send_col_20", list_start_time_view_num);
                                    values.put("send_col_21", list_end_time_view_num);

                                    /**
                                     *  樹脂成型課　用　データ
                                     */
                                    values.put("send_col_24", iro_z_edit_a_tmp);
                                    values.put("send_col_25", Kata_z_edit_a_tmp);
                                    values.put("send_col_26", Kikai_z_input_a_tmp);

                                    //********* アップデート処理
                                    update_db.update("Send_table_01", values, "id = ?", new String[]{String.valueOf(listId)});

                                    // ******** トランザクション　成功
                                    update_db.setTransactionSuccessful();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } finally {
                                    // ******* トランザクション　終了
                                    update_db.endTransaction();

                                    if (update_db != null) {
                                        update_db.close();
                                    }
                                }

                                //************ 更新　完了メッセージ *************
                                toastMake("更新が完了しました。", -200, 0);

                                finish();


                                System.out.println("swich 値::::HH_hikaku_01_i:::アップデート前値" + HH_hikaku_01_i);
                                //******* 値セット *******
                                switch (HH_hikaku_01_i) {
                                    case 8:
                                        Update_Grafu_01("Sou_Num_01", Update_G_SUM, 0);
                                        Log.v("アップデート処理:::", "Sou_Num_01 アップデート完了");
                                        System.out.println("Sou_Num_01 アップデート完了");
                                        break;

                                    case 9:
                                        Update_Grafu_01("Sou_Num_02", Update_G_SUM, 1);
                                        Log.v("アップデート処理:::", "Sou_Num_02 アップデート完了");
                                        System.out.println("Sou_Num_02 アップデート完了");
                                        break;

                                    case 10:
                                        Update_Grafu_01("Sou_Num_03", Update_G_SUM, 2);
                                        Log.v("アップデート処理:::", "Sou_Num_03 アップデート完了");
                                        System.out.println("Sou_Num_03 アップデート完了");
                                        break;

                                    case 11:
                                        Update_Grafu_01("Sou_Num_04", Update_G_SUM, 3);
                                        Log.v("アップデート処理:::", "Sou_Num_04 アップデート完了");
                                        System.out.println("Sou_Num_04 アップデート完了");
                                        break;

                                    case 12:
                                        Update_Grafu_01("Sou_Num_05", Update_G_SUM, 4);
                                        Log.v("アップデート処理:::", "Sou_Num_05 アップデート完了");
                                        System.out.println("Sou_Num_05 アップデート完了");
                                        break;

                                    case 13:
                                        Update_Grafu_01("Sou_Num_06", Update_G_SUM, 5);
                                        Log.v("アップデート処理:::", "Sou_Num_06 アップデート完了");
                                        System.out.println("Sou_Num_06 アップデート完了");
                                        break;

                                    case 14:
                                        Update_Grafu_01("Sou_Num_07", Update_G_SUM, 6);
                                        Log.v("アップデート処理:::", "Sou_Num_07 アップデート完了");
                                        System.out.println("Sou_Num_07 アップデート完了");
                                        break;

                                    case 15:
                                        Update_Grafu_01("Sou_Num_08", Update_G_SUM, 7);
                                        Log.v("アップデート処理:::", "Sou_Num_08 アップデート完了");
                                        System.out.println("Sou_Num_08 アップデート完了");
                                        break;

                                    case 16:
                                        Update_Grafu_01("Sou_Num_09", Update_G_SUM, 8);
                                        Log.v("アップデート処理:::", "Sou_Num_09 アップデート完了");
                                        System.out.println("Sou_Num_09 アップデート完了");
                                        break;

                                    case 17:
                                        Update_Grafu_01("Sou_Num_10", Update_G_SUM, 9);
                                        Log.v("アップデート処理:::", "Sou_Num_10 アップデート完了");
                                        System.out.println("Sou_Num_10 アップデート完了");
                                        break;

                                } // -------- END swich



                                //******* ダイアログ　非表示
                                dialog.dismiss();

                                //キーボード　非表示
                                if (getCurrentFocus() != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }

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


                    }

                    /**
                     *   ********* 「品目区分」 == 9 の　場合の　アップデート　処理 *************
                     *      リスト入力
                     */
                } else if(Hinmoku_K_i == 9 && !(Hinmoku_C.contains("SG"))) {

                    String hantei_str = list_get_spiiner_text_01.getText().toString();

                    if(d_list_sagyou_time_num.getText().toString().equals("")) {

                        toastMake("「作業時間」が入力されていません。", -200, 0);
                        return;

                    } else if(hantei_str.equals("変更する項目を選択してください。")) {

                        toastMake("「項目」を選択してください。", -200, 0);
                        return;

                    } else {

                        // ------------- エラー　作業時間がマイナスだったら
                        String d_list_sagyou_time_num_s = d_list_sagyou_time_num.getText().toString();
                        int d_list_sagyou_time_num_i = Integer.parseInt(d_list_sagyou_time_num_s);

                        if(d_list_sagyou_time_num_i < 0) {
                            toastMake("「作業時間」にマイナスの値が入力されています。", -200,0);
                            d_list_sagyou_time_num.setText("");
                            return;
                        }

                        //******************* アラート　表示 *******************
                        //-------　タイトル
                        TextView titleView;
                        // アクティビティ名を入れる
                        titleView = new TextView(List_Details.this);
                        titleView.setText("タスクデータ　更新確認");
                        titleView.setTextSize(20);
                        titleView.setTextColor(Color.WHITE);
                        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        titleView.setPadding(20,20,20,20);
                        titleView.setGravity(Gravity.CENTER);

                        AlertDialog.Builder bilder = new AlertDialog.Builder(List_Details.this);
                        //-------- アラートログの表示 開始 ----------

                        /**
                         *  ダイアログの項目セット
                         */
                        //------- タイトルセット
                        bilder.setCustomTitle(titleView);
                        //------- メッセージセット
                        bilder.setMessage("タスクを更新しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        /**
                                         *  アップデート処理
                                         */

                                        // 段取時間
                                        String update_dandori_t = d_list_dandori_time_num.getText().toString();
                                        //　作業時間　を　格納
                                        String update_sagyou_t = d_list_sagyou_time_num.getText().toString();

                                        // 商品名　スピナー
                                        String Syouhin_Mei = list_get_spiiner_text_01.getText().toString();

                                        /**
                                         *  開始時間　、　終了時間　追加
                                         *
                                         */
                                        // ****** 開始時間
                                        String list_start_time_view_num = list_start_time_view.getText().toString();
                                        // ****** 終了時間
                                        String list_end_time_view_num = list_end_time_view.getText().toString();

                                        //******** SQLlite へ　アップデート処理
                                        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
                                        SQLiteDatabase update_db = helper.getWritableDatabase();

                                        try {
                                            // ****** トランザクション　開始
                                            update_db.beginTransaction();

                                            ContentValues values = new ContentValues();

                                            values.put("send_col_06", Syouhin_Mei); // 商品名

                                            values.put("send_col_09", update_dandori_t); // 段取時間
                                            values.put("send_col_10", update_sagyou_t); // 作業時間

                                            /**
                                             *  開始時間　& 終了時間　追加
                                             */
                                            values.put("send_col_20", list_start_time_view_num); // 開始時間
                                            values.put("send_col_21", list_end_time_view_num); // 開始時間


                                            //********* アップデート処理
                                            update_db.update("Send_table_01",values, "id = ?",new String[] {String.valueOf(listId)});

                                            // ******** トランザクション　成功
                                            update_db.setTransactionSuccessful();

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } finally {
                                            // ******* トランザクション　終了
                                            update_db.endTransaction();

                                            if(update_db != null) {
                                                update_db.close();
                                            }
                                        }

                                        //************ 更新　完了メッセージ *************
                                        toastMake("更新が完了しました。", -200,0);

                                        // 遅延　スレッディング 0.6 秒
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 600);


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

                    }  //---------- END if

                    /**
                     *   手入力
                     */

                } else if (Hinmoku_K_i == 9 && Hinmoku_C.contains("SG")) {

                    if(d_list_sagyou_time_num.getText().toString().equals("")) {

                        toastMake("「作業時間」が入力されていません。値を修正してください。", -200,0);
                        return;

                    } else {

                        // ------------- エラー　作業時間がマイナスだったら
                        String d_list_sagyou_time_num_s = d_list_sagyou_time_num.getText().toString();
                        int d_list_sagyou_time_num_i = Integer.parseInt(d_list_sagyou_time_num_s);

                        if(d_list_sagyou_time_num_i < 0) {
                            toastMake("「作業時間」にマイナスの値が入力されています。値を修正してください。", -200,0);
                            d_list_sagyou_time_num.setText("");
                            return;
                        }

                        //******************* アラート　表示 *******************
                        //-------　タイトル
                        TextView titleView;
                        // アクティビティ名を入れる
                        titleView = new TextView(List_Details.this);
                        titleView.setText("タスクデータ　更新確認");
                        titleView.setTextSize(20);
                        titleView.setTextColor(Color.WHITE);
                        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                        titleView.setPadding(20,20,20,20);
                        titleView.setGravity(Gravity.CENTER);

                        AlertDialog.Builder bilder = new AlertDialog.Builder(List_Details.this);
                        //-------- アラートログの表示 開始 ----------

                        /**
                         *  ダイアログの項目セット
                         */
                        //------- タイトルセット
                        bilder.setCustomTitle(titleView);
                        //------- メッセージセット
                        bilder.setMessage("タスクを更新しますか？");

                        bilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                return;
                            }
                        });

                        bilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /**
                                 *  アップデート処理
                                 */

                                // 段取時間
                                String update_dandori_t = d_list_dandori_time_num.getText().toString();
                                //　作業時間　を　格納
                                String update_sagyou_t = d_list_sagyou_time_num.getText().toString();

                                /**
                                 *  開始時間　、　終了時間　追加
                                 *
                                 */
                                // ****** 開始時間
                                String list_start_time_view_num = list_start_time_view.getText().toString();
                                // ****** 終了時間
                                String list_end_time_view_num = list_end_time_view.getText().toString();

                                //******** SQLlite へ　アップデート処理
                                DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
                                SQLiteDatabase update_db = helper.getWritableDatabase();

                                try {
                                    // ****** トランザクション　開始
                                    update_db.beginTransaction();

                                    ContentValues values = new ContentValues();

                                    values.put("send_col_09", update_dandori_t); // 段取時間
                                    values.put("send_col_10", update_sagyou_t); // 作業時間

                                    /**
                                     *  開始時間　& 終了時間　追加
                                     */
                                    values.put("send_col_20", list_start_time_view_num); // 開始時間
                                    values.put("send_col_21", list_end_time_view_num); // 開始時間


                                    //********* アップデート処理
                                    update_db.update("Send_table_01",values, "id = ?",new String[] {String.valueOf(listId)});

                                    // ******** トランザクション　成功
                                    update_db.setTransactionSuccessful();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } finally {
                                    // ******* トランザクション　終了
                                    update_db.endTransaction();

                                    if(update_db != null) {
                                        update_db.close();
                                    }
                                }

                                //************ 更新　完了メッセージ *************
                                toastMake("更新が完了しました。", -200,0);

                                // 遅延　スレッディング 0.6 秒
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 600);


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

                    }  //---------- END if


                }
            }
        }); //------------------- 「更新」　ボタン END

        /**********************************　　フォーカスチェンジ イベント *****************************
         *
         */

        /**
         *  「段取り時間」　エディットテキスト　フォーカスチェンジ
         */
        d_list_dandori_time_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                     // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {
                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            } //************ END if

        });

        /**
         *  「作業時間」　エディットテキスト　フォーカスチェンジ
         */
        d_list_sagyou_time_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {
                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            } //************ END if

        });

       /**
         *
         *  「開始時」　エディットテキスト　フォーカスチェンジ
         */
        input_starttime_list_d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //******** フォーカス　排他処理
                    Edit_1_3_control(input_starttime_list_d, Input_endtime_list_d,
                            Input_kakousuu_list_d,input_furyou_list_d);

                     */

                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                }

            }
        }); //************* END 　「開始時」　フォーカス

        /**
         *  「終了時」　エディットテキスト　フォーカスチェンジ
         */
        Input_endtime_list_d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    /*
                    //******** フォーカス　排他処理
                    Edit_1_3_control(Input_endtime_list_d, input_starttime_list_d,
                            Input_kakousuu_list_d,input_furyou_list_d);

                     */

                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                }

            }
        }); //************* END 　「終了時」　フォーカス

        /**
         *  「加工数」　エディットテキスト　フォーカスチェンジ
         */
        Input_kakousuu_list_d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {

                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);



                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                }

            }
        }); //************* END 　「加工数」　フォーカス

        /**
         *  「不良品数」　エディットテキスト　フォーカスチェンジ
         */
        input_furyou_list_d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO 自動生成されたメソッド・スタブ
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                //************ フォーカス　を取得したら *************
                if(hasFocus) {
                    // ソフトキーボードの表示
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);


                } else {
                    //******* フォーカスが離れたら ********
                    // ## フォーカスが外れた時
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                }

            }
        }); //************* END 　「不良品数」　フォーカス


        /**
         * 　　「予定数」　アクション
         */
        d_list_yotei_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //-------- ソフトキーボード　「エンター」　ボタンが　押されたら
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    String d_list_yotei_num_tmp = d_list_yotei_num.getText().toString(); // 予定数
                    String d_list_souseisan_num_tmp = d_list_souseisan_num.getText().toString(); // 総生産数

                    if(Tosou_Flg) {
                        d_list_souseisan_num.setText(d_list_yotei_num_tmp); // 「予定数」を「総生産数」にセットする
                        input_syoukei_list_d.setText(d_list_yotei_num_tmp); // 「予定数」を「総生産数」にセットする

                    }  //************ 塗装フラグ　ON

                    // ########### ソフトキーボードを非表示
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(d_list_yotei_num.getWindowToken(), 0);

                }

                return false;
            }
        });

        /**
         *  　input_starttime_list_d　「開始時」　　エディットテキスト　イベント
         */
        input_starttime_list_d.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //-------- ソフトキーボード　「エンター」　ボタンが　押されたら
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //***********
                    // //************** エディットテキスト　排他処理　解除  *******************//
                    Edit_4_control_ON(input_starttime_list_d, Input_endtime_list_d, Input_kakousuu_list_d,
                            input_furyou_list_d);

                    // *********** 塗装課フラグ　ON
                    if(Tosou_Flg == true) {
                        toastMake("塗装課入力で、入力できません。「予定数」を入力してください。", -200,0);

                        Input_endtime_list_d.setText("0");
                        input_starttime_list_d.setText("0");

                        // 予定数へ　フォーカスを移動させる
                        d_list_yotei_num.requestFocus();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(d_list_yotei_num, InputMethodManager.SHOW_IMPLICIT);


                        return false;
                    } //********************** END 塗装課　フラグ　処理

                    // 「空」　チェック
                    if(input_starttime_list_d.getText().toString().equals("")) {
                        toastMake("値が入力されていません。", -200,0);
                        return false;

                    } else {

                               //-------- 「終了時」　テキストエディット　「値」　が　ある場合
                        // 開始時 取得
                        if(input_furyou_list_d.getText().toString().equals("") ||
                                Input_endtime_list_d.getText().toString().equals("")) {

                            toastMake("「入力項目」の値が「空」です。", 0, -200);
                            return false;

                        } else {

                            // 開始時 取得
                            String tmp_start_time = input_starttime_list_d.getText().toString();
                            int tmp_start_time_i = Integer.parseInt(tmp_start_time);

                            // 終了時　取得
                            String tmp_end_time = Input_endtime_list_d.getText().toString();
                            int tmp_end_time_i = Integer.parseInt(tmp_end_time);

                            // 総生産数　取得
                            String tmp_souseisan = d_list_souseisan_num.getText().toString();
                            int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                            // 不良品数　取得
                            String tmp_furyouhin = input_furyou_list_d.getText().toString();
                            int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                            // 良品数 取得
                            String tmp_ryouhin = Input_ryouhinn_list_d.getText().toString();
                            int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);

                            //------- 開始時 > 終了時 エラー
                            if(tmp_start_time_i > tmp_end_time_i) {
                                toastMake("「開始時」の値が不正な値です。", -200, 0);
                                input_starttime_list_d.setText("");
                                return false;

                            } else {

                                //************* 新たな　値のセット ***************
                                int goukei_sum = tmp_end_time_i - tmp_start_time_i;
                                d_list_souseisan_num.setText(String.valueOf(goukei_sum));

                                //　小計にもセット
                                input_syoukei_list_d.setText(String.valueOf(goukei_sum));

                                int goukri_ryouhin_sum = goukei_sum - tmp_furyouhin_i;
                                Input_ryouhinn_list_d.setText(String.valueOf(goukri_ryouhin_sum));

                            }

                        }


                    }

                } //---------------------------------

                // 総生産数　取得
                String tmp_souseisan = d_list_souseisan_num.getText().toString();
                int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                // 不良品数　取得
                String tmp_furyouhin = input_furyou_list_d.getText().toString();
                int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                //-------  「不良品数」 > 「総生産数」 エラー
                if(tmp_furyouhin_i > tmp_souseisan_i) {
                    toastMake("計算結果の値が不正な値です。", -200,0);
                    input_starttime_list_d.setText("");
                    return false;
                }


                return false;
            }
        });


        /**
         *  Input_endtime_list_d 「終了時」　エディットテキスト　イベント
         */
        Input_endtime_list_d.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // 「終了時」　 ソフトキーボード　エンターボタン　が　押されたら
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //***********
                    // //************** エディットテキスト　排他処理　解除  *******************//
                    Edit_4_control_ON(input_starttime_list_d, Input_endtime_list_d, Input_kakousuu_list_d,
                            input_furyou_list_d);

                    // *********** 塗装課フラグ　ON
                    if(Tosou_Flg == true) {
                        toastMake("塗装課入力で、入力できません。「予定数」を入力してください。", -200,0);

                        Input_endtime_list_d.setText("0");
                        input_starttime_list_d.setText("0");

                        // 予定数へ　フォーカスを移動させる
                        d_list_yotei_num.requestFocus();

                        // ########### ソフトキーボードを非表示
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(Input_endtime_list_d.getWindowToken(), 0);


                        return false;
                    } //********************** END 塗装課　フラグ　処理　

                    //---------- 「終了時」　エラー　チェック　値　「空」　チェック
                    if(Input_endtime_list_d.getText().toString().equals("")) {

                        toastMake("「終了時」の値が不正な値です。", -200,0);
                        return false;

                    } else {

                        //-------- 「終了時」　テキストエディット　「値」　が　ある場合
                        // 開始時 取得
                        if(input_starttime_list_d.getText().toString().equals("") ||
                                input_furyou_list_d.getText().toString().equals("")) {

                            toastMake("「入力項目」の値が「空」です。", 0, -200);
                            return false;

                        } else {

                            String tmp_start_time = input_starttime_list_d.getText().toString();
                            int tmp_start_time_i = Integer.parseInt(tmp_start_time);

                            // 終了時　取得
                            String tmp_end_time = Input_endtime_list_d.getText().toString();
                            int tmp_end_time_i = Integer.parseInt(tmp_end_time);

                            // 不良品数　取得
                            String tmp_furyouhin = input_furyou_list_d.getText().toString();
                            int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                            // 総生産数　取得
                            String tmp_souseisan = d_list_souseisan_num.getText().toString();
                            int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                            // 良品数 取得
                            String tmp_ryouhin = Input_ryouhinn_list_d.getText().toString();
                            int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);

                            /**
                             *  加工数　追加
                             */
                            String tmp_kakou = Input_kakousuu_list_d.getText().toString();
                            int tmp_kakou_i = Integer.parseInt(tmp_kakou);

                            //--------- 「開始時」 > 「終了時」 の場合　エラー
                            if(tmp_start_time_i > tmp_end_time_i) {

                                toastMake("「終了時」の値が不正な値です。", -200,0);
                                return false;

                            } else {


                                //************* 新たな　値のセット ***************
                                /**
                                 *  加工数　&  生産数（小計）　＊＊＊＊　追加
                                 */

                                int goukei_sum = (tmp_end_time_i - tmp_start_time_i) / tmp_kakou_i;
                                // 生産数　（小計）に値を入れる
                                input_syoukei_list_d.setText(String.valueOf(goukei_sum));

                             //   int Souseisan_SUM = goukei_sum + Souseisan_NUM;
                                //-------＊＊＊＊＊ 総生産数　を　加算 ＊＊＊＊＊＊　
                                d_list_souseisan_num.setText(String.valueOf(goukei_sum));

                                int goukri_ryouhin_sum = goukei_sum - tmp_furyouhin_i;
                                Input_ryouhinn_list_d.setText(String.valueOf(goukri_ryouhin_sum));

                            }
                        }

                    }
                } //-------------------------

                // 総生産数　取得
                String tmp_souseisan = d_list_souseisan_num.getText().toString();
                int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                // 不良品数　取得
                String tmp_furyouhin = input_furyou_list_d.getText().toString();
                int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                //-------  「不良品数」 > 「総生産数」 エラー
                if(tmp_furyouhin_i > tmp_souseisan_i) {
                    toastMake("計算結果の値が不正な値です。", -200,0);
                    Input_endtime_list_d.setText("");
                    return false;
                }

                return false;
            }
        });


        /***
         *   　＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊　加工数　追加　＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
         */
        Input_kakousuu_list_d.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    //***********
                    // //************** エディットテキスト　排他処理　解除  *******************//
                    Edit_4_control_ON(input_starttime_list_d, Input_endtime_list_d, Input_kakousuu_list_d,
                            input_furyou_list_d);

                    // ******　「空」　の　場合
                    if(Input_kakousuu_list_d.getText().toString().equals("")) {
                         toastMake("「値」が入力されていません。",0,-200);
                         return false;

                    //***** 「空」じゃなかった場合
                    } else {

                        //**************  「開始数時」 「不良品数」が　空　の場合
                        if(input_starttime_list_d.getText().toString().equals("") ||
                                input_furyou_list_d.getText().toString().equals("")) {

                            toastMake("「入力項目」の値が「空」です。", 0, -200);
                            return false;

                        } else {

                            // 開始時　
                            String tmp_start_time = input_starttime_list_d.getText().toString();
                            int tmp_start_time_i = Integer.parseInt(tmp_start_time);

                            // 終了時　取得
                            String tmp_end_time = Input_endtime_list_d.getText().toString();
                            int tmp_end_time_i = Integer.parseInt(tmp_end_time);

                            // 不良品数　取得
                            String tmp_furyouhin = input_furyou_list_d.getText().toString();
                            int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                            // 総生産数　取得
                            String tmp_souseisan = d_list_souseisan_num.getText().toString();
                            int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                            // 良品数 取得
                            String tmp_ryouhin = Input_ryouhinn_list_d.getText().toString();
                            int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);

                            /**
                             *  加工数　追加
                             */
                            String tmp_kakou = Input_kakousuu_list_d.getText().toString();
                            int tmp_kakou_i = Integer.parseInt(tmp_kakou);

                            //  開始時 > 終了時
                            if(tmp_start_time_i > tmp_end_time_i) {
                                toastMake("「不正な値」が入力されています。", 0 ,-200);
                                return false;

                            } else {

                                //************* 新たな　値のセット ***************
                                /**
                                 *  加工数　&  生産数（小計）　＊＊＊＊　追加
                                 */

                                // ********* 加工数で　割り切れなかった処理
                                if((tmp_end_time_i - tmp_start_time_i) % tmp_kakou_i != 0) {

                                        toastMake("「加工数」が割り切れない値です。\nもう一度入力してください。",
                                                0,-200);
                                        Input_kakousuu_list_d.setText("1");
                                        return false;

                                }  else {

                                        int goukei_sum = (tmp_end_time_i - tmp_start_time_i) / tmp_kakou_i;
                                        // 生産数　（小計）に値を入れる
                                        input_syoukei_list_d.setText(String.valueOf(goukei_sum));

                                     //   int Souseisan_SUM = goukei_sum + Souseisan_NUM;
                                        //-------＊＊＊＊＊ 総生産数　を　加算 ＊＊＊＊＊＊　
                                        d_list_souseisan_num.setText(String.valueOf(goukei_sum));

                                        int goukri_ryouhin_sum = goukei_sum - tmp_furyouhin_i;
                                        Input_ryouhinn_list_d.setText(String.valueOf(goukri_ryouhin_sum));

                                } //===== END if  加工数計算

                            }

                        }

                    }


                }

                return false;
            }
        });


        /**
         *  　input_furyou_list_d　「不良品数」　　エディットテキスト　イベント
         */
        input_furyou_list_d.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    //***********
                    // //************** エディットテキスト　排他処理　解除  *******************//
                    Edit_4_control_ON(input_starttime_list_d, Input_endtime_list_d, Input_kakousuu_list_d,
                            input_furyou_list_d);

                    //---------------- 値が「空」なら　エラー
                    if(input_furyou_list_d.getText().toString().equals("")) {
                        toastMake("値が入力されていません。", -200,0);
                        return false;

                    } else {

                        // 「開始時」　or 「終了時」　が　空だった場合
                        if(input_starttime_list_d.getText().toString().equals("") ||
                                Input_endtime_list_d.getText().toString().equals("")) {

                            toastMake("「入力項目」の値が「空」です。", 0, -200);
                            return false;

                        } else {

                            //------------- 値取得
                            //-------- 「終了時」　テキストエディット　「値」　が　ある場合
                            // 開始時 取得
                            String tmp_start_time = input_starttime_list_d.getText().toString();
                            int tmp_start_time_i = Integer.parseInt(tmp_start_time);

                            // 終了時　取得
                            String tmp_end_time = Input_endtime_list_d.getText().toString();
                            int tmp_end_time_i = Integer.parseInt(tmp_end_time);

                            // 総生産数　取得
                            String tmp_souseisan = d_list_souseisan_num.getText().toString();
                            int tmp_souseisan_i = Integer.parseInt(tmp_souseisan);

                            // 不良品数　取得
                            String tmp_furyouhin = input_furyou_list_d.getText().toString();
                            int tmp_furyouhin_i = Integer.parseInt(tmp_furyouhin);

                            // 良品数 取得
                            String tmp_ryouhin = Input_ryouhinn_list_d.getText().toString();
                            int tmp_ryouhin_i = Integer.parseInt(tmp_ryouhin);
                            /**
                             *  加工数　追加
                             */
                            String tmp_kakou = Input_kakousuu_list_d.getText().toString();
                            int tmp_kakou_i = Integer.parseInt(tmp_kakou);

                            //---------------- エラーチェック

                            if(tmp_souseisan_i < tmp_furyouhin_i) {
                                toastMake("「不良品数」の値が不正な値です。", -200,0);
                            } else {

                                //************* 新たな　値のセット ***************
                                /**
                                 *  加工数　&  生産数（小計）　＊＊＊＊　追加
                                 */

                                // ********* 加工数で　割り切れなかった処理
                                if((tmp_end_time_i - tmp_start_time_i) % tmp_kakou_i != 0) {

                                    toastMake("「加工数」が割り切れない値です。\nもう一度入力してください。",
                                            0, -200);
                                    Input_kakousuu_list_d.setText("1");
                                    return false;

                                } else {

                                    int goukei_sum = (tmp_end_time_i - tmp_start_time_i) / tmp_kakou_i;
                                    // 生産数　（小計）に値を入れる
                                    input_syoukei_list_d.setText(String.valueOf(goukei_sum));

                              //      int Souseisan_SUM = goukei_sum + Souseisan_NUM;
                                    //-------＊＊＊＊＊ 総生産数　を　加算 ＊＊＊＊＊＊　
                                    d_list_souseisan_num.setText(String.valueOf(goukei_sum));

                                    int goukri_ryouhin_sum = goukei_sum - tmp_furyouhin_i;
                                    Input_ryouhinn_list_d.setText(String.valueOf(goukri_ryouhin_sum));

                                } //******* END if

                            }

                        }

                    }
                } //-------- ソフトキーボード　エンター　が押されたら END ---------->

                return false;
            }
        });


    } //---------------------------------------------- onCreate END

    /**
     *  //----------------- メニュー　追加
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu_01,menu);

        return true;
    }

    /**
     *    // メニューボタン 「削除」 ボタン
     *     //******************* 作業追加 画面へ　遷移 ***********************
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.list_menu_btn_delete:

                //-------------- アラートログ　削除　-----------
                // タイトル
                TextView textView;
                textView = new TextView(List_Details.this);
                textView.setText("タスクの削除");
                textView.setTextSize(20);
                textView.setBackgroundColor(getResources().getColor(R.color.menu_color));
                textView.setTextColor(Color.WHITE);
                textView.setPadding(20, 20, 20, 20);
                textView.setGravity(Gravity.CENTER);

                //--------------　アラートログの表示　開始
                AlertDialog.Builder bilder = new AlertDialog.Builder(List_Details.this);

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

                        dbAdapter.openDB();

                        dbAdapter.selectDelete(String.valueOf(list_id));

                        Log.d("ログ： アイテムクリック削除　OK",String.valueOf(listId));

                        dbAdapter.closeDB();

                        finish();
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


                return false;

        }

        return true;

    }

     /**
     *  リスタートした時の処理
     */
    @Override
    protected void onRestart() {


        super.onRestart();
    }



    /**
     *  グラフ　作成
     */

    private void addEntry() {

        LineChart chart = findViewById(R.id.chart);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        // Grid背景色
        chart.setDrawGridBackground(true);
        chart.setEnabled(true);


        //-------- アクション操作 -------
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(true);

        //------ アニメーション

        chart.animateY(1200, Easing.EasingOption.Linear);
        chart.animateX(1200,Easing.EasingOption.Linear);


        //********** X軸周り ************
        XAxis xAxis = chart.getXAxis();

        xAxis.setTextColor(Color.parseColor("#394656"));

        // 最小値
        xAxis.setAxisMinimum(8);
        // 最大値
        xAxis.setAxisMaximum(18);
        // ラベル　個数
        xAxis.setLabelCount(10);

      //  xAxis.getAxisR().

        // 整数表示に
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + (int)value + " " +
                        "時";
            }
        }); // ----- setValueFormatter END

        //********** 追加 ************* // 動的リストからアイテムの数をintとして取得し、setLabelCountで使用
   //     chart.getAxisLeft().setLabelCount(8, true);

        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        // X軸に表示する Label の　リスト　最初の　原点は　""
        /*
        final String[] labels = {"", "テスト01"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
         */

        //*************** Y軸 設定
        YAxis left = chart.getAxisLeft();

        left.setTextColor(Color.parseColor("#394656"));



        // 最小値
        left.setAxisMinimum(0);

        //***************************************************
        //*********** グラフ　「予定数」セット ***********
        //***************************************************
        int g_num_max = 800;

        if(d_list_yotei_num_str.length() != 0) {
            int i_max = Integer.parseInt(d_list_yotei_num_str);
            left.setAxisMaximum(i_max);

        } else {
            // 「予定数」が空なら　800 を　セット
            left.setAxisMaximum(g_num_max);
        }

        //*** 多分　ラベルの　数
        left.setLabelCount(10);


        lineData = new LineData(getILineData());
        chart.setData(lineData);

    }

    /***
     * 　グラフ　値　取得
     */
    private List<ILineDataSet> getILineData() {

        int num_data = 100;
        int num_data_02 = 20;

        List<Entry> entries = new ArrayList<>();

        /*------------------ データ　セット 設定 -----------------*/

        int g_max_num = 10;
        int [] arr_g_data = new int[g_max_num];

        int [] arr_time = new int [10];

        //----------------  時刻用　8 ~ 17
        for(int i = 0; i < arr_time.length; i++) {
           arr_time[i] = i + 8;
        }

        //---------------- グラフ　値セット
        /*
        for(int i = 0; i < 10; i++) {
                System.out.println("グラフセット値****:::" + arr_Grafu_Num[i]);
                entries.add(new Entry(arr_time[i],  arr_Grafu_Num[i]));
        }
         */


        /****
         *  **************** グラフ　データセット *****************
         */
        for(int i = 0; i < S_Gurafu_Num.length; i++) {
            entries.add(new Entry(arr_time[i], S_Gurafu_Num[i]));
        }


        List<ILineDataSet> bars = new ArrayList<>();
        LineDataSet valueDataSet = new LineDataSet(entries, "生産数");

        // 整数で表示
        valueDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int)value;
            }
        });

        // データの文字サイス
        valueDataSet.setValueTextSize(11);
        // データの〇の色
        valueDataSet.setCircleColor(R.color.sankou_bulue);
        // データの文字色
        // valueDataSet.setValueTextColor(R.color.sankou_bulue);

        bars.add(valueDataSet);

        return bars;
    }


    private void GET_List_data() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayList<String> list_item = new ArrayList<>();

        String [] arr_list = {list_id};

        String[] arr_item = new String[26];
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM Send_table_01 WHERE id = ?",arr_list);

            if(cursor.moveToFirst()) {

                do {

                    //-------- id を　作業番号に　加工
                    int idx = cursor.getColumnIndex("send_col_01");
                    arr_item[0] = cursor.getString(idx);

                    d_list_id_str = date_DD + "-0" + list_id;

                    //******************** 作業部署　取得
                    idx = cursor.getColumnIndex("send_col_02");
                    arr_item[1] = cursor.getString(idx);

                    Get_Sgayou_B = arr_item[1];


                    idx = cursor.getColumnIndex("send_col_03");
                    arr_item[2] = cursor.getString(idx);

                    idx = cursor.getColumnIndex("send_col_04");
                    arr_item[3] = cursor.getString(idx);

                    // 品目区分　取得
                    Hinmoku_K = arr_item[3];

                    /**
                     *  品目　区分がなかった場合　（手打ち用）　処理
                     */

                    if(Hinmoku_K.equals("")) {
                        Hinmoku_K_i = 9;
                        Hinmoku_K = "9";
                        Hinmoku_C = "SG00";
                        System.out.println("load 内 Hinmoku_K_i:::" + Hinmoku_K_i);
                    } else {
                        Hinmoku_K_i = Integer.parseInt(Hinmoku_K);
                        System.out.println("load 内 Hinmoku_K_i:::" + Hinmoku_K_i);
                    }


                    // 品目コード　取得
                    idx = cursor.getColumnIndex("send_col_05");
                    arr_item[4] = cursor.getString(idx);

                    // 品目コード
                    Hinmoku_C = arr_item[4];

                    //-------- 商品名
                    idx = cursor.getColumnIndex("send_col_06");
                    arr_item[5] = cursor.getString(idx);

                    d_list_name_str = arr_item[5];


                    idx = cursor.getColumnIndex("send_col_07");
                    arr_item[6] = cursor.getString(idx);

                    idx = cursor.getColumnIndex("send_col_08");
                    arr_item[7] = cursor.getString(idx);

                    // 段取時間
                    idx = cursor.getColumnIndex("send_col_09");
                    arr_item[8] = cursor.getString(idx);

                    d_list_dandori_time_num_str = arr_item[8];

                    //---------- 作業時間
                    idx = cursor.getColumnIndex("send_col_10");
                    arr_item[9] = cursor.getString(idx);

                    d_list_sagyou_time_num_str = arr_item[9];

                    // --------- 予定数量
                    idx = cursor.getColumnIndex("send_col_11");
                    arr_item[10] = cursor.getString(idx);

                    d_list_yotei_num_str = arr_item[10];
                    //************************************** 予定数　取得
                    int yotei_i = 0;
                    if(d_list_yotei_num_str.length() != 0) {
                        yotei_i = Integer.parseInt(d_list_yotei_num_str); // 予定数
                    } else {
                        d_list_yotei_num_str = "0";
                    }


                    //---------- 開始数量
                    idx = cursor.getColumnIndex("send_col_12");
                    arr_item[11] = cursor.getString(idx);

                    d_list_start_num = arr_item[11];

                    //---------- 終了数量
                    idx = cursor.getColumnIndex("send_col_13");
                    arr_item[12] = cursor.getString(idx);

                    d_list_end_num = arr_item[12];

                    //-------- 総生産数
                    idx = cursor.getColumnIndex("send_col_14");
                    arr_item[13] = cursor.getString(idx);

                    d_list_souseisan_num_str = arr_item[13];

                    //********************** 総生産数　取得
                    int Seisan_i = 0;
                    if(d_list_souseisan_num_str.length() != 0) {
                        Seisan_i = Integer.parseInt(d_list_souseisan_num_str);
                    } else {
                        d_list_souseisan_num_str = "0";
                    }

                    /**
                     *  「総生産数」が　０　以上 , かつ　「予定数」 == 「総生産数」 　塗装課データとして判断する
                     */
                    if(Seisan_i != 0 && yotei_i == Seisan_i) {
                        Tosou_Flg = true;
                    } else {
                        Tosou_Flg = false;
                    }


                    //-------- ********  グラフ用に 総生産数を list に 格納する ********
                    String gurafu_num = d_list_souseisan_num_str;


                    // 不良品数
                    idx = cursor.getColumnIndex("send_col_15");
                    arr_item[14] = cursor.getString(idx);

                    d_list_furyou_num = arr_item[14];

                    // 良品数
                    idx = cursor.getColumnIndex("send_col_16");
                    arr_item[15] = cursor.getString(idx);

                    d_list_ryouhin_num = arr_item[15];

                    // 最終工程
                    idx = cursor.getColumnIndex("send_col_17");
                    arr_item[16] = cursor.getString(idx);

                    // 最終チェック　挿入
                    check_str = arr_item[16];

                    System.out.println("最終チェック:::::値:::::" + check_str);

                    /***
                     *  加工数　追加
                     */
                    // ****** 加工数
                    idx = cursor.getColumnIndex("send_col_18");
                    arr_item[17] = cursor.getString(idx);

                    Input_kakousuu_list_d_str = arr_item[17];

                    // 再送フラグ
                    idx = cursor.getColumnIndex("send_col_19");
                    arr_item[18] = cursor.getString(idx);

                    /**
                     *  開始時間　& 終了時間 & グラフ id(send_col_22) 追加
                     */
                    // 開始時間
                    idx = cursor.getColumnIndex("send_col_20");
                    arr_item[19] = cursor.getString(idx);
                    Start_time_str = arr_item[19];

                    // 終了時間
                    idx = cursor.getColumnIndex("send_col_21");
                    arr_item[20] = cursor.getString(idx);
                    End_time_str = arr_item[20];

                    // グラフ id
                    idx = cursor.getColumnIndex("send_col_22");
                    arr_item[21] = cursor.getString(idx);
                    G_id = arr_item[21];

                    //  yyyyMMdd
                    idx = cursor.getColumnIndex("send_col_23");
                    arr_item[22] = cursor.getString(idx);

                    //  色段取時間
                    idx = cursor.getColumnIndex("send_col_24");
                    arr_item[23] = cursor.getString(idx);
                    String tmp_Iro_Time_str = arr_item[23];

                    if(tmp_Iro_Time_str.length() != 0) {
                        Iro_Time_i = Integer.parseInt(tmp_Iro_Time_str);
                    } else {
                        tmp_Iro_Time_str = "0";
                    }


                    //  型段取時間
                    idx = cursor.getColumnIndex("send_col_25");
                    arr_item[24] = cursor.getString(idx);
                    String tmp_Kata_Time_str = arr_item[24];

                    if(tmp_Kata_Time_str.length() != 0) {
                        Kata_Time_i = Integer.parseInt(tmp_Kata_Time_str);
                    } else {
                        tmp_Kata_Time_str = "0";
                    }


                    //  機械コード
                    idx = cursor.getColumnIndex("send_col_26");
                    arr_item[25] = cursor.getString(idx);

                    // 機械コード　値　取得
                    Kikai_Code = arr_item[25];


                    for(int i = 0; i < arr_item.length; i++) {
                        list_item.add(arr_item[i]);
                    }

                } while (cursor.moveToNext()); //******************* END while
            }

            for(String s : list_item) {
                System.out.println("サーチ結果" + s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


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
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.
     */
    public static String getNowDate_02(){
        //  final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    /**
     *   //-------------------- トースト作成 ----------------------
     */
    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    private void init_xml() {

        /**
         *  リスト選択　の　ビュー
         */
        if(Hinmoku_K_i == 9) {

            setContentView(R.layout.list_operation);

        }

    }


    private void init() {

        //------------- ラベル
        d_list_id_label = findViewById(R.id.d_list_id_label);
        d_list_name_label = findViewById(R.id.d_list_name_label);
        d_list_yotei_num_label = findViewById(R.id.d_list_yotei_num_label);
        d_list_souseisan_label = findViewById(R.id.d_list_souseisan_label);
        d_list_ryouhinn_l = findViewById(R.id.d_list_ryouhinn_l);

        // カードビュー
        content_back_01 = findViewById(R.id.content_back_01);

        // 開始時　ラベル
        d_list_kaishi_l = findViewById(R.id.d_list_kaishi_l);
        // 終了時　ラベル
        d_list_syuuryou_l = findViewById(R.id.d_list_syuuryou_l);
        // 加工数　ラベル
        d_list_kakou = findViewById(R.id.d_list_kakou);
        // 不良品数
        d_list_furyou_l = findViewById(R.id.d_list_furyou_l);
        // 良品数
        d_list_ryouhinn_l = findViewById(R.id.d_list_ryouhinn_l);

        //------------- 値　代入　textView
        // 作業番号
        d_list_id = (TextView) findViewById(R.id.d_list_id);
        // 商品名
        d_list_name = (TextView) findViewById(R.id.d_list_name);

        // 総生産数
        d_list_souseisan_num = (TextView) findViewById(R.id.d_list_souseisan_num);
        d_list_souseisan_num.setFocusable(false);
        d_list_souseisan_num.setFocusableInTouchMode(false);
        d_list_souseisan_num.setEnabled(false);

        //--------------- 予定数量
        d_list_yotei_num = (EditText) findViewById(R.id.d_list_yotei_num);
        d_list_yotei_num.setInputType(InputType.TYPE_CLASS_NUMBER);

        //--------------- 作業時間
        d_list_sagyou_time_num =(EditText)findViewById(R.id.d_list_sagyou_time_num);
        d_list_sagyou_time_num.setInputType(InputType.TYPE_CLASS_NUMBER);

        //--------------- 段取時間
        d_list_dandori_time_num = (EditText) findViewById(R.id.d_list_dandori_time_num);
        d_list_dandori_time_num.setInputType(InputType.TYPE_CLASS_NUMBER);



        /**
         *  開始時間　& 終了時間　追加
         */
        list_start_time = (Button) findViewById(R.id.list_start_time);
        list_end_time = (Button) findViewById(R.id.list_end_time);

        list_start_time_view = (TextView) findViewById(R.id.list_start_time_view);
        list_end_time_view = (TextView) findViewById(R.id.list_end_time_view);



        //--------------- エディットテキスト

        // 良品数
        Input_ryouhinn_list_d = (EditText) findViewById(R.id.Input_ryouhinn_list_d);
        Input_ryouhinn_list_d.setFocusable(false);
        Input_ryouhinn_list_d.setFocusableInTouchMode(false);
        Input_ryouhinn_list_d.setEnabled(false);

        //------------------- ボタン
        d_list_back_btn = (MaterialButton) findViewById(R.id.d_list_back_btn);
        d_list_save_btn = (MaterialButton) findViewById(R.id.d_list_save_btn);

        /**
         * 加工数　追加
         */
        Input_kakousuu_list_d = (EditText) findViewById(R.id.Input_kakousuu_list_d);
        Input_kakousuu_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);

        /**
         *  生産数　（小計）　追加
         */
        input_syoukei_list_d = (EditText) findViewById(R.id.input_syoukei_list_d);

        input_syoukei_list_d.setEnabled(false);
        input_syoukei_list_d.setFocusableInTouchMode(false);
        input_syoukei_list_d.setFocusable(false);

        // チェックボックス & ラベル
        last_koutei_label = findViewById(R.id.last_koutei_label);
        list_last_check = (CheckBox) findViewById(R.id.list_last_check);

        /**
         *  リスト選択　の　ビュー
         */

        System.out.println("init内 Hinmoku_K:::" + Hinmoku_K);
        System.out.println("init内 Hinmoku_K_i:::" + Hinmoku_K_i);
        System.out.println("init内 Hinmoku_K_i:::" + Hinmoku_C);



        /**
         *   ****** スピナー
         */
        list_spinner_Tosou = findViewById(R.id.list_spinner_Tosou); // 塗装課用　スピナー

        list_spinner_001 = findViewById(R.id.list_spinner_001);
        list_spinner_002 = findViewById(R.id.list_spinner_002);
        list_spinner_003 = findViewById(R.id.list_spinner_003);
        list_spinner_004 = findViewById(R.id.list_spinner_004);

        //***** スピナー受け取り用　テキストビュー
        list_get_spiiner_text_01 = findViewById(R.id.list_get_spiiner_text_01);

        //**** エディットテキスト　画面タップ　ソフトキーボード　外し用
        focusView_02 = findViewById(R.id.focusView_02);

        /***
         *   樹脂成型課　用　
         */
        Zyushi_Seikei_btn_a = findViewById(R.id.Zyushi_Seikei_btn_a);

        iro_z_box_a = (TextInputLayout)findViewById(R.id.iro_z_box_a);
        Kata_z_box_a = (TextInputLayout)findViewById(R.id.Kata_z_box_a);
        Kikai_z_box_a = (TextInputLayout)findViewById(R.id.Kikai_z_box_a);

        iro_z_edit_a = (EditText) findViewById(R.id.iro_z_edit_a);
        Kata_z_edit_a = (EditText) findViewById(R.id.Kata_z_edit_a);
        Kikai_z_input_a = (EditText) findViewById(R.id.Kikai_z_input_a);

        iro_z_edit_a.setInputType(InputType.TYPE_CLASS_NUMBER);
        Kata_z_edit_a.setInputType(InputType.TYPE_CLASS_NUMBER);
        Kikai_z_input_a.setInputType(InputType.TYPE_CLASS_NUMBER);

        /**
         *  *********************************  品目区分 9, 00 ,  その他　での　View の　分岐 ***************************
         */
        if(Hinmoku_K.equals("9") && !(Hinmoku_C.contains("SG")) && !(Hinmoku_C.startsWith("T-"))) {

            input_starttime_list_d = (EditText) findViewById(R.id.input_starttime_list_d);
            Input_endtime_list_d = (EditText) findViewById(R.id.Input_endtime_list_d);
            input_furyou_list_d = (EditText) findViewById(R.id.input_furyou_list_d);

            // 開始時
            input_starttime_list_d.setFocusable(false);
            input_starttime_list_d.setFocusableInTouchMode(false);
            input_starttime_list_d.setEnabled(false);

            // 終了時
            Input_endtime_list_d.setFocusable(false);
            Input_endtime_list_d.setFocusableInTouchMode(false);
            Input_endtime_list_d.setEnabled(false);

            // 不良品数
            input_furyou_list_d.setFocusable(false);
            input_furyou_list_d.setFocusableInTouchMode(false);
            input_furyou_list_d.setEnabled(false);

            // 最終工程チェック   **** 値は 0 で　チェック　なし に設定
            list_last_check = (CheckBox) findViewById(R.id.list_last_check);
            list_last_check.setChecked(false);

            // 段取時間　非　アクティブ
            d_list_dandori_time_num.setFocusable(false);
            d_list_dandori_time_num.setEnabled(false);
            d_list_dandori_time_num.setFocusableInTouchMode(false);

            /***
             * 非表示　設定
             */

            // 開始時
            input_starttime_list_d.setVisibility(View.GONE);
            // 終了時
            Input_endtime_list_d.setVisibility(View.GONE);
            // 加工数
            Input_kakousuu_list_d.setVisibility(View.GONE);
            // 不良品数
            input_furyou_list_d.setVisibility(View.GONE);
            // 生産数
            input_syoukei_list_d.setVisibility(View.GONE);
            // 良品数
            Input_ryouhinn_list_d.setVisibility(View.GONE);

            // 最終工程チェック 非表示
            list_last_check.setVisibility(View.GONE);

            //****** ラベル
            d_list_kaishi_l.setVisibility(View.GONE);
            d_list_syuuryou_l.setVisibility(View.GONE);
            d_list_kakou.setVisibility(View.GONE);
            d_list_furyou_l.setVisibility(View.GONE);
            d_list_ryouhinn_l.setVisibility(View.GONE);
            last_koutei_label.setVisibility(View.GONE);

            // カードビュー
            content_back_01.setVisibility(View.GONE);

            // 最終工程チェック 非表示
            list_last_check.setVisibility(View.GONE);

            // グラフ　非表示
            LineChart chart = findViewById(R.id.chart);
            chart.setVisibility(View.GONE);

            //************* リスト 表示
            list_select_header_view_02.setVisibility(View.VISIBLE);


            /**
             *  リスト　表示
             */

            list_spinner_Tosou.setVisibility(View.GONE); // 塗装課用　スピナー

            list_spinner_001.setVisibility(View.VISIBLE);
            list_spinner_002.setVisibility(View.VISIBLE);
            list_spinner_003.setVisibility(View.VISIBLE);
            list_spinner_004.setVisibility(View.VISIBLE);
            list_get_spiiner_text_01.setVisibility(View.VISIBLE);

            /**
             *   樹脂成型課　非表示
             */
            Zyushi_Seikei_btn_a.setVisibility(View.GONE);
            iro_z_edit_a.setVisibility(View.GONE);
            Kata_z_edit_a.setVisibility(View.GONE);
            Kikai_z_input_a.setVisibility(View.GONE);

            iro_z_box_a.setVisibility(View.GONE);
            Kata_z_box_a.setVisibility(View.GONE);
            Kikai_z_box_a.setVisibility(View.GONE);

            // --------------- 「予定数」「総生産数」　非表示
            d_list_yotei_num_label.setVisibility(View.GONE);
            d_list_yotei_num.setVisibility(View.GONE);
            d_list_souseisan_label.setVisibility(View.GONE);
            d_list_souseisan_num.setVisibility(View.GONE);


            set_num_09();

            /**
             *＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊  塗装課　用　＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
             *          品目 9  && T-11 , T-22 etc
             */
        } else if(Hinmoku_K.contains("9") && Hinmoku_C.startsWith("T-")) {

            System.out.println("でてまっせ～");

            input_starttime_list_d = (EditText) findViewById(R.id.input_starttime_list_d);
            Input_endtime_list_d = (EditText) findViewById(R.id.Input_endtime_list_d);
            input_furyou_list_d = (EditText) findViewById(R.id.input_furyou_list_d);

            // 開始時
            input_starttime_list_d.setFocusable(false);
            input_starttime_list_d.setFocusableInTouchMode(false);
            input_starttime_list_d.setEnabled(false);

            // 終了時
            Input_endtime_list_d.setFocusable(false);
            Input_endtime_list_d.setFocusableInTouchMode(false);
            Input_endtime_list_d.setEnabled(false);

            // 不良品数
            input_furyou_list_d.setFocusable(false);
            input_furyou_list_d.setFocusableInTouchMode(false);
            input_furyou_list_d.setEnabled(false);

            // 最終工程チェック   **** 値は 0 で　チェック　なし に設定
            list_last_check = (CheckBox) findViewById(R.id.list_last_check);
            list_last_check.setChecked(false);

            // 段取時間　非　アクティブ
            d_list_dandori_time_num.setFocusable(false);
            d_list_dandori_time_num.setEnabled(false);
            d_list_dandori_time_num.setFocusableInTouchMode(false);

            /***
             * 非表示　設定
             */

            // 開始時
            input_starttime_list_d.setVisibility(View.GONE);
            // 終了時
            Input_endtime_list_d.setVisibility(View.GONE);
            // 加工数
            Input_kakousuu_list_d.setVisibility(View.GONE);
            // 不良品数
            input_furyou_list_d.setVisibility(View.GONE);
            // 生産数
            input_syoukei_list_d.setVisibility(View.GONE);
            // 良品数
            Input_ryouhinn_list_d.setVisibility(View.GONE);

            // 最終工程チェック 非表示
            list_last_check.setVisibility(View.GONE);

            //****** ラベル
            d_list_kaishi_l.setVisibility(View.GONE);
            d_list_syuuryou_l.setVisibility(View.GONE);
            d_list_kakou.setVisibility(View.GONE);
            d_list_furyou_l.setVisibility(View.GONE);
            d_list_ryouhinn_l.setVisibility(View.GONE);
            last_koutei_label.setVisibility(View.GONE);

            // カードビュー
            content_back_01.setVisibility(View.GONE);

            // 最終工程チェック 非表示
            list_last_check.setVisibility(View.GONE);

            // グラフ　非表示
            LineChart chart = findViewById(R.id.chart);
            chart.setVisibility(View.GONE);

            //************* リスト 表示
            list_select_header_view_02.setVisibility(View.VISIBLE);


            /**
             *  リスト　表示
             */
            list_spinner_Tosou.setVisibility(View.VISIBLE); // 塗装課用　スピナー
            list_spinner_001.setVisibility(View.VISIBLE);
            list_spinner_002.setVisibility(View.VISIBLE);
            list_spinner_003.setVisibility(View.VISIBLE);
            list_spinner_004.setVisibility(View.VISIBLE);
            list_get_spiiner_text_01.setVisibility(View.VISIBLE);

            /**
             *   樹脂成型課　非表示
             */
            Zyushi_Seikei_btn_a.setVisibility(View.GONE);
            iro_z_edit_a.setVisibility(View.GONE);
            Kata_z_edit_a.setVisibility(View.GONE);
            Kikai_z_input_a.setVisibility(View.GONE);

            iro_z_box_a.setVisibility(View.GONE);
            Kata_z_box_a.setVisibility(View.GONE);
            Kikai_z_box_a.setVisibility(View.GONE);


            set_num_09_Tosou();

            //**************************** END 塗装課用 ****************************

        } else if (Hinmoku_K.contains("9") && Hinmoku_C.contains("SG00") || Hinmoku_C.contains("SG01") || Hinmoku_C.contains("SG02") ||
        Hinmoku_C.contains("SG03")) {

            /**
             *   ********** 手入力 *********** & 「遅刻」「早退」「残業」
             */

            input_starttime_list_d = (EditText) findViewById(R.id.input_starttime_list_d);
            Input_endtime_list_d = (EditText) findViewById(R.id.Input_endtime_list_d);
            input_furyou_list_d = (EditText) findViewById(R.id.input_furyou_list_d);

            //----------- 作業時間
            d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);

            // 段取時間　非　アクティブ
            d_list_dandori_time_num.setFocusable(true);
            d_list_dandori_time_num.setEnabled(true);
            d_list_dandori_time_num.setFocusableInTouchMode(true);


            /***
             *  非表示　処理
             */
            //****** ラベル
            d_list_kaishi_l.setVisibility(View.GONE);
            d_list_syuuryou_l.setVisibility(View.GONE);
            d_list_kakou.setVisibility(View.GONE);
            d_list_furyou_l.setVisibility(View.GONE);
            d_list_ryouhinn_l.setVisibility(View.GONE);
            last_koutei_label.setVisibility(View.GONE);

            // カードビュー
            content_back_01.setVisibility(View.GONE);


            // 開始時
            input_starttime_list_d.setVisibility(View.GONE);
            // 終了時
            Input_endtime_list_d.setVisibility(View.GONE);
            // 加工数
            Input_kakousuu_list_d.setVisibility(View.GONE);
            // 不良品数
            input_furyou_list_d.setVisibility(View.GONE);
            // 生産数
            input_syoukei_list_d.setVisibility(View.GONE);
            // 良品数
            Input_ryouhinn_list_d.setVisibility(View.GONE);

            // 最終工程チェック 非表示
            list_last_check.setVisibility(View.GONE);

            // グラフ　非表示
            LineChart chart = findViewById(R.id.chart);
            chart.setVisibility(View.GONE);

            // ************ リスト　表示
            list_select_header_view_02.setVisibility(View.GONE);

            /**
             *  リスト　非表示
             */
            list_spinner_Tosou.setVisibility(View.GONE); // 塗装課用　スピナー
            list_spinner_001.setVisibility(View.GONE);
            list_spinner_002.setVisibility(View.GONE);
            list_spinner_003.setVisibility(View.GONE);
            list_spinner_004.setVisibility(View.GONE);
            list_get_spiiner_text_01.setVisibility(View.GONE);

            /**
             *   樹脂成型課　非表示
             */
            Zyushi_Seikei_btn_a.setVisibility(View.GONE);
            iro_z_edit_a.setVisibility(View.GONE);
            Kata_z_edit_a.setVisibility(View.GONE);
            Kikai_z_input_a.setVisibility(View.GONE);

            iro_z_box_a.setVisibility(View.GONE);
            Kata_z_box_a.setVisibility(View.GONE);
            Kikai_z_box_a.setVisibility(View.GONE);

            // --------------- 「予定数」「総生産数」　非表示
            d_list_yotei_num_label.setVisibility(View.GONE);
            d_list_yotei_num.setVisibility(View.GONE);
            d_list_souseisan_label.setVisibility(View.GONE);
            d_list_souseisan_num.setVisibility(View.GONE);


            set_num_0000();


        } else if(Iro_Time_i > 0 || Kata_Time_i > 0) { //********* 樹脂成型課

            /**
             * *********************** 「樹脂成型課」　更新　入力用 *****************************
             */

            //----- 開始時
            input_starttime_list_d = (EditText) findViewById(R.id.input_starttime_list_d);
            input_starttime_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);

            //-----終了時
            Input_endtime_list_d = (EditText) findViewById(R.id.Input_endtime_list_d);
            Input_endtime_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);


            // 不良品数
            input_furyou_list_d = (EditText) findViewById(R.id.input_furyou_list_d);
            input_furyou_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 開始時
            input_starttime_list_d.setFocusable(true);
            input_starttime_list_d.setFocusableInTouchMode(true);
            input_starttime_list_d.setEnabled(true);

            // 終了時
            Input_endtime_list_d.setFocusable(true);
            Input_endtime_list_d.setFocusableInTouchMode(true);
            Input_endtime_list_d.setEnabled(true);

            // 不良品数
            input_furyou_list_d.setFocusable(true);
            input_furyou_list_d.setFocusableInTouchMode(true);
            input_furyou_list_d.setEnabled(true);

            // 最終工程チェック
            list_last_check = (CheckBox) findViewById(R.id.list_last_check);

            int check_str_i = Integer.parseInt(check_str);
            if(check_str_i == 1) {
                list_last_check.setChecked(true);
            } else {
                list_last_check.setChecked(false);
            }

            //**************** リスト非表示
            list_select_header_view_02.setVisibility(View.GONE);

            /**
             *  リスト　非表示
             */
            list_spinner_Tosou.setVisibility(View.GONE); // 塗装課用　スピナー
            list_spinner_001.setVisibility(View.GONE);
            list_spinner_002.setVisibility(View.GONE);
            list_spinner_003.setVisibility(View.GONE);
            list_spinner_004.setVisibility(View.GONE);
            list_get_spiiner_text_01.setVisibility(View.GONE);

            /**
             *   樹脂成型課　非表示
             */
            Zyushi_Seikei_btn_a.setVisibility(View.VISIBLE);
            iro_z_edit_a.setVisibility(View.VISIBLE);
            Kata_z_edit_a.setVisibility(View.VISIBLE);
            Kikai_z_input_a.setVisibility(View.VISIBLE);

            iro_z_box_a.setVisibility(View.VISIBLE);
            Kata_z_box_a.setVisibility(View.VISIBLE);
            Kikai_z_box_a.setVisibility(View.VISIBLE);

            //******** 値セット
            set_num_Zyushi();

        } else {

            /**
             *  　＊＊＊＊＊＊「QR , 現品票,  入力」　通常入力　＊＊＊＊＊＊＊
             */


            //----- 開始時
            input_starttime_list_d = (EditText) findViewById(R.id.input_starttime_list_d);
            input_starttime_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);
            //-----終了時
            Input_endtime_list_d = (EditText) findViewById(R.id.Input_endtime_list_d);
            Input_endtime_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 不良品数
            input_furyou_list_d = (EditText) findViewById(R.id.input_furyou_list_d);
            input_furyou_list_d.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 開始時
            input_starttime_list_d.setFocusable(true);
            input_starttime_list_d.setFocusableInTouchMode(true);
            input_starttime_list_d.setEnabled(true);

            // 終了時
            Input_endtime_list_d.setFocusable(true);
            Input_endtime_list_d.setFocusableInTouchMode(true);
            Input_endtime_list_d.setEnabled(true);

            // 不良品数
            input_furyou_list_d.setFocusable(true);
            input_furyou_list_d.setFocusableInTouchMode(true);
            input_furyou_list_d.setEnabled(true);

            // 最終工程チェック
            list_last_check = (CheckBox) findViewById(R.id.list_last_check);

            int check_str_i = Integer.parseInt(check_str);
            if(check_str_i == 1) {
                list_last_check.setChecked(true);
            } else {
                list_last_check.setChecked(false);
            }

            //**************** リスト非表示
            list_select_header_view_02.setVisibility(View.GONE);


            /**
             *  リスト　非表示
             */
            list_spinner_Tosou.setVisibility(View.GONE); // 塗装課用　スピナー
            list_spinner_001.setVisibility(View.GONE);
            list_spinner_002.setVisibility(View.GONE);
            list_spinner_003.setVisibility(View.GONE);
            list_spinner_004.setVisibility(View.GONE);
            list_get_spiiner_text_01.setVisibility(View.GONE);

            /**
             *   樹脂成型課　非表示
             */
            Zyushi_Seikei_btn_a.setVisibility(View.GONE);
            iro_z_edit_a.setVisibility(View.GONE);
            Kata_z_edit_a.setVisibility(View.GONE);
            Kikai_z_input_a.setVisibility(View.GONE);

            iro_z_box_a.setVisibility(View.GONE);
            Kata_z_box_a.setVisibility(View.GONE);
            Kikai_z_box_a.setVisibility(View.GONE);

            //******** 値セット
            set_num();

            // 値を　int　型に　パース　する
            Edit_Parse_01();

        }

    }

    /**
     *  値　セット    9 , 0000 , 以外
     */
    private void set_num_Zyushi() {

        /**
         *  SELECT 値セット
         */

        // 作業番号  dd-0 + id
        //  d_list_id.setText(d_list_id_str);
        d_list_id.setText(G_id);
        // 商品名
        d_list_name.setText(d_list_name_str);
        // 予定数量
        d_list_yotei_num.setText(d_list_yotei_num_str);

        // 総生産数
        d_list_souseisan_num.setText(d_list_souseisan_num_str);

        // 開始時  ******* 終了時　の　値セット  d_list_start_num  d_list_end_num
        input_starttime_list_d.setText(d_list_start_num);
        // 終了時  d_list_end_num
        Input_endtime_list_d.setText(d_list_end_num);
        // 不良品数
        input_furyou_list_d.setText(d_list_furyou_num);
        // 良品数
        Input_ryouhinn_list_d.setText(d_list_ryouhin_num);

        //----------- 作業時間
        d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);
        //----------- 段取時間
        d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        /**
         *   加工数　追加
         */
        Input_kakousuu_list_d.setText(Input_kakousuu_list_d_str);

        //************** 小計　値セット start
        String tmp_01 = d_list_start_num; // 開始時値
        String tmp_02 = d_list_end_num; // 終了時値

        int tmp_01_i = Integer.parseInt(tmp_01); // 開始時
        int tmp_02_i = Integer.parseInt(tmp_02); // 終了時
        int tmp_sum = tmp_02_i - tmp_01_i;

        // ******** 小計に値セット
        input_syoukei_list_d.setText(String.valueOf(tmp_sum));
        //************** 小計　値セット END

        //****** 開始時　値セット
        list_start_time_view.setText(Start_time_str);
        //****** 終了時　値セット
        list_end_time_view.setText(End_time_str);

        /**
         *  　「樹脂成型課」　値セット
         *    Iro_Time_i　=> 色段取時間  , Kata_Time_i => 型段取時間   Kikai_Code => 機械コード
         */

        iro_z_edit_a.setText(String.valueOf(Iro_Time_i)); // 色段取時間
        Kata_z_edit_a.setText(String.valueOf(Kata_Time_i)); // 型段取時間
        Kikai_z_input_a.setText(Kikai_Code); // 機械コード

    }

    /**
     *  値　セット    9 , 0000 , 以外   「通常入力」
     */
    private void set_num() {

        /**
         *  SELECT 値セット
         */

        // 作業番号  dd-0 + id
      //  d_list_id.setText(d_list_id_str);
          d_list_id.setText(G_id);
        // 商品名
        d_list_name.setText(d_list_name_str);
        // 予定数量
        d_list_yotei_num.setText(d_list_yotei_num_str);

        // 総生産数
        d_list_souseisan_num.setText(d_list_souseisan_num_str);

        // 開始時  ******* 終了時　の　値セット  d_list_start_num  d_list_end_num
        input_starttime_list_d.setText(d_list_start_num);
        // 終了時  d_list_end_num
        Input_endtime_list_d.setText(d_list_end_num);
        // 不良品数
        input_furyou_list_d.setText(d_list_furyou_num);
        // 良品数
        Input_ryouhinn_list_d.setText(d_list_ryouhin_num);

        //----------- 作業時間
        d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);
        //----------- 段取時間
        d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        /**
         *   加工数　追加
         */
        Input_kakousuu_list_d.setText(Input_kakousuu_list_d_str);

        //************** 小計　値セット start
        String tmp_01 = d_list_start_num; // 開始時値
        String tmp_02 = d_list_end_num; // 終了時値

        int tmp_01_i = Integer.parseInt(tmp_01); // 開始時
        int tmp_02_i = Integer.parseInt(tmp_02); // 終了時
        int tmp_sum = tmp_02_i - tmp_01_i;

        // ******** 小計に値セット
        input_syoukei_list_d.setText(String.valueOf(tmp_sum));
        //************** 小計　値セット END

        //****** 開始時　値セット
        list_start_time_view.setText(Start_time_str);
        //****** 終了時　値セット
        list_end_time_view.setText(End_time_str);

    }

    private void set_num_09() {

        /**
         *  SELECT 値セット
         */

        // 作業番号  dd-0 + id
 //       d_list_id.setText(d_list_id_str);
        d_list_id.setText(G_id);
        // 商品名
        d_list_name.setText(d_list_name_str);

        //----------- 段取時間
        if(d_list_dandori_time_num_str.length() == 0 || d_list_dandori_time_num_str.equals("")) {
            d_list_dandori_time_num_str = "0";

            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }  else {
            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }

        //----------- 作業時間
        d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);

        //****** 開始時　値セット
        list_start_time_view.setText(Start_time_str);
        //****** 終了時　値セット
        list_end_time_view.setText(End_time_str);

        /**
         *  スピナー　アダプター　セット
         */
        //****** 01
        GET_Spinner_A();

        spinner_item_A.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_A
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_001.setAdapter(adapter);

        //****** 02
        GET_Spinner_B();

        spinner_item_B.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_02 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_B
        );
        adapter_02.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_002.setAdapter(adapter_02);

        //****** 03
        GET_Spinner_C();

        spinner_item_C.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_03 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_C
        );
        adapter_03.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_003.setAdapter(adapter_03);

        //****** 04
        GET_Spinner_D();

        spinner_item_D.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_04 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_D
        );
        adapter_04.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_004.setAdapter(adapter_04);

        int get_spi_index = 0;
        if(spinner_item_A.contains(d_list_name_str)) {
            get_spi_index = spinner_item_A.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_001.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_001.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_001.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_002.setSelection(0);
            list_spinner_003.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if(spinner_item_B.contains(d_list_name_str)) {
            get_spi_index = spinner_item_B.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_002.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_002.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_002.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_001.setSelection(0);
            list_spinner_003.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if (spinner_item_C.contains(d_list_name_str)) {
            get_spi_index = spinner_item_C.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_003.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_003.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_003.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);


            list_spinner_001.setSelection(0);
            list_spinner_002.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if (spinner_item_D.contains(d_list_name_str)) {
            get_spi_index = spinner_item_D.indexOf(d_list_name_str);

            list_spinner_004.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_004.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_004.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_001.setSelection(0);
            list_spinner_002.setSelection(0);
            list_spinner_003.setSelection(0);
        }

    } //***************  END set_num_09


    /**
     *  塗装課　値セット
     */
    private void set_num_09_Tosou() {

        /**
         *  SELECT 値セット
         */

        // 作業番号  dd-0 + id
 //       d_list_id.setText(d_list_id_str);
        d_list_id.setText(G_id);
        // 商品名
        d_list_name.setText(d_list_name_str);

        //----------- 段取時間
        if(d_list_dandori_time_num_str.length() == 0 || d_list_dandori_time_num_str.equals("")) {
            d_list_dandori_time_num_str = "0";

            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }  else {
            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }

        //----------- 作業時間
        d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);

        //****** 開始時　値セット
        list_start_time_view.setText(Start_time_str);
        //****** 終了時　値セット
        list_end_time_view.setText(End_time_str);

        /**
         *  スピナー　アダプター　セット
         */

        //************************* 塗装課用　アップデート
        GET_Spinner_Tosou();

        spinner_item_Tosou.add(0, "（塗装課用）リスト");

        ArrayAdapter<String> tosou_adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_Tosou
        );
        tosou_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_Tosou.setAdapter(tosou_adapter);

        //****** 01
        GET_Spinner_A();

        spinner_item_A.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_A
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_001.setAdapter(adapter);

        //****** 02
        GET_Spinner_B();

        spinner_item_B.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_02 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_B
        );
        adapter_02.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_002.setAdapter(adapter_02);

        //****** 03
        GET_Spinner_C();

        spinner_item_C.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_03 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_C
        );
        adapter_03.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_003.setAdapter(adapter_03);

        //****** 04
        GET_Spinner_D();

        spinner_item_D.add(0,"変更する項目を選択してください。");

        ArrayAdapter<String> adapter_04 = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinner_item_D
        );
        adapter_04.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        list_spinner_004.setAdapter(adapter_04);

        int get_spi_index = 0;
        if(spinner_item_A.contains(d_list_name_str)) {
            get_spi_index = spinner_item_A.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_001.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_001.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_001.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_002.setSelection(0);
            list_spinner_003.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if(spinner_item_B.contains(d_list_name_str)) {
            get_spi_index = spinner_item_B.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_002.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_002.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_002.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_001.setSelection(0);
            list_spinner_003.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if (spinner_item_C.contains(d_list_name_str)) {
            get_spi_index = spinner_item_C.indexOf(d_list_name_str);

            // スピナーにセット
            list_spinner_003.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_003.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_003.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);


            list_spinner_001.setSelection(0);
            list_spinner_002.setSelection(0);
            list_spinner_004.setSelection(0);

        } else if (spinner_item_D.contains(d_list_name_str)) {
            get_spi_index = spinner_item_D.indexOf(d_list_name_str);

            list_spinner_004.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_004.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_004.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_001.setSelection(0);
            list_spinner_002.setSelection(0);
            list_spinner_003.setSelection(0);

        } else if (spinner_item_Tosou.contains(d_list_name_str)) {

            get_spi_index = spinner_item_Tosou.indexOf(d_list_name_str);

            list_spinner_Tosou.setSelection(get_spi_index);

            //****************** スピナー　下の　テキストビューに　値セット
            // 選択されているアイテムのIndexを取得
            int idx = list_spinner_Tosou.getSelectedItemPosition();
            // 選択されているアイテムを取得
            String item = (String)list_spinner_Tosou.getSelectedItem();

            //***** TextView に　値をセット
            list_get_spiiner_text_01.setText(item);

            list_spinner_001.setSelection(0);
            list_spinner_002.setSelection(0);
            list_spinner_003.setSelection(0);
            list_spinner_004.setSelection(0);

        }

    } //***************  END set_num_09

    private void set_num_0000() {

        /**
         *  SELECT 値セット
         */

        // 作業番号  dd-0 + id
   //     d_list_id.setText(d_list_id_str);
        d_list_id.setText(G_id);
        // 商品名
        d_list_name.setText(d_list_name_str);

        //----------- 段取時間
        if(d_list_dandori_time_num_str.length() == 0 || d_list_dandori_time_num_str.equals("")) {
            d_list_dandori_time_num_str = "0";

            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }  else {
            d_list_dandori_time_num.setText(d_list_dandori_time_num_str);
        }

        //----------- 作業時間
        d_list_sagyou_time_num.setText(d_list_sagyou_time_num_str);

        //****** 開始時　値セット
        list_start_time_view.setText(Start_time_str);
        //****** 終了時　値セット
        list_end_time_view.setText(End_time_str);

    }




    /**
     *  エディットテキスト　パース
     */
    private void Edit_Parse_01() {

        // 開始時
        if(input_starttime_list_d.getText().toString().equals("") == false) {
            d_list_start_num_i = Integer.parseInt(d_list_start_num);
        } else {
            input_starttime_list_d.setText(d_list_start_num);
        }

        String zeto_str = "0";
        // 終了時
        if(Input_endtime_list_d.getText().toString().equals("") == false) {
            d_list_end_num_i = Integer.parseInt(zeto_str);
        } else {
            Input_endtime_list_d.setText("0");
        }

        // 総生産
        if(d_list_souseisan_num.getText().toString().equals("") == false) {

            d_list_souseisan_num_str_i = Integer.parseInt(d_list_souseisan_num_str);
        } else {
            d_list_souseisan_num.setText(d_list_souseisan_num_str);
        }

        //

    } //=========================== end function


     /**
     *    //**************** 樹脂成型課　用
     */
    private void Allahto_Dailog_Zyushi_Seikei_A () {

        //******************** オリジナルアラートログの表示 処理 開始  ********************//
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View bilde_layout_05 = inflater.inflate(R.layout.dialog_05, (ViewGroup) findViewById(R.id.alertdialog_layout_05));

        //*********** コンポーネント　初期化
        iro_input_a = bilde_layout_05.findViewById(R.id.iro_input_a); // 色段取時間
        kata_input_a = bilde_layout_05.findViewById(R.id.kata_input_a); // 型段取時間
        kikai_input_a = bilde_layout_05.findViewById(R.id.kikai_input_a); // 機械コード

        iro_input_a.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字
        kata_input_a.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字
        kikai_input_a.setInputType(InputType.TYPE_CLASS_NUMBER); // 数字

        dia_5_touroku_btn_001 = bilde_layout_05.findViewById(R.id.dia_5_touroku_btn_001); // 保存　ボタン
        dia_5_touroku_btn_002 = bilde_layout_05.findViewById(R.id.dia_5_touroku_btn_002); // 保存　ボタン

        //--------------- アラートダイヤログ タイトル　設定 ---------------//
        android.app.AlertDialog.Builder bilder = new android.app.AlertDialog.Builder(List_Details.this);
        // タイトル
        TextView titleView;
        titleView = new TextView(List_Details.this);
        titleView.setText("樹脂成型課 用 入力（更新）");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.colorPinku));
        titleView.setPadding(20, 30, 20, 30);
        titleView.setGravity(Gravity.CENTER);

        // ダイアログに　「タイトル」　を　セット
        bilder.setCustomTitle(titleView);

        // カスタムレイアウト　を　セット
        bilder.setView(bilde_layout_05);

        android.app.AlertDialog dialog = bilder.create();
        dialog.show();

        /**
         *  「色段取時間」
         */
        iro_input_a.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    //***** エラー　空処理
                    if(iro_input_a.getText().toString().equals("") == false) {
                        tmp_iro_input_a = iro_input_a.getText().toString();
                    }

                }

                return false;
            }
        });


        /**
         *  「型段取時間」
         */
        kata_input_a.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    tmp_kata_input_a = kata_input_a.getText().toString();
                }

                return false;
            }
        });

        /**
         *  「機械コード」
         */
        kikai_input_a.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //********* ソフトキーボード　エンターが押されたら
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    tmp_kikai_input_a = kikai_input_a.getText().toString();
                }

                return false;
            }
        });

        /**
         *   「保存」　ボタン
         */
        dia_5_touroku_btn_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tmp_iro_input_a = iro_input_a.getText().toString(); // 色段取時間
                tmp_kata_input_a = kata_input_a.getText().toString(); // 型段取時間
                tmp_kikai_input_a = kikai_input_a.getText().toString(); // 機械コード

                //*** 色段取時間セット
                if(tmp_iro_input_a.length() != 0) {
                    iro_z_edit_a.setText(tmp_iro_input_a);
                } else {
                    iro_z_edit_a.setText("0");
                }

                //*** 型段取時間セット
                if(tmp_kata_input_a.length() != 0) {
                    Kata_z_edit_a.setText(tmp_kata_input_a);
                } else {
                    Kata_z_edit_a.setText("0");
                }

                //*** 機械コードセット
                if(tmp_kikai_input_a.length() != 0) {
                    Kikai_z_input_a.setText(tmp_kikai_input_a);
                } else {
                    Kikai_z_input_a.setText("0");
                }

                //******* ダイアログ　非表示
                dialog.dismiss();

            }
        });

        /**
         *   「キャンセル」　ボタン
         */
        dia_5_touroku_btn_002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //************* キャンセル
                dialog.dismiss();

            }
        });



    }

    /**
     *  品目区分　が　9 だったら　「開始時」　「終了時」　「不良品数」　エディットテキスト　入力不可
     */


    /**
     *  グラフ　に挿入する　「総生産数」　取得
     */
    private void GET_Souseisan() {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_Souseisan_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        String get_souseisan = "";

        try {

            // ***** G_id    翌日対応  id
            String [] arr_list = {G_id};

            // データ取得　格納用　配列

            cursor = db_Souseisan_01.rawQuery("select * from Send_Grafu_Table where Send_id = ?", arr_list);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    //------------ 総生産数　を　格納する。

                    //--- id 取得
                    int idx = cursor.getColumnIndex("id");
                    int Send_Grafu_id = cursor.getInt(idx);

                    //--- Send_id 取得
                    idx = cursor.getColumnIndex("Send_id");
                    Send_Grafu_Send_id = cursor.getString(idx);

                    //--- Sou_Num_01
                    idx = cursor.getColumnIndex("Sou_Num_01"); // 8
                 //   Sou_Num_01_i = cursor.getInt(idx);
                    S_Gurafu_Num[0] = cursor.getInt(idx);

                    //--- Sou_Num_02
                    idx = cursor.getColumnIndex("Sou_Num_02"); // 9
                    S_Gurafu_Num[1] = cursor.getInt(idx);

                    //--- Sou_Num_03
                    idx = cursor.getColumnIndex("Sou_Num_03"); // 10
                    S_Gurafu_Num[2] = cursor.getInt(idx);

                    //--- Sou_Num_04
                    idx = cursor.getColumnIndex("Sou_Num_04"); // 11
                    S_Gurafu_Num[3] = cursor.getInt(idx);

                    //--- Sou_Num_05
                    idx = cursor.getColumnIndex("Sou_Num_05"); // 12
                    S_Gurafu_Num[4] = cursor.getInt(idx);

                    //--- Sou_Num_06
                    idx = cursor.getColumnIndex("Sou_Num_06"); // 13
                    S_Gurafu_Num[5] = cursor.getInt(idx);

                    //--- Sou_Num_07
                    idx = cursor.getColumnIndex("Sou_Num_07"); // 14
                    S_Gurafu_Num[6] = cursor.getInt(idx);

                    //--- Sou_Num_08
                    idx = cursor.getColumnIndex("Sou_Num_08"); // 15
                    S_Gurafu_Num[7] = cursor.getInt(idx);

                    //--- Sou_Num_09
                    idx = cursor.getColumnIndex("Sou_Num_09"); // 16
                    S_Gurafu_Num[8] = cursor.getInt(idx);

                    //--- Sou_Num_10
                    idx = cursor.getColumnIndex("Sou_Num_10"); // 17
                    S_Gurafu_Num[9] = cursor.getInt(idx);

                    //--- Sou_Num_11
                    idx = cursor.getColumnIndex("Sou_Num_11"); // 18
                    int SS_gurafu_last_num = cursor.getInt(idx);

                    System.out.println("SELECT テスト::::db_Souseisan_01" + Send_Grafu_id + ":" + Send_Grafu_Send_id + ":" + S_Gurafu_Num[0] +
                            S_Gurafu_Num[1] +  S_Gurafu_Num[2] +  S_Gurafu_Num[3] +  S_Gurafu_Num[4] +  S_Gurafu_Num[5] +
                            S_Gurafu_Num[6] +  S_Gurafu_Num[7] +  S_Gurafu_Num[8] +  S_Gurafu_Num[9]);
                }


            } else {
                return;

            } //-------------- END if


        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if(db_Souseisan_01 != null) {
                db_Souseisan_01.close();
            }
        }

    }


    /***
     *  グラフの値　Update 用
     */
    private void Update_Grafu_01(String colmun, int G_num, int index) {

        DBAdapter.DBHelper helper = new DBAdapter.DBHelper(getApplicationContext());
        SQLiteDatabase db_up_Grafu_01 = helper.getWritableDatabase();

        // トランザクション　開始
        db_up_Grafu_01.beginTransaction();

        try {

            // 総生産数 取得　d_list_souseisan_num => TextView
            /*
            String tmp_souseisan_num = d_list_souseisan_num.getText().toString();
            int tmp_souseisan_num_i = Integer.parseInt(tmp_souseisan_num);

             */

            int tmp_i = S_Gurafu_Num[index];

            ContentValues values = new ContentValues();

            int G_sum = G_num + tmp_i;

            values.put(colmun, G_sum);

            db_up_Grafu_01.update("Send_Grafu_Table", values, "Send_id = ?", new String[]{Send_Grafu_Send_id});

            System.out.println("アップデート処理::::対象カラム" + colmun + ":::値:::" +  update_souseisan_t_i);

            // トランザクション　成功
            db_up_Grafu_01.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            db_up_Grafu_01.endTransaction();

            if(db_up_Grafu_01 != null) {
                db_up_Grafu_01.close();
            }
        }

    }

    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム A 取得
     */
    private void GET_Spinner_A() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;
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

    } //------------ GET_Spinner_A END ---------------->


    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム B 取得
     */
    private void GET_Spinner_B() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db_02 = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;
        //------------- スピナー　アイテム取得
        try {

            Cursor cursor = db_02.rawQuery("select * from NP_data_table" +
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
            if (db_02 != null) {
                db_02.close();
            }
        }

    } //------------ GET_Spinner_B END ---------------->


    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム C 取得
     */
    private void GET_Spinner_C() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

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

    } //------------ GET_Spinner_C END ---------------->


    /**
     *  スピナー用 NP_data_table ＊＊＊ アイテム C 取得
     */

    private void GET_Spinner_D() {

        TestOpenHelper helper_sppiner = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper_sppiner.getReadableDatabase();

        String[] arr_item = new String[2];

        int num = 0;
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

    } //------------ GET_Spinner_C END ---------------->

    private void Edit_1_3_control(EditText e1, EditText e2, EditText e3, EditText e4) {

        e1.setFocusable(true);
        e1.setFocusableInTouchMode(true);
        e1.setEnabled(true);

        e2.setFocusable(false);
        e2.setFocusableInTouchMode(false);
        e2.setEnabled(false);

        e3.setFocusable(false);
        e3.setFocusableInTouchMode(false);
        e3.setEnabled(false);

        e4.setFocusable(false);
        e4.setFocusableInTouchMode(false);
        e4.setEnabled(false);

    } //============ END function

    private void Edit_4_control_ON(EditText e1, EditText e2, EditText e3, EditText e4) {

        e1.setFocusable(true);
        e1.setFocusableInTouchMode(true);
        e1.setEnabled(true);

        e2.setFocusable(true);
        e2.setFocusableInTouchMode(true);
        e2.setEnabled(true);

        e3.setFocusable(true);
        e3.setFocusableInTouchMode(true);
        e3.setEnabled(true);

        e4.setFocusable(true);
        e4.setFocusableInTouchMode(true);
        e4.setEnabled(true);


    } //============ END function

    /***
     *  エディットテキスト　フォーカスを　画面外へ　移動　して　ソフトキーボードを消す
     */

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mFocusView.requestFocus();
        return super.onTouchEvent(event);
    }

     */

    /***
     *  エディットテキスト　フォーカスを　画面外へ　移動　して　ソフトキーボードを消す
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        focusView_02.requestFocus();
        return super.dispatchTouchEvent(ev);
    }

    /**
     *   「塗装課」　スピナー　取得
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




}