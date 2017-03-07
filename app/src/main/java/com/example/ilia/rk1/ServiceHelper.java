package com.example.ilia.rk1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;

import ru.mail.weather.lib.Topics;

public class ServiceHelper {
    private static ServiceHelper ourInstance = new ServiceHelper();
    private ResultsReceiver mResultReceiver = new ResultsReceiver(new Handler());

    public static ServiceHelper getInstance() {
        return ourInstance;
    }

    private ServiceHelper() {
    }


    public void requestNews(Context context, String topic, ResultReceiver mReceiver){
        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction("REQUEST");
        intent.putExtra(NewsIntentService.EXTRA_TOPIC, topic);
        intent.putExtra(NewsIntentService.EXTRA_RESULT_RECEIVER, mReceiver);
        context.startService(intent);
    }
}