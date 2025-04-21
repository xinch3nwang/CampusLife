package com.wxc.campuslife.prompt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.MyApplication;
import com.wxc.campuslife.utils.dbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindAllAdapter extends RecyclerView.Adapter<RemindAllAdapter.ViewHolder>{

    private List<Remind> mRemindList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View RemindView;
        TextView RemindTime;
        CheckBox RemindName;

        public ViewHolder(View view) {
            super(view);
            RemindView = view;
            RemindTime = (TextView) view.findViewById(R.id.textView_timetodo);
            RemindName = (CheckBox) view.findViewById(R.id.checkBox_isfin);
            //RemindName.findViewById(R.id.checkBox_isfin).setClickable(false);
        }
    }

    public RemindAllAdapter(List<Remind> RemindList) {
        mRemindList = RemindList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.RemindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Remind Remind = mRemindList.get(position);
                //Toast.makeText(v.getContext(), "you clicked view " + Remind.getEventName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.RemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Remind Remind = mRemindList.get(position);
                //Toast.makeText(v.getContext(), "time:" + Remind.getEventTime(), Toast.LENGTH_SHORT).show();
                String title = Remind.getEventName();
                String ntitle = title.substring(0,title.length());//-5
                String detail = "\n          时间:"+Remind.getEventTime().toString().substring(0,20)
                        +"\n          详细信息：";
                detail += Remind.getEventDetail();
                final TextView tv_detail = new TextView(parent.getContext());
                tv_detail.setText(detail);
                AlertDialog.Builder detailDialog = new AlertDialog.Builder(parent.getContext());
                detailDialog.setTitle(ntitle);
                detailDialog.setView(tv_detail);
                detailDialog.setNeutralButton("修改待办名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final EditText editText = new EditText(parent.getContext());
                        editText.setSingleLine();
                        editText.requestFocus();
                        editText.setFocusable(true);
                        AlertDialog.Builder inputDialog = new AlertDialog.Builder(parent.getContext());
                        inputDialog.setTitle("修改名称");
                        inputDialog.setView(editText);
                        inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newtitle = editText.getText().toString();
                                //addtosql
                                try {
                                    //添加进数据库
                                    String sql = "UPDATE record SET title = '" +
                                            newtitle +
                                            "' WHERE title = '" +
                                            ntitle +
                                            "';";
                                    dbHelper myhelper = new dbHelper(parent.getContext());
                                    SQLiteDatabase db = myhelper.getWritableDatabase();
                                    db.execSQL(sql);
                                    //给用户提示添加成功
                                    //Toast.makeText(parent.getContext(), "记录已更新", Toast.LENGTH_LONG).show();
                                    refresh(position);
                                } catch (Exception e) {
                                    //插入失败
                                    Toast.makeText(parent.getContext(), newtitle, Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        inputDialog.show();
                    }
                });
                detailDialog.setPositiveButton("修改详细", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final EditText editText = new EditText(parent.getContext());
                        editText.setSingleLine();
                        editText.requestFocus();
                        editText.setFocusable(true);
                        AlertDialog.Builder inputDialog = new AlertDialog.Builder(parent.getContext());
                        inputDialog.setTitle("修改详细信息");
                        inputDialog.setView(editText);
                        inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newdetail = editText.getText().toString();

                                //addtosql
                                try{
                                    //添加进数据库
                                    String sql="UPDATE record SET detail = '" +
                                            newdetail +
                                            "' WHERE title = '" +
                                            ntitle +
                                            "';";
                                    dbHelper myhelper = new dbHelper(parent.getContext());
                                    SQLiteDatabase db = myhelper.getWritableDatabase();
                                    db.execSQL(sql);
                                    //给用户提示添加成功
                                    //Toast.makeText(parent.getContext(),"记录已更新",Toast.LENGTH_LONG).show();
                                    refresh(position);
                                } catch (Exception e){
                                    //插入失败
                                    Toast.makeText(parent.getContext(),newdetail,Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        inputDialog.show();
                    }
                });
                detailDialog.show();

            }

        });
        holder.RemindName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                Remind Remind = mRemindList.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
                dialog.setTitle("删除待办？");
                //dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        remove(position);
                        try{
                            Date time = Remind.getEventTime();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(time);
                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            int min = cal.get(Calendar.MINUTE);

                            String sql="DELETE FROM record WHERE title = '" +
                                    Remind.getEventName() +
//                                    "' AND hour = " +
//                                    hour +
                                    "' AND min = " +
                                    min +
                                    ";";
                            dbHelper myhelper = new dbHelper(MyApplication.getContext());
                            SQLiteDatabase db = myhelper.getWritableDatabase();
                            db.execSQL(sql);
                            Log.d("database","delete ok"+hour+Remind.getEventName()+min);
                            Toast.makeText(parent.getContext(),"记录已删除",Toast.LENGTH_LONG).show();

                        } catch (Exception e){
                            Toast.makeText(parent.getContext(),"fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
        holder.RemindName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Remind Remind = mRemindList.get(position);
            }
        });
//        holder.RemindName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    int position = holder.getAdapterPosition();
//                    Remind remind = mRemindList.get(position);
//                    String title = remind.getEventName();
//                    Date t = remind.getEventTime();
//                    Calendar cal=Calendar.getInstance();
//                    cal.setTime(t);
//                    int hour = cal.get(Calendar.HOUR_OF_DAY);
//                    String sql="UPDATE record SET isfin = 1 WHERE title='" + title + "' AND hour="+hour+";";
//                    Log.d("database","change to finish "+hour);
//                    dbHelper myhelper = new dbHelper(MyApplication.getContext());
//                    SQLiteDatabase db = myhelper.getWritableDatabase();
//                    db.execSQL(sql);
//                    holder.RemindName.setChecked(false);
//
//                }
//                else
//            }
//        });
        return holder;


    }

    private void remove(int position) {
        mRemindList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remind Remind = mRemindList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm");
        String eventTime = sdf.format(Remind.getEventTime());

        holder.RemindTime.setText(eventTime);
        holder.RemindName.setText(Remind.getEventName());
        if(Remind.getIsFin()==1) holder.RemindName.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return mRemindList.size();
    }


    public void showRemind() {
        dbHelper remindDB = new dbHelper(MyApplication.getContext());
        SQLiteDatabase db = remindDB.getWritableDatabase();

        try {
            String title = "";
            String detail = "";
            String time = "";
            int isfin;
            Cursor c = db.rawQuery("select * from record order by isfin,year,month,day,hour,min",null);
            //如果成功则显示
            if(c.getCount() > 0) {
                while (c.moveToNext()){
                    isfin = c.getInt(9);
                    title = c.getString(7);
                    detail = c.getString(8);
                    time = c.getString(1)+"-"+c.getString(2)+"-"+c.getString(3)+" "+c.getString(4)+":"+c.getString(5)+":"+c.getString(6);
                    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = fmt.parse(time);
//                    if(isfin==1)
//                        title="(已完成)"+title;
//                    else
//                        title="(未完成)"+title;
                    Remind item = new Remind(title,detail,date,isfin);
                    mRemindList.add(item);
                }
            }
            //Log.d("datebase","find all ok");
        } catch (Exception e) {
            Toast.makeText(MyApplication.getContext(), e+"", Toast.LENGTH_LONG).show();
        }
    }

    private void refresh(int position) {
        mRemindList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        mRemindList.clear();
        showRemind();
    }
}
