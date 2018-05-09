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

    void showAuthDialog(String[] args) {
        FragmentTransaction ft = cordova.getActivity().getFragmentManager().beginTransaction();
        Fragment prev = cordova.getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = UserInfoAuthorizationDialogFragment.newInstance(args);
        newFragment.show(ft, "dialog");
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
//        args.getString()
        int argsLength = args.length();
        String[] argsInString = new String[argsLength];
        for (int i = 0; i < argsLength; i++) {
            argsInString[i] = args.getString(i);
        }
        if ("getUserInfo".equals(action)) {
            showAuthDialog(argsInString);
            return true;
        }
        return false;
    }
}
