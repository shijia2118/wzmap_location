package com.example.wzmap_location.wzMap;

public interface IMapMarkLayer {
    void showInfoWindow(String markerId);

    void hideInfoWindow(String markerId);

    boolean isInfoWindowShown(String markerId);

    void removeOverlay(String markId);
}
