package am.ze.wookoo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Intent datas = getIntent();
        String filePath = datas.getStringExtra("ImagePath");

        if(filePath != null){
            CircleImageView circleImageView = findViewById(R.id.circle_favorite);
            Picasso.with(getApplicationContext()).load(filePath).into(circleImageView);
        }

        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.favorite_whole_body));
        buttons.add((Button)findViewById(R.id.favorite_upper_body));
        buttons.add((Button)findViewById(R.id.favorite_chest));
        buttons.add((Button)findViewById(R.id.favorite_arm));
        buttons.add((Button)findViewById(R.id.favorite_hips));
        buttons.add((Button)findViewById(R.id.favorite_lower_body));
        buttons.add((Button)findViewById(R.id.favorite_leg));
        buttons.add((Button)findViewById(R.id.favorite_stomach));
        buttons.add((Button)findViewById(R.id.favorite_back));
        buttons.add((Button)findViewById(R.id.favorite_shoulder));
        buttons.add((Button)findViewById(R.id.favorite_pelvis));


        for(final Button btn : buttons){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn.isSelected()){
                        btn.setSelected(false);
                        btn.setTextColor(Color.WHITE);
                    }else{
                        btn.setSelected(true);
                        btn.setTextColor(Color.BLACK);
                    }
                }
            });
        }

    Button done = findViewById(R.id.favorite_done);
    done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(FavoriteActivity.this,AlarmActivity.class));
               finish();
            }
        });



    }
}
