package ru.mrcolt.anidub.activities;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import ru.mrcolt.anidub.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private int currentWindow;
    private long playbackPosition;
    private boolean playWhenReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.video_view);
        try {
            initComponents();
            configComponents();
            initPlayer();
            hideUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() throws Exception {
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultRenderersFactory(this.getApplicationContext(), DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER), new DefaultTrackSelector());
    }

    private void configComponents() {
        playerView.setPlayer(player);
        playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        playerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        player.setPlayWhenReady(true);
        player.seekTo(currentWindow, playbackPosition);
    }

    private void initPlayer() {
        String valor = getIntent().getExtras().getString("url");
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0", null);
        httpDataSourceFactory.getDefaultRequestProperties().set("Referer", "https://anime.anidub.com");
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, null, httpDataSourceFactory);
        HlsMediaSource archivoMultimedia = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(valor));
        player.prepare(archivoMultimedia);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            configComponents();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            configComponents();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }
}
