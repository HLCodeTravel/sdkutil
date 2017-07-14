package com.transsion.sdk.demo.subfragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.transsion.http.DownloadEngine;
import com.transsion.http.HttpClient;
import com.transsion.http.RequestCall;
import com.transsion.http.impl.BitmapCallback;
import com.transsion.http.impl.DownloadCallback;
import com.transsion.http.impl.StringCallback;
import com.transsion.http.log.Console;
import com.transsion.http.util.StorageUtils;
import com.transsion.sdk.demo.Constant;
import com.transsion.sdk.demo.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private String FILE_NAME;
    private Button mTestButton, mCancelButton,mPauseButton;
    private TextView mTextView;
    private ImageView mImageView;
    private int successCount = 0;
    private int failCount = 0;
    private static Map<String, String> postMap = new HashMap<>();
    private DownloadEngine downloadEngine;
    private RequestCall requestCall;
    private ProgressBar progressBar;

    static {
        postMap.put("mediav", "2.0.0");
        postMap.put("mediaid", "002");
        postMap.put("control_params", "es|wee");
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HttpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
        }
        downloadEngine = DownloadEngine.getEngine();
        initDownload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_http,
                container, false);
        mTestButton = (Button) rootView.findViewById(R.id.start);
        mPauseButton = (Button)rootView.findViewById(R.id.pause);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadEngine.pauseLoad(Constant.DOWNLOAD_TEST_URL);
            }
        });
        progressBar = (ProgressBar) rootView.findViewById(R.id.download_progress);
        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //execute();
                //downloadImage();
                //deleteAllCache();
                //downloadFile();
                downloadEngine.execute(requestCall, new DownloadCallback() {
                    @Override
                    public void onFailure(String url, String message) {
                        Console.log.d("download", "onFailure->" + message);
                    }

                    @Override
                    public void onSuccess(String url, File downFile) {
                        Console.log.d("download", "MainActivity onSuccess->" + downFile);
                    }

                    @Override
                    public void onLoading(String url, long current, long total) {
                        progressBar.setProgress((int) (current * 100 / total));
                    }

                    @Override
                    public void onCancel() {
                        Console.log.d("download", "onCancel->");
                    }

                    @Override
                    public void onFinish() {
                        Console.log.d("download", "onFinish->");
                    }

                    @Override
                    public void onPause() {
                        Console.log.d("download", "onPause->");
                    }
                });
            }
        });
        mCancelButton = (Button) rootView.findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadEngine.continueLoad(Constant.DOWNLOAD_TEST_URL);
            }
        });
        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        Log.d("book", "cache size:" + HttpClient.getCacheSize(getContext()));
        return rootView;
    }

    private void downloadImage() {
        for (int i = 0; i < Constant.IMAGES.length; i++) {
            HttpClient.image(getContext()).log(true).url(Constant.IMAGES[i]).build().execute(new BitmapCallback() {
                @Override
                public void onFailure(int statusCode, Bitmap responseString, Throwable throwable) {
                    failCount++;
                    mTextView.setText(String.valueOf(statusCode));
                    Console.log.d("book", "failed:" + throwable.toString());
                    Console.log.d("book", "failed load:" + failCount);
                }

                @Override
                public void onSuccess(int statusCode, Bitmap responseFile) {
                    Console.log.d("book", "success " + statusCode);
                    mTextView.setText(String.valueOf(statusCode));
                    mImageView.setImageBitmap(responseFile);
                    successCount++;
                    Console.log.d("book", "success load:" + successCount);
                }
            });
        }
    }

    private void initDownload() {
        String path = StorageUtils.getCacheDirectory(getContext()).getPath() + "/test.apk";
        requestCall = HttpClient.download(getContext()).pathname(path).
                url(Constant.DOWNLOAD_TEST_URL).log(true).tag(Constant.DOWNLOAD_TEST_URL).build();
    }

    private void execute() {

        for (int i = 0; i < 50; i++) {
            HttpClient.postJson().log(true).params(postMap).url(Constant.CONFIG_URL).build().execute(new StringCallback() {
                @Override
                public void onFailure(int statusCode, String responseString, Throwable throwable) {
                    Console.log.d("book", "statusCode:" + statusCode);
                }

                @Override
                public void onSuccess(int statusCode, String responseString) {
                    Console.log.d("book", "Current Looper:" + (Looper.myLooper() == Looper.getMainLooper() ? "Main Looper" : "thread Looper"));
                    Console.log.d("book", "statusCode:" + statusCode);
                    Console.log.d("book", "responseString:" + responseString);
                    mTextView.setText(responseString);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadEngine.cancelLoad(Constant.DOWNLOAD_TEST_URL);
    }

    private void deleteAllCache() {
        HttpClient.clearCache(getContext());
    }

}
