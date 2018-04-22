package com.appdiscovery.app.cordovaPlugins;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.appdiscovery.app.UserInfoAuthorizationDialogFragment;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class Auth extends CordovaPlugin {

    void showAuthDialog() {
        FragmentTransaction ft = cordova.getActivity().getFragmentManager().beginTransaction();
        Fragment prev = cordova.getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = UserInfoAuthorizationDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("getUserInfo".equals(action)) {
            showAuthDialog();
            return true;
        }
        return false;
    }
}
