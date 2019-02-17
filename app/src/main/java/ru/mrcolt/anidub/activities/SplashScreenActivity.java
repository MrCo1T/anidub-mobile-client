package ru.mrcolt.anidub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class SplashScreenActivity extends AppCompatActivity {

    private NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkUtils.sendGETRequest(this,
                "http://anidub-de.mrcolt.ru",
                new HashMap<>(),
                new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                runOnUiThread(() -> {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                });
            }

            @Override
            public void onFailure(String e) {
                runOnUiThread(() -> Toast.makeText(getBaseContext(), "Ошибка: проверьте подключение к интернету", Toast.LENGTH_LONG).show());
            }
        });
    }
}
