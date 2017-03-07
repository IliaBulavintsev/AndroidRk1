package com.example.ilia.rk1;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;
import java.util.prefs.BackingStoreException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class MainActivity extends AppCompatActivity implements ResultsReceiver.Receiver {

    private Button mButtonUpdateNews;
    private Button mButtonSettings;
    private Button mButtonBack;
    private Button mButtonNoBack;
    private TextView mNewsText;
    private TextView mNewsDate;
    private TextView mNewsTitle;

    private Storage mNewsStorage;
    private News news;
    private ServiceHelper serviceHelper;
    private String topic;

    private ResultsReceiver mReceiver ;
    private Scheduler scheduler;

    public static final  String BACKGROUND = "BACKGROUND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsStorage = Storage.getInstance(this);
        scheduler = Scheduler.getInstance();
        mNewsText = (TextView) findViewById(R.id.news_text);
        mNewsDate = (TextView) findViewById(R.id.news_date);
        mNewsTitle = (TextView) findViewById(R.id.news_title);
        mButtonSettings = (Button) findViewById(R.id.settings);
        mButtonBack = (Button) findViewById(R.id.update_back);
        mButtonNoBack = (Button) findViewById(R.id.update_no_back);


        mReceiver = new ResultsReceiver(new Handler());
        mReceiver.setReceiver(this);

        mButtonUpdateNews = (Button) findViewById(R.id.update_button);
        mButtonUpdateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceHelper.requestNews(MainActivity.this, topic, mReceiver);
            }
        });

        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBack(true);
            }
        });

        mButtonNoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBack(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new ResultsReceiver(new Handler());
        mReceiver.setReceiver(this);
        topic = mNewsStorage.loadCurrentTopic();
        mButtonUpdateNews.setText("Обновить "+ topic);

        serviceHelper = ServiceHelper.getInstance();
        serviceHelper.requestNews(this, topic, mReceiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mReceiver.setReceiver(null);
    }

    @Override
    public void onCallbackActivity(int resultCode, Bundle data) {
        if (NewsIntentService.EXTRA_SUCCESS == resultCode) {
            try{
                News news = mNewsStorage.getLastSavedNews();
                Log.d("123456", news.getTitle());
                mNewsTitle.setText(news.getTitle());
                mNewsText.setText(news.getBody());
                mNewsDate.setText(String.format("%s", (new Date(news.getDate() * 1000))));

            } catch (NullPointerException ex) {
                Toast.makeText(this, "Toast NUll error from Activity", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "ERROR INTERNET ERROR MAYBE", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleBack(boolean update){
        Intent intent = new Intent(this, NewsIntentService.class);
        Scheduler scheduler = Scheduler.getInstance();
        intent.setAction("REQUEST");
        intent.putExtra(NewsIntentService.EXTRA_TOPIC, topic);
        intent.putExtra(NewsIntentService.EXTRA_RESULT_RECEIVER, mReceiver);
        if (update) {
            scheduler.schedule(this, intent, 60000L);
            Log.d("12345", "START BACKGROUND");
        }
        else {
            scheduler.unschedule(this, intent);
            Log.d("12345", "STOP BACKGROUND");
        }
    }
}
