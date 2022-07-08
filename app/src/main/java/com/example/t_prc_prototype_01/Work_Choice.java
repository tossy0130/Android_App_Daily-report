package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;

import kotlin.jvm.internal.PropertyReference0Impl;

public class Work_Choice extends AppCompatActivity {

    //------------ Log Tag コード
    private String TAG = "Work_Choice";

    //------------ バーコード　読み取り
    private Button work_C_btn_01;
    private IntentIntegrator integrator;

    //------------ 画面遷移 ボタン
    private Button work_C_btn_02;
    private Button work_C_btn_03;
    private Button send_csv_01_btn;

    //------------ XML コンポーネント
    private TextView ch_user_name;
    private TextView ch_busyo_view;
    private Spinner ch_spinner_busyo;
    private TextView ch_busyo_text_box;
    private TextView ch_busyo_view_Hon;

    //------------ Home.java から　値を受け取る
    private String get_TMNF_01,get_TMNF_02,dia_edit_01_num,dia_user_key;
    private String get_TMNF_01_result,get_TMNF_02_result,dia_edit_01_num_result,dia_user_key_result,get_BUMF_01;
    private int i_dia_chack_01_num, i_dia_chack_01_num_result,get_result_num;

    private String Busho_Code;

    private TextView ch_busyo_label_02;
    private ImageButton ch_busyo_label_03, ch_back_btn_y;

    private Button send_log_btn;

    private Button worker_test_btn;

    //----------------- 大本部署コード　、　作業用部署コード　取得用
    private String dia_user_key_str,get_TMNF_03_str;

    private String SAGYOU_Busyo_Code_SELECT_01_str;

    //----------------- 終日フラグ　SELECT 結果格納
    private int Syuuzitu_Flg;

