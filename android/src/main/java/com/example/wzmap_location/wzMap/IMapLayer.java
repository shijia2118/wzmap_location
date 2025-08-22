package com.example.wzmap_location.wzMap;


import com.example.wzmap_location.wzMap.model.BaseOptions;

public interface IMapLayer {

    void removeOverlay(String layerId);

    void updateOption(String layerId, BaseOptions polylineOptions);

    void removeAll();

}
