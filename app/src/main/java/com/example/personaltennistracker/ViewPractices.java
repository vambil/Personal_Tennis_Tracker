package com.example.personaltennistracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.personaltennistracker.Database.AppDatabase;
import com.example.personaltennistracker.Database.PracticeEntity;
import com.example.personaltennistracker.Database.PracticeWithStrokes;
import com.example.personaltennistracker.Database.StrokeEntity;
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.PracticeSurvey.NewPractice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPractices extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> tips = new ArrayList<>();
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_practices);

        user = (UserEntity) getIntent().getSerializableExtra("User");

        //dump database

        //populate recycleView
        this.loadCards();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.homeBottomNav:
                        intent = new Intent(ViewPractices.this, HomeActivity.class);
                        intent.putExtra("User", user);
                        break;
                    case R.id.logoutBottomNav:
                        intent = new Intent(ViewPractices.this, LoginActivity.class);
                        break;
                    default:
                        return false;
                }
                ViewPractices.this.startActivity(intent);
                return true;
            }
        });

        ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.newPracticeBtn);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPractice = new Intent(ViewPractices.this, NewPractice.class);
                newPractice.putExtra("User", user);
                ViewPractices.this.startActivity(newPractice);
            }
        });
    }

    private void loadCards(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tennisTracker")
                .allowMainThreadQueries()
                .build();

        //get all practices for this user
        List<PracticeEntity> practiceList = db.practiceDao().findByUserId(user.getUserId());
        Collections.sort(practiceList, new Comparator<PracticeEntity>() {
            @Override
            public int compare(PracticeEntity p1, PracticeEntity p2) {
                return p2.getDate().compareTo(p1.getDate()); //sort reverse chronological
            }
        });

        for (PracticeEntity p: practiceList) {
            titles.add(p.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy h:mm a");
            dates.add(format.format(p.getDate()));
            durations.add(p.getDuration()+" hrs");

            //find stroke with highest rating, display tips
            PracticeWithStrokes practiceWithStrokes = db.practiceDao().getPracticesWithStrokesPracticeId(p.getPracticeId());
            List<StrokeEntity> strokes = practiceWithStrokes.strokes;

            tips.add(Collections.max(strokes).getTips());
        }

        loadRecyclerView();
    }

    private void loadRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, dates, durations, tips, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}