package com.wxc.campuslife.prompt;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.AlarmManagerUtils;
import com.wxc.campuslife.utils.MyApplication;
import com.wxc.campuslife.utils.dbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ViewHolder>{

    private List<Remind> mRemindList;
    private AlarmManagerUtils alarmManagerUtils;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View RemindView;
        TextView RemindTime;
        CheckBox RemindName;

        public ViewHolder(View view) {
            super(view);
            RemindView = view;
            RemindTime = (TextView) view.findViewById(R.id.textView_timetodo);
            RemindName = (CheckBox) view.findViewById(R.id.checkBox_isfin);
        }
    }

    public RemindAdapter(List<Remind> RemindList) {
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
                Toast.makeText(v.getContext(), Remind.getEventName()+":"+Remind.getEventDetail(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.RemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Remind Remind = mRemindList.get(position);
                Date time = Remind.getEventTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                alarmManagerUtils = AlarmManagerUtils.getInstance(parent.getContext());
                alarmManagerUtils.createAlarmManager();
                alarmManagerUtils.alarmManagerStartWork(hour, min);
                Toast.makeText(v.getContext(), "已设置提醒时间： " + Remind.getEventTime(), Toast.LENGTH_SHORT).show();
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
                                    "' AND hour = " +
                                    hour +
                                    " AND min = " +
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

        holder.RemindName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    int position = holder.getAdapterPosition();
                    Remind remind = mRemindList.get(position);
                    String title = remind.getEventName();
                    Date t = remind.getEventTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(t);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    String sql = "UPDATE record SET isfin = 1 WHERE title='" + title + "' AND hour=" + hour + ";";
                    Log.d("database", "change to finish " + hour);
                    dbHelper myhelper = new dbHelper(MyApplication.getContext());
                    SQLiteDatabase db = myhelper.getWritableDatabase();
                    db.execSQL(sql);
                }
                else {
                    int position = holder.getAdapterPosition();
                    Remind remind = mRemindList.get(position);
                    String title = remind.getEventName();
                    Date t = remind.getEventTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(t);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    String sql = "UPDATE record SET isfin = 0 WHERE title='" + title + "' AND hour=" + hour + ";";
                    Log.d("database", "change to unfinish " + hour);
                    dbHelper myhelper = new dbHelper(MyApplication.getContext());
                    SQLiteDatabase db = myhelper.getWritableDatabase();
                    db.execSQL(sql);
                }

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            String title = remind.getEventName();
//                            Date t = remind.getEventTime();
//                            Calendar cal=Calendar.getInstance();
//                            cal.setTime(t);
//                            int hour = cal.get(Calendar.HOUR_OF_DAY);
//                            String sql="UPDATE record SET isfin = 1 WHERE title='" + title + "' AND hour="+hour+";";
//                            Log.d("database","change to finish "+hour);
//                            dbHelper myhelper = new dbHelper(MyApplication.getContext());
//                            SQLiteDatabase db = myhelper.getWritableDatabase();
//                            db.execSQL(sql);
//
//                            mRemindList.remove(position);
//                            notifyItemRemoved(position);
//                            notifyDataSetChanged();
//                            holder.RemindName.setChecked(false);
//
//                        }
//                    }, 500);

            }
        });
        return holder;
    }

    private void remove(int position) {
        mRemindList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        //RemindName.setChecked(false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remind Remind = mRemindList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm\nyyyy.MM.dd");
        String eventTime = sdf.format(Remind.getEventTime());
        holder.RemindTime.setText(eventTime);
        holder.RemindName.setText(Remind.getEventName());
    }

    @Override
    public int getItemCount() {
        return mRemindList.size();
    }

}
