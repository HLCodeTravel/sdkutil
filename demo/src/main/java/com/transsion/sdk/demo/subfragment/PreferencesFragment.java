package com.transsion.sdk.demo.subfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.transsion.core.utils.SharedPreferencesUtil;
import com.transsion.sdk.demo.R;

public class PreferencesFragment extends Fragment {

    private String Tag = PreferencesFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Context mContext;
    private String Tag1 = Tag + 1;
    private String Tag2 = Tag + 2;
    private String Tag3 = Tag + 3;
    private String Tag4 = Tag + 4;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PreferencesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);
        Button button = (Button) rootView.findViewById(R.id.preferences_button);
        final EditText edit = (EditText) rootView.findViewById(R.id.preferences_edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edit.getText().toString())) {
                    getInstance().putString(Tag4, edit.getText().toString());
                    Toast.makeText(mContext, "" + getInstance().getLong(Tag4), Toast.LENGTH_SHORT).show();
                } else {
                    getInstance().putString(Tag, "字符串");
                    getInstance().putInt(Tag1, 1);
                    getInstance().putFloat(Tag2, 2);
                    getInstance().putLong(Tag3, 3);
                    Toast.makeText(mContext, getInstance().getString(Tag), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "" + getInstance().getInt(Tag1), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "" + getInstance().getFloat(Tag2), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "" + getInstance().getLong(Tag3), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private SharedPreferencesUtil getInstance() {
        return SharedPreferencesUtil.getInstance("PreferencesFragment");
    }
}
