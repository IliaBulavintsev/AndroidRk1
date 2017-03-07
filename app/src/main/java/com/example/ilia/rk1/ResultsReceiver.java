package com.example.ilia.rk1;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class ResultsReceiver extends ResultReceiver {

    public interface Receiver {
        public void onCallbackActivity(int resultCode, Bundle data);
    }

    private Receiver mReceiver;

    public ResultsReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onCallbackActivity(resultCode, resultData);
        } else {
            Log.d("12333", "onReceiveResult: ");
        }
    }
}