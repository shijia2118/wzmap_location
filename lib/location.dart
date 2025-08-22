import 'package:flutter/cupertino.dart';

/// 定位结果 model
class Location {
  Location({
    @required this.address,
    @required this.longitude,
    @required this.latitude,
    @required this.altitude,
    @required this.bearing,
    @required this.country,
    @required this.province,
    @required this.city,
    @required this.cityCode,
    @required this.adCode,
    @required this.district,
    @required this.street,
    @required this.speed,
    @required this.placeName,
  });

  /// 地址全称
  String address;

  double longitude; // 经度

  double latitude; // 纬度

  /// 海拔
  double altitude;

  /// 设备朝向/移动方向
  double bearing;

  /// 国家
  String country;

  /// 省份
  String province;

  /// 城市
  String city;

  /// 城市编号
  String cityCode;

  /// 邮编
  String adCode;

  /// 区域
  String district;

  /// 街道
  String street;

  /// 速度
  double speed;

  String placeName;

  @override
  String toString() {
    return 'Location{\naddress: $address, \Longitude: $longitude, \Latitude: $latitude, \naltitude: $altitude, \nbearing: $bearing, \ncountry: $country, \nprovince: $province, \ncity: $city, \ncityCode: $cityCode, \nadCode: $adCode, \ndistrict: $district,  \nstreet: $street\n}';
  }
}

/// 后台定位notification
class BackgroundNotification {
  BackgroundNotification({
    @required this.contentTitle,
    @required this.contentText,
    this.when,
    @required this.channelId,
    @required this.channelName,
    this.enableLights = true,
    this.showBadge = true,
  });

  String contentTitle;
  String contentText;
  int when;
  String channelId;
  String channelName;
  bool enableLights;
  bool showBadge;
}