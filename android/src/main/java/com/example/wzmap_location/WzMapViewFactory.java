package com.example.wzmap_location;

import android.content.Context;

import androidx.annotation.NonNull;


import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import io.flutter.plugin.common.StandardMessageCodec;

public class WzMapViewFactory extends PlatformViewFactory {

    private final BinaryMessenger messenger;

    public WzMapViewFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlatformView create(@NonNull Context context, int viewId, Object args) {

        java.util.Map<String, Object> params = null;
        if (args instanceof java.util.Map) {
            // 安全转换
            params = (java.util.Map<String, Object>) args;
        }

        return new WzMapPlatformView(context, messenger, viewId, params);
    }
}
