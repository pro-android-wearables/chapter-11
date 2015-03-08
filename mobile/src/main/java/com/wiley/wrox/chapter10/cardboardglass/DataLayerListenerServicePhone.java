package com.wiley.wrox.chapter10.cardboardglass;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class DataLayerListenerServicePhone extends WearableListenerService {

    private static String TAG = "wrox-mobile";
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        Log.v(TAG, "Data arrived");

        final List<DataEvent> events =
                FreezableUtils.freezeIterable(dataEvents);
        for(DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();
            final String path = uri!=null ? uri.getPath() : null;
            if("/WEAR2PHONE".equals(path)) {
                final DataMap map =
                        DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                // read your values from map:
                boolean touch = map.getBoolean("touch");
                String reply = "Touched:" + touch;
                Log.v(TAG, reply);
                // if there was a touch, trigger the event detection
                Intent localIntent = new Intent("cardboard.localIntent");
                localIntent.putExtra("result", touch);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        }
    }
}