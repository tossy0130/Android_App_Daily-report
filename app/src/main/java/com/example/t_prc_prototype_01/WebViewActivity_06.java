package com.example.t_prc_prototype_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;

public class WebViewActivity_06 extends AppCompatActivity {

    private static final String JIM_TEST_URL = "http://192.168.254.226/JimApk/index.php";  // JIM　社内 OK *****
    private static final String JIM_TEST_URL_02 = "http://192.168.11.100/JimApk/index.php";  // JIM　社内 OK *****




    private WebView webview_06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_06);


        webview_06 = findViewById(R.id.webview_06);


        // PDF 確認
        webview_06.getSettings().setJavaScriptEnabled(true);

          webview_06.loadUrl(JIM_TEST_URL); //--- 社内 テスト 01
      //  webview_06.loadUrl(JIM_TEST_URL_02); //--------- 社内　テスト 02
      //  webview_06.loadUrl(TOYAMA_URL); //******* 本番

        webview_06.setDownloadListener(new DownloadListener() {
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
