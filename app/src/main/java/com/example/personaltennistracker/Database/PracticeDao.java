package com.example.personaltennistracker.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

@Dao
public interface PracticeDao {
    @Query("SELECT * FROM practices")
    List<PracticeEntity> getAll();

    @Query("SELECT * FROM practices WHERE practiceId IN (:practiceIds)")
    List<PracticeEntity> loadAllByIds(int[] practiceIds);

    @Query("SELECT * FROM practices WHERE practiceId LIKE :id LIMIT 1")
    PracticeEntity findById(int id);

    @Query("SELECT * FROM practices WHERE userId LIKE :id")
    List<PracticeEntity> findByUserId(int id);

    @Query("SELECT * FROM practices WHERE date BETWEEN :start AND :end")
    List<PracticeEntity> getPracticesBetween(Date start, Date end);

    @Transaction
    @Query("SELECT * FROM practices")
    List<PracticeWithStrokes> getPracticesWithStrokes();

    @Transaction
    @Query("SELECT * FROM practices WHERE practiceId LIKE :practiceId LIMIT 1")
    PracticeWithStrokes getPracticesWithStrokesPracticeId(int practiceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(PracticeEntity practiceEntity);

    @Insert
    void insertAll(PracticeEntity... practiceEntities);

    @Delete
    void delete(PracticeEntity practiceEntity);
}