    //----------------
    private String Sagyou_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work__choice);

        // コンポーネント　初期化
        init();

        /***
         *   終日フラグ　値取得
         */
        Syuuzitu_Flg_SELECT();

        /**
         * SAGYOU_Busyo_Code_SELECT_01 作業部署　SELECT
         */
        SAGYOU_Busyo_Code_SELECT_01();

        System.out.println("SAGYOU_Busyo_Code_SELECT_01_str::::" + SAGYOU_Busyo_Code_SELECT_01_str);

        ch_spinner_busyo.setVisibility(View.GONE);


        //---------------------
        // ***********************  Home.java から　値を受け取る
        if(getIntent() != null) {
            // 担当者コード
            get_TMNF_01 = getIntent().getStringExtra("get_TMNF_01");
            // 担当名
            get_TMNF_02 = getIntent().getStringExtra("get_TMNF_02");
            // 作業場所名
            dia_edit_01_num = getIntent().getStringExtra("dia_edit_01_num");

            get_BUMF_01 = getIntent().getStringExtra("get_BUMF_01");

            /*** 部署コード　取得
             *    get_TMNF_03_str => 大本の部署コード , dia_user_key_str => エディットテキストで選択した部署コード
             */
            //****** 元々の 所属部署コード *******
            get_TMNF_03_str = getIntent().getStringExtra("get_TMNF_03");
            //****** エディットテキストで　選択した　作業部署コード

            dia_user_key_str = getIntent().getStringExtra("dia_user_key");
            //****** 元々の 所属部署コード *******　END
            // 終日チェック  0: チェックなし,  1: チェックあり
            i_dia_chack_01_num = getIntent().getIntExtra("i_dia_chack_01_num", i_dia_chack_01_num);

            // ２回目　以降　で受け取るデータ
            i_dia_chack_01_num_result = getIntent().getIntExtra("i_dia_chack_01_num_result", 1);
            get_result_num = getIntent().getIntExtra("get_result_num", get_result_num);

            // ダイアログ　確認作業コード
            dia_user_key = getIntent().getStringExtra("dia_user_key");

            //------ 大本の　所属部署名　取得
            Busho_Code = getIntent().getStringExtra("Busho_Code");

            System.out.println("判別値：：：" + i_dia_chack_01_num + ":::" + get_result_num);


        }

        /***
         *  終日フラグで　判別  0 = 終日チェックなし  ,  1 = 終日チェックあり
         */
        if (Syuuzitu_Flg == 1) {

            // 担当者名　セット
            if(get_TMNF_02 != null) {
                ch_user_name.setText(get_TMNF_02);
            } else {
                ch_user_name.setText(get_TMNF_02_result);
            }

            // 部署名セット（大本）
            ch_busyo_view_Hon.setText(get_BUMF_01);

            // 部署名セット
            if(dia_edit_01_num != null) {

            } else {
                ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);
            }

            //  作業部署　値セット
            ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);

            // 終日チェックあり　テキスト
            ch_busyo_text_box.setText("終日チェック あり");

            // 終日チェック　なし　アイコン 非表示1
            ch_busyo_label_02.setVisibility(View.GONE);
            // 表示
            ch_busyo_label_03.setVisibility(View.VISIBLE);
        } else {

            // 担当者名　セット
            if(get_TMNF_02 != null) {
                ch_user_name.setText(get_TMNF_02);
            } else {
                ch_user_name.setText(get_TMNF_02_result);
            }

            // 部署名セット（大本）
            ch_busyo_view_Hon.setText(get_BUMF_01);

            // 部署名セット
            if(dia_edit_01_num != null) {

            } else {
                ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);
            }

            //  作業部署　値セット
            ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);

            ch_busyo_text_box.setText("終日チェック なし");
            ch_spinner_busyo.setVisibility(View.VISIBLE);
            i_dia_chack_01_num = 0;

            // 終日チェック　なし　アイコン 表示
            ch_busyo_label_02.setVisibility(View.VISIBLE);
            // 非表示
            ch_busyo_label_03.setVisibility(View.GONE);
        }

        /**
         *  作業部署　コード　取得
         */
        Syuuzitu_Flg_SELECT_02();

        /**
         *  ➡　戻る　ボタン
         */
        ch_back_btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //************* ➡　戻る　ボタン

                  /*
        if(ch_busyo_view.getText().toString().equals("") == false) {
            dia_edit_01_num = ch_busyo_view.getText().toString();
        }
         */

                /**
                 *  最終チェック　が 「チェックなし」　時の　データの受け渡し
                 */
                if(i_dia_chack_01_num_result == 0 || get_result_num == 0) {

                    Intent intent = new Intent();
                    intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                    intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                    intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                    intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                    // 最終チェック　判定 0: チェックなし 1: チェックあり
                    intent.putExtra("i_dia_chack_01_num_result", i_dia_chack_01_num);

                    //********************* 値　返却 *******************
                    setResult(RESULT_OK, intent);
                    finish();

                } else if(i_dia_chack_01_num_result == 1 || get_result_num == 1) {

                    /**
                     *  最終チェック　が　「チェックあり」　時の　データの受け渡し
                     */
                    Intent intent = new Intent();
                    intent.putExtra("get_TMNF_01_result", get_TMNF_01_result);

                    intent.putExtra("get_TMNF_02_result", get_TMNF_02_result);

                    intent.putExtra("dia_edit_01_num_result",dia_edit_01_num_result);

                    intent.putExtra("dia_user_key_result", dia_user_key_result);

                    //----------
                    intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                    intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                    intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                    intent.putExtra("dia_user_key",dia_user_key); // 作業コード


                    // 最終チェック　判定 0: チェックなし 1: チェックあり
                    intent.putExtra("i_dia_chack_01_num_result", i_dia_chack_01_num);

                    //********************* 値　返却 *******************
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });


        /**
         *  QR_Barcode_Read.java へ　遷移
         */
        work_C_btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplication(), QR_Barcode_Read.class);

                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                // 終日チェック  0: チェックなし,  1: チェックあり
                intent.putExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                /**
                 * 　確実データ
                 */
                //------ 担当者名 TextView から　取得
                String ch_user_name_num = ch_user_name.getText().toString();
                intent.putExtra("ch_user_name_num", ch_user_name_num);

                //------ 部署名 TextView から　取得　（大本部署）
                String ch_busyo_view_Hon_num = ch_busyo_view_Hon.getText().toString();
                intent.putExtra("ch_busyo_view_Hon_num",ch_busyo_view_Hon_num);

                //------ 作業部署名 TextView から　取得
                String ch_busyo_view_num = ch_busyo_view.getText().toString();
                intent.putExtra("ch_busyo_view_num",ch_busyo_view_num);

                //------ 作業部署コード dia_user_key
                intent.putExtra("dia_user_key" , dia_user_key);

                /***
                 * 部署コード　　get_TMNF_03_str => 大本の部署コード、  dia_user_key_str => エディットテキストで選択した作業用　部署コード
                 */
                intent.putExtra("get_TMNF_03_str_GO",get_TMNF_03_str);
                intent.putExtra("dia_user_key_str_GO", dia_user_key_str);

                intent.putExtra("Sagyou_B", Sagyou_B);

                startActivity(intent);

                finish();

            }
        });

        //------------------------- スピナー　選択画面へ　遷移
        work_C_btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // スピナー　選択画面へ　画面遷移
                Intent intent = new Intent(getApplication(), Operation_Input.class);


                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                // 終日チェック  0: チェックなし,  1: チェックあり
                intent.putExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                /**
                 * 　確実データ
                 */
                //------ 担当者名 TextView から　取得
                String ch_user_name_num = ch_user_name.getText().toString();
                intent.putExtra("ch_user_name_num", ch_user_name_num);

                //------ 部署名 TextView から　取得　（大本部署）
                String ch_busyo_view_Hon_num = ch_busyo_view_Hon.getText().toString();
                intent.putExtra("ch_busyo_view_Hon_num",ch_busyo_view_Hon_num);

                //------ 作業部署名 TextView から　取得
                String ch_busyo_view_num = ch_busyo_view.getText().toString();
                intent.putExtra("ch_busyo_view_num",ch_busyo_view_num);

                //------ 作業部署コード dia_user_key
                intent.putExtra("dia_user_key" , dia_user_key);

                /***
                 * 部署コード　　get_TMNF_03_str => 大本の部署コード、  dia_user_key_str => エディットテキストで選択した作業用　部署コード
                 */
                intent.putExtra("get_TMNF_03_str_GO",get_TMNF_03_str);
                intent.putExtra("dia_user_key_str_GO", dia_user_key_str);

                /**
                 *  作業部署　コード
                 */
                intent.putExtra("SAGYOU_Busyo_Code_SELECT_01_str", SAGYOU_Busyo_Code_SELECT_01_str);

                intent.putExtra("Sagyou_B", Sagyou_B);

                startActivity(intent);

                finish();

            }
        });

        work_C_btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Free_Input.class);


                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                // 終日チェック  0: チェックなし,  1: チェックあり
                intent.putExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                /**
                 * 　確実データ
                 */
                //------ 担当者名 TextView から　取得
                String ch_user_name_num = ch_user_name.getText().toString();
                intent.putExtra("ch_user_name_num", ch_user_name_num);

                //------ 部署名 TextView から　取得　（大本部署）
                String ch_busyo_view_Hon_num = ch_busyo_view_Hon.getText().toString();
                intent.putExtra("ch_busyo_view_Hon_num",ch_busyo_view_Hon_num);

                //------ 作業部署名 TextView から　取得
                String ch_busyo_view_num = ch_busyo_view.getText().toString();
                intent.putExtra("ch_busyo_view_num",ch_busyo_view_num);

                //------ 作業部署コード dia_user_key
                intent.putExtra("dia_user_key" , dia_user_key);

                /***
                 * 部署コード　　get_TMNF_03_str => 大本の部署コード、  dia_user_key_str => エディットテキストで選択した作業用　部署コード
                 */
                intent.putExtra("get_TMNF_03_str_GO",get_TMNF_03_str);
                intent.putExtra("dia_user_key_str_GO", dia_user_key_str);

                /**
                 *  作業部署　コード
                 */
                intent.putExtra("SAGYOU_Busyo_Code_SELECT_01_str", SAGYOU_Busyo_Code_SELECT_01_str);

                intent.putExtra("Sagyou_B", Sagyou_B);

                startActivity(intent);

                finish();
            }
        });


        /***
         *     //----------- CSV 送信画面へ　遷移
         */
        send_csv_01_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Send_CSV_01.class);

                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                // 終日チェック  0: チェックなし,  1: チェックあり
                intent.putExtra("i_dia_chack_01_num", i_dia_chack_01_num);

                /**
                 * 　確実データ
                 */
                //------ 担当者名 TextView から　取得
                String ch_user_name_num = ch_user_name.getText().toString();
                intent.putExtra("ch_user_name_num", ch_user_name_num);

                //------ 部署名 TextView から　取得　（大本部署）
                String ch_busyo_view_Hon_num = ch_busyo_view_Hon.getText().toString();
                intent.putExtra("ch_busyo_view_Hon_num",ch_busyo_view_Hon_num);

                //------ 作業部署名 TextView から　取得
                String ch_busyo_view_num = ch_busyo_view.getText().toString();
                intent.putExtra("ch_busyo_view_num",ch_busyo_view_num);

                //------ 作業部署コード dia_user_key
                intent.putExtra("dia_user_key" , dia_user_key);

                /**
                 *  作業部署コード　取得
                 */
                intent.putExtra("Sagyou_B", Sagyou_B);

                startActivity(intent);

                finish();
            }
        });

        /***
         *  送信ログ　画面　へ　遷移
         */
        send_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), File_Send_log.class);
                startActivity(intent);

            }
        });

        /**
         *  worker　テスト画面
         */

        worker_test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Worker_TEST_01.class);

                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("Sagyou_B", Sagyou_B); // 作業者コード

                startActivity(intent);

                finish();

            }
        });


        /**
         *   終日　フラグ　変更ボタン ON => OFF に　する
         */
        ch_busyo_label_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClick_Syuuzitu_Dairogu_01();

            }
        });

    } //------------------------------------------------------------------------------- END create

    /**
     *  メニュー　追加
      */
    //----------------- メニュー　追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.work_choice_menu, menu);

        return true;
    }

    //----------------- メニューボタンが押された時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {

            case R.id.menu_work_c_btn_01 :

                Intent intent = new Intent(getApplicationContext(), WebViewActivity_05.class);
                startActivity(intent);

                break;

        }

            return true;
    }



    /**
     *  終日チェック　ダイアログ
     */
    public void onClick_Syuuzitu_Dairogu_01() {

        //******************* アラート　表示 *******************
        //-------　タイトル
        TextView titleView;
        // アクティビティ名を入れる
        titleView = new TextView(Work_Choice.this);
        titleView.setText("終日チェックを解除確認");
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.menu_color));
        titleView.setPadding(20,20,20,20);
        titleView.setGravity(Gravity.CENTER);

        //-------- アラートログの表示 開始 ----------
        AlertDialog.Builder bilder = new AlertDialog.Builder(Work_Choice.this);

        /**
         *  ダイアログの項目セット
         */
        //------- タイトルセット
        bilder.setCustomTitle(titleView);
        //------- メッセージセット
        bilder.setMessage("「終日チェック」を解除しますか？");

        bilder.setPositiveButton("解除しない", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;
            }
        });

        bilder.setNegativeButton("解除する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //******* 終日チェックを　解除する ********
                onClickSyuuZitu_OFF();

                return;
            }
        });

        AlertDialog dialog = bilder.show();
        dialog.show();

        //******************************************* ボタン　配色　変更
        //********* ボタン はい **********
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //********* ボタン いいえ **********
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF4081"));
        //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red));

        //******************************************* END ボタン　配色　変更


    }  //****************** END onClick_Syuuzitu_Dairogu_01


    /**
     *   終日　チェック　タップ処理
     */
    private void onClickSyuuZitu_OFF() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Syuu_S_db_OFF = helper.getWritableDatabase();

        // トランザクション開始
        Syuu_S_db_OFF.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            /**
             *  終日フラグを　「０」　にする   ＊＊＊　終日解除 ＊＊＊
             */
            values.put("Syuuzitu_Flg",0);
            //******* アップデート処理 **********
            Syuu_S_db_OFF.update("Flg_Table", values, null,null);

            // トランザクション　完了
            Syuu_S_db_OFF.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // トランザクション　終了
            Syuu_S_db_OFF.endTransaction();

            if(Syuu_S_db_OFF != null) {
                Syuu_S_db_OFF.close();
            }
        }


        /**
         *  終日フラグ　取得
         */
        Syuuzitu_Flg_SELECT();


        /***
         *  終日フラグで　判別  0 = 終日チェックなし  ,  1 = 終日チェックあり
         */
        if (Syuuzitu_Flg == 1) {

            // 担当者名　セット
            if(get_TMNF_02 != null) {
                ch_user_name.setText(get_TMNF_02);
            } else {
                ch_user_name.setText(get_TMNF_02_result);
            }

            // 部署名セット（大本）
            ch_busyo_view_Hon.setText(get_BUMF_01);

            // 部署名セット
            if(dia_edit_01_num != null) {

            } else {
                ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);
            }

            //  作業部署　値セット
            ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);

            // 終日チェックあり　テキスト
            ch_busyo_text_box.setText("終日チェック あり");

            // 終日チェック　なし　アイコン 非表示1
            ch_busyo_label_02.setVisibility(View.GONE);
            // 表示
            ch_busyo_label_03.setVisibility(View.VISIBLE);
        } else {

            // 担当者名　セット
            if(get_TMNF_02 != null) {
                ch_user_name.setText(get_TMNF_02);
            } else {
                ch_user_name.setText(get_TMNF_02_result);
            }

            // 部署名セット（大本）
            ch_busyo_view_Hon.setText(get_BUMF_01);

            // 部署名セット
            if(dia_edit_01_num != null) {

            } else {
                ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);
            }

            //  作業部署　値セット
            ch_busyo_view.setText(SAGYOU_Busyo_Code_SELECT_01_str);

            ch_busyo_text_box.setText("終日チェック なし");
            ch_spinner_busyo.setVisibility(View.VISIBLE);
            i_dia_chack_01_num = 0;

            // 終日チェック　なし　アイコン 表示
            ch_busyo_label_02.setVisibility(View.VISIBLE);
            // 非表示
            ch_busyo_label_03.setVisibility(View.GONE);
        }



    } //********************* END onClickSyuuZitu_OFF

    /**
     *  Back　ボタンが押されたら
     */
    @Override
    public void onBackPressed() {

        /*
        if(ch_busyo_view.getText().toString().equals("") == false) {
            dia_edit_01_num = ch_busyo_view.getText().toString();
        }
         */

        /**
         *  最終チェック　が 「チェックなし」　時の　データの受け渡し
         */
            if(i_dia_chack_01_num_result == 0 || get_result_num == 0) {

                Intent intent = new Intent();
                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード

                // 最終チェック　判定 0: チェックなし 1: チェックあり
                intent.putExtra("i_dia_chack_01_num_result", i_dia_chack_01_num);

                //********************* 値　返却 *******************
                setResult(RESULT_OK, intent);
                finish();

            } else if(i_dia_chack_01_num_result == 1 || get_result_num == 1) {

                /**
                 *  最終チェック　が　「チェックあり」　時の　データの受け渡し
                 */
                Intent intent = new Intent();
                intent.putExtra("get_TMNF_01_result", get_TMNF_01_result);

                intent.putExtra("get_TMNF_02_result", get_TMNF_02_result);

                intent.putExtra("dia_edit_01_num_result",dia_edit_01_num_result);

                intent.putExtra("dia_user_key_result", dia_user_key_result);

                //----------
                intent.putExtra("get_TMNF_01",get_TMNF_01); // 担当者コード
                intent.putExtra("get_TMNF_02",get_TMNF_02); // 担当者名

                intent.putExtra("dia_edit_01_num",dia_edit_01_num); // 作業部署名
                intent.putExtra("dia_user_key",dia_user_key); // 作業コード


                // 最終チェック　判定 0: チェックなし 1: チェックあり
                intent.putExtra("i_dia_chack_01_num_result", i_dia_chack_01_num);

                //********************* 値　返却 *******************
                setResult(RESULT_OK, intent);
                finish();
            }


    }

    private void init() {

        // QR バーコード　読み取りカメラ　起動ボタン
        work_C_btn_01 = findViewById(R.id.work_C_btn_01);

        // スピナー選択画面　btn
        work_C_btn_02 = findViewById(R.id.work_C_btn_02);
        work_C_btn_03 = findViewById(R.id.work_C_btn_03);

        send_csv_01_btn = findViewById(R.id.send_csv_01_btn);

        worker_test_btn = (Button) findViewById(R.id.worker_test_btn);

        ch_back_btn_y = (ImageButton) findViewById(R.id.ch_back_btn_y);

        //---------- コンポーネント
        ch_user_name = (TextView) findViewById(R.id.ch_user_name);
        ch_busyo_view = (TextView) findViewById(R.id.ch_busyo_view);
        ch_spinner_busyo = (Spinner) findViewById(R.id.ch_spinner_busyo);
        ch_busyo_text_box = (TextView) findViewById(R.id.ch_busyo_text_box);
        ch_busyo_view_Hon = (TextView) findViewById(R.id.ch_busyo_view_Hon);

        // 終日チェックアイコン
        ch_busyo_label_02 = (TextView) findViewById(R.id.ch_busyo_label_02);
        ch_busyo_label_03 = (ImageButton) findViewById(R.id.ch_busyo_label_03);

        // 送信ログ　遷移ボタン
        send_log_btn = (Button) findViewById(R.id.send_log_btn);
    }

    /**
     *   作業部署　SELECT
     */
    private void SAGYOU_Busyo_Code_SELECT_01() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Sagyou_Busyo_Code_S_01 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Sagyou_Busyo_Code_S_01.rawQuery("select BUMF_table.BUMF_c_02 from Flg_Table " +
                    "inner join BUMF_table on " +
                    "Flg_Table.Sagyou_busyo_code = BUMF_table.BUMF_c_01;", null);

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {

                    SAGYOU_Busyo_Code_SELECT_01_str = cursor.getString(0);

                    System.out.println("SAGYOU_Busyo_Code_SELECT_01_str:::: 値" + SAGYOU_Busyo_Code_SELECT_01_str);

                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(Sagyou_Busyo_Code_S_01 != null) {
                Sagyou_Busyo_Code_S_01.close();
            }
        }

    } // ****************************** END SAGYOU_Busyo_Code_SELECT_01

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

    /**
     *  作業部署　コード　取得
     */
    private void Syuuzitu_Flg_SELECT_02() {

        TestOpenHelper helper = new TestOpenHelper(getApplicationContext());
        SQLiteDatabase Syuu_S_db_02 = helper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = Syuu_S_db_02.rawQuery("select BUMF_c_01 from BUMF_table where BUMF_c_02 = ?",new String[] {SAGYOU_Busyo_Code_SELECT_01_str});

            if(cursor != null && cursor.getCount() > 0) {

                if(cursor.moveToFirst()) {
                    //*************** 終日　値取得  0: 終日チェックなし, 1: 終日チェックあり
                    Sagyou_B = cursor.getString(0);
                    System.out.println("終日フラグ値：：：" + Sagyou_B);
                }

            } else {
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    } //*****************************************  END Syuuzitu_Flg_SELECT


}