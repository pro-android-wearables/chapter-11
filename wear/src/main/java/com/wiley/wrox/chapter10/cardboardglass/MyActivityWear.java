package com.wiley.wrox.chapter10.cardboardglass;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class MyActivityWear extends Activity {

    private GoogleApiClient mGoogleApiClient;

    private TextView mTextView;
    private int mColor = Color.rgb(255,255,255);
    private boolean mTouch = false;
    private static final  String TAG = "wrox-wear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wear);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.v(TAG, "Connection established");
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.v(TAG, "Connection suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                   @Override
                   public void onConnectionFailed(ConnectionResult result) {
                       Log.v(TAG, "Connection failed");
                   }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new
        WatchViewStub.OnLayoutInflatedListener() {
         @Override
         public void onLayoutInflated(WatchViewStub stub) {
             mTextView = (TextView) stub.findViewById(R.id.text);

             stub.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View view, MotionEvent event) {
                     Log.v(TAG, "UI touched");
                     toggleBackgroundColor();

                     if(mGoogleApiClient==null)
                         return false;

                     final PutDataMapRequest putRequest = PutDataMapRequest.create("/WEAR2PHONE");
                     final DataMap map = putRequest.getDataMap();
                     mTouch = !mTouch;
                     map.putBoolean("touch", mTouch);
                     Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());

                     return false;
                 }
             });
         }
        });
    }

    private void toggleBackgroundColor(){
        if (mColor == Color.rgb(0, 0, 0))
            mColor = Color.rgb(255, 255, 255);
        else
            mColor = Color.rgb(0, 0, 0);
        setBackgroundColor(mColor);
    }

    private void setBackgroundColor(int color) {
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setBackgroundColor(color);
    }

}
