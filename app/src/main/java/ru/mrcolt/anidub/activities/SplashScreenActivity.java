package ru.mrcolt.anidub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import ru.mrcolt.anidub.utils.NetworkUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkUtils networkUtils = new NetworkUtils();
        networkUtils.getAnidubRequest("http://anidub-de.mrcolt.ru", new NetworkUtils.OKHttpNetwork() {
            @Override
            public void onSuccess(String body) {
                runOnUiThread(() -> {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                });
            }

            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> Toast.makeText(getBaseContext(), "Ошибка: проверьте подключение к интернету", Toast.LENGTH_LONG).show());
            }
        });
    }
}
