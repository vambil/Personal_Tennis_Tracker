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

public class Backhand extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backhand);

        //TODO unpack practice
        final PracticeEntity practice = (PracticeEntity) getIntent().getSerializableExtra("Practice");
        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");
        final StrokeEntity forehand = (StrokeEntity) getIntent().getSerializableExtra("Forehand");

        MaterialButton toServeBtn =  (MaterialButton) findViewById(R.id.toServeBtn);

        toServeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slider backhandSlider = (Slider) findViewById(R.id.backhandRating);
                double backhandRating = (double) backhandSlider.getValue();
                EditText backhandDidWellInput = (TextInputEditText) findViewById(R.id.backhandDidWell);
                String backhandDidWell = backhandDidWellInput.getText().toString();
                EditText backhandDidBadInput = (TextInputEditText) findViewById(R.id.backhandDidBad);
                String backhandDidBad = backhandDidBadInput.getText().toString();
                EditText backhandTipsInput = (TextInputEditText) findViewById(R.id.backhandTips);
                String backhandTips = backhandTipsInput.getText().toString();
                final StrokeEntity backhand = new StrokeEntity(practice.getPracticeId(), StrokeDao.StrokeType.BACKHAND, backhandRating, backhandDidWell, backhandDidBad, backhandTips);

                Intent intent = new Intent(Backhand.this, Serve.class);
                intent.putExtra("User", user);
                intent.putExtra("Practice", practice);
                intent.putExtra("Forehand", forehand);
                intent.putExtra("Backhand", backhand);
                Backhand.this.startActivity(intent);
            }
        });
    }
}
