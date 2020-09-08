package com.example.personaltennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import com.example.personaltennistracker.Database.UserEntity;
import com.example.personaltennistracker.PracticeSurvey.NewPractice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        /*Add Weekly Bar Chart*/

        //load the weekly data
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Define time range
                Date monday = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY).toDate();
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
            }
        }).start();



        /*Stroke Progress*/
        AnyChartView strokeProgressChart = findViewById(R.id.strokeProgressChart);
        APIlib.getInstance().setActiveAnyChartView(strokeProgressChart);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Stroke Progress");

        cartesian.yAxis(0).title("Rating");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
        seriesData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
        seriesData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
        seriesData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
        seriesData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
        seriesData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
        seriesData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
        seriesData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
        seriesData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
        seriesData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
        seriesData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
        seriesData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
        seriesData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
        seriesData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
        seriesData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
        seriesData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
        seriesData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
        seriesData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
        seriesData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
        seriesData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
        seriesData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
        seriesData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
        seriesData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
        seriesData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));

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

        strokeProgressChart.setChart(cartesian);


        /*End of stroke progress*/




        /*Add stroke radar chart*/
        AnyChartView strengthChart = findViewById(R.id.strengthChart);
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
        strengthData.add(new ValueDataEntry("Forehand", 3.79));
        strengthData.add(new ValueDataEntry("Backhand", 2.014));
        strengthData.add(new ValueDataEntry("Serve", 1.4));
        strengthData.add(new ValueDataEntry("Volley", 4.7));

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

        strengthChart.setChart(radar);

        /*End of chart*/
        progressBar.setVisibility(View.GONE);




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

    private static Cartesian createStrokeProgressChart(){
        return null;
    }

    private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }
}