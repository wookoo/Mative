package am.ze.wookoo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


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

        mView.setSelectionColor(Color.parseColor("#B01717"));
        mView.setSelectedDate(CalendarDay.from(2019,7,10));
        mView.setDateSelected(CalendarDay.from(2019,7,15),true);
        mView.setSelectionColor(Color.parseColor("#FFF000"));
        mView.setDateSelected(CalendarDay.from(2019,7,16),true);

    }

}
