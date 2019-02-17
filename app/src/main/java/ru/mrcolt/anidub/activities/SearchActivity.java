package ru.mrcolt.anidub.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

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
import ru.mrcolt.anidub.adapters.MediaSearchListAdapter;
import ru.mrcolt.anidub.listeners.EndlessScrollListener;
import ru.mrcolt.anidub.models.MediaSearchListModel;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class SearchActivity extends AppCompatActivity {

    private List<MediaSearchListModel> mediaSearchListModels = new ArrayList<MediaSearchListModel>();
    private RecyclerView recyclerView;
    private MediaSearchListAdapter mediaSearchListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private TextView searchTitle;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchQuery = getIntent().getExtras().getString("query");
        try {
            initComponents();
            configComponents();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMediaSearchList(searchQuery, 1);
    }

    private void initComponents() throws Exception {
        toolbar = findViewById(R.id.search_toolbar);
        searchTitle = findViewById(R.id.search_title);
        recyclerView = findViewById(R.id.media_search_list_id);
        mediaSearchListAdapter = new MediaSearchListAdapter(this, mediaSearchListModels);
        linearLayoutManager = new LinearLayoutManager(this);
    }  

    private void configComponents() throws Exception {
        searchTitle.setText(getIntent().getExtras().getString("query"));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mediaSearchListAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMediaSearchList(searchQuery, page);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            Animatoo.animateSlideRight(this);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Animatoo.animateSlideRight(this);
        return true;
    }

    private void clearRecyclerView() {
        mediaSearchListModels.clear();
        mediaSearchListAdapter.notifyDataSetChanged();
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
                clearRecyclerView();
                searchView.setIconified(true);
                menuItem.collapseActionView();
                searchTitle.setText(query);
                loadMediaSearchList(query, 1);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });
        return true;
    }

    private void loadMediaSearchList(String query, int page) {
        NetworkUtils okNetworkUtils = new NetworkUtils();
        okNetworkUtils.getAPIRequest("http://anidub-de.mrcolt.ru/media/search?q=" + query + "&page=" + String.valueOf(page), new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                try {
                    prepareMediaSearchList(body);
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

    private void prepareMediaSearchList(String body) throws JSONException {
        JSONObject result = new JSONObject(body);
        JSONArray resultData = result.getJSONArray("data");
        for (int i = 0; i < resultData.length(); i++) {
            JSONObject jsonData = (JSONObject) resultData.get(i);
            JSONObject title = jsonData.getJSONObject("title");
            mediaSearchListModels.add(new MediaSearchListModel(
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
        runOnUiThread(() -> mediaSearchListAdapter.notifyItemInserted(mediaSearchListModels.size()));
    }
}
