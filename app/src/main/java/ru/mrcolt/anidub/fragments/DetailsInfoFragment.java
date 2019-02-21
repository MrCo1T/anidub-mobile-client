package ru.mrcolt.anidub.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ru.mrcolt.anidub.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DetailsInfoFragment extends Fragment {
    String Poster, TitleRU, TitleEN, Rating, Year, Genre, Country, Description, NewsID;
    TextView film_title_ru, film_title_en, film_rating, film_year, film_genre, film_country;
    ExpandableTextView film_description;
    SimpleDraweeView film_poster_bg, film_poster;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        film_poster.setOnClickListener(v -> onButtonShowPopupWindowClick(v));
        film_title_ru = view.findViewById(R.id.details_title_ru);
        film_title_en = view.findViewById(R.id.details_title_en);
        film_rating_bar = view.findViewById(R.id.details_rating_bar);
        film_rating = view.findViewById(R.id.details_rating);
        film_year = view.findViewById(R.id.details_year);
        film_genre = view.findViewById(R.id.details_genre);
        film_country = view.findViewById(R.id.details_country);
        film_description = view.findViewById(R.id.details_description);

        film_poster.setImageURI(Uri.parse(Poster));
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(Poster))
                .setPostprocessor(new IterativeBoxBlurPostProcessor(8))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(film_poster_bg.getController())
                .build();
        film_poster_bg.setController(controller);

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

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.popup_window, null, false);
        SimpleDraweeView popupposter = inflate.findViewById(R.id.popup_poster);
        popupposter.setImageURI(Uri.parse(Poster));

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(inflate, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}
