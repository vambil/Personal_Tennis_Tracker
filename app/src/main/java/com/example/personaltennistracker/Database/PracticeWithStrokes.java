package com.example.personaltennistracker.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PracticeWithStrokes {
    @Embedded public PracticeEntity practice;
    @Relation(
            parentColumn = "practiceId",
            entityColumn = "practiceId"
    )
    public List<StrokeEntity> strokes;
}
