package com.example.wzmap_location.wzMap.common;


import com.example.wzmap_location.wzMap.util.Constants;

public enum MapType {

    DEFAULT(Constants.MAP_STYLE_DEFAULT),
    LIGHT_BLUE(Constants.MAP_STYLE_LIGHT_BLUE),
    GRAY_WHITE(Constants.MAP_STYLE_GRAY_WHITE),
    DARK_BLUE(Constants.MAP_STYLE_DARK_BLUE);

    private String styleStr ;

    MapType(String type) {
        styleStr = type;
    }

    @Override
    public String toString() {
        return String.valueOf (styleStr);

    }
}
