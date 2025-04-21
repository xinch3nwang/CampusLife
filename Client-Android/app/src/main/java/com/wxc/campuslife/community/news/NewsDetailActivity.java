package com.wxc.campuslife.community.news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wxc.campuslife.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsDetailActivity extends AppCompatActivity {
    String strUrl;
    TextView tvDetailTitle,tvDetailContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        tvDetailTitle=(TextView)findViewById(R.id.tvDetailTitle);
        tvDetailContent=(TextView)findViewById(R.id.tvDetailContent);
        Bundle bundle= getIntent().getExtras();
        strUrl=bundle.get("url").toString();

        tvDetailTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final WebView web = new WebView(NewsDetailActivity.this);
                web.requestFocus();
                web.setFocusable(true);
                web.setWebViewClient(new WebViewClient());
                web.loadUrl(strUrl);
                AlertDialog.Builder webDialog = new AlertDialog.Builder(NewsDetailActivity.this);
                webDialog.setView(web);
                webDialog.show();

            }
        });

        new DetailTask().execute();
    }
    class DetailTask extends AsyncTask<Void,Void,String[]> {
        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            tvDetailTitle.setText(s[0]);
            tvDetailContent.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
            tvDetailContent.setText(s[1]);
        }
        @Override
        protected String[] doInBackground(Void... voids) {
            String resultData="";
            try {
                URL url=new URL(strUrl);
                HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
                InputStreamReader in=new InputStreamReader(httpConnection.getInputStream(),"UTF-8");
                BufferedReader buffer=new BufferedReader(in);
                String inputLine=null;
                while ((inputLine=buffer.readLine())!=null){
                    resultData+=inputLine+"\n";
                }
                in.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document doc = Jsoup.parse(resultData);
            Elements element=doc.select("#wenzhang > div:nth-child(2)");
            String title=element.text().trim();
            element=doc.select("#zoom");
            String content= element.text().trim();
            String[] arr=new String[2];
            arr[0]=title;
            arr[1]=content;
            return arr;
        }
    }
}
