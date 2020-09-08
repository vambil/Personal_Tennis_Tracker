package com.example.personaltennistracker.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;

@Entity(tableName = "strokes",
        foreignKeys = @ForeignKey(entity = PracticeEntity.class,
        parentColumns = "practiceId",
        childColumns = "practiceId",
        onDelete = ForeignKey.CASCADE),
    indices = {@Index("practiceId")})
public class StrokeEntity implements Serializable, Comparable<StrokeEntity>{
    @PrimaryKey(autoGenerate = true)
    public int strokeId;
    public int practiceId;
    public StrokeDao.StrokeType strokeType;
    public double rating;
    public String didWell;
    public String didBad;
    public String tips;


    public StrokeEntity(int practiceId, StrokeDao.StrokeType strokeType, double rating, String didWell, String didBad, String tips) {
        this.practiceId = practiceId;
        this.strokeType = strokeType;
        this.rating = rating;
        this.didWell = didWell;
        this.didBad = didBad;
        this.tips = tips;
    }

    public int getStrokeId() {
        return strokeId;
    }

    public void setStrokeId(int strokeId) {
        this.strokeId = strokeId;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public StrokeDao.StrokeType getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(StrokeDao.StrokeType strokeType) {
        this.strokeType = strokeType;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDidWell() {
        return didWell;
    }

    public void setDidWell(String didWell) {
        this.didWell = didWell;
    }

    public String getDidBad() {
        return didBad;
    }

    public void setDidBad(String didBad) {
        this.didBad = didBad;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public int compareTo(StrokeEntity strokeEntity) {
        return (int) Math.ceil(this.getRating() - strokeEntity.getRating());
    }
}
