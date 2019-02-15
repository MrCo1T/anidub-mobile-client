package ru.mrcolt.anidubmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.activities.VideoPlayerActivity;
import ru.mrcolt.anidubmobile.adapters.EpisodesListAdapter;
import ru.mrcolt.anidubmobile.models.EpisodesListModel;
import ru.mrcolt.anidubmobile.utils.HttpUtils;

public class EpisodesListFragment extends Fragment {

    View view;
    ArrayList<EpisodesListModel> episodesListModel = new ArrayList<>();
    OkHttpClient okHttpClient = new OkHttpClient();
    ListView listView;

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

    private void playVideo(String url) {
        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episodes_list, container, false);
        listView = view.findViewById(R.id.episodes_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EpisodesListModel episode = episodesListModel.get(i);
                loadEpisode(episode.getURL());
            }
        });

        loadMediaEpisodesList(getExtras("NewsID"));
        return view;
    }

    private String createTempFile(String content) throws IOException {
        File outputDir = getContext().getCacheDir();
        File outputFile = File.createTempFile("anidubmobile", ".tmp", outputDir);
        String tempFilePath = outputFile.getAbsolutePath();

        FileOutputStream fOut = new FileOutputStream(tempFilePath);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(content);

        myOutWriter.close();

        fOut.flush();
        fOut.close();
        return tempFilePath;
    }

    private String prepareChunk(String chunk, String chunkURL) {
        String cdnURL = chunkURL.split("chunk.m3u8")[0];
        return chunk.replaceAll("n_", cdnURL + "n_");
    }

    private void prepareEpisode(String body) throws JSONException {
        String chunkURL = body.split("\n")[2];
        HttpUtils okHttpUtils = new HttpUtils();
        okHttpUtils.getAnidubRequest(chunkURL, new HttpUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                String preparedChunk = prepareChunk(body, chunkURL);
                try {
                    String chunkFilePath = createTempFile(preparedChunk);
                    playVideo(chunkFilePath);
                } catch (IOException e) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Не могу создать данные для эпизода", Toast.LENGTH_LONG).show());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Сервер не доступен", Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }
        });
    }

    private void loadEpisode(String url) {
        HttpUtils okHttpUtils = new HttpUtils();
        okHttpUtils.getAnidubRequest(url, new HttpUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                try {
                    prepareEpisode(body);
                } catch (JSONException e) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Не могу обработать эпизод", Toast.LENGTH_LONG).show());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Сервер не доступен", Toast.LENGTH_LONG).show());
                e.printStackTrace();
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
                    "AniDub"));
        }
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listView.setAdapter(new EpisodesListAdapter(getContext(), episodesListModel)));
    }

    private void loadMediaEpisodesList(String newsID) {
        HttpUtils okHttpUtils = new HttpUtils();
        okHttpUtils.getAPIRequest("http://anidub-de.mrcolt.ru/media/episodes?news_id=" + newsID, new HttpUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                try {
                    prepareMediaEpisodesList(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        });
    }

}
