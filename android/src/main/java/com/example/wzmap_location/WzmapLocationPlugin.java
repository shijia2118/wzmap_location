package com.example.wzmap_location;

import android.app.Activity;
import android.content.Context;
import android.Manifest;

import androidx.annotation.NonNull;

import com.wayz.location.MapsInitializer;
import com.wayz.location.WzException;
import com.wayz.location.WzLocation;
import com.wayz.location.WzLocationClient;
import com.wayz.location.WzLocationClientOption;
import com.wayz.location.WzLocationListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

import pub.devrel.easypermissions.EasyPermissions;

/** WzmapLocationPlugin (V1 embedding) */
public class WzmapLocationPlugin implements
        MethodChannel.MethodCallHandler,
        PluginRegistry.RequestPermissionsResultListener {

  private MethodChannel channel;
  private WzLocationClient client;
  private WzLocationClientOption option;
  private Context context;
  private Activity activity;
  private static final int LOCATION_REQUEST_CODE = 123;
  private MethodChannel.Result pendingResult;

  // ================ V1 embedding =================
  public static void registerWith(PluginRegistry.Registrar registrar) {
    WzmapLocationPlugin plugin = new WzmapLocationPlugin();
    plugin.activity = registrar.activity();
    plugin.context = registrar.activity().getApplicationContext();

    plugin.channel = new MethodChannel(registrar.messenger(), "wzmap_location");
    plugin.channel.setMethodCallHandler(plugin);

    registrar.addRequestPermissionsResultListener(plugin);

    // 注册 PlatformView
    registrar.platformViewRegistry().registerViewFactory(
            "wzmap_view", new WzMapViewFactory(registrar.messenger()));
  }

  // ================ MethodCall =================
  @Override
  public void onMethodCall(MethodCall call, MethodChannel.Result result) {
    switch (call.method) {
      case "startLocation":
        startLocation(result);
        break;
      case "stopLocation":
        stopLocation(result);
        break;
      default:
        result.notImplemented();
    }
  }

  // ================ 权限检查 =================
  private boolean checkPermissions(MethodChannel.Result result) {
    if (activity == null) {
      result.error("NO_ACTIVITY", "插件没有绑定 Activity", null);
      return false;
    }

    String[] perms = android.os.Build.VERSION.SDK_INT >= 31
            ? new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}
            : new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    if (!EasyPermissions.hasPermissions(activity, perms)) {
      pendingResult = result;
      EasyPermissions.requestPermissions(activity,
              "需要定位权限", LOCATION_REQUEST_CODE, perms);
      return false;
    }
    return true;
  }

  // ================ SDK逻辑 =================
  private void initLocationClient() {
    MapsInitializer.updatePrivacyShow(context, true, false);
    MapsInitializer.updatePrivacyAgree(context, true);

    option = new WzLocationClientOption();
    option.setInterval(5000);
    option.setOnceLocate(false);
    option.setLockScreenLocation(true);
    option.setUsingSensor(false);
    option.setReportHost("");
    option.setFastLocation(false);

    client = new WzLocationClient(context, option);
  }

  private void startLocation(final MethodChannel.Result result) {
    if (!checkPermissions(result)) return;

    if (client == null) initLocationClient();

    client.startLocation(new WzLocationListener() {
      @Override
      public void onLocationReceived(WzLocation location) {
        Map<String, Object> map = new HashMap<>();

        map.put("address", location.getAddress());
        map.put("latitude", location.getLatitude());
        map.put("longitude", location.getLongitude());
        map.put("altitude", location.getAltitude());
        map.put("bearing", location.getBearing());
        map.put("country", location.getCountry());
        map.put("province", location.getProvince());
        map.put("city", location.getCity());
        map.put("cityCode", location.getCityCode());
        map.put("district", location.getDistrict());
        map.put("street", location.getStreet());
        map.put("adCode", location.getPostCode());
        map.put("speed", location.getSpeed());

        if (location.getPlace() != null)
          map.put("placeName", location.getPlace().getName());
        channel.invokeMethod("onLocationChanged", map);
      }

      @Override
      public void onLocationError(WzException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", exception.getErrorMessage());
        channel.invokeMethod("onLocationError", map);
      }
    });
    result.success(null);
  }

  private void stopLocation(MethodChannel.Result result) {
    if (client != null) client.stopLocation();
    result.success(null);
  }

  // ================ 权限回调 =================
  @Override
  public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
    if (requestCode == LOCATION_REQUEST_CODE && pendingResult != null) {
      if (EasyPermissions.hasPermissions(activity, permissions)) {
        startLocation(pendingResult);
      } else {
        pendingResult.error("PERMISSION_DENIED", "用户拒绝了必要的权限", null);
      }
      pendingResult = null;
      return true;
    }
    return false;
  }
}
