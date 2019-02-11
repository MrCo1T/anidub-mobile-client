package ru.mrcolt.anidubmobile.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.adapters.MediaListAdapter;
import ru.mrcolt.anidubmobile.listeners.EndlessScrollListener;
import ru.mrcolt.anidubmobile.models.MediaListModel;
import ru.mrcolt.anidubmobile.utils.DialogUtils;
import ru.mrcolt.anidubmobile.utils.HttpUtil;

public class MainActivity extends AppCompatActivity {

    private List<MediaListModel> mediaListModels = new ArrayList<MediaListModel>();
    private RecyclerView recyclerView;
    private MediaListAdapter mediaListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DialogUtils dialogUtils = new DialogUtils();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        progressDialog = dialogUtils.createLoading(this, "", "Загрузка");

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
        mediaListAdapter = new MediaListAdapter(this, mediaListModels);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    private void configComponents() throws Exception {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mediaListAdapter);
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
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

    private void loadMediaList(int page) {
        HttpUtil okHttpUtil = new HttpUtil();
        okHttpUtil.getAPIRequest("http://anidub-ru.mrcolt.ru/media?page=" + String.valueOf(page), new HttpUtil.OKHttpNetwork() {
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
        dialogUtils.destroyLoading(progressDialog);
    }
}
