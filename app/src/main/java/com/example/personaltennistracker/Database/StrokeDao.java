package com.example.personaltennistracker.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StrokeDao {
    enum StrokeType{
        FOREHAND,
        BACKHAND,
        SERVE,
        VOLLEY;
    }

    @Query("SELECT * FROM strokes")
    List<StrokeEntity> getAll();

    @Query("SELECT * FROM strokes WHERE strokeId IN (:strokeIds)")
    List<StrokeEntity> loadAllByIds(int[] strokeIds);

    @Query("SELECT * FROM strokes WHERE strokeId LIKE :strokeId LIMIT 1")
    StrokeEntity findById(int strokeId);

    @Query("SELECT * FROM strokes WHERE practiceId IN (:practiceIds)")
    List<StrokeEntity> loadAllByPracticeIds(int[] practiceIds);

    @Query("SELECT * FROM strokes WHERE practiceId LIKE :practiceId")
    List<StrokeEntity> loadAllByPracticeId(int practiceId);

    @Insert
    void insertAll(StrokeEntity... strokeEntities);

    @Delete
    void delete(StrokeEntity strokeEntity);


    
}
