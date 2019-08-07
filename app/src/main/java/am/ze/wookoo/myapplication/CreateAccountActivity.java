package am.ze.wookoo.myapplication;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.security.Permission;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccountActivity extends AppCompatActivity {
    CircleImageView but;
    private File tempFile;
    private String filePath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        tedPermission();
        final EditText name = findViewById(R.id.register_name);
        final EditText age = findViewById(R.id.register_age);
        final EditText weight = findViewById(R.id.register_weight);

        Button mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CreateAccountActivity.this,"자동 실행 버튼 입니다. 제작중",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(CreateAccountActivity.this,ExerciseActivity.class));
                finish();
            }
        });





        Button doneButton = findViewById(R.id.register_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();

                sb.append("INSERT INTO user (NAME, AGE, WEIGHT,ImageURL) VALUES (");

                sb.append("'"+ name.getText()+"',");
                sb.append("'"+ age.getText()+"',");
                sb.append("'"+ weight.getText()+"',");



                Intent intent = new Intent(CreateAccountActivity.this,FavoriteActivity.class);


                if(filePath != null ){
                    intent.putExtra("ImagePath",filePath);
                    sb.append("'"+ filePath +"')");

                }else{
                    sb.append("'null')");
                }
/*
                SQLiteDatabase mDB;
                DBHelper dbHelper = new DBHelper(CreateAccountActivity.this);
                mDB = dbHelper.getWritableDatabase();
                mDB.execSQL(sb.toString());*/
                startActivity(intent);
                finish();
            }
        });

        but = (CircleImageView) findViewById(R.id.button5);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateAccountActivity.this,"사진을 선택하세요.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,1);

            }
        });

        ArrayList<EditText> editTexts = new ArrayList<EditText>();
        editTexts.add((EditText)findViewById(R.id.register_age));
        editTexts.add((EditText)findViewById(R.id.register_gender));
        editTexts.add((EditText)findViewById(R.id.register_name));
        editTexts.add((EditText)findViewById(R.id.register_weight));

        for(final EditText m_text : editTexts){
            m_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(m_text.getText().length() !=0){
                        m_text.setSelected(true);
                        m_text.setTextColor(Color.BLACK);
                    }
                    else{
                        m_text.setSelected(false);
                        m_text.setTextColor(Color.WHITE);
                    }

                }
            });
        }



    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try{
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri !=null;
                cursor = getContentResolver().query(photoUri,proj,null,null,null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        filePath = "file://"+tempFile.getAbsolutePath();
      //  Log.d("이미지 URL", "file:/" + tempFile.getAbsolutePath());
        Picasso.with(getApplicationContext()).load(filePath).into(but);

    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("프로필 사진을 선택하기 위해선 사진 권한이 필요합니다.")
                .setDeniedMessage("프로필 사진을 선택하기 위해선 사진 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

}
