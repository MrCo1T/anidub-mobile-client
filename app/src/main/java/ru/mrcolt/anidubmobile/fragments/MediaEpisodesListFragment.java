package ru.mrcolt.anidubmobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.activities.MediaDetailsActivity;
import ru.mrcolt.anidubmobile.adapters.MediaEpisodesListAdapter;
import ru.mrcolt.anidubmobile.models.MediaEpisodesListModel;

import static android.support.constraint.Constraints.TAG;

public class MediaEpisodesListFragment extends Fragment {

    View view;
    ArrayList<MediaEpisodesListModel> mediaEpisodesListModel = new ArrayList<>();
    OkHttpClient okHttpClient = new OkHttpClient();
    ListView listView;

    public MediaEpisodesListFragment() {
    }

    public static MediaEpisodesListFragment newInstance() {
        MediaEpisodesListFragment fragment = new MediaEpisodesListFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_episodes_list, container, false);
        listView = view.findViewById(R.id.episodes_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MediaEpisodesListModel episode = mediaEpisodesListModel.get(i);
                okHttpClient.newBuilder()
                        .readTimeout(5000, TimeUnit.MILLISECONDS)
                        .callTimeout(5000, TimeUnit.MILLISECONDS)
                        .build();

                Request request = new Request.Builder().url("http://anidub-ru.mrcolt.ru/media/content?media_url=" + String.valueOf(episode.getURL()))
                        .addHeader("User-Agent", "Mozilla/5.0")
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Ошибка: не удаётся установить соединение с сервером", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String data = response.body().string();
                        try {
                            final JSONArray result = new JSONArray(data);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json_result = (JSONObject) result.get(0);
                                        Log.d(TAG, "run: " + json_result.getString("SD"));
                                        MediaDetailsActivity.playVideo(getContext(), episode.getTitle(), json_result.getString("SD"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        loadMediaEpisodesList(getExtras("NewsID"));
        return view;
    }

    private String getEpisodeURL(String url) {
        return "";
    }

    private void loadMediaEpisodesList(String newsID) {
        okHttpClient.newBuilder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder().url("http://anidub-de.mrcolt.ru/media/episodes/" + String.valueOf(newsID))
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Ошибка: не удаётся установить соединение с сервером", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String data = response.body().string();
                try {
                    StringBuilder services = new StringBuilder();
                    JSONArray result = new JSONArray(data);
                    JSONObject json_data = (JSONObject) result.get(0);
                    JSONArray json_players = json_data.getJSONArray("players");
                    for (int i = 0; i < json_players.length(); i++) {
                        String service = json_players.get(i).toString();
                        services.append(service);
                        services.append(", ");
                    }
                    services.setLength(services.length() - 2);
                    JSONArray json_titles = json_data.getJSONObject("episodes").getJSONArray("title");
                    JSONArray json_episodes = json_data.getJSONObject("episodes").getJSONObject("url").getJSONArray("Sibnet");

                    for (int i = 0; i < json_titles.length(); i++) {
                        mediaEpisodesListModel.add(new MediaEpisodesListModel(
                                json_titles.get(i).toString(),
                                json_episodes.get(i).toString(),
                                services.toString()));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new MediaEpisodesListAdapter(getContext(), mediaEpisodesListModel));
                        }
                    });
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Видео не доступно на территории РФ. Спасибо РКН :)", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

}