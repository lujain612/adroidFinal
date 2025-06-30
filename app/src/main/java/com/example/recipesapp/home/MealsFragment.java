package com.example.recipesapp.home;

import com.example.recipesapp.ui.meals.MealAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.R;
import com.example.recipesapp.api.RetrofitInstance;
import com.example.recipesapp.model.Meal;
import com.example.recipesapp.model.MealResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealsFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private RecyclerView recyclerView;
    private MealAdapter adapter;

    public static MealsFragment newInstance(String category) {
        MealsFragment fragment = new MealsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        // 🔙 Back button setup
        Button buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        recyclerView = view.findViewById(R.id.recyclerViewMeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            String category = getArguments().getString(ARG_CATEGORY);
            loadMeals(category);
        }

        return view;
    }

    private void loadMeals(String category) {
        RetrofitInstance.getMealService().getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    adapter = new MealAdapter(meals);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnMealClickListener(meal -> {
                        MealDetailsFragment fragment = MealDetailsFragment.newInstance(meal.getIdMeal());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    });

                } else {
                    Toast.makeText(getContext(), "Failed to load meals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
