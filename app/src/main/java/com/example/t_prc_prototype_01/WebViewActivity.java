package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private static final String JIM_TEST_URL = "http://192.168.254.226/tana_phppost_file/view_pdf/new.pdf";

    private WebView webview_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview_01 = findViewById(R.id.webview_01);

        // 社内テスト
         String jim = "http://192.168.254.87/tana_phppost_file/view_pdf/test_0824.pdf";


        // PDF 確認
        webview_01.getSettings().setJavaScriptEnabled(true);

        webview_01.loadUrl(JIM_TEST_URL);  //--- 社内
      //  webview_01.loadUrl(TOYAMA_URL);  //******* 本番


        webview_01.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }


        }); //---------- setDownloadListner END
    }

    //------ 戻るボタン処理
    @Override
    public void onBackPressed(){
        finish();
    }

}