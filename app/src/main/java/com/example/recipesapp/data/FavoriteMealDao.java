package com.example.recipesapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.recipesapp.model.FavoriteMeal;

import java.util.List;

@Dao
public interface FavoriteMealDao {
    @Query("SELECT * FROM favorite_meals")
    List<FavoriteMeal> getAllFavorites();

    @Insert
    void insert(FavoriteMeal meal);

    @Delete
    void delete(FavoriteMeal meal);
    @Query("SELECT * FROM favorite_meals WHERE idMeal = :mealId LIMIT 1")
    FavoriteMeal getFavoriteById(String mealId);

    @Query("DELETE FROM favorite_meals")
    void deleteAllFavorites();
}
