package com.example.t_prc_prototype_01;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;



public class HttpGetTask extends AsyncTask<URL, Void, String> {

    private CallBackTask callBackTask;

    @Override
    protected String doInBackground(URL... urls) {

        //------- 接続確認用 ------
        final StringBuilder result = new StringBuilder();
        // アクセス先
        final URL url = urls[0];
        // コネクション
        HttpURLConnection con = null;

        try {

            con = (HttpURLConnection) url.openConnection();
            con.connect();

            final int stats = con.getResponseCode();

            // スタッツが 200 だったら　= OK だったら
            if(stats == HttpURLConnection.HTTP_OK) {
                System.out.println("接続 200 OK");
                result.append("200");

            } else {
                System.out.println("接続 NG 200 以外");
                result.append("200以外");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if(con != null) {
                con.disconnect();
            }
        }

        return result.toString();

    }

    //------- 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(String result) {

        callBackTask.CallBack(result);
    }

    public void setOnCallBack(CallBackTask _cbj) {callBackTask = _cbj;}

    /**
     * コールバック用のstaticなclass
     */
    public static class CallBackTask {
        public void CallBack(String result) {

        }
    }




}
