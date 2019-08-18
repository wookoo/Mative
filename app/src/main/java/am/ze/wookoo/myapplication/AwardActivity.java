package am.ze.wookoo.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AwardActivity extends AppCompatActivity {
    ViewPager vpPager;
    FragmentPagerAdapter adapterViewPager;
    Button AchieveBtn;
    Button AllBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        Button mMain = findViewById(R.id.award_main);
        mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vpPager = findViewById(R.id.vppager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        AchieveBtn = findViewById(R.id.achieved_btn);
        AllBtn = findViewById(R.id.all_btn);



        AchieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpPager.setCurrentItem(0);
                adapterViewPager.notifyDataSetChanged();
                AchieveBtn.setSelected(true);
                AchieveBtn.setTextColor(Color.parseColor("#B880F7"));

                AllBtn.setSelected(false);
                AllBtn.setTextColor(Color.WHITE);
            }
        });

        AllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpPager.setCurrentItem(1);
                adapterViewPager.notifyDataSetChanged();
                AchieveBtn.setSelected(false);
                AchieveBtn.setTextColor(Color.WHITE);
                AllBtn.setSelected(true);
                AllBtn.setTextColor(Color.parseColor("#B880F7"));
            }
        });

        AchieveBtn.callOnClick();

        vpPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        }); //스크롤 잠금



        Button mCalander = findViewById(R.id.award_calander);
        mCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AwardActivity.this,CalanderBeforeActivity.class));
                finish();
            }
        });

    }
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        if(KeyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(AwardActivity.this,"5초후 알람이 울립니다." ,Toast.LENGTH_SHORT).show();
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
                    Intent my_intent = new Intent(AwardActivity.this,Alarm_Reciver.class);


                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.SECOND,0);

                    my_intent.putExtra("state","alarm on");

                    PendingIntent pendingIntent;

                    pendingIntent = PendingIntent.getBroadcast(AwardActivity.this, 0, my_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

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
    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private  static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            Log.d("아이템",""+position);
            switch (position) {
                case 0:
                    return FirstAward.newInstance(0, "Page # 1");

                case 1:

                    return SecondAward.newInstance(1, "Page # 2");
                default:
                    return null;
            }
        }
    }

}
