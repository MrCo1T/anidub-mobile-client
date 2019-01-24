package ru.mrcolt.anidubmobile.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import ru.mrcolt.anidubmobile.R;


public class MediaDetailsInfoFragment extends Fragment {
    String Poster, TitleRU, TitleEN, Rating, Year, Genre, Country, Episode, PubDate,
            Producer, Author, Voicer, Description, NewsID;
    TextView film_title_ru, film_title_en, film_rating, film_year, film_genre, film_country,
            film_episode, film_pub_date, film_producer, film_author, film_voicer, film_News_ID;
    ExpandableTextView film_description;
    SimpleDraweeView film_poster_bg, film_poster;
    View view;

    public MediaDetailsInfoFragment() {
    }

    public static MediaDetailsInfoFragment newInstance() {
        MediaDetailsInfoFragment fragment = new MediaDetailsInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String getExtras(String name) {
        return getActivity().getIntent().getExtras().getString(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_details_info, container, false);

        Poster      = getExtras("Poster");
        TitleRU     = getExtras("TitleRU");
        TitleEN     = getExtras("TitleEN");
        Rating      = getExtras("Rating");
        Year        = getExtras("Year");
        Genre       = getExtras("Genre");
        Country     = getExtras("Country");
        Episode     = getExtras("Episode");
        PubDate     = getExtras("PubDate");
        Producer    = getExtras("Producer");
        Author      = getExtras("Author");
        Voicer      = getExtras("Voicer");
        Description = getExtras("Description");
        NewsID      = getExtras("NewsID");

        film_poster_bg = view.findViewById(R.id.details_poster_bg);
        film_poster = view.findViewById(R.id.details_poster);
        film_title_ru = view.findViewById(R.id.details_title_ru);
        film_title_en = view.findViewById(R.id.details_title_en);
        film_rating = view.findViewById(R.id.details_rating);
        film_year = view.findViewById(R.id.details_year);
        film_genre = view.findViewById(R.id.details_genre);
        film_country = view.findViewById(R.id.details_country);
        film_episode = view.findViewById(R.id.details_episode);
        film_pub_date = view.findViewById(R.id.details_pubdate);
        film_producer = view.findViewById(R.id.details_producer);
        film_author = view.findViewById(R.id.details_author);
        film_voicer = view.findViewById(R.id.details_voicer);
        film_description = view.findViewById(R.id.details_description);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Poster))
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(5))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(film_poster_bg.getController())
                    .build();

        film_poster_bg.setController(controller);
        film_poster.setImageURI(Uri.parse(Poster));
        film_title_ru.setText(TitleRU);
        film_title_en.setText(TitleEN);
        film_rating.setText(Rating);
        film_year.setText(Year);
        film_genre.setText(Genre);
        film_country.setText(Country);
        film_episode.setText(Episode);
        film_pub_date.setText(PubDate);
        film_producer.setText(Producer);
        film_author.setText(Author);
        film_voicer.setText(Voicer);
        film_description.setContent(Description);

        return view;
    }

}
