package com.example.t_prc_prototype_01;

import android.app.Activity;
import android.content.ComponentName;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Http2Connection;

import static com.example.t_prc_prototype_01.QR_Barcode_Read.Sousin_log_Flg_QR; // QR 入力用
import static com.example.t_prc_prototype_01.Operation_Input.Sousin_log_Flg_Op; // リスト入力用
import static com.example.t_prc_prototype_01.Free_Input.Sousin_log_Flg_Fr; // 手入力用

public class PostAsyncTask extends AsyncTask<Object, Void, Object> {

    //********** コールバック用　
    private CallBackTask callBackTask;

    private WeakReference<Activity> w_Activity;

    private String GET_result;

    // csv ファイル名　取得
    private String get_csv_name;

    //②コンストラクタで、 呼び出し元Activityを弱参照で変数セット
    public PostAsyncTask(Activity activity) {
        this.w_Activity =  new WeakReference<>(activity);
    }

    public PostAsyncTask() {

    }

    //③バックグラウンド処理
    @Override
    protected Object doInBackground(Object[] data) {

        //Object配列でパラメータを持ってこれたか確認
        String url = (String) data[0];
        String description = (String) data[1];
        get_csv_name = description;

        String filePath = (String) data[2];

        //④HTTP処理用オプジェクト作成
        OkHttpClient client = new OkHttpClient();

        //⑤送信用POSTデータを構築（Multipart)
        MediaType MEDIA_TYPE_CSV = MediaType.parse("text/csv; charset=Shift_JIS");

        // リクエストボディの作成
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"description\""),
                        RequestBody.create(MEDIA_TYPE_CSV, description)
                )
                .addFormDataPart(
                        "uploaded_file",
                        get_csv_name, //****** 　送信形式　ファイル名に　入れ替え ******
                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath))
                )
                .build();


        //⑥送信用リクエストを作成
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();

        //⑦受信用オブジェクトを作成 call back 用
        Call call = client.newCall(request);
        String result = "";

        //⑧送信と受信
        try {

            Response response = call.execute();
            ResponseBody body = response.body();

            if (body != null) {

                result = body.string();

                //************* PHP から　0 が返ってきたら　ファイル 0　バイト　エラー
                if (result.equals("0")) {
                    Sousin_log_Flg_QR = "0"; // QR_Barcode_Read 送信　ログ用　フラグ
                    Sousin_log_Flg_Op = "0"; // Operation_Input 送信　ログ用　フラグ
                    Sousin_log_Flg_Fr = "0"; // Free_Input 送信　ログ用　フラグ

                } else {

                    Sousin_log_Flg_QR = "1"; // QR_Barcode_Read 送信　ログ用　フラグ
                    Sousin_log_Flg_Op = "1"; // Operation_Input 送信　ログ用　フラグ
                    Sousin_log_Flg_Fr = "1"; // Free_Input 送信　ログ用　フラグ

                }

            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            result = "0:「送信失敗」通信エラーで、ファイルが送信できませんでした。\n\n" +
                    "Wi-Fi状況などを確認し、時間をおいて、送信処理を行ってください。";

        } catch (IOException e) {
            e.printStackTrace();

            Sousin_log_Flg_QR = "2"; // QR_Barcode_Read 送信　ログ用　フラグ
            Sousin_log_Flg_Op = "2"; // Operation_Input 送信　ログ用　フラグ
            Sousin_log_Flg_Fr = "2"; // Free_Input 送信　ログ用　フラグ

            result = "0:例外：「送信失敗」送信タスクで例外エラーが発生しました。\n\n" +
            "Wi-Fi状況などを確認し、時間をおいて、送信処理を行ってください。";

        }

        //⑨結果を返し、onPostExecute で受け取る
        return result;

         } //------------- doInBackground END


    //****** ⑩バックグラウンド完了処理 ********
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        try {

            Log.v("onPostExecute　POST 送信結果", (String) result + "送信結果 POST");
            System.out.println("送信結果【" + result + "】");
            System.out.println("Sousin_log_Flg_QR 値:" + Sousin_log_Flg_QR);

            //********** コールバック取得
            callBackTask.CallBack((String)result);

        } catch (ClassCastException e) {
            e.printStackTrace();

            result = "「送信失敗」ファイル送信に失敗しました";
            //********** コールバック取得
            callBackTask.CallBack((String)result);

            return;
        }


        //⑪画面表示

    } //------------------------------------ onPostExecute END

    public void setOnCallBack(CallBackTask _cbj) {callBackTask = _cbj;}

    /**
     * コールバック用のstaticなclass
     */
    public static class CallBackTask {
        public void CallBack(Object result) {

        }

    }


}
