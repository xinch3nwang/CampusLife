package com.wxc.campuslife.community.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.wxc.campuslife.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    List<Map<Integer,String>> listTitle=new ArrayList<>();
    ArrayList<String> newslist=new ArrayList<>();
    ListView lvTitles;
    Button btnPrev,btnNext;
    int page = 9;
    int sel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        btnPrev=(Button)findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPrev.setText("上一页");
                btnNext.setText("下一页");
                sel = 0;
                new HttpTask().execute();
            }
        });
        btnNext=(Button)findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sel = 1;
                new HttpTask().execute();
            }
        });

        btnPrev.performClick();

        lvTitles=(ListView)findViewById(R.id.lvTitle);
        lvTitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<Integer,String> m=listTitle.get(i);
                //Toast.makeText(MainActivity.this, m.get(new Integer(i)),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(NewsActivity.this, NewsDetailActivity.class);
                intent.putExtra("url",m.get(new Integer(i)));
                startActivity(intent);
            }
        });
    }



    class HttpTask extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            newslist.clear();
            listTitle.clear();
            Elements element;
            String str_element;
            Document doc = Jsoup.parse(s);
            element = doc.select(" #msgList > div > ul");
            Elements children = element.select("a");
            String ss="";
            for(int i=0; i<children.size(); i++){
                Element e = children.get(i);
                newslist.add(e.text().trim());
                String contentUrl="https://news.ntu.edu.cn"+e.attr("href");
                Log.i("HTTPTEST",contentUrl);
                Map<Integer,String> m=new HashMap<>();
                m.put(i,contentUrl);
                listTitle.add(m);
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<>(NewsActivity.this,android.R.layout.simple_list_item_1,newslist);
            lvTitles.setAdapter(adapter);

        }

        @Override
        protected String doInBackground(Void... voids) {
            if(sel == 1) page++;
            else page--;
            if(page<9) page = 9;

            String httpUrl="https://news.ntu.edu.cn/"+page+"/list.htm";

            String resultData="";
            URL url=null;
            try {
                url=new URL(httpUrl);
                HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
                InputStreamReader in=new InputStreamReader(httpConnection.getInputStream(),"UTF-8");
                BufferedReader buffer=new BufferedReader(in);
                String inputLine=null;
                while ((inputLine=buffer.readLine())!=null){
                    resultData+=inputLine+"\n";
                }
                in.close();
                httpConnection.disconnect();
                return resultData;
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }
}