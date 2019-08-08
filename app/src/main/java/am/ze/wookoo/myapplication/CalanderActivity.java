package am.ze.wookoo.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class CalanderActivity extends AppCompatActivity {
    MaterialCalendarView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        Button mMainBtn = findViewById(R.id.award_main);
        mMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button mAward = findViewById(R.id.calander_award);
        mAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalanderActivity.this, AwardBeforeActivity.class));
                finish();
            }
        });

        mView = findViewById(R.id.calendarView);




        List<CalendarDay>  failList = new ArrayList<>();

        List<CalendarDay> successLIst = new ArrayList<>();


        failList.add(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), 1 ));
        failList.add(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), 3 ));

        for (int i = CalendarDay.today().getMonth()-1; i >= 0; i -- ){
            for(int k = 1; k < 27; k++){
                double ran = Math.random();
                if (ran < 0.3){
                    successLIst.add(CalendarDay.from(CalendarDay.today().getYear(),i,k));
                }
            }
        }

        for (int i = CalendarDay.today().getMonth(); i >= 0; i -- ){
            for(int k = 1; k < CalendarDay.today().getDay(); k++){
                if(k == 1 || k == 3){
                    continue;
                }
                double ran = Math.random();
                if (ran < 0.5){
                    successLIst.add(CalendarDay.from(CalendarDay.today().getYear(),i,k));
                }
            }
        }


        //파란색 4F8BF6 보라색 B880F7
        mView.addDecorator(new FailEventDecorator(Color.parseColor("#B880F7"),failList,CalanderActivity.this));

        mView.addDecorator(new EventDecorator(Color.parseColor("#4F8BF6"),successLIst,CalanderActivity.this));

        mView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);

        final LinearLayout mFail = findViewById(R.id.fail);

        mView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                if(date.getMonth() != CalendarDay.today().getMonth()){
                    mFail.setVisibility(View.INVISIBLE);
                }else{
                    mFail.setVisibility(View.VISIBLE);
                }
            //    Toast.makeText(CalanderActivity.this,"" + date.getMonth() + "/" + CalendarDay.today().getMonth(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        if(KeyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(CalanderActivity.this,"5초후 알람이 울립니다." ,Toast.LENGTH_SHORT).show();
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
                    Intent my_intent = new Intent(CalanderActivity.this,Alarm_Reciver.class);


                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.SECOND,0);



                    // reveiver에 string 값 넘겨주기
                    my_intent.putExtra("state","alarm on");

                    PendingIntent pendingIntent;

                    pendingIntent = PendingIntent.getBroadcast(CalanderActivity.this, 0, my_intent,
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
