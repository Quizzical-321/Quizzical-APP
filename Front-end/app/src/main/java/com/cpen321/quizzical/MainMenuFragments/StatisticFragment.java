package com.cpen321.quizzical.MainMenuFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cpen321.quizzical.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StatisticFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView realTimeText;
    String serverLink = "http://193.122.108.23:8080/Time";

    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    FloatingActionButton fab;

    boolean is_Instructor;

    public StatisticFragment(boolean is_Instructor)
    {
        this.is_Instructor = is_Instructor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (is_Instructor)
        {
            return inflater.inflate(R.layout.fragment_class_statistic, container, false);
        }
        else {
            return inflater.inflate(R.layout.fragment_leaderboard, container, false);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // may need to separate for teacher and students
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        swipeRefreshLayout = view.findViewById(R.id.statistic_swipe_layout);

        realTimeText = view.findViewById(R.id.statistic_test_text);
        new Thread(this::updateText).start();

        swipeRefreshLayout.setOnRefreshListener(()->new Thread(this::updateText).start());

        rotateOpen = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this.getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this.getContext(), R.anim.to_bottom_anim);

        if (is_Instructor)
        {
            fab = getView().findViewById(R.id.teacher_class_switch_fab);
        }
        else
        {
            fab = getView().findViewById(R.id.class_switch_fab);
        }

        fab.setOnClickListener(v->onSwitchClassButtonClicked());

    }

    private void onSwitchClassButtonClicked()
    {
        Toast.makeText(getContext(), "change class clicked", Toast.LENGTH_SHORT).show();
    }

    private void updateText()
    {
        //we need to update UI on UI thread, otherwise it will crash the app
        //however, it adds the load onto the Main thread, causing lags
        //so we need to run a new thread which runs this function
        String result = readFromURL();
        getActivity().runOnUiThread(()->realTimeText.setText(result));
    }


    private String readFromURL()
    {
        String result = getString(R.string.server_get_failed);
        try {
            URL url = new URL(serverLink);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,getString(R.string.encoding_utf_8)));
            StringBuilder stb = new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null)
            {
                stb.append(line);
            }
            result = stb.toString();
        } catch (Exception e)
        {
            String eMessage = e.getMessage() + "";
            Log.d("html exception", eMessage);
        }
        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }
        return result;
    }
}