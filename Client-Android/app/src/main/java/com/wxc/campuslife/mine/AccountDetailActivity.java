package com.wxc.campuslife.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.AccessServerData;

public class AccountDetailActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Handler handler_vis = new Handler();
    String nickname;
    String age;
    String gender;
    String email;
    String status;
    String intro;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

//        Toast.makeText(AccountDetailActivity.this, author,Toast.LENGTH_SHORT).show();

        Button btn_readed = findViewById(R.id.button_readed);
        Button btn_liked = findViewById(R.id.button_liked);
        Button btn_collected = findViewById(R.id.button_collected);

        Button btn_update = findViewById(R.id.button_edit_account);
//        TextView tv_username = findViewById(R.id.textview_mine_name);
        EditText et_nickname = findViewById(R.id.editTextNickname);
        EditText et_age = findViewById(R.id.editTextAge);
        EditText et_gender =findViewById(R.id.editTextGender);
        EditText et_email = findViewById(R.id.editTextEmail);
        EditText et_status = findViewById(R.id.editTextStatus);
        EditText et_intro = findViewById(R.id.editTextIntro);

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        userid = preferences.getString("id","");

        try{
            Intent intent = getIntent();
            String author = intent.getStringExtra("author");
            Log.d("TAG",author);

            new Thread(new Runnable() {
                @Override
                    public void run() {
                    userid = new AccessServerData().getUserid(author);
                    handler_vis.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_update.setVisibility(View.GONE);
                            et_nickname.setFocusable(false);
                            et_age.setFocusable(false);
                            et_email.setFocusable(false);
                            et_gender.setFocusable(false);
                            et_intro.setFocusable(false);
                            et_status.setFocusable(false);
                        }
                    });
                }
            }).start();
        }
        catch (Exception e) {
            Log.e("exception", "e");
        }
        finally {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UserInfo userInfo = new AccessServerData().getUserProfile(userid);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                        tv_username.setText(userInfo.getResult()[0].getNickname());
                            nickname = userInfo.getResult()[0].getNickname();
                            age = String.valueOf(userInfo.getResult()[0].getFields().getAge());
                            email = userInfo.getResult()[0].getFields().getEmail();
                            gender = userInfo.getResult()[0].getFields().getGender();
                            intro = userInfo.getResult()[0].getFields().getIntro();
                            status = userInfo.getResult()[0].getFields().getStatus();

                            et_nickname.setText(nickname);
                            et_age.setText(age);
                            et_email.setText(email);
                            et_gender.setText(gender);
                            et_intro.setText(intro);
                            et_status.setText(status);

                            SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
                            editor.putString("nickname", nickname);
                            editor.apply();

                        }
                    });
                }
            }).start();

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nickname = et_nickname.getText().toString();
                    age = et_age.getText().toString();
                    email = et_email.getText().toString();
                    gender = et_gender.getText().toString();
                    intro = et_intro.getText().toString();
                    status = et_status.getText().toString();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new AccessServerData().updateUserProfile(nickname, age, email, gender, intro, status, userid);
                        }
                    }).start();

                }
            });
        }


    }
}