package ru.mrcolt.anidubmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.budiyev.android.imageloader.ImageLoader;
import com.budiyev.android.imageloader.ImageRequestDelegate;

import java.util.List;

import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.activities.DetailsActivity;
import ru.mrcolt.anidubmobile.models.MediaListModel;

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

        holder.film_title.setText(current.getTitleRU());
        holder.film_rating.setText(current.getRating());
        holder.film_episodes.setText(current.getEpisode());
        holder.film_genre.setText(current.getGenre());

//        holder.film_poster.setImageURI(Uri.parse(current.getPoster()));

        ImageLoader
                .with(context)
                .from(current.getPoster())
                .load(holder.film_poster);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);

            intent.putExtra("Poster", current.getPoster());
            intent.putExtra("TitleRU", current.getTitleRU());
            intent.putExtra("TitleEN", current.getTitleEN());
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

        ImageView film_poster;
        TextView film_title, film_year, film_rating, film_episodes, film_genre;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            film_poster = itemView.findViewById(R.id.poster);
            film_title = itemView.findViewById(R.id.title);
            film_rating = itemView.findViewById(R.id.rating);
            film_episodes = itemView.findViewById(R.id.episodes);
            film_genre = itemView.findViewById(R.id.genres);
        }
    }
}

