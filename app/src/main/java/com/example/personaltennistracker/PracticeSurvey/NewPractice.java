package com.example.personaltennistracker.PracticeSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.personaltennistracker.Database.PracticeEntity;
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import java.util.Date;

public class NewPractice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_practice);

        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");

        //new practice page
        MaterialButton toForehandBtn =  (MaterialButton) findViewById(R.id.toForehandBtn);

        toForehandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = user.getUserId();
                Slider durationSlider = (Slider) findViewById(R.id.practiceDuration);
                double duration= (double) durationSlider.getValue();
                TextView title = (TextView) findViewById(R.id.titleOfPractice);
                final PracticeEntity practice = new PracticeEntity(userId, duration, title.getText().toString(), new Date());
                //TODO Add media file uploads

                Intent intent = new Intent(NewPractice.this, Forehand.class);
                intent.putExtra("User", user);
                intent.putExtra("Practice", practice);
                NewPractice.this.startActivity(intent);
            }
        });

    }
}