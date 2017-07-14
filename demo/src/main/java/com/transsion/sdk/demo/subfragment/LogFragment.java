package com.transsion.sdk.demo.subfragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.transsion.core.log.LogUtils;
import com.transsion.core.log.ObjectLogUtils;
import com.transsion.sdk.demo.R;

public class LogFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LogFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogUtils.Builder()
                        .setLogSwitch(true) //日志开关，default:true
                        .setGlobalTag("LogTest") //全局tag，输出log时如果不传入tag参数，则使用这个,default:null
                        .setBorderSwitch(true)    //每条log是否增加边框效果, default:true
                        .setConsoleFilter(LogUtils.V) //设置能在终端输出的日志的等级，default:V
                        .setLogHeadSwitch(true) //是否显示打印日志代码所在的函数和行号，default:true
                        .setConsoleSwitch(true) //是否允许在终端输出日志 default:true
                        .setLog2FileSwitch(true) //是否允许日志输出到文件 default:false
                        .setFileFilter(LogUtils.V) //设置输出到文件的日志的等级，default:V
                        .setDir(Environment.getExternalStorageDirectory().getPath() + System.getProperty("file.separator") + "test") //制定输出文件的路径，缺省在应用的私有目录下
                ;


                LogUtils.d("myTag", "My log \nlogloglog!");

                ObjectLogUtils logUtils01 = new ObjectLogUtils.Builder().setGlobalTag("ObjectLogUtils").create();
                logUtils01.i("----logUtils01----");

                ObjectLogUtils logUtils02 = new ObjectLogUtils.Builder()
                        .setGlobalTag("ObjectLogUtils")
                        .setBorderSwitch(false)
                        .setLogHeadSwitch(false)
                        .create();
                logUtils02.i("----logUtils02----\n----logUtils02----\n----logUtils02----\n----logUtils02----\n----logUtils02----\n");

            }
        });
        return rootView;
    }
}
