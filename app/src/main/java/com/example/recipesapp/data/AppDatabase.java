package com.example.recipesapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipesapp.model.FavoriteMeal;

@Database(entities = {FavoriteMeal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract FavoriteMealDao favoriteMealDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "meals_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
