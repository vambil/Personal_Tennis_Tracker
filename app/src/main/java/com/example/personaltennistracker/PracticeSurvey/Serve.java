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

public class Serve extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serve);

        //TODO unpack practice
        final PracticeEntity practice = (PracticeEntity) getIntent().getSerializableExtra("Practice");
        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");
        final StrokeEntity forehand = (StrokeEntity) getIntent().getSerializableExtra("Forehand");
        final StrokeEntity backhand = (StrokeEntity) getIntent().getSerializableExtra("Backhand");

        MaterialButton toVolleyBtn =  (MaterialButton) findViewById(R.id.toVolleyBtn);

        toVolleyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slider serveSlider = (Slider) findViewById(R.id.serveRating);
                double serveRating = (double) serveSlider.getValue();
                EditText serveDidWellInput = (TextInputEditText) findViewById(R.id.serveDidWell);
                String serveDidWell = serveDidWellInput.getText().toString();
                EditText serveDidBadInput = (TextInputEditText) findViewById(R.id.serveDidBad);
                String serveDidBad = serveDidBadInput.getText().toString();
                EditText serveTipsInput = (TextInputEditText) findViewById(R.id.serveTips);
                String serveTips = serveTipsInput.getText().toString();
                final StrokeEntity serve = new StrokeEntity(practice.getPracticeId(), StrokeDao.StrokeType.SERVE, serveRating, serveDidWell, serveDidBad, serveTips);

                Intent intent = new Intent(Serve.this, Volley.class);
                intent.putExtra("User", user);
                intent.putExtra("Practice", practice);
                intent.putExtra("Forehand", forehand);
                intent.putExtra("Backhand", backhand);
                intent.putExtra("Serve", serve);
                Serve.this.startActivity(intent);
            }
        });

    }
}
