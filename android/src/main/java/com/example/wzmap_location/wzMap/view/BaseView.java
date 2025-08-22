package com.example.wzmap_location.wzMap.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.wzmap_location.wzMap.Map;


public abstract class BaseView extends LinearLayout implements Map {

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void onCreate(Bundle bundle);

    public abstract void onStart();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onLowMemory();

    public abstract void onDestroy();
}
