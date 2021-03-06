package ru.mrcolt.anidub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.adapters.MediaListAdapter;
import ru.mrcolt.anidub.listeners.EndlessScrollListener;
import ru.mrcolt.anidub.models.MediaListModel;
import ru.mrcolt.anidub.utils.DialogUtils;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private List<MediaListModel> mediaListModels = new ArrayList<MediaListModel>();
    private RecyclerView recyclerView;
    private MediaListAdapter mediaListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private NetworkUtils okNetworkUtils = new NetworkUtils();
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        initComponents();
        configComponents();

        loadMediaList(currentPage);
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.media_list_id);
        progressBar = findViewById(R.id.media_loading);
        mediaListAdapter = new MediaListAdapter(this, mediaListModels);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    private void configComponents() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mediaListAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage++;
                loadMediaList(currentPage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
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
        okNetworkUtils.sendGETRequest(this,
                "http://anidub-de.mrcolt.ru/media?page=" + String.valueOf(page),
                new HashMap<>(),
                new NetworkUtils.httpNetwork() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            prepareMediaList(body);
                        } catch (Exception e) {
                            DialogUtils.defaultAlert(MainActivity.this, "Ошибка", "Не удается обработать данные", "ОК");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        DialogUtils.defaultAlert(MainActivity.this, "Ошибка", "Сервер не доступен", "ОК");
                    }
                });
    }

    private void prepareMediaList(String body) throws JSONException {
        JSONObject result = new JSONObject(body);
        JSONArray resultData = result.getJSONArray("data");
        if (!resultData.isNull(0)) {
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
            mediaListAdapter.notifyItemInserted(mediaListModels.size());
        } else {
            currentPage++;
            loadMediaList(currentPage);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
