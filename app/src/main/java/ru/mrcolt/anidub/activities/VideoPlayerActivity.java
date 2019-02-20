package ru.mrcolt.anidub.activities;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import ru.mrcolt.anidub.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private int rewindTime = 10000;
    private ProgressBar loading;
    private ImageButton player_forward, player_backward, player_fullscreen, player_back, player_zoom;
    private TextView player_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.video_view);
        loading = findViewById(R.id.loading);
        player_back = findViewById(R.id.player_back);
        player_title = findViewById(R.id.player_title);
        player_fullscreen = findViewById(R.id.player_fullscreen);
        player_forward = findViewById(R.id.player_forward);
        player_backward = findViewById(R.id.player_backward);
        player_zoom = findViewById(R.id.player_zoom);
        player_title.setText(getIntentAsString("title"));
        player_forward.setOnClickListener(v -> player.seekTo(currentWindow, player.getContentPosition() + rewindTime));
        player_backward.setOnClickListener(v -> player.seekTo(currentWindow, player.getContentPosition() - rewindTime));
        player_back.setOnClickListener(v -> onBackPressed());
        player_fullscreen.setOnClickListener(v -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                player_fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                player_fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        player_zoom.setOnClickListener(v -> {
            if (playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_FIT) {
                player_zoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_zoom_in));
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            } else {
                player_zoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_zoom_out));
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }
        });
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private String getIntentAsString(String key) {
        return getIntent().getExtras().getString(key);
    }

    private void initializePlayer() {
        //--------------------------------------
        //Creating default track selector
        //and init the player
        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(null);
        player = ExoPlayerFactory.newSimpleInstance(
                this,
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelection),
                new DefaultLoadControl());

        //init the player
        playerView.setPlayer(player);

        //-------------------------------------------------
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0", null);
        httpDataSourceFactory.getDefaultRequestProperties().set("Referer", "https://anime.anidub.com/");
//        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,
                null, httpDataSourceFactory);

        //-----------------------------------------------
        //Create media source
        String hls_url = getIntentAsString("url");
        Uri uri = Uri.parse(hls_url);

        MediaSource mediaSource = new HlsMediaSource(uri,
                dataSourceFactory, null, null);
        player.prepare(mediaSource);


        player.setPlayWhenReady(playWhenReady);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case Player.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        player.seekTo(currentWindow, playbackPosition);
        playerView.setKeepScreenOn(true);
        player.prepare(mediaSource, true, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();
        player.release();
        player = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        Animatoo.animateZoom(this);
    }
}
