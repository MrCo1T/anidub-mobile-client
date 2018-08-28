package ru.mrcolt.anidubmoblie;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<AnimeDataModel> data= Collections.emptyList();

    // create constructor to innitilize context and data sent from MainActivity
    public AnimeAdapter(Context context, List<AnimeDataModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardlayout, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder= (MyHolder) holder;
        AnimeDataModel current=data.get(position);

//        Glide.with(context).load(current.poster).into(myHolder.poster);
        SimpleDraweeView draweeView = (SimpleDraweeView) myHolder.poster;
        draweeView.getHierarchy().setPlaceholderImage(R.drawable.placeholder);
        draweeView.setImageURI(current.poster);

        myHolder.title.setText(current.title.split("/")[0]);
        myHolder.year.setText("Год: " + current.year);
        myHolder.country.setText("Страна: " + current.country);
        myHolder.rating.setText("Рейтинг: " + current.rating);
        myHolder.genre.setText("Жанр: " + current.genre);
    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        TextView title, year, country, rating, genre;
        // create constructor to get widget reference
        MyHolder(final View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title= itemView.findViewById(R.id.title);
            year = itemView.findViewById(R.id.year);
            country = itemView.findViewById(R.id.country);
            rating = itemView.findViewById(R.id.rating);
            genre = itemView.findViewById(R.id.genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),AnimeDetails.class);
                    intent.putExtra("poster", data.get(getAdapterPosition()).getPoster());
                    intent.putExtra("title", data.get(getAdapterPosition()).getTitle());
                    intent.putExtra("year", data.get(getAdapterPosition()).getYear());
                    intent.putExtra("country", data.get(getAdapterPosition()).getCountry());
                    intent.putExtra("rating", data.get(getAdapterPosition()).getRating());
                    intent.putExtra("genre", data.get(getAdapterPosition()).getGenre());
                    intent.putExtra("description", data.get(getAdapterPosition()).getDescription());
                    intent.putExtra("newsID", data.get(getAdapterPosition()).getNewsID());
                    context.startActivity(intent);
                }
            });
        }

    }



}
