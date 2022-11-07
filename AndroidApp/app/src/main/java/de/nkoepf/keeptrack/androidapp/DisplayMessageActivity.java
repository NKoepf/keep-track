package de.nkoepf.keeptrack.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.nkoepf.keeptrack.androidapp.ui.login.LoginActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String userName = intent.getStringExtra(LoginActivity.USER_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.welcome_message);
        textView.setText(String.format(getString(R.string.user_logged_in_message), userName));

        View storageOverViewButton = findViewById(R.id.addStorageButton);

    }


}