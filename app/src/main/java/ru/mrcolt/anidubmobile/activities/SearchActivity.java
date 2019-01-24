package ru.mrcolt.anidubmobile.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mrcolt.anidubmobile.models.MediaListModel;
import ru.mrcolt.anidubmobile.utils.EndlessRecyclerViewScrollListener;
import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.adapters.MediaSearchListAdapter;
import ru.mrcolt.anidubmobile.models.MediaSearchListModel;

import static android.support.constraint.Constraints.TAG;

public class SearchActivity extends AppCompatActivity {

    private List<MediaSearchListModel> mediaSearchListModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private MediaSearchListAdapter mediaSearchListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String searchText;
    private Integer lastPage = 1;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private SearchView searchView;
    private TextView searchToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchText = getIntent().getStringExtra("searchText");
        searchToolbar = findViewById(R.id.search_toolbar_title);
        searchToolbar.setText(searchText);

        if (isOnline()) {
            initRecyclerView();
        } else {
            Toast.makeText(this, "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.media_search_list_id);
        mediaSearchListAdapter = new MediaSearchListAdapter(this, mediaSearchListModels);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mediaSearchListAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "onLoadMore: loading more media");
                loadMediaList(searchText, page);
            }
        });

        loadMediaList(searchText, 1);
    }

    public void recyclerViewClear() {
        mediaSearchListModels.clear();
        mediaSearchListAdapter.notifyDataSetChanged();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Поиск");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchToolbar.setText(query);
                searchView.setIconified(true);
                menuItem.collapseActionView();
                recyclerViewClear();
                loadMediaList(query, 1);
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

    private void loadMediaList(String searchText, final int page) {
        if (page <= lastPage) {
            okHttpClient.newBuilder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .callTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder().url("http://anidub-ru.mrcolt.ru/media/search?q=" + searchText + "&page=" + String.valueOf(page))
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ошибка: не удаётся установить соединение с сервером", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String data = response.body().string();
                    JSONArray result = null;
                    try {
                        result = new JSONArray(data);
                        JSONObject json_result = (JSONObject) result.get(0);
                        JSONArray json_datas = json_result.getJSONArray("data");
                        if (page == 1) {
                            lastPage = json_result.getInt("last_page");
                        }
                        for (int i = 0; i < json_datas.length(); i++) {
                            try {
                                JSONObject json_data = (JSONObject) json_datas.get(i);
                                JSONObject title =  json_data.getJSONObject("title");
                                Log.d(TAG, "onResponse: " + String.valueOf(title.getString("ru")));
                                mediaSearchListModels.add(new MediaSearchListModel(
                                        json_data.getString("poster"),
                                        title.getString("ru"),
                                        title.getString("en"),
                                        json_data.getString("rating"),
                                        json_data.getString("year"),
                                        json_data.getString("genre"),
                                        json_data.getString("country"),
                                        json_data.getString("episode"),
                                        json_data.getString("pubDate"),
                                        json_data.getString("producer"),
                                        json_data.getString("author"),
                                        json_data.getString("voicer"),
                                        json_data.getString("description"),
                                        json_data.getString("newsID")));
                            } catch (JSONException e1) {
                                Toast.makeText(getApplicationContext(), "Ошибка: не могу получить данные", Toast.LENGTH_LONG).show();
                                e1.printStackTrace();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mediaSearchListAdapter.notifyItemInserted(mediaSearchListModels.size());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
