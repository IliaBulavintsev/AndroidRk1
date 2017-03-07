package com.example.ilia.rk1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class Main2Activity extends AppCompatActivity {

    private Storage mNewsStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mNewsStorage = Storage.getInstance(this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main2);
        for(String topic: Topics.ALL_TOPICS) {

            final Button ButtonTopic = new Button(this);
            ButtonTopic.setText(topic);


            ButtonTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mNewsStorage.saveCurrentTopic(ButtonTopic.getText().toString());
                    finish();
                }
            });

            layout.addView(ButtonTopic);
        }
    }
}
