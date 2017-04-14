package com.bskup.donotdisturbtiles;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

/**
 * Created on 9/21/2016.
 */
public class AlarmsTileService extends TileService {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("android.app.action.INTERRUPTION_FILTER_CHANGED")){
                // Do this when notification policy changed
                setAlarmsIconToMatchCurrentState();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");

        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("AlarmsTileService", "onDestroy called");
        unregisterReceiver(receiver);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.v("AlarmsTileService", "onTileAdded called");
        setAlarmsIconToMatchCurrentState();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.v("AlarmsTileService", "onTileRemoved called");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.v("AlarmsTileService", "onStartListening called");
        setAlarmsIconToMatchCurrentState();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.v("AlarmsTileService", "onStopListening called");
    }

    @Override
    public void onClick() {
        super.onClick();
        toggleAlarmsTile();
    }

    // Helper method to check and update tile and toggle do not disturb state
    public void toggleAlarmsTile() {
        Icon icon;
        Tile tile = getQsTile();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.v("AlarmsTileService", "toggleAlarmsTile called");
        int currentDoNotDisturbStatus = nm.getCurrentInterruptionFilter();
        // If Alarms Only is off, turn it on
        if (currentDoNotDisturbStatus != NotificationManager.INTERRUPTION_FILTER_ALARMS) {
            nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
            Log.v("SilenceTileService", "set interruption filter to INTERRUPTION_FILTER_ALARMS - only Alarms get through");

            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qs_dnd_on);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
            // If Alarms Only is on, turn it off
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
    public void setAlarmsIconToMatchCurrentState() {
        Tile tile = getQsTile();
        Icon icon;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int currentInterruptionFilter = nm.getCurrentInterruptionFilter();
        if (currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALARMS) {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qs_dnd_off);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        } else {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qs_dnd_on);
            tile.setIcon(icon);
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }
}