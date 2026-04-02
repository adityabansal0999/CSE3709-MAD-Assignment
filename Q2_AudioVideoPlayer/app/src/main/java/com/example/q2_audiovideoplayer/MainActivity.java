package com.example.q2_audiovideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

/**
 * Audio + Video Player App
 * - Plays audio from device
 * - Streams video from URL
 * - Handles clear + replay correctly
 */
public class MainActivity extends AppCompatActivity {

    // ===== AUDIO =====
    Button btnOpenFile, btnAudioPlay, btnAudioPause, btnAudioStop, btnAudioRestart;
    TextView txtFileName, txtAudioStatus;
    SeekBar seekbarAudio;

    MediaPlayer audioPlayer;
    boolean audioReady = false;
    Uri audioUri;

    // ===== VIDEO =====
    Button btnOpenUrl, btnVideoPlay, btnVideoPause, btnVideoStop, btnVideoRestart;
    EditText editUrl;
    VideoView videoView;
    TextView txtVideoStatus;

    boolean videoReady = false;

    // ⭐ IMPORTANT: store last video URL
    String currentVideoUrl = "";

    Handler handler = new Handler();

    ActivityResultLauncher<String> filePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== AUDIO VIEWS =====
        btnOpenFile = findViewById(R.id.btn_open_file);
        btnAudioPlay = findViewById(R.id.btn_audio_play);
        btnAudioPause = findViewById(R.id.btn_audio_pause);
        btnAudioStop = findViewById(R.id.btn_audio_stop);
        btnAudioRestart = findViewById(R.id.btn_audio_restart);
        txtFileName = findViewById(R.id.txt_file_name);
        txtAudioStatus = findViewById(R.id.txt_audio_status);
        seekbarAudio = findViewById(R.id.seekbar_audio);

        // ===== VIDEO VIEWS =====
        btnOpenUrl = findViewById(R.id.btn_open_url);
        btnVideoPlay = findViewById(R.id.btn_video_play);
        btnVideoPause = findViewById(R.id.btn_video_pause);
        btnVideoStop = findViewById(R.id.btn_video_stop);
        btnVideoRestart = findViewById(R.id.btn_video_restart);
        editUrl = findViewById(R.id.edit_url);
        videoView = findViewById(R.id.video_view);
        txtVideoStatus = findViewById(R.id.txt_video_status);

        audioPlayer = new MediaPlayer();

        // File picker
        filePicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        audioUri = result;
                        loadAudio(audioUri);
                    }
                });

        // Default working URL
        editUrl.setText("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");

        setupAudio();
        setupVideo();

        // Video controller UI
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);
    }

    // ===== AUDIO =====

    private void loadAudio(Uri uri) {
        try {
            audioPlayer.reset();
            audioPlayer.setDataSource(this, uri);
            audioPlayer.prepare();

            audioReady = true;
            seekbarAudio.setMax(audioPlayer.getDuration());

            txtFileName.setText("File: " + uri.getLastPathSegment());
            txtAudioStatus.setText("Status: Ready");

        } catch (Exception e) {
            Toast.makeText(this, "Error loading audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAudio() {

        btnOpenFile.setOnClickListener(v -> filePicker.launch("audio/*"));

        btnAudioPlay.setOnClickListener(v -> {
            if (!audioReady) {
                Toast.makeText(this, "Select audio first", Toast.LENGTH_SHORT).show();
                return;
            }
            audioPlayer.start();
            txtAudioStatus.setText("Status: Playing");
            updateSeekBar();
        });

        btnAudioPause.setOnClickListener(v -> {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
                txtAudioStatus.setText("Status: Paused");
            }
        });

        btnAudioStop.setOnClickListener(v -> {
            if (audioReady) {
                audioPlayer.stop();
                txtAudioStatus.setText("Status: Stopped");
                audioReady = false;

                if (audioUri != null) loadAudio(audioUri);
            }
        });

        btnAudioRestart.setOnClickListener(v -> {
            if (audioReady) {
                audioPlayer.seekTo(0);
                audioPlayer.start();
                txtAudioStatus.setText("Status: Playing");
            }
        });

        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (fromUser && audioReady) {
                    audioPlayer.seekTo(progress);
                }
            }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    private void updateSeekBar() {
        if (audioPlayer.isPlaying()) {
            seekbarAudio.setProgress(audioPlayer.getCurrentPosition());
            handler.postDelayed(this::updateSeekBar, 500);
        }
    }

    // ===== VIDEO =====

    private void loadVideo(String url) {
        try {
            Uri uri = Uri.parse(url);

            videoView.setVideoURI(uri);
            txtVideoStatus.setText("Status: Loading...");

            videoView.setOnPreparedListener(mp -> {
                videoReady = true;
                txtVideoStatus.setText("Status: Ready");

                videoView.start();
                videoView.pause();
            });

            videoView.setOnCompletionListener(mp ->
                    txtVideoStatus.setText("Status: Finished"));

            videoView.setOnErrorListener((mp, what, extra) -> {
                txtVideoStatus.setText("Status: Error");
                Toast.makeText(this,
                        "Video not supported / network issue",
                        Toast.LENGTH_LONG).show();
                videoReady = false;
                return true;
            });

            videoView.requestFocus();

        } catch (Exception e) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupVideo() {

        // OPEN URL
        btnOpenUrl.setOnClickListener(v -> {
            String url = editUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Enter URL", Toast.LENGTH_SHORT).show();
                return;
            }

            currentVideoUrl = url; // ⭐ SAVE URL
            loadVideo(url);
        });

        // PLAY
        btnVideoPlay.setOnClickListener(v -> {

            // ⭐ reload if cleared
            if (!videoReady && !currentVideoUrl.isEmpty()) {
                loadVideo(currentVideoUrl);
                return;
            }

            if (videoReady) {
                videoView.start();
                txtVideoStatus.setText("Status: Playing");
            }
        });

        // PAUSE
        btnVideoPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                txtVideoStatus.setText("Status: Paused");
            }
        });

        // STOP + CLEAR
        btnVideoStop.setOnClickListener(v -> {
            if (videoReady) {
                videoView.stopPlayback();
                videoView.setVideoURI(null);

                txtVideoStatus.setText("Status: Cleared");
                videoReady = false;
            }
        });

        // RESTART
        btnVideoRestart.setOnClickListener(v -> {
            if (videoReady) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
        }
    }
}