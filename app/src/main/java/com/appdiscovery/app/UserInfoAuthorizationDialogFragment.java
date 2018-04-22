package com.appdiscovery.app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UserInfoAuthorizationDialogFragment extends DialogFragment {
    public static UserInfoAuthorizationDialogFragment newInstance() {
        UserInfoAuthorizationDialogFragment f = new UserInfoAuthorizationDialogFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        // Watch for button clicks.
        Button button = v.findViewById(R.id.show);
        button.setOnClickListener(v1 -> {
            dismiss();
        });
        return v;
    }
}
