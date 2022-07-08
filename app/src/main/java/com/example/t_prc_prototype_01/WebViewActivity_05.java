package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;

public class WebViewActivity_05 extends AppCompatActivity {

    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/video/work_choice/index.html";


    private WebView webview_05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_05);

        webview_05 = findViewById(R.id.webview_05);

        // 社内テスト
        String jim = "http://192.168.254.87/tana_phppost_file/pdf_file_list/index.php";


        // PDF 確認
        webview_05.getSettings().setJavaScriptEnabled(true);

        webview_05.loadUrl(JIM_TEST_URL); //--- 社内 本番
     //   webview_05.loadUrl(TOYAMA_URL); //******* 本番

        webview_05.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        }); //---------- setDownloadListner END

    } //*******************  END onCreate

    //------- 戻るボタン処理
    @Override
    public void onBackPressed(){
        finish();
    }

}