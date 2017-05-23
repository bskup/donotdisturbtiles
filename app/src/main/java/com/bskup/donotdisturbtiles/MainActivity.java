package com.bskup.donotdisturbtiles;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;

    public void openAccessControl(View v) {
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("MainActivity", "onCreate called");

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        TextView mainTextView = (TextView) findViewById(R.id.main_text_view);
        TextView notificationPolicyAccessStatusTextView = (TextView) findViewById(R.id.notification_policy_access_status_text_view);
        Button goButton = (Button) findViewById(R.id.go_button);

        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            goButton.setVisibility(View.VISIBLE);
            mainTextView.setText(R.string.policy_control_disabled);
            notificationPolicyAccessStatusTextView.setText(R.string.disabled);
        } else {
            goButton.setVisibility(View.GONE);
            mainTextView.setText(R.string.policy_control_enabled);
            notificationPolicyAccessStatusTextView.setText(R.string.enabled);
        }

        // Find toolbar in layout and set it as support action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        // Get support action bar corresponding to this toolbar
        ActionBar myActionBar = getSupportActionBar();
        // Turn on the title of that action bar
        myActionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        TextView mainTextView = (TextView) findViewById(R.id.main_text_view);
        TextView notificationPolicyAccessStatusTextView = (TextView) findViewById(R.id.notification_policy_access_status_text_view);
        Button goButton = (Button) findViewById(R.id.go_button);

        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            goButton.setVisibility(View.VISIBLE);
            mainTextView.setText(R.string.policy_control_disabled);
            notificationPolicyAccessStatusTextView.setText(R.string.disabled);
        } else {
            goButton.setVisibility(View.GONE);
            mainTextView.setText(R.string.policy_control_enabled);
            notificationPolicyAccessStatusTextView.setText(R.string.enabled);
        }
    }
}
