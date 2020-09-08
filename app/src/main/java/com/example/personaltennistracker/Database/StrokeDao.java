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

    @Query("SELECT * FROM strokes WHERE strokeId LIKE :id LIMIT 1")
    StrokeEntity findById(int id);

    @Insert
    void insertAll(StrokeEntity... strokeEntities);

    @Delete
    void delete(StrokeEntity strokeEntity);
    
}
