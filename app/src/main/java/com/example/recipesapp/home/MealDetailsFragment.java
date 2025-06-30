package com.example.recipesapp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.api.RetrofitInstance;
import com.example.recipesapp.model.Meal;
import com.example.recipesapp.model.MealResponse;

import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.recipesapp.data.AppDatabase;
import com.example.recipesapp.model.FavoriteMeal;

public class MealDetailsFragment extends Fragment {

    private static final String ARG_MEAL_ID = "meal_id";
    private String mealId;

    private ImageView imageMeal;
    private TextView textMealName, textInstructions, textIngredients;
    private Button buttonBack;
    private Meal meal;
    private Button  buttonSaveFavorite;

    public static MealDetailsFragment newInstance(String mealId) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEAL_ID, mealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getString(ARG_MEAL_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        imageMeal = view.findViewById(R.id.imageMeal);
        textMealName = view.findViewById(R.id.textMealName);
        textInstructions = view.findViewById(R.id.textInstructions);
        textIngredients = view.findViewById(R.id.textIngredients);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonSaveFavorite = view.findViewById(R.id.buttonSaveFavorite);

        buttonBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        buttonSaveFavorite.setOnClickListener(v -> {
            if (meal == null) return;

            FavoriteMeal favoriteMeal = new FavoriteMeal(
                    meal.getIdMeal(),
                    meal.getStrMeal(),
                    meal.getStrMealThumb()
            );

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());

                // Check if this meal is already in the database
                FavoriteMeal existing = db.favoriteMealDao().getFavoriteById(meal.getIdMeal());
                if (existing == null) {
                    db.favoriteMealDao().insert(favoriteMeal);

                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Saved to Favorites!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Already in Favorites", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });


        loadMealDetails();

        return view;
    }

    private void loadMealDetails() {
        RetrofitInstance.getMealService().getMealDetails(mealId).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    meal = response.body().getMeals().get(0);

                    textMealName.setText(meal.getStrMeal());
                    textInstructions.setText(meal.getStrInstructions());

                    Glide.with(requireContext())
                            .load(meal.getStrMealThumb())
                            .into(imageMeal);

                    StringBuilder ingredientsBuilder = new StringBuilder();
                    try {
                        for (int i = 1; i <= 20; i++) {
                            Method ingredientMethod = Meal.class.getMethod("getStrIngredient" + i);
                            Method measureMethod = Meal.class.getMethod("getStrMeasure" + i);

                            String ingredient = (String) ingredientMethod.invoke(meal);
                            String measure = (String) measureMethod.invoke(meal);

                            if (ingredient != null && !ingredient.isEmpty() && !ingredient.equals("null")) {
                                ingredientsBuilder.append("• ").append(ingredient);
                                if (measure != null && !measure.isEmpty() && !measure.equals("null")) {
                                    ingredientsBuilder.append(" - ").append(measure);
                                }
                                ingredientsBuilder.append("\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    textIngredients.setText(ingredientsBuilder.toString());

                } else {
                    Toast.makeText(getContext(), "Meal not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
