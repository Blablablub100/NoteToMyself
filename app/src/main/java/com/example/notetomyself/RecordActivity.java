package com.example.notetomyself;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Movie;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.example.notetomyself.Storage.Memo;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private final static int PERMISSION_STORAGE_AUDIO = 300;

    private ImageView playButton;
    private ImageView deleteButton;
    private ImageView saveButton;

    File currRecording;
    MediaRecorder recorder;
    enum states {
        NO_INIT, RECORDING, PAUSED, STOPPED, SAVED
    }
    states state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("_____________ONCREATE CALLED");
        state = states.NO_INIT;
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();           // for hiding action bar
        setContentView(R.layout.activity_record);
        setViewVariables();
        checkPermissions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("_____________CALLED ON DESTROY");
        if (state != states.SAVED) {
            stopRecording();
            deleteTempMemo();
        }
    }

    void checkPermissions() {
        // Record permissions
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.RECORD_AUDIO
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , PERMISSION_STORAGE_AUDIO);

        } else {
            startRecording();
        }
    }

    void startRecording() {
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {

            playButton.setImageDrawable(getDrawable(R.drawable.img_pause));

            File tmp = Util.createAudioFile(this);
            if (tmp == null) return;

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(tmp.getAbsolutePath());
            try {
                recorder.prepare();
            } catch (IOException e) {
                Util.showSnackBar(this, getString(R.string.err_unable_to_open_file));
                e.printStackTrace();
            }
            currRecording = tmp;
            recorder.start();
            state = states.RECORDING;
        }
    }

    void pauseRecording() {
        playButton.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        state = states.PAUSED;
        recorder.pause();
    }

    void resumeRecording() {
        playButton.setImageDrawable(getDrawable(R.drawable.img_pause));
        state = states.RECORDING;
        recorder.resume();
    }

    void stopRecording() {
        playButton.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        if (state == states.STOPPED) return;
        recorder.stop();
        recorder.reset();
        recorder.release();
        state = states.STOPPED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_STORAGE_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Util.showSnackBar(this
                            , getString(R.string.err_no_storage_audio_permission));
                }
            }
        }
    }

    void setViewVariables() {
        playButton = findViewById(R.id.imageView_play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO DO THIS BETTER
                if (state == states.NO_INIT) {
                    startRecording();

                } else if (state == states.RECORDING) {
                    pauseRecording();

                } else if (state == states.PAUSED) {
                    resumeRecording();

                } else  if (state == states.STOPPED) {
                    Util.showSnackBar(RecordActivity.this
                            , getString(R.string.info_recording_already_stopped));

                } else {
                    Util.showSnackBar(RecordActivity.this
                            , getString(R.string.err_something_went_wrong));
                }
            }
        });
        deleteButton = findViewById(R.id.imageView_discard_memo);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTempMemo();
            }
        });
        saveButton = findViewById(R.id.imageView_save_memo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMemo();
            }
        });

    }


    void deleteTempMemo() {
        if (state == states.NO_INIT) {
            Util.showSnackBar(this, getString(R.string.info_delete_nothing));
            return;
        }
        stopRecording();
        if (currRecording == null) return;
        currRecording.delete();
        currRecording = null;
        state = states.NO_INIT;
        Util.showSnackBar(this, getString(R.string.info_discarded_recordings));
    }


    void saveMemo() {
        if (state == states.RECORDING || state == states.PAUSED) {
            stopRecording();
            showDialog();
        } else if (state == states.STOPPED) {
            showDialog();
        } else if (state == states.SAVED) {
            // TODO what should heppen here?
        } else if (state == states.NO_INIT) {
            Util.showSnackBar(this, getString(R.string.info_save_no_recording));
        } else {
            Util.showSnackBar(this, getString(R.string.err_something_went_wrong));
        }
    }

    void showDialog() {
        EditMemoDialog dialog = new EditMemoDialog(new Memo(null
                , currRecording
                , Calendar.getInstance().getTime()
                , false
                , null), this);
    }
}
