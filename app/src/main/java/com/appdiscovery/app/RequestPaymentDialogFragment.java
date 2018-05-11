package com.appdiscovery.app;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        mPaymentParams = new PaymentParams(args.getInt("amountToPay"), args.getString("orderId"), args.getString("orderTitle"), args.getString("orderDescription"));

        View v = inflater.inflate(R.layout.fragment_payment_dialog, container, false);

        Button acceptButton = v.findViewById(R.id.accept_payment_request);
        Button rejectButton = v.findViewById(R.id.reject_payment_request);

        DecimalFormat df = new DecimalFormat("####0.00");
        v.<TextView>findViewById(R.id.payment_amount_text).setText(String.format("付款金额: ￥%s", df.format((float) mPaymentParams.amountToPay / 100)));
        v.<TextView>findViewById(R.id.payment_title_text).setText(String.format("付款标题: %s", mPaymentParams.orderTitle));
        v.<TextView>findViewById(R.id.payment_description_text).setText(String.format("付款描述: %s", mPaymentParams.orderDescription));

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
        public int amountToPay;
        public String orderId;
        public String orderTitle;
        public String orderDescription;

        public PaymentParams(int amountToPay, String orderId, String orderTitle, String orderDescription) {
            this.amountToPay = amountToPay;
            this.orderId = orderId;
            this.orderTitle = orderTitle;
            this.orderDescription = orderDescription;
        }
    }
}
