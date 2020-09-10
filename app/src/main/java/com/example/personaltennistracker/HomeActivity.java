package com.example.personaltennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Radar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.radar.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.personaltennistracker.Database.AppDatabase;
import com.example.personaltennistracker.Database.PracticeEntity;
import com.example.personaltennistracker.Database.PracticeWithStrokes;
import com.example.personaltennistracker.Database.StrokeDao;
import com.example.personaltennistracker.Database.StrokeEntity;
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.PracticeSurvey.NewPractice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final UserEntity user = (UserEntity) getIntent().getSerializableExtra("User");
        Boolean signedIn = (Boolean) getIntent().getSerializableExtra("SignedIn");
        if(signedIn != null && signedIn != false){
            Toast.makeText(HomeActivity.this, "Welcome, "+ user.getFirstName(), Toast.LENGTH_SHORT).show();
        }


        /*Charts begin*/
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        /*Add Weekly Bar Chart*/

        //load the weekly data
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Define time range
                Date monday = DateTime.now().withTimeAtStartOfDay().withDayOfWeek(DateTimeConstants.MONDAY).toDate();
                Date now = new Date();

                //query practices for this week
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tennisTracker")
                        .build();
                List<PracticeEntity> thisWeek = db.practiceDao().getPracticesBetween(monday, now);

                //populate weeklyDuration chart
                final AnyChartView weeklyDuration = findViewById(R.id.weeklyDuration);
                final Cartesian barChart = createWeeklyDurationChart(weeklyDuration, thisWeek);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weeklyDuration.setChart(barChart);
                    }
                });

                //populate strokeProgress chart
                List<PracticeWithStrokes> strokesThisWeek = new ArrayList<>();
                for(PracticeEntity p : thisWeek){
                    strokesThisWeek.add(db.practiceDao().getPracticesWithStrokesPracticeId(p.getPracticeId()));
                }
                final AnyChartView strokeProgressChart = findViewById(R.id.strokeProgressChart);
                final Cartesian lineChart = createStrokeProgressChart(strokesThisWeek, strokeProgressChart);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        strokeProgressChart.setChart(lineChart);
                    }
                });

                //populate strengthChart

                //get alltime strokes for user
                List<StrokeEntity> allStrokes = db.userDao().getAllStrokes(user.getUserId());
                final AnyChartView strengthChart = findViewById(R.id.strengthChart);
                final Radar radarChart = createStrengthChart(allStrokes, strengthChart);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        strengthChart.setChart(radarChart);
                        progressBar.setVisibility(View.GONE);
                    }
                });

                //TODO Stroke Tips
                //select the highest rating tip for serve, backhand and forehand
                final StrokeEntity maxForehand = getMaxRating(allStrokes, StrokeDao.StrokeType.FOREHAND);
                final StrokeEntity maxBackhand = getMaxRating(allStrokes, StrokeDao.StrokeType.BACKHAND);
                final StrokeEntity maxServe = getMaxRating(allStrokes, StrokeDao.StrokeType.SERVE);

                //update StrokeTips
                final TextView tip1 = (TextView) findViewById(R.id.tip1);
                final TextView tip2 = (TextView) findViewById(R.id.tip2);
                final TextView tip3 = (TextView) findViewById(R.id.tip3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tip1.setText(maxForehand.getTips());
                        tip2.setText(maxBackhand.getTips());
                        tip3.setText(maxServe.getTips());
                    }
                });
            }
        }).start();


        ExtendedFloatingActionButton newPracticeBtn = (ExtendedFloatingActionButton) findViewById(R.id.newPracticeBtn);
        newPracticeBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent newPracticeIntent = new Intent(HomeActivity.this, NewPractice.class);
                  newPracticeIntent.putExtra("User", user);
                  HomeActivity.this.startActivity(newPracticeIntent);
              }
            }
        );

        ExtendedFloatingActionButton activitiesThisWeek = (ExtendedFloatingActionButton) findViewById(R.id.activitiesThisWeek);
        activitiesThisWeek.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  Intent newPracticeIntent = new Intent(HomeActivity.this, ViewPractices.class);
                                                  newPracticeIntent.putExtra("User", user);
                                                  HomeActivity.this.startActivity(newPracticeIntent);
                                              }
                                          }
        );

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.practicesBottomNav:
                        intent = new Intent(HomeActivity.this, ViewPractices.class);
                        intent.putExtra("User", user);
                        break;
                    case R.id.logoutBottomNav:
                        intent = new Intent(HomeActivity.this, LoginActivity.class);
                        break;
                    default:
                        return false;
                }
                HomeActivity.this.startActivity(intent);
                return true;
            }
        });


    }

    private static Cartesian createWeeklyDurationChart(AnyChartView weeklyDuration, List<PracticeEntity> thisWeek){
        //populate weeklyDuration chart
        APIlib.getInstance().setActiveAnyChartView(weeklyDuration);
        final Cartesian barChart = AnyChart.column();

        List<DataEntry> weekDurationData = new ArrayList<>();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EE");

        //Initialize the chart
        HashMap<String, Double> map = new HashMap<>();
        for(String s : new String[]{"Sun", "Mon", "Tue", "Wed","Thu","Fri","Sat"}){
            map.put(s, 0.0);
        }
        //Merge durations on same day
        for(PracticeEntity p : thisWeek){
            String day = simpleDateformat.format(p.getDate());
            map.put(day, map.get(day)+p.getDuration());
        }

        //convert HashMap to WeeklyDuration
        for(String key : new String[]{"Sun", "Mon", "Tue", "Wed","Thu","Fri","Sat"}){
            weekDurationData.add(new ValueDataEntry(key, map.get(key)));
        }

        Column column = barChart.column(weekDurationData);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value} hrs");

        barChart.animation(true);
        barChart.title("This Week");
        barChart.yScale().minimum(0d);

        barChart.tooltip().positionMode(TooltipPositionMode.POINT);
        barChart.interactivity().hoverMode(HoverMode.BY_X);
        barChart.yAxis(0).title("Hours");

        return barChart;
    }

    private static Cartesian createStrokeProgressChart(List<PracticeWithStrokes> practiceWithStrokes, AnyChartView strokeProgressChart){
        APIlib.getInstance().setActiveAnyChartView(strokeProgressChart);
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title("Stroke Progress");
        cartesian.yAxis(0).title("Rating");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EE");

        HashMap<String, List<Double>> forehandRatings = new HashMap<>();
        HashMap<String, List<Double>> backhandRatings = new HashMap<>();
        HashMap<String, List<Double>> serveRatings = new HashMap<>();

        //initialize
        for(String day : new String[]{"Sun", "Mon", "Tue", "Wed","Thu","Fri","Sat"}){
            forehandRatings.put(day, new ArrayList<Double>());
            backhandRatings.put(day, new ArrayList<Double>());
            serveRatings.put(day, new ArrayList<Double>());
        }

        for(PracticeWithStrokes p : practiceWithStrokes){
            String day = simpleDateformat.format(p.practice.getDate());
            StrokeEntity forehand = StrokeEntity.getStroke(p.strokes, StrokeDao.StrokeType.FOREHAND);
            StrokeEntity backhand = StrokeEntity.getStroke(p.strokes, StrokeDao.StrokeType.BACKHAND);
            StrokeEntity serve = StrokeEntity.getStroke(p.strokes, StrokeDao.StrokeType.SERVE);

            forehandRatings.get(day).add(forehand.getRating());
            backhandRatings.get(day).add(backhand.getRating());
            serveRatings.get(day).add(serve.getRating());
        }

        //populate seriesData
        for(String day : new String[]{"Sun", "Mon", "Tue", "Wed","Thu","Fri","Sat"}){
            //average values, add to seriesData
            seriesData.add(new CustomDataEntry(day, average(forehandRatings.get(day)), average(backhandRatings.get(day)), average(serveRatings.get(day))));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        com.anychart.core.cartesian.series.Line series1 = cartesian.line(series1Mapping);
        series1.name("Forehand");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        com.anychart.core.cartesian.series.Line series2 = cartesian.line(series2Mapping);
        series2.name("Backhand");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        com.anychart.core.cartesian.series.Line series3 = cartesian.line(series3Mapping);
        series3.name("Serve");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        return cartesian;
    }

    private static Radar createStrengthChart(List<StrokeEntity> allStrokes, AnyChartView strengthChart){
        APIlib.getInstance().setActiveAnyChartView(strengthChart);
        Radar radar = AnyChart.radar();
        radar.title("Stroke Analysis");

        radar.yScale().minimum(0d);
        radar.yScale().minimumGap(0d);
        radar.yScale().maximum(5d);
        radar.yScale().ticks().interval(1d);
        radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

        radar.legend()
                .align(Align.CENTER)
                .enabled(true);

        List<DataEntry> strengthData = new ArrayList<>();
        double avgForehand = 0.0;
        double avgBackhand = 0.0;
        double avgServe = 0.0;
        double avgVolley = 0.0;

        for(StrokeEntity stroke : allStrokes){
            switch(stroke.getStrokeType()){
                case FOREHAND:
                    avgForehand += stroke.getRating();
                    break;
                case BACKHAND:
                    avgBackhand += stroke.getRating();
                    break;
                case SERVE:
                    avgServe += stroke.getRating();
                    break;
                case VOLLEY:
                    avgVolley += stroke.getRating();
                    break;
            }
        }

        avgForehand = allStrokes.size() == 0? 0: avgForehand/(allStrokes.size()/4);
        avgBackhand = allStrokes.size() == 0? 0: avgBackhand/(allStrokes.size()/4);
        avgServe = allStrokes.size() == 0? 0: avgServe/(allStrokes.size()/4);
        avgVolley = allStrokes.size() == 0? 0: avgVolley/(allStrokes.size()/4);

        strengthData.add(new ValueDataEntry("Forehand", avgForehand));
        strengthData.add(new ValueDataEntry("Backhand", avgBackhand));
        strengthData.add(new ValueDataEntry("Serve", avgServe));
        strengthData.add(new ValueDataEntry("Volley", avgVolley));

        Set set2 = Set.instantiate();
        set2.data(strengthData);
        Mapping shamanData = set2.mapAs("{ x: 'x', value: 'value' }");

        Line shamanLine = radar.line(shamanData);
        shamanLine.name("Average Rating");
        shamanLine.markers()
                .enabled(true)
                .type(MarkerType.CIRCLE)
                .size(3d);


        radar.tooltip().format("Avg: {%Value}");
        return radar;
    }

    private static double average(List<Double> list){
        if(list.isEmpty())
            return 0.0;
        double sum  = 0.0;
        for(Double d : list)
            sum += d;
        return  sum/list.size();
    }

    private static StrokeEntity getMaxRating(List<StrokeEntity> allStrokes, StrokeDao.StrokeType strokeType){
        StrokeEntity max = null;
        double maxRating = 0.0;
        for(StrokeEntity stroke : allStrokes){
            if(stroke.getStrokeType().equals(strokeType) && stroke.getRating() > maxRating){
                maxRating = stroke.getRating();
                max = stroke;
            }
        }
        return max;
    }


    private static class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }
}