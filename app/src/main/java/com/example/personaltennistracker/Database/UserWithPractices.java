package com.example.personaltennistracker.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithPractices {
    @Embedded public UserEntity user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    public List<PracticeEntity> practices;

}
