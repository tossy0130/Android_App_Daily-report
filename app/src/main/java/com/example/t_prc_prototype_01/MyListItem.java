package com.example.t_prc_prototype_01;

import android.util.Log;

public class MyListItem {

    private int Send_id; // Send_id => プライマリー:0
    private String Tantou_c; // 担当者C :1
    private String Busho_c; //  部署C :2
    private String GenpinHyou_c; // 現品票C :3
    private String Hinmoku_k; // 品目K :4
    private String Hinmoku_c; // 品目C :5
    private String Hinmoku_name; // 品目名 :6
    private String Hinmoku_bikou; // 品備考 :7
    private String Sagyou_basho; // 作業場所 :8
    private String Dandori_time; // 段取時間 :9
    private String Sagyou_time; // 作業時間 :10
    private String Yotei_num; // 予定数量 :11
    private String Kaishi_num; // 開始数量 :12
    private String Syuuryou_num; // 終了数量 :13
    private String Souseisan_num; // 総生産数 :14
    private String Furyouhin_num; // 不良品数 :15
    private String Ryouhinn_num; // 良品数 : 16
    private String Saishyuu_k_check; // 最終工程 :17
    /**
     * 加工数　追加
     */
    private String Kakou_num; // 最終工程  : 18
    private String Saisou_Flg; // 再送フラグ : 19

    private String Start_Time; // 開始時間 : 20
    private String End_Time; // 終了時間 : 21
    private String GR_id; // グラフ ID  &  作業 ID : 22
    private String year_str; // yyyMMdd データ  20201210
    private String Iro_Time_num; // 色段取時間
    private String Kara_Time_num; // 型段取時間
    private String Kikai_Code; // 機械コード


    public MyListItem(int Send_id,String Tantou_c,String Busho_c,String GenpinHyou_c,String Hinmoku_k,String Hinmoku_c,
                      String Hinmoku_name,String Hinmoku_bikou, String Sagyou_basho,String Dandori_time,String Sagyou_time,
                      String Yotei_num,String Kaishi_num, String Syuuryou_num,String Souseisan_num,
                      String Furyouhin_num, String Ryouhinn_num,String Saishyuu_k_check,String Kakou_num,String Saisou_Flg,
                      String Start_Time, String End_Time, String GR_id,
                      String year_str,String Iro_Time_num, String Kara_Time_num, String Kikai_Code){

        //--------- 合計：２７
        //--------------------- 追加
        this.Send_id = Send_id;
        this.Tantou_c = Tantou_c;
        this.Busho_c = Busho_c;
        this.GenpinHyou_c = GenpinHyou_c;
        this.Hinmoku_k = Hinmoku_k;
        this.Hinmoku_c = Hinmoku_c;
        this.Hinmoku_name = Hinmoku_name;
        this.Hinmoku_bikou = Hinmoku_bikou;
        this.Sagyou_basho = Sagyou_basho;
        this.Dandori_time = Dandori_time;
        this.Sagyou_time = Sagyou_time;
        this.Yotei_num = Yotei_num;
        this.Kaishi_num = Kaishi_num;
        this.Syuuryou_num = Syuuryou_num;
        this.Souseisan_num = Souseisan_num;
        this.Furyouhin_num = Furyouhin_num;
        this.Ryouhinn_num = Ryouhinn_num;
        this.Saishyuu_k_check = Saishyuu_k_check; // 18
        /**
         *  加工数
         */
        this.Kakou_num = Kakou_num; // 19
        this.Saisou_Flg = Saisou_Flg; // 20

        this.Start_Time = Start_Time; // 21　開始時間
        this.End_Time = End_Time; // 22 終了時間
        this.GR_id = GR_id; // 23  index は 22  ======>  グラフ ID &&  作業 ID
        /**
         * 追加　色段取時間　etc
         */
        this.year_str = year_str;  // yyyyMMdd
        this.Iro_Time_num = Iro_Time_num; // 色段取時間
        this.Kara_Time_num = Kara_Time_num; // 型段取時間
        this.Kikai_Code = Kikai_Code; // 機械コード
    }


    /*** 最終　ゲッター
     *
     */

    public int getId() {
        Log.d("ログ: 取得したID：", String.valueOf(Send_id));
        return Send_id;
    }

    // Send_id
    public int getSend_id() {
        return this.Send_id;
    }
    // 担当者C
    public String getTantou_c() {
        return this.Tantou_c;
    }
    //  部署C
    public String getBusho_c() {
        return this.Busho_c;
    }
    // 現品票C
    public String getGenpinHyou_c() {
        return this.GenpinHyou_c;
    }
    // 品目K
    public String getHinmoku_k() {
        return this.Hinmoku_k;
    }
    // 品目C
    public String getHinmoku_c() {
        return this.Hinmoku_c;
    }
    // 品目名
    public String getHinmoku_name() {
        return this.Hinmoku_name;
    }
    // 品備考
    public String getHinmoku_bikou() {
        return this.Hinmoku_bikou;
    }
    // 作業場所
    public String getSagyou_basho() {
        return this.Sagyou_basho;
    }
    // 段取時間
    public String getDandori_time() {
        return this.Dandori_time;
    }
    // 作業時間
    public String getSagyou_time() {
        return this.Sagyou_time;
    }
    // 予定数量
    public String getYotei_num() {
        return this.Yotei_num;
    }
    // 開始数量
    public String getKaishi_num() {
        return this.Kaishi_num;
    }
    // 終了数量
    public String getSyuuryou_num() {
        return this.Syuuryou_num;
    }
    // 総生産数
    public String getSouseisan_num() {
        return this.Souseisan_num;
    }
    // 不良品数
    public String getFuryouhin_num() {
        return this.Furyouhin_num;
    }
    // 良品数
    public String getRyouhinn_num() {
        return this.Ryouhinn_num;
    }
    // 最終工程
    public String getSaishyuu_k_check() {
        return this.Saishyuu_k_check;
    }

    /**
     *  加工数　追加
     * @return
     */
    public String getKakou_num() {
        return this.Kakou_num;
    }

    public String getSaisou_Flg() {
        return this.Saisou_Flg;
    }

    /**
     *    追加　「開始時間」　「終了時間」　「グラフID && 作業 ID」
     */
    public String getStart_Time() {
        return this.Start_Time;
    }

    public String getEnd_Time() {
        return this.End_Time;
    }

    public String getGR_id () {
        return this.GR_id;
    }

    /**
     *  追加　「色段取時間」など
     */
    public String getYear_str () {
        return this.Iro_Time_num;
    }

    // 色段取時間
    public String getIro_Time_num () {
        return this.Iro_Time_num;
    }

    // 型段取時間
    public String getKara_Time_num () {
        return this.Kara_Time_num;
    }

    // 機械コード
    public String getKikai_Code () {
        return this.Kikai_Code;
    }


    //------------------------ ゲ ッター END


}
