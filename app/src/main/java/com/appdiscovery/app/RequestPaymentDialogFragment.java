package com.appdiscovery.app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RequestPaymentDialogFragment extends DialogFragment {
    PaymentParams mPaymentParams;
    public View.OnClickListener mOnAccept;
    public View.OnClickListener mOnReject;

    public static RequestPaymentDialogFragment newInstance(Bundle bundle) {
        RequestPaymentDialogFragment f = new RequestPaymentDialogFragment();
        f.setArguments(bundle);
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

        Bundle args = getArguments();
        mPaymentParams = new PaymentParams(args.getInt("amountToPay"), args.getString("orderId"), args.getString("orderTitle"), args.getString("orderDescription"));

        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        Button acceptButton = v.findViewById(R.id.accept_auth_request);
        Button rejectButton = v.findViewById(R.id.reject_auth_request);
        TextView tv = v.findViewById(R.id.auth_hint_text);
        StringBuilder hintBuilder = new StringBuilder();
        hintBuilder.append("APP请求付款");
        tv.setText(hintBuilder.toString());
        acceptButton.setOnClickListener(v1 -> {
            mOnAccept.onClick(v1);
            dismiss();
        });
        rejectButton.setOnClickListener(v1 -> {
            mOnReject.onClick(v1);
            dismiss();
        });
        return v;
    }

    private class PaymentParams {
        public PaymentParams(int amountToPay, String orderId, String orderTitle, String orderDescription) {
        }
    }
}
