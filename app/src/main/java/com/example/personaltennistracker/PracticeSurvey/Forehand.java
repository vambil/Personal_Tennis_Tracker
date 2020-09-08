package com.example.personaltennistracker.PracticeSurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personaltennistracker.Database.PracticeEntity;
import com.example.personaltennistracker.Database.StrokeDao;
import com.example.personaltennistracker.Database.StrokeEntity;
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

public class Forehand extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forehand);

        //TODO unpack practice
        final PracticeEntity practice = (PracticeEntity) getIntent().getSerializableExtra("Practice");
        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");

        //forehand page
        MaterialButton toBackhandBtn =  (MaterialButton) findViewById(R.id.toBackhandBtn);

        toBackhandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slider forehandSlider = (Slider) findViewById(R.id.forehandRating);
                double forehandRating = (double) forehandSlider.getValue();
                EditText forehandDidWellInput = (TextInputEditText) findViewById(R.id.forehandDidWell);
                String forehandDidWell = forehandDidWellInput.getText().toString();
                EditText forehandDidBadInput = (TextInputEditText) findViewById(R.id.forehandDidBad);
                String forehandDidBad = forehandDidBadInput.getText().toString();
                EditText forehandTipsInput = (TextInputEditText) findViewById(R.id.forehandTips);
                String forehandTips = forehandTipsInput.getText().toString();
                final StrokeEntity forehand = new StrokeEntity(practice.getPracticeId(), StrokeDao.StrokeType.FOREHAND, forehandRating, forehandDidWell, forehandDidBad, forehandTips);

                Intent intent = new Intent(Forehand.this, Backhand.class);
                intent.putExtra("Forehand", forehand);
                intent.putExtra("User", user);
                intent.putExtra("Practice", practice);
                Forehand.this.startActivity(intent);
            }
        });

    }
}
