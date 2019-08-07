package am.ze.wookoo.myapplication;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import me.relex.circleindicator.CircleIndicator;

public class ReadyActivity extends AppCompatActivity {
    int page = 1;
    private BluetoothSPP bt;
    // final String Address = "50:77:05:43:72:FA";
    String Address = "00:18:E4:35:65:8D";
    boolean stoped = true;
    int mainTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        TextView mTime = findViewById(R.id.time);
        TextView mPm = findViewById(R.id.pm);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String strNow = sdf.format(date);

        SimpleDateFormat temp = new SimpleDateFormat("aa");
        String amPm = "AM";
        String strAmPM = temp.format(date);
        if(strAmPM.equals("오후")){
            amPm = "PM";
        }

        mTime.setText(strNow);
        mPm.setText(amPm);
        Log.d("AMPM",amPm);


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

            }

            public void onDeviceDisconnected() { //연결해제
          //      Toast.makeText(getApplicationContext()
            //            , "매트와 연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
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
                if(message.equals("y") && stoped)  {
                    stoped = false;


                    Intent my_intent = new Intent(ReadyActivity.this,Alarm_Reciver.class);
                    my_intent.putExtra("state","alarm off");
                    sendBroadcast(my_intent);

                    startActivity(new Intent(ReadyActivity.this,TodayBeforeActivity.class ));
                    finish();
                    }

                }

                //Toast.makeText(ExerciseActivity.this, "받아온 값  :  " + message + " 스트링 길이 : " + message.length(), Toast.LENGTH_SHORT).show();

        });

        Button mPass = findViewById(R.id.pass);
        mPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_intent = new Intent(ReadyActivity.this,Alarm_Reciver.class);
                my_intent.putExtra("state","alarm off");
                sendBroadcast(my_intent);

                startActivity(new Intent(ReadyActivity.this,RecordActivity.class ));
                finish();
            }
        });



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



}
