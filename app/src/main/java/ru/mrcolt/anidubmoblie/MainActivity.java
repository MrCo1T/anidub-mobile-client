package ru.mrcolt.anidubmoblie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycle_view;
    private AnimeAdapter anime_adapter;
    RequestQueue  requestQueue;
    String url;

    List<AnimeDataModel> anime_data=new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Make call to AsyncTask
        //new jasonparse().execute();

        Fresco.initialize(this);

        requestQueue = Volley.newRequestQueue(this);
        recycle_view = findViewById(R.id.userList);
//        recycle_view.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        recycle_view.addOnScrollListener(scrollListener);
        loadAnime();
    }

    public void loadNextDataFromApi(int offset) {
        Toast.makeText(MainActivity.this, "loading more", Toast.LENGTH_SHORT).show();
        loadMoreAnime(offset);
    }

    public void loadAnime() {
        url = "https://hastebin.com/raw/hizuhojowe";

        JsonArrayRequest getData = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray resp) {
                try {

                    for(int i=0;i<resp.length();i++){
                        JSONObject json_data = (JSONObject) resp.get(i);

//                        AnimeDataModel User = new AnimeDataModel();
//                        User.setPoster(json_data.getString("poster"));
//                        User.setTitle(json_data.getString("title"));
//                        User.setYear(json_data.getString("year"));
//                        User.setCountry(json_data.getString("country"));
//                        User.setRating(json_data.getString("rating"));
//                        User.setGenre(json_data.getString("genres"));
                        //User.setStreet(json_data.getJSONObject("address").getString("street"));
                        anime_data.add(
                                new AnimeDataModel(
                                        json_data.getString("poster"),
                                        json_data.getString("title"),
                                        json_data.getString("year"),
                                        json_data.getString("country"),
                                        json_data.getString("rating"),
                                        json_data.getString("genres"),
                                        json_data.getString("description"),
                                        json_data.getString("newsId")
                                        ));


                    }

                    anime_adapter = new AnimeAdapter(MainActivity.this, anime_data);
                    recycle_view.setAdapter(anime_adapter);
                }catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Ошибка: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Проверьте, есть ли у Вас интернет, бомжара", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(getData);

    }

    public void loadMoreAnime(final int page) {
        url = "https://hastebin.com/raw/hizuhojowe";

        JsonArrayRequest getData = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray resp) {
                try {

                    for(int i=0;i<resp.length();i++){
                        JSONObject json_data = (JSONObject) resp.get(i);

                        anime_data.add(
                                new AnimeDataModel(
                                        json_data.getString("poster"),
                                        json_data.getString("title"),
                                        json_data.getString("year"),
                                        json_data.getString("country"),
                                        json_data.getString("rating"),
                                        json_data.getString("genres"),
                                        json_data.getString("description"),
                                        json_data.getString("newsId")
                                ));


                    }

                    anime_adapter.notifyItemInserted(anime_data.size() - page);

                }catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Ошибка: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Проверьте, есть ли у Вас интернет, бомжара", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(getData);

    }

/*   public void sendjsonrequest(){

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.myjson.com/bins/gv2on", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json_data) {
                try {



                    muser = findViewById(R.id.userList);
                    mAdapter = new useradupter(MainActivity.this, udata);
                    muser.setAdapter(mAdapter);
                    muser.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                }catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }

                requestQueue.add(jsonObjectRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }
*/
/*    private class jasonparse extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

               URL url = new URL("https://api.myjson.com/bins/gv2on");
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            List<user> udata=new ArrayList<>();

            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    user User = new user();
                    User.setName(json_data.getString("name"));
                    User.setUsername(json_data.getString("username"));
                    User.setEmail(json_data.getString("email"));
                    User.setPhone(json_data.getString("phone"));
                    User.setWebsite(json_data.getString("website"));
                    User.setStreet(json_data.getJSONObject("address").getString("street"));
                    User.setSuit(json_data.getJSONObject("address").getString("suite"));
                    User.setCity(json_data.getJSONObject("address").getString("city"));
                    User.setZip(json_data.getJSONObject("address").getString("zipcode"));
                    User.setCname(json_data.getJSONObject("company").getString("name"));
                    User.setCatchpharse(json_data.getJSONObject("company").getString("catchPhrase"));
                    User.setBs(json_data.getJSONObject("company").getString("bs"));
                    udata.add(User);

                }

                // Setup and Handover data to recyclerview
                muser = findViewById(R.id.userList);
                mAdapter = new useradupter(MainActivity.this, udata);
                muser.setAdapter(mAdapter);
                muser.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }*/

}
