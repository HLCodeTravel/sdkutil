package com.transsion.sdk.demo.subfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transsion.core.deviceinfo.DeviceInfo;
import com.transsion.sdk.demo.R;

public class ClientIDFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClientIDFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clientid,
                container, false);

        String strImei = DeviceInfo.getIMEI();
        String gaId = DeviceInfo.getGAId();
        String androidId = DeviceInfo.getAndroidID();
        String strImsi = DeviceInfo.getIMSI();

        ((TextView) rootView.findViewById(R.id.textView_IMEI)).setText(strImei);
        ((TextView) rootView.findViewById(R.id.textView_GAID)).setText(gaId);
        ((TextView) rootView.findViewById(R.id.textView_AndroidID)).setText(androidId);
        ((TextView) rootView.findViewById(R.id.textView_IMSI)).setText(strImsi);
        return rootView;
    }

}
