package com.example.recipesapp.api;

import com.example.recipesapp.model.CategoryResponse;
import com.example.recipesapp.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {


    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Call<MealResponse> getMealDetails(@Query("i") String mealId);
}
