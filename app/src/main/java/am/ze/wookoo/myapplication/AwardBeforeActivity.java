package am.ze.wookoo.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AwardBeforeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_before);
        ImageView mView = (ImageView)findViewById(R.id.award_img);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(AwardBeforeActivity.this,AwardActivity.class));
              finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        if(KeyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(AwardBeforeActivity.this,"5초후 알람이 울립니다." ,Toast.LENGTH_SHORT).show();
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
                    Intent my_intent = new Intent(AwardBeforeActivity.this,Alarm_Reciver.class);


                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.SECOND,0);



                    // reveiver에 string 값 넘겨주기
                    my_intent.putExtra("state","alarm on");

                    PendingIntent pendingIntent;

                    pendingIntent = PendingIntent.getBroadcast(AwardBeforeActivity.this, 0, my_intent,
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
