package am.ze.wookoo.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    ArrayList <EditText> mRecords;



    Calendar calendar;


    private  class  Day{
        public TextView day;
        public  TextView number;


        public Day(View viewById, View viewById1) {
            this.day = (TextView)viewById;
            this.number = (TextView)viewById1;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        calendar = Calendar.getInstance();

        Intent intent = getIntent();
        String setted = intent.getStringExtra("time");

        if (setted == null || setted.equals("")){
            setted  = "알람을 설정해주세요";
        }






/*
        SQLiteDatabase mDB;
        DBHelper dbHelper = new DBHelper(RecordActivity.this);
        mDB = dbHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT  * FROM user",null);
        cursor.moveToNext();
        String setted = cursor.getString(4);*/


        ArrayList<Integer> days = new ArrayList<Integer>();

        int today = calendar.get(Calendar.DAY_OF_MONTH);

        for(int i = 1; i <=7; i++){
            calendar.set(Calendar.DAY_OF_WEEK,i);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            days.add(day);
        }




        for(int d : days){
            Log.d("오늘",String.valueOf(d));
        }

        ArrayList<Day> weeks = new ArrayList<Day>();

        weeks.add(new Day(findViewById(R.id.textView24),findViewById(R.id.sun)));
        weeks.add(new Day(findViewById(R.id.textView26),findViewById(R.id.mon)));
        weeks.add(new Day(findViewById(R.id.textView28),findViewById(R.id.tue)));
        weeks.add(new Day(findViewById(R.id.textView29),findViewById(R.id.wed)));
        weeks.add(new Day(findViewById(R.id.textView30),findViewById(R.id.thu)));
        weeks.add(new Day(findViewById(R.id.textView32),findViewById(R.id.fri)));
        weeks.add(new Day(findViewById(R.id.textView22),findViewById(R.id.sat)));
        Typeface temp = weeks.get(0).day.getTypeface();

        boolean run = true;
        for(int i = 0; i< 7; i ++){

            weeks.get(i).number.setText(String.valueOf(days.get(i)));

            if(run) {

                weeks.get(i).day.setTextColor(Color.BLACK);
                weeks.get(i).number.setTextColor(Color.BLACK);

                weeks.get(i).day.setTypeface(temp, Typeface.BOLD);
                weeks.get(i).number.setTypeface(temp, Typeface.BOLD);

            }
            if(days.get(i) == today){
                run = false;
            }



    }

        final TextView mPlus = findViewById(R.id.plus);
        final EditText mSquat = findViewById(R.id.record_squat);
        final EditText mLunge = findViewById(R.id.record_lunge);
        final EditText mPlank = findViewById(R.id.plank_record);
        final EditText mBurpee = findViewById(R.id.record_burpee);

        mRecords  = new ArrayList<EditText>();
        mRecords.add(mSquat);
        mRecords.add(mLunge);
        mRecords.add(mPlank);
        mRecords.add(mBurpee);
        for (EditText Buttons : mRecords){
            Buttons.setFocusable(false);
            Buttons.setClickable(false);
            Buttons.setFocusableInTouchMode(false);
        }


        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //false = 0
                //true = 1;



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    if(mSquat.getFocusable() == 1){
                        Toast.makeText(RecordActivity.this,"기록 수동 추가 취소",Toast.LENGTH_SHORT).show();
                        mPlus.setText("    +    ");
                        for (EditText Buttons : mRecords) {
                            Buttons.setFocusable(false);
                            Buttons.setClickable(false);
                            Buttons.setFocusableInTouchMode(false);
                        }
                    }
                    else{
                        mPlus.setText("    -    ");
                        Toast.makeText(RecordActivity.this,"기록 수동 추가 ",Toast.LENGTH_SHORT).show();
                        for (EditText Buttons : mRecords) {
                            Buttons.setFocusable(true);
                            Buttons.setClickable(true);
                            Buttons.setFocusableInTouchMode(true);
                        }
                    }

                }
            }
        });

        TextView mAlarmText = findViewById(R.id.setted);
        LinearLayout mtemp = findViewById(R.id.lay);
        mAlarmText.setText(setted);
        mtemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordActivity.this,AlarmActivity.class));
                finish();
            }
        });

        Button mCalander = findViewById(R.id.award_calander);
        mCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordActivity.this,CalanderBeforeActivity.class));
            }
        });
        Button mAward = findViewById(R.id.calander_award);
        mAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordActivity.this,AwardBeforeActivity.class));
            }
        });


    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        if(KeyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(RecordActivity.this,"5초후 알람이 울립니다." ,Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat mhour = new SimpleDateFormat("kk");
                    String strnow = mhour.format(date);
                    int hour = Integer.parseInt(strnow);
                    SimpleDateFormat mMin = new SimpleDateFormat("mm");
                    String strMin = mMin.format(date);
                    int min = Integer.parseInt(strMin);
                    SimpleDateFormat mSec = new SimpleDateFormat("ss");
                    String strSec = mSec.format(date);
                    int sec = Integer.parseInt(strSec);


                    Log.d("시",""+hour);
                    Log.d("분",""+min);
                    Log.d("초",""+sec);

                    Calendar calendar = Calendar.getInstance();
                    Intent my_intent = new Intent(RecordActivity.this,Alarm_Reciver.class);


                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.SECOND,0);



                    // reveiver에 string 값 넘겨주기
                    my_intent.putExtra("state","alarm on");

                    PendingIntent pendingIntent;

                    pendingIntent = PendingIntent.getBroadcast(RecordActivity.this, 0, my_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    // 알람셋팅
                    AlarmManager alarm_manager;
                    alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pendingIntent);



                }
            },5000);

            return true;
        }

        return super.onKeyDown(KeyCode, event);
    }
}
