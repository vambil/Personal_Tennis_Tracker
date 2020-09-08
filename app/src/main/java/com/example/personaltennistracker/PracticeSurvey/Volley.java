package com.example.personaltennistracker.PracticeSurvey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.personaltennistracker.Database.AppDatabase;
import com.example.personaltennistracker.Database.PracticeEntity;
import com.example.personaltennistracker.Database.StrokeDao;
import com.example.personaltennistracker.Database.StrokeEntity;
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.R;
import com.example.personaltennistracker.ViewPractices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

public class Volley extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley);

        //TODO unpack practice
        final PracticeEntity practice = (PracticeEntity) getIntent().getSerializableExtra("Practice");
        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");
        final StrokeEntity forehand = (StrokeEntity) getIntent().getSerializableExtra("Forehand");
        final StrokeEntity backhand = (StrokeEntity) getIntent().getSerializableExtra("Backhand");
        final StrokeEntity serve = (StrokeEntity) getIntent().getSerializableExtra("Serve");

        MaterialButton savePracticeBtn =  (MaterialButton) findViewById(R.id.savePracticeBtn);


        savePracticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slider volleySlider = (Slider) findViewById(R.id.volleyRating);
                double volleyRating = (double) volleySlider.getValue();
                EditText volleyDidWellInput = (TextInputEditText) findViewById(R.id.volleyDidWell);
                String volleyDidWell = volleyDidWellInput.getText().toString();
                EditText volleyDidBadInput = (TextInputEditText) findViewById(R.id.volleyDidBad);
                String volleyDidBad = volleyDidBadInput.getText().toString();
                EditText volleyTipsInput = (TextInputEditText) findViewById(R.id.volleyTips);
                String volleyTips = volleyTipsInput.getText().toString();
                final StrokeEntity volley = new StrokeEntity(practice.getPracticeId(), StrokeDao.StrokeType.VOLLEY, volleyRating, volleyDidWell, volleyDidBad, volleyTips);



                //TODO upload to Room db
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tennisTracker")
                                .build();
                        //run db queries on new thread
                        int practiceId = (int) db.practiceDao().insertOne(practice);
                        //set practiceId for strokes
                        forehand.setPracticeId(practiceId);
                        backhand.setPracticeId(practiceId);
                        serve.setPracticeId(practiceId);
                        volley.setPracticeId(practiceId);

                        db.strokeDao().insertAll(forehand, backhand, serve, volley);
                    }
                }).start();



                //Toast practice saved
                Toast.makeText(Volley.this, "New Practice Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Volley.this, ViewPractices.class);
                intent.putExtra("User", user);
                Volley.this.startActivity(intent); //move back to viewPractices
            }
        });

    }
}
