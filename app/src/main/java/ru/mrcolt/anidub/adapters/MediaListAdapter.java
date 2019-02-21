package ru.mrcolt.anidub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.activities.DetailsActivity;
import ru.mrcolt.anidub.models.MediaListModel;

public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.ViewHolder> {

    private Context context;
    private List<MediaListModel> data;

    public MediaListAdapter(Context context, List<MediaListModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_media_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MediaListModel current = data.get(position);

        String currentTitleRU = current.getTitleRU();
        String currentRating = current.getRating();
        String currentGenre = current.getGenre();
        holder.film_title.setText(currentTitleRU);
        holder.film_rating.setText(currentRating);
        holder.film_genre.setText(currentGenre);

        float ratingBar = ((currentRating.contains("%")) ? Float.parseFloat(currentRating.replace("%", "")) / 20 : Float.parseFloat(currentRating));

        holder.film_rating_bar.setRating(ratingBar);
        holder.film_poster.setImageURI(Uri.parse(current.getPoster()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);

            intent.putExtra("Poster", current.getPoster());
            intent.putExtra("TitleRU", current.getTitleRU());
            intent.putExtra("TitleEN", current.getTitleEN());
            intent.putExtra("RatingBar", ratingBar);
            intent.putExtra("Rating", current.getRating());
            intent.putExtra("Year", current.getYear());
            intent.putExtra("Genre", current.getGenre());
            intent.putExtra("Country", current.getCountry());
            intent.putExtra("Episode", current.getEpisode());
            intent.putExtra("Description", current.getDescription());
            intent.putExtra("NewsID", current.getNewsID());

            context.startActivity(intent);
            Animatoo.animateSlideLeft(context);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView film_poster;
        TextView film_title, film_rating, film_genre;
        MaterialRatingBar film_rating_bar;

        public ViewHolder(View itemView) {
            super(itemView);

            film_poster = itemView.findViewById(R.id.poster);
            film_title = itemView.findViewById(R.id.title);
            film_rating_bar = itemView.findViewById(R.id.rating_bar);
            film_rating = itemView.findViewById(R.id.rating);
            film_genre = itemView.findViewById(R.id.genres);
        }
    }
}

