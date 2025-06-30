package com.example.recipesapp.home;

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
import com.example.recipesapp.data.AppDatabase;
import com.example.recipesapp.model.FavoriteMeal;
import com.example.recipesapp.ui.favorites.FavoriteMealAdapter;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteMealAdapter adapter;

    public FavoritesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🔙 Back button
        Button buttonBack = view.findViewById(R.id.buttonBackFavorites);
        buttonBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        new Thread(() -> {
            List<FavoriteMeal> favoriteMeals = AppDatabase
                    .getInstance(requireContext())
                    .favoriteMealDao()
                    .getAllFavorites();

            requireActivity().runOnUiThread(() -> {
                if (favoriteMeals.isEmpty()) {
                    Toast.makeText(getContext(), "No favorites yet", Toast.LENGTH_SHORT).show();
                }

                adapter = new FavoriteMealAdapter(favoriteMeals);
                recyclerView.setAdapter(adapter);

                adapter.setOnFavoriteClickListener(meal -> {
                    MealDetailsFragment fragment = MealDetailsFragment.newInstance(meal.getIdMeal());
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                });

                adapter.setOnDeleteClickListener(meal -> {
                    new Thread(() -> {
                        AppDatabase.getInstance(requireContext())
                                .favoriteMealDao()
                                .delete(meal);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Removed " + meal.getStrMeal(), Toast.LENGTH_SHORT).show();
                            loadFavorites();
                        });
                    }).start();
                });
            });
        }).start();
    }
}
