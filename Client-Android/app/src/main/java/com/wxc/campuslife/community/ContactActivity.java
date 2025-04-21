package com.wxc.campuslife.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.AccessServerData;
import com.wxc.campuslife.utils.AlarmManagerUtils;

public class ContactActivity extends AppCompatActivity {

    String userid;
    String nickname;
    private AlarmManagerUtils alarmManagerUtils;
    Handler handler = new Handler();
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        userid = preferences.getString("id","");
        nickname = preferences.getString("nickname","");

        Intent intent = getIntent();
        if(intent.getStringExtra("mode").equals("public")){
            getChatLobby(userid);
        }
        else{
            String userid1 = userid;
            String userid2 = "448a9dc9-a146-475e-b5fa-e27e63cb6edc";
            getChatChannel(userid1,userid2);
        }
    }

    private void getChatLobby(String userid) {
        String url = new AccessServerData().getUrl()+"chatapp/chat/lobby/";

        WebView webViewChat = findViewById(R.id.web_chat);
        webViewChat.getSettings().setJavaScriptEnabled(true);
        webViewChat.setWebViewClient(new WebViewClient());
        webViewChat.loadUrl(url + nickname + "/");
    }


    private void getChatChannel(String userid1, String userid2) {
        String url = "";
        if(userid1.compareTo(userid2)>0){
            url = new AccessServerData().getUrl()+"chatapp/chat/"
                    +userid2.substring(0,7)+userid1.substring(0,7)+"/";
        }
        else{
            url = new AccessServerData().getUrl()+"chatapp/chat/"
                    +userid1.substring(0,7)+userid2.substring(0,7)+"/";
        }

        WebView webViewChat = findViewById(R.id.web_chat);
        webViewChat.getSettings().setJavaScriptEnabled(true);
        webViewChat.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewChat.getSettings().setDomStorageEnabled(true);

        webViewChat.loadUrl(url + nickname + "/");
        WebChromeClient webChromeClient = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                NotificationChannel notificationChannel = null;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String channelId = "chat";
                String channelName = "聊天";
                manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "chat")
                        .setContentTitle(title)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(1, notification);
            }
        };
        webViewChat.setWebChromeClient(webChromeClient);

    }
}