package com.example.q2_audiovideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // audio views
    Button btnOpenFile, btnAudioPlay, btnAudioPause, btnAudioStop, btnAudioRestart;
    TextView txtFileName, txtAudioStatus;
    SeekBar seekbarAudio;

    // video views
    Button btnOpenUrl, btnVideoPlay, btnVideoPause, btnVideoStop, btnVideoRestart;
    EditText editUrl;
    VideoView videoView;
    TextView txtVideoStatus;

    // MediaPlayer for audio
    MediaPlayer audioPlayer;
    boolean audioReady = false;
    Uri audioUri;

    // handler for updating seekbar
    Handler handler = new Handler();

    // video state
    boolean videoReady = false;

    // file picker launcher
    ActivityResultLauncher<String> filePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // find audio views
        btnOpenFile = findViewById(R.id.btn_open_file);
        btnAudioPlay = findViewById(R.id.btn_audio_play);
        btnAudioPause = findViewById(R.id.btn_audio_pause);
        btnAudioStop = findViewById(R.id.btn_audio_stop);
        btnAudioRestart = findViewById(R.id.btn_audio_restart);
        txtFileName = findViewById(R.id.txt_file_name);
        txtAudioStatus = findViewById(R.id.txt_audio_status);
        seekbarAudio = findViewById(R.id.seekbar_audio);

        // find video views
        btnOpenUrl = findViewById(R.id.btn_open_url);
        btnVideoPlay = findViewById(R.id.btn_video_play);
        btnVideoPause = findViewById(R.id.btn_video_pause);
        btnVideoStop = findViewById(R.id.btn_video_stop);
        btnVideoRestart = findViewById(R.id.btn_video_restart);
        editUrl = findViewById(R.id.edit_url);
        videoView = findViewById(R.id.video_view);
        txtVideoStatus = findViewById(R.id.txt_video_status);

        // create MediaPlayer
        audioPlayer = new MediaPlayer();

        // setup file picker to choose audio from device
        filePicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        audioUri = result;
                        loadAudio(audioUri);
                    }
                }
        );

        // pre-fill a sample video URL for testing
        editUrl.setText(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

        // setup all button listeners
        setupAudioButtons();
        setupVideoButtons();
        setupSeekBar();
    }

    // loads audio file into MediaPlayer
    private void loadAudio(Uri uri) {
        try {
            audioPlayer.reset();
            audioPlayer.setDataSource(this, uri);
            audioPlayer.prepare();
            audioReady = true;

            seekbarAudio.setMax(audioPlayer.getDuration());
            seekbarAudio.setProgress(0);

            txtFileName.setText("File: " + uri.getLastPathSegment());
            txtAudioStatus.setText("Status: Ready");
            Toast.makeText(this, "Audio loaded", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Error loading audio", Toast.LENGTH_SHORT).show();
            audioReady = false;
        }
    }

    // all audio button click listeners
    private void setupAudioButtons() {

        // open file picker
        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker.launch("audio/*");
            }
        });

        // play
        btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioReady) {
                    Toast.makeText(MainActivity.this,
                            "Open an audio file first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!audioPlayer.isPlaying()) {
                    audioPlayer.start();
                    txtAudioStatus.setText("Status: Playing");
                    updateSeekBar();
                }
            }
        });

        // pause
        btnAudioPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                    txtAudioStatus.setText("Status: Paused");
                }
            }
        });

        // stop
        btnAudioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioReady) {
                    audioPlayer.stop();
                    seekbarAudio.setProgress(0);
                    txtAudioStatus.setText("Status: Stopped");
                    audioReady = false;

                    // after stop we need to reload the file
                    if (audioUri != null) {
                        loadAudio(audioUri);
                    }
                }
            }
        });

        // restart from beginning
        btnAudioRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioReady) {
                    audioPlayer.seekTo(0);
                    if (!audioPlayer.isPlaying()) {
                        audioPlayer.start();
                    }
                    txtAudioStatus.setText("Status: Playing");
                    updateSeekBar();
                }
            }
        });
    }

    // seekbar listener so user can drag to change position
    private void setupSeekBar() {
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && audioReady) {
                    audioPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // keeps updating seekbar position while audio plays
    private void updateSeekBar() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            seekbarAudio.setProgress(audioPlayer.getCurrentPosition());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            }, 500);
        }
    }

    // loads video from url into VideoView
    private void loadVideo(String url) {
        try {
            Uri videoUri = Uri.parse(url);
            videoView.setVideoURI(videoUri);
            txtVideoStatus.setText("Status: Loading...");

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoReady = true;
                    txtVideoStatus.setText("Status: Ready");
                    Toast.makeText(MainActivity.this,
                            "Video loaded", Toast.LENGTH_SHORT).show();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    txtVideoStatus.setText("Status: Finished");
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(MainActivity.this,
                            "Error loading video", Toast.LENGTH_SHORT).show();
                    txtVideoStatus.setText("Status: Error");
                    videoReady = false;
                    return true;
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    // all video button click listeners
    private void setupVideoButtons() {

        // open url
        btnOpenUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Enter a video URL", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadVideo(url);
            }
        });

        // play
        btnVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoReady) {
                    Toast.makeText(MainActivity.this,
                            "Load a video first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!videoView.isPlaying()) {
                    videoView.start();
                    txtVideoStatus.setText("Status: Playing");
                }
            }
        });

        // pause
        btnVideoPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    txtVideoStatus.setText("Status: Paused");
                }
            }
        });

        // stop
        btnVideoStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoReady) {
                    videoView.stopPlayback();
                    txtVideoStatus.setText("Status: Stopped");
                    videoReady = false;
                }
            }
        });

        // restart
        btnVideoRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoReady) {
                    videoView.seekTo(0);
                    if (!videoView.isPlaying()) {
                        videoView.start();
                    }
                    txtVideoStatus.setText("Status: Playing");
                } else {
                    Toast.makeText(MainActivity.this,
                            "Load a video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // pause audio when app goes to background
    @Override
    protected void onPause() {
        super.onPause();
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.pause();
            txtAudioStatus.setText("Status: Paused");
        }
    }

    // release resources when activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}