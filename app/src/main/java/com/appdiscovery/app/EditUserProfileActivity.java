package com.appdiscovery.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserProfileActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    EditText mUserProfileNameInput;
    EditText mUserProfileEmailInput;
    EditText mUserProfileMobileInput;
    Button mSubmitButton;

    EditUserProfileActivity() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        setContentView(R.layout.edit_user_profile);
        mUserProfileNameInput = findViewById(R.id.userProfileNameInput);
        mUserProfileEmailInput = findViewById(R.id.userProfileEmailInput);
        mUserProfileMobileInput = findViewById(R.id.userProfileMobileInput);

        mUserProfileNameInput.setText(sharedPref.getString(getString(R.string.user_name_key), ""));
        mUserProfileEmailInput.setText(sharedPref.getString(getString(R.string.user_email_key), ""));
        mUserProfileMobileInput.setText(sharedPref.getString(getString(R.string.user_mobile_key), ""));

        mSubmitButton = findViewById(R.id.userProfileSubmitButton);
        mSubmitButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.user_name_key), mUserProfileNameInput.getText().toString());
            editor.putString(getString(R.string.user_email_key), mUserProfileEmailInput.getText().toString());
            editor.putString(getString(R.string.user_mobile_key), mUserProfileMobileInput.getText().toString());
            editor.commit();

            Toast.makeText(EditUserProfileActivity.this, "用户信息已经更新", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
