package ru.mrcolt.anidub.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.activities.VideoPlayerActivity;
import ru.mrcolt.anidub.adapters.EpisodesListAdapter;
import ru.mrcolt.anidub.models.EpisodesListModel;
import ru.mrcolt.anidub.utils.DialogUtils;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class EpisodesListFragment extends Fragment {

    private View view;
    private ArrayList<EpisodesListModel> episodesListModel = new ArrayList<>();
    private ListView listView;
    private EpisodesListModel episode;
    private DialogUtils dialogUtils = new DialogUtils();
    private ProgressDialog progressDialog;
    private NetworkUtils okNetworkUtils = new NetworkUtils();

    public EpisodesListFragment() {
    }

    public static EpisodesListFragment newInstance() {
        return new EpisodesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String getExtras(String name) {
        return getActivity().getIntent().getExtras().getString(name);
    }

    private void playVideo(String title, String url) {
        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episodes_list, container, false);
        listView = view.findViewById(R.id.episodes_listview);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            episode = episodesListModel.get(i);
            loadEpisode(episode.getURL());
            progressDialog = dialogUtils.createLoading(getContext(), "Загрузка эпизода");
        });

        loadMediaEpisodesList(getExtras("NewsID"));
        return view;
    }

    private void loadEpisode(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://anime.anidub.com/");
        okNetworkUtils.sendGETRequest(getContext(),
                url,
                headers,
                new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                String chunkURL = body.split("\n")[2].replaceAll("https://(.*?).anivid", "https://cdn100.anivid");
                dialogUtils.destroyLoading(progressDialog);
                playVideo(episode.getTitle(), chunkURL);
            }

            @Override
            public void onFailure(String e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Сервер не доступен", Toast.LENGTH_LONG).show());
            }
        });
    }

    private void prepareMediaEpisodesList(String body) throws JSONException {
        JSONObject result = new JSONObject(body);
        JSONArray resultTitle = result.getJSONObject("data").getJSONObject("episodes").getJSONArray("title");
        JSONArray resultUrl = result.getJSONObject("data").getJSONObject("episodes").getJSONObject("url").getJSONArray("Anidub");
        for (int i = 0; i < resultTitle.length(); i++) {
            episodesListModel.add(new EpisodesListModel(
                    resultTitle.get(i).toString(),
                    resultUrl.get(i).toString(),
                    "Anidub"));
        }
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listView.setAdapter(new EpisodesListAdapter(getContext(), episodesListModel)));
    }

    private void loadMediaEpisodesList(String newsID) {
        okNetworkUtils.sendGETRequest(getContext(),
                "http://anidub-de.mrcolt.ru/media/episodes?news_id=" + newsID,
                new HashMap<>(),
                new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                try {
                    prepareMediaEpisodesList(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String e) {

            }
        });
    }

}
