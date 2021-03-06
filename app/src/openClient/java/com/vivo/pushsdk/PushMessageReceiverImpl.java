package com.vivo.pushsdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    @Override
    public void onReceiveRegId(Context context, String s) {
        Log.d("tanliang", " onReceiveRegId= " + s);
    }

    @Override
    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
        super.onTransmissionMessage(context, unvarnishedMessage);
        Toast.makeText(context, " 收到透传通知： " + unvarnishedMessage.getMessage(), Toast.LENGTH_LONG).show();
        Log.d("OpenClientPushMessageReceiverImpl", " onTransmissionMessage= " + unvarnishedMessage.getMessage());
    }

}
