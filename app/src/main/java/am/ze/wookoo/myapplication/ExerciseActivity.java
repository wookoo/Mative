package am.ze.wookoo.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import me.relex.circleindicator.CircleIndicator;

public class ExerciseActivity extends AppCompatActivity {

    int page = 1;
    private BluetoothSPP bt;
   // final String Address = "50:77:05:43:72:FA";
    String Address = "00:18:E4:35:65:8D";
    int mainTime = 30;
    TextView mTimer;
    Timer Timer;
    ViewPager vpPager;
    FragmentPagerAdapter adapterViewPager;
    TimerTask task;
    boolean pressed = false;

    TextView mFirst;
    TextView mSecond;
    TextView mThird;

    boolean stoped = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mFirst = findViewById(R.id.first);
        mSecond = findViewById(R.id.second);
        mThird = findViewById(R.id.third);



        vpPager= findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = findViewById(R.id.circleIndicator);
        indicator.setViewPager(vpPager);

        //바꾸는거
      //  vpPager.setCurrentItem(1);
       // adapterViewPager.notifyDataSetChanged();

        mTimer = findViewById(R.id.TimerView);


        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "매트에 연결되었습니다.",Toast.LENGTH_SHORT).show();

                bt.send("110100\n", false);
                //시작 함수 실행
                runTimer();

            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "매트와 연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "매트에 연결할수 없습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                message = message.trim();

                //Log.d("블루투스 수신",message);
                if(message.equals("y")){

                    /*
                    if(stoped){
                        Intent my_intent = new Intent(ExerciseActivity.this,Alarm_Reciver.class);
                        my_intent.putExtra("state","alarm off");
                        sendBroadcast(my_intent);
                        stoped = false;
                    }*/



                    pressed = true;
                }
                else if (message.equals("n")){
                    pressed = false;
                }
                //Toast.makeText(ExerciseActivity.this, "받아온 값  :  " + message + " 스트링 길이 : " + message.length(), Toast.LENGTH_SHORT).show();
            }
        });



        vpPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        }); //스크롤 잠금

        //Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        //startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return FirstFragment.newInstance(0, "Page # 1");
                case 1:
                    return SecondFragment.newInstance(1, "Page # 2");
                case 2:
                    return ThirdFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
            }
            bt.connect(Address);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void runTimer(){
        Timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                while(!pressed){
                    Log.d("오류","와일 수행중");
                }
                pressed = false;


                mainTime --;
                int min = mainTime / 60;
                int sec = mainTime % 60;
                String strTime = String.format("%02d",sec);
                mTimer.setText(strTime);

                if(mainTime <= 0){

                    mainTime = 30;

                    mTimer.setText("30");
                    if(page <=2){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1){
                                    bt.send("110001\n", false);
                                    mFirst.setText("Donky Kick");
                                    mSecond.setText("Push up");
                                    mThird.setText("squat");
                                }else if (page == 2){
                                    bt.send("001001\n", false);
                                    mFirst.setText("Push up");
                                    mSecond.setText("Split Squat");
                                    mThird.setText("");
                                }
                                vpPager.setCurrentItem(page ++);
                                adapterViewPager.notifyDataSetChanged();

                            }
                        });


                    }
                    else{
                        bt.send("000000", false);
                        Timer.cancel();
                        startActivity(new Intent(ExerciseActivity.this, RecordActivity.class));
                        finish();
                    }
                }
            }
        };


        Timer.schedule(task,0,300);

    }


}
