package ru.mrcolt.anidub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.adapters.MediaListAdapter;
import ru.mrcolt.anidub.listeners.EndlessScrollListener;
import ru.mrcolt.anidub.models.MediaListModel;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private List<MediaListModel> mediaListModels = new ArrayList<MediaListModel>();
    private RecyclerView recyclerView;
    private MediaListAdapter mediaListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        try {
            initComponents();
            configComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMediaList(1);
    }

    private void initComponents() throws Exception {
        recyclerView = findViewById(R.id.media_list_id);
        progressBar = findViewById(R.id.media_loading);
        mediaListAdapter = new MediaListAdapter(this, mediaListModels);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    private void configComponents() throws Exception {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mediaListAdapter);
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMediaList(page);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setIconified(true);
                menuItem.collapseActionView();

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }


        });
        searchView.setOnCloseListener(() -> true);
        return true;
    }

    private void loadMediaList(int page) {
        NetworkUtils okNetworkUtils = new NetworkUtils();
        okNetworkUtils.getAPIRequest("http://anidub-de.mrcolt.ru/media?page=" + String.valueOf(page), new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                try {
                    prepareMediaList(body);
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(getBaseContext(), "Ошибка: Не удается обработать данные", Toast.LENGTH_LONG).show());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> Toast.makeText(getBaseContext(), "Ошибка: сервер не доступен", Toast.LENGTH_LONG).show());
            }
        });
    }

    private void prepareMediaList(String body) throws JSONException {
        JSONObject result = new JSONObject(body);
        JSONArray resultData = result.getJSONArray("data");
        for (int i = 0; i < resultData.length(); i++) {
            JSONObject jsonData = (JSONObject) resultData.get(i);
            JSONObject title = jsonData.getJSONObject("title");
            mediaListModels.add(new MediaListModel(
                    jsonData.getString("poster"),
                    title.getString("ru"),
                    title.getString("en"),
                    jsonData.getString("rating"),
                    jsonData.getString("year"),
                    jsonData.getString("genre"),
                    jsonData.getString("country"),
                    jsonData.getString("episode"),
                    jsonData.getString("description"),
                    jsonData.getString("news_id")));
        }
        runOnUiThread(() -> mediaListAdapter.notifyItemInserted(mediaListModels.size()));
        progressBar.setVisibility(View.INVISIBLE);
    }
}
