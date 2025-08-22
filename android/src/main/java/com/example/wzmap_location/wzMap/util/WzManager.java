package com.example.wzmap_location.wzMap.util;

import android.content.Context;

import com.mapbox.mapboxsdk.Mapbox;

public class WzManager {

    public static void getInstance(Context context){
        Mapbox.getInstance(context, Constants.MAPBOX_KEY);
    }
}
