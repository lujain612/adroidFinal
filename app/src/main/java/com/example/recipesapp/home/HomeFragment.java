package com.example.recipesapp.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.R;
import com.example.recipesapp.api.RetrofitInstance;
import com.example.recipesapp.model.Category;
import com.example.recipesapp.model.CategoryResponse;
//import com.example.recipesapp.meals.MealsFragment;

import java.util.List;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button buttonFavorites = view.findViewById(R.id.buttonFavorites);
        buttonFavorites.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FavoritesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadCategories();

        return view;
    }

    private void loadCategories() {
        RetrofitInstance.getMealService().getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body().getCategories();
                    adapter = new CategoryAdapter(categoryList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
                        @Override
                        public void onCategoryClick(Category category) {
                            MealsFragment fragment = MealsFragment.newInstance(category.getStrCategory());

                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Failed to get categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
