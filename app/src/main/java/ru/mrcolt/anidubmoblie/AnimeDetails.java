package ru.mrcolt.anidubmoblie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class AnimeDetails extends AppCompatActivity {
    private ImageView poster;
    private TextView title, year, country, rating, genre, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animedetails);

        Fresco.initialize(this);

        poster= (ImageView) findViewById(R.id.details_poster);
        title= (TextView) findViewById(R.id.details_title);
        year= (TextView) findViewById(R.id.details_year);
        country= (TextView) findViewById(R.id.details_country);
        rating= (TextView) findViewById(R.id.details_rating);
        genre= (TextView) findViewById(R.id.details_genre);
        description= (TextView) findViewById(R.id.details_description);

        Intent intent=getIntent();

//        Glide.with(this).load(getIntent().getStringExtra("poster")).into(poster);

        SimpleDraweeView draweeView = (SimpleDraweeView) poster;
        draweeView.getHierarchy().setPlaceholderImage(R.drawable.placeholder);
        draweeView.setImageURI(getIntent().getStringExtra("poster"));

        title.setText(getIntent().getStringExtra("title").split("/")[0]);
        year.setText("Год: " + getIntent().getStringExtra("year"));
        country.setText("Страна: " + getIntent().getStringExtra("country"));
        rating.setText("Рейтинг: " + getIntent().getStringExtra("rating"));
        genre.setText("Жанр: " + getIntent().getStringExtra("genre"));
        description.setText("Описание: " + getIntent().getStringExtra("description"));
        final String newsID = getIntent().getStringExtra("newsID");

        Button play_btn = (Button) findViewById(R.id.play_online);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Пока не могу ничего показать, увы. Но вот ID анимешки: " + newsID, Toast.LENGTH_SHORT).show();
            }
        });

//        AnimeDataModel item= (AnimeDataModel) intent.getSerializableExtra("AnimeDataModel");
//        Glide.with(this).load(item.getPoster()).into(animePoster);
//        animeTitle.setText(item.getTitle());
//        animeYear.setText(item.getYear());

    }
}
