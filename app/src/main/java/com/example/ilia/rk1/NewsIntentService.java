package com.example.ilia.rk1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.ResultReceiver;
import android.widget.Toast;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NewsIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_REQUEST = "REQUEST";

    // TODO: Rename parameters

    public static final String EXTRA_TOPIC = "TOPIC";

    public static final String EXTRA_RESULT_RECEIVER = "resultReceiver";

    public static final int EXTRA_ERROR = 0;
    public static final int EXTRA_SUCCESS = 1;

    public NewsIntentService() {
        super("NewsIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REQUEST.equals(action)) {
                final String topic = intent.getStringExtra(EXTRA_TOPIC);
                final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
                handleActionRequest(topic, receiver);
            }
        }
    }

    private void handleActionRequest(String topic, ResultReceiver receiver) {
        try {
            NewsLoader nl = new NewsLoader();
            News news = nl.loadNews(topic);
            Storage.getInstance(this).saveNews(news);
            receiver.send(EXTRA_SUCCESS, null);

        } catch (IOException ex) {
            Toast.makeText(this, "Server error", Toast.LENGTH_SHORT).show();
            receiver.send(EXTRA_ERROR, null);
        }
    }

}
