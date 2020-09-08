package com.example.personaltennistracker.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "practices")
public class PracticeEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int practiceId;
    public int userId; //reference to UserEntity
    public double duration; //minutes
    public String title;
    //TODO: add media
    public Date date;

    public PracticeEntity(int userId, double duration, String title, Date date) {
        this.userId = userId;
        this.duration = duration;
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PracticeEntity{" +
                "practiceId=" + practiceId +
                ", userId=" + userId +
                ", duration=" + duration +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
