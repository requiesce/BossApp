/**
 * Created by x on 10/12/2017.
 */
package com.example.x.bossbandsapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebSearcher extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);
        webView = (WebView) findViewById(R.id.webviewWebsite);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("wp-android-native");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // FORCE BROWSER TO IDENTIFY USER AS PHONE TO ALLOW SITE TO LOAD MOBILE VERSION
        //webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        String searchFor = getIntent().getStringExtra("passedQuery");
        webView.loadUrl("http://runescape.wikia.com/search?noads=&query=" + searchFor);

    }


}