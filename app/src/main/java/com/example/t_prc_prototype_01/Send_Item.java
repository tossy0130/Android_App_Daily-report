package com.example.t_prc_prototype_01;

public class Send_Item {

    //------------------------ 変数　設定 ---------------------

    // 担当者コード カラム 02
    private String Tantou_Code;
    // 担当者名
    private String Tantou_Name;
    // 担当者部署コード
    private String Tantou_Busho_Code;
    // 担当者部署名
    private String Tantou_Busho_Name;

    //--------------- 現品票　コード情報関係
    // 現品票コード
    private String G_pin_code;
    // 品目区分
    private String Hin_kubun;
    // 品目コード
    private String Hin_Code;
    // 商品名
    private String Product_Name;
    // 品目備考
    private String Hin_bikou;
    // 作業場所
    private String Sagyou_Place;
    //--------------- 現品票　コード情報関係 END

    // 標準工数
    private String Hyoujyun_Hours;
    // 段取時間
    private String Processing_Time;
    // 作業時間
    private String Work_Hours;
    // 開始時　数量
    private String Start_Quantity;
    // 終了時　数量
    private String End_Quantity;
    // 不良品数
    private String Defective_Products;
    // 総生産数
    private String Num_Production;
    // 良品数
    private String Good_Products_Num;

    /**
     *  加工数 追加
     */
    // 最終工程　チェック
    private String Kakou_num;

    // 最終工程　チェック
    private String Final_Process_Check;

    /***
     *  最終決定項目  メンバ変数
     */
    private int Send_id; // Send_id => プライマリー
    private String Tantou_c; // 担当者C
    private String Busho_c; //  部署C
    private String GenpinHyou_c; // 現品票C
    private String Hinmoku_k; // 品目K
    private String Hinmoku_c; // 品目C
    private String Hinmoku_name; // 品目名
    private String Hinmoku_bikou; // 品備考
    private String Sagyou_basho; // 作業場所
    private String Dandori_time; // 段取時間
    private String Sagyou_time; // 作業時間
    private String Yotei_num; // 予定数量
    private String Kaishi_num; // 開始数量
    private String Syuuryou_num; // 終了数量
    private String Souseisan_num; // 総生産数
    private String Furyouhin_num; // 不良品数
    private String Ryouhinn_num; // 良品数
    private String Saishyuu_k_check; // 最終工程
    /**
     *  追加　加工数
     */

    private String Saisou_Flg; // 再送フラグ

    //------------------------ 変数　設定 --------------------- END

    //---------------------- セッター 定義 ------------------------

    /*** 最終決定用
     *   セッター
     */

    public Send_Item(int Send_id,String Tantou_c,String Busho_c,String GenpinHyou_c,String Hinmoku_k,String Hinmoku_c,
            String Hinmoku_name,String Hinmoku_bikou, String Sagyou_basho,String Dandori_time,String Sagyou_time,
                     String Yotei_num,String Kaishi_num, String Syuuryou_num,String Souseisan_num,
                     String Furyouhin_num, String Ryouhinn_num,String Saishyuu_k_check,String Saisou_Flg) {

        //--------- 合計：１９
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
        this.Saishyuu_k_check = Saishyuu_k_check;
        this.Saisou_Flg = Saisou_Flg;
    }


    //------------------------ セッター　END

    /*** 最終　ゲッター
     *
     */

    // Send_id
    public int getSend_id() {
        return this.Send_id;
    }

    // 担当者C
    public String getTantou_Code() {
        return this.Tantou_Code;
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

    public String getSaisou_Flg() {
        return this.Saisou_Flg;
    }

    //------------------------ ゲ ッター END

    // 担当者名
    public void setTantou_Name(String Tantou_Name) {
        this.Tantou_Name = Tantou_Name;
    }

    //------------------------------- 現品票　コード情報関係
    //------ 現品票コード
    public void setG_pin_code(String G_pin_code) {
        this.G_pin_code = G_pin_code;
    }
    //------ 品目区分
    public void setHin_kubun(String Hin_kubun) {
        this.Hin_kubun = Hin_kubun;
    }
    //------ 品目コード
    public void setHin_Code(String Hin_Code) {
        this.Hin_Code = Hin_Code;
    }
    // 商品名
    public void setProduct_Name(String Product_Name) {
        this.Product_Name = Product_Name;
    }

    //------ 品目備考
    public void setHin_bikou(String Hin_bikou) {
        this.Hin_bikou = Hin_bikou;
    }

    //----- 作業場所
    public void setSagyou_Place(String Sagyou_Place) {
        this.Sagyou_Place = Sagyou_Place;
    }
    //------------------------------- 現品票　コード情報関係 END





    // 標準工数
    public void setHyoujyun_Hours(String Hyoujyun_Hours) {
        this.Hyoujyun_Hours = Hyoujyun_Hours;
    }

    // 段取時間
    public void setProcessing_Time(String Processing_Time) {
        this.Processing_Time = Processing_Time;
    }

    // 作業時間
    public void setWork_Hours(String Work_Hours) {
        this.Work_Hours = Work_Hours;
    }

    // 良品数
    public void setGood_Products_Num(String Good_Products_Num) {
        this.Good_Products_Num = Good_Products_Num;
    }

    // 最終工程
    public void setFinal_Process_Check(String Final_Process_Check) {
        this.Final_Process_Check = Final_Process_Check;
    }


    //---------------------- ゲッター 定義 ------------------------

    // 担当者名
    public String getTantou_Name() {
        return this.Tantou_Name;
    }

    // 商品名
    public String getProduct_Name() {
        return this.Product_Name;
    }

    // 標準工数
    public String getHyoujyun_Hours() {
        return this.Hyoujyun_Hours;
    }

    // 段取時間
    public String getProcessing_Time() {
        return this.Processing_Time;
    }

    //------------------------------- 現品票　コード情報関係
    //------ 現品票コード
    public String getG_pin_code() {
        return this.G_pin_code;
    }
    //------ 品目区分
    public String getHin_kubun() {
        return this.Hin_kubun;
    }
    //------ 品目コード
    public String getHin_Code() {
        return this.Hin_Code;
    }
    // 作業時間
    public String getWork_Hours() {
        return this.Work_Hours;
    }

    public String getHin_bikou() {
        return this.Hin_bikou;
    }
    //----- 作業場所
    public String getSagyou_Place() {
        return this.Sagyou_Place;
    }

    //------------------------------- 現品票　コード情報関係 END

    // 良品数
    public String getGood_Products_Num() {
        return this.Good_Products_Num;
    }

    // 最終工程
    public String getFinal_Process_Check() {
        return this.Final_Process_Check;
    }





}
