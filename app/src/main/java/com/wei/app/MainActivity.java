package com.wei.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_start;

    private float progress;
    private ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = (ProgressView) findViewById(R.id.progress_view);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            while ((progress += 0.01) < 1) {
                                Thread.sleep(200L);
                                runOnUiThread(new MyRunnable(progress, progressView));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    public final static class MyRunnable implements Runnable {
        private ProgressView progressView;
        private float progress;

        public MyRunnable(float progress, ProgressView progressView) {
            this.progress = progress;
            this.progressView = progressView;

        }

        @Override
        public void run() {
            progress += 0.01;
            progressView.setData(progress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_start.setOnClickListener(null);
    }
}
