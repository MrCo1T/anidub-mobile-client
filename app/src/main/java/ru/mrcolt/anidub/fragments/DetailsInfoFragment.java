package ru.mrcolt.anidub.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.imageloader.ImageLoader;
import com.budiyev.android.imageloader.LoadCallback;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;

import androidx.fragment.app.Fragment;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.utils.BlurUtils;

public class DetailsInfoFragment extends Fragment {
    String Poster, TitleRU, TitleEN, Rating, Year, Genre, Country, Description, NewsID;
    TextView film_title_ru, film_title_en, film_rating, film_year, film_genre, film_country;
    ExpandableTextView film_description;
    ImageView film_poster_bg, film_poster;
    Float RatingBar;
    MaterialRatingBar film_rating_bar;
    View view;

    public DetailsInfoFragment() {
    }

    public static DetailsInfoFragment newInstance() {
        return new DetailsInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String getExtraAsString(String name) {
        return getActivity().getIntent().getExtras().getString(name);
    }

    private Float getExtraAsFloat(String name) {
        return getActivity().getIntent().getExtras().getFloat(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_info, container, false);

        Poster = getExtraAsString("Poster");
        TitleRU = getExtraAsString("TitleRU");
        TitleEN = getExtraAsString("TitleEN");
        RatingBar = getExtraAsFloat("RatingBar");
        Rating = getExtraAsString("Rating");
        Year = getExtraAsString("Year");
        Genre = getExtraAsString("Genre");
        Country = getExtraAsString("Country");
        Description = getExtraAsString("Description");
        NewsID = getExtraAsString("NewsID");

        film_poster_bg = view.findViewById(R.id.details_poster_bg);
        film_poster = view.findViewById(R.id.details_poster);
        film_title_ru = view.findViewById(R.id.details_title_ru);
        film_title_en = view.findViewById(R.id.details_title_en);
        film_rating_bar = view.findViewById(R.id.details_rating_bar);
        film_rating = view.findViewById(R.id.details_rating);
        film_year = view.findViewById(R.id.details_year);
        film_genre = view.findViewById(R.id.details_genre);
        film_country = view.findViewById(R.id.details_country);
        film_description = view.findViewById(R.id.details_description);

        ImageLoader.with(getContext()).from(Poster).onLoaded(new LoadCallback() {
            @Override
            public void onLoaded(Bitmap image) {
                getActivity().runOnUiThread(() -> {
                    BlurUtils blurUtils = new BlurUtils();
                    film_poster_bg.setImageBitmap(blurUtils.create(getContext(), image, 4f));
                    film_poster.setImageBitmap(image);
                });
            }
        }).load();

        film_title_ru.setText(TitleRU);
        film_title_en.setText(TitleEN);
        film_rating_bar.setRating(RatingBar);
        film_rating.setText(Rating);
        film_year.setText(Year);
        film_genre.setText(Genre);
        film_country.setText(Country);
        film_description.setContent(Description);

        return view;
    }

}
