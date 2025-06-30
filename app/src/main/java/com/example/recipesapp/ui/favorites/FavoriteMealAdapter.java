package com.example.recipesapp.ui.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.model.FavoriteMeal;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.FavoriteViewHolder> {

    private List<FavoriteMeal> favoriteMeals;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteMeal meal);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(FavoriteMeal meal);
    }

    private OnFavoriteClickListener favoriteClickListener;
    private OnDeleteClickListener deleteClickListener;

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public FavoriteMealAdapter(List<FavoriteMeal> favoriteMeals) {
        this.favoriteMeals = favoriteMeals;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteMeal meal = favoriteMeals.get(position);

        holder.textMealName.setText(meal.getStrMeal());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .into(holder.imageMeal);

        holder.itemView.setOnClickListener(v -> {
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(meal);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMeal, buttonDelete;
        TextView textMealName;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMeal = itemView.findViewById(R.id.imageMeal);
            textMealName = itemView.findViewById(R.id.textMealName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
