package com.transsion.sdk.demo.subfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transsion.json.Tson;
import com.transsion.sdk.demo.Constant;
import com.transsion.sdk.demo.R;
import com.transsion.sdk.demo.bean.AdBean;
import com.transsion.sdk.demo.bean.BuyerBean;
import com.transsion.sdk.demo.bean.PersonBean;

import java.util.ArrayList;
import java.util.List;

public class JsonFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private TextView mTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JsonFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_json,
                container, false);
        mTextView = (TextView) rootView.findViewById(R.id.json_textView);
        Button button = (Button) rootView.findViewById(R.id.json_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json2Bybean();
                //json2AdBean();
                //json2bean();
            }
        });
        return rootView;
    }

    private void json2Bybean() {
        /*BuyerBean buyerBean = Tson.fromJson(Constant.TEST_JSON_TWO, BuyerBean.class);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(buyerBean.getBuyer()).append("\n")
                .append(buyerBean.getSex()).append("\n")
                .append(buyerBean.getLineitems().get(0).getName()).append("\n")
                .append(buyerBean.getLineitems().get(0).getQuantity()).append("\n");
        //mTextView.setText(stringBuffer.toString());*/
        List<String> inList = new ArrayList<>();
        inList.add("one");
        inList.add("two");
        inList.add("three");
        BuyerBean buyerBean = new BuyerBean();
        buyerBean.setAddress("祥科路");
        buyerBean.setPhone(130882737);
        buyerBean.setSex("man");
        buyerBean.setTestList(inList);
        mTextView.setText(Tson.toJson(buyerBean,true));
    }


    private void json2AdBean() {
        AdBean adBean = Tson.fromJson(Constant.AD_CONFIG, AdBean.class);
        Log.d("book", "showTime:" + adBean.showTime);
        Log.d("book", "timeOut:" + adBean.requestTimeout);
    }

    private void json2bean() {
        PersonBean personBean = Tson.fromJson(Constant.TEST_JSON, PersonBean.class);
        String jsonString = Tson.toJson(personBean);
        Log.d("book", "jsonString:" + jsonString);
        Log.d("book", "firstname:" + personBean.getFirstname());
        Log.d("book", "lastname:" + personBean.getLastname());
        Log.d("book", "age:" + personBean.getAge());
        Log.d("book", "birthplace:" + personBean.getBirthplace());
    }
}
