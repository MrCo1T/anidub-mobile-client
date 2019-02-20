package ru.mrcolt.anidub.activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import ru.mrcolt.anidub.BuildConfig;
import ru.mrcolt.anidub.utils.ApkInstallerUtils;
import ru.mrcolt.anidub.utils.NetworkUtils;
import ru.mrcolt.anidub.utils.PermissionUtils;
import stream.customalert.CustomAlertDialogue;

public class SplashScreenActivity extends AppCompatActivity {

    private NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkUtils.sendGETRequest(getBaseContext(),
                "http://anidub-de.mrcolt.ru/app/update",
                new HashMap<>(),
                new NetworkUtils.httpNetwork() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            JSONObject result = new JSONObject(body);
                            String download = result.getJSONObject("data").getString("download");
                            int version = result.getJSONObject("data").getInt("version");
                            if (BuildConfig.VERSION_CODE < version) {
                                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(SplashScreenActivity.this)
                                        .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                                        .setTitle("Обновления приложения")
                                        .setMessage("Доступна новая версия приложения. Если не обновить, могут возникнуть ошибки.")
                                        .setPositiveText("Обновить сейчас")
                                        .setNegativeText("Позже")
                                        .setOnPositiveClicked((view, dialog) -> {
                                            dialog.dismiss();
                                            PermissionUtils.get(SplashScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                                            new DownloadFile().execute(download);
                                        })
                                        .setOnNegativeClicked((view, dialog) -> {
                                            dialog.dismiss();
                                            openHomePage();
                                        })
                                        .setDecorView(getWindow().getDecorView())
                                        .build();
                                alert.show();
                            } else {
                                openHomePage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        runOnUiThread(() -> Toast.makeText(getBaseContext(), "Ошибка: проверьте подключение к интернету", Toast.LENGTH_LONG).show());
                    }
                });
    }

    private void openHomePage() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(SplashScreenActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("Download Task", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Загружено в: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "произошла ошибка";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
//            Toast.makeText(SplashScreenActivity.this,
//                    message, Toast.LENGTH_LONG).show();
            ApkInstallerUtils.installApplication(SplashScreenActivity.this, folder + fileName);
        }
    }

}
