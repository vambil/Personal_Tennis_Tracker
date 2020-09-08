package com.example.personaltennistracker.Database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDao {
    enum UserErrors {
        BLANK_FIELDS,
        PASSWORDS_NO_MATCH,
        EMAIL_EXISTS,
        UNKNOWN_ERROR,
        NO_ERROR;
    }

    @Query("SELECT * FROM users")
    List<UserEntity> getAll();

    @Query("SELECT * FROM users WHERE userId IN (:userIds)")
    List<UserEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE firstName LIKE :first AND lastName LIKE :last LIMIT 1")
    UserEntity findByName(String first, String last);

    @Query("SELECT * FROM users WHERE email LIKE :email LIMIT 1")
    UserEntity findByEmail(String email);

    @Query("SELECT * FROM users WHERE email LIKE :email AND password LIKE :password LIMIT 1")
    UserEntity validateLogin(String email, String password);

    @Transaction
    @Query("SELECT * FROM users")
    List<UserWithPractices> getUsersWithPractices();

    @Insert
    void insertAll(UserEntity... users);

    @Delete
    void delete(UserEntity user);

}
