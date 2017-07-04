package com.example.hd.jitpacktest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hd.cuterecorder.CuteRecorder;

public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnStop;
    private TextView tvOutputFile;
    private TextView tvCurrentLevel;
    private TextView tvRecordTime;

    private CuteRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        btnStart = (Button) findViewById(R.id.btn_recorder_start);
        btnStop = (Button) findViewById(R.id.btn_recorder_stop);
        tvOutputFile = (TextView) findViewById(R.id.tv_output_file);
        tvCurrentLevel = (TextView) findViewById(R.id.tv_voice_level);
        tvRecordTime = (TextView) findViewById(R.id.tv_record_time);
    }

    private void initEvent() {
        String outPutDir = Environment.getExternalStorageDirectory() + "/test_cuteRecorder";
        recorder = new CuteRecorder.Builder()
                .maxTime(60)
                .minTime(3)
                .outPutDir(outPutDir)
                .voiceLevel(CuteRecorder.NORMAL)
                .build();
        recorder.setOnAudioRecordListener(new CuteRecorder.AudioRecordListener() {
            @Override
            public void hasRecord(final int seconds) {
                Log.i("HD", "hasRecord = " + seconds);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvRecordTime.setText("录制时间: " + seconds);
                    }
                });
            }

            @Override
            public void finish(int seconds, final String filePath) {
                Log.i("HD", "finish = " + seconds + "  " + filePath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvOutputFile.setText("输出文件:  " + filePath);
                    }
                });
            }

            @Override
            public void tooShort() {
                Log.i("HD", "tooShort");
                tvOutputFile.setText("录音时间过短");
            }

            @Override
            public void curVoice(final int voice) {
                Log.i("HD", "curVoice = " + voice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCurrentLevel.setText("当前音量:  " + voice);
                    }
                });
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recorder.isPrepared()) {
                    recorder.start();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
            }
        });
    }

}
