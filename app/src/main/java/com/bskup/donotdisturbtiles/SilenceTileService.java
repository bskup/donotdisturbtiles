package com.bskup.donotdisturbtiles;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.IntDef;
import android.util.Log;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created on 9/21/2016.
 */
public class SilenceTileService extends TileService {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("SilenceTileService", "onReceive called");
            String action = intent.getAction();
            if(action.equals("android.app.action.INTERRUPTION_FILTER_CHANGED")){
                // Do this when notification policy changed
                setSilenceIconToMatchCurrentState();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("SilenceTileService", "onCreate called");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");

        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("SilenceTileService", "onDestroy called");
        unregisterReceiver(receiver);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.v("SilenceTileService", "onTileAdded called");

        setSilenceIconToMatchCurrentState();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.v("SilenceTileService", "onTileRemoved called");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.v("SilenceTileService", "onStartListening called");
        setSilenceIconToMatchCurrentState();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.v("SilenceTileService", "onStopListening called");
    }

    @Override
    public void onClick() {
        super.onClick();
        toggleSilenceTile();
    }

    // Helper method to check and update tile and toggle do not disturb state
    public void toggleSilenceTile() {
        Icon icon;
        Tile tile = getQsTile();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.v("SilenceTileService", "toggleSilenceTile called");
        int currentDoNotDisturbStatus = nm.getCurrentInterruptionFilter();
        // If Total Silence is off, turn it on
        if (currentDoNotDisturbStatus != NotificationManager.INTERRUPTION_FILTER_NONE) {

            nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            Log.v("SilenceTileService", "set interruption filter to INTERRUPTION_FILTER_NONE - none get through");

            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_dnd_total_silence);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
            // If Total Silence is on, turn it off
        } else {
            nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            Log.v("SilenceTileService", "set interruption filter to INTERRUPTION_FILTER_ALL - all get through");

            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qs_dnd_off);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    // Helper method to set icon state to match current do not disturb state without toggling DND
    public void setSilenceIconToMatchCurrentState() {
        Tile tile = getQsTile();
        Icon icon;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int currentInterruptionFilter = nm.getCurrentInterruptionFilter();
        if (currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_NONE) {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qs_dnd_off);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        } else {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_dnd_total_silence);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }
}