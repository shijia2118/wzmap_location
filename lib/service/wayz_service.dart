import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';

import '../wayz_models.dart';
import '../wayz_poi_models.dart';

const accessKey = "hGYINWz43i65SaCRUzUgSBmClDD7l2C3";

class WayzService {
  static Future<WayzResponse> reverseGeocode({
    @required double latitude,
    @required double longitude,
    @required String uuid,
  }) async {
    try {
      final dio = Dio(BaseOptions(
        headers: {"Content-Type": "application/json"},
        connectTimeout: 10000,
        receiveTimeout: 10000,
      ));

      final url =
          "https://api.newayz.com/location/hub/v1/track_points?access_key=$accessKey&response_sprf=gcj02";

      final body = {
        "asset": uuid,
        "location": {
          "position": {
            "timestamp": DateTime.now().millisecondsSinceEpoch,
            "point": {"longitude": longitude, "latitude": latitude},
            "spatialReference": "gcj02",
            "source": "gnss",
            "accuracy": 5.0,
          }
        }
      };

      final response = await dio.post(url, data: body);

      if (response.statusCode == 200) {
        return WayzResponse.fromJson(response.data);
      } else {
        print("Wayz API 请求失败: ${response.statusCode} - ${response.data}");
        return null;
      }
    } catch (e) {
      print("Wayz API 调用异常: $e");
      return null;
    }
  }

  static Future<List<WayzPoi>> searchPoi({
    String keyword,
    String city,
  }) async {
    try {
      final dio = Dio(BaseOptions(
        headers: {"Content-Type": "application/json"},
        connectTimeout: 10000,
        receiveTimeout: 10000,
      ));

      final queryParams = {
        "name": keyword,
        "access_key": accessKey,
        "city": city,
      };

      // 去掉 null 值
      queryParams.removeWhere((key, value) => value == null);

      final url = "https://api.newayz.com/location/scenario/v1/placesearch";

      final response = await dio.get(url, queryParameters: queryParams);

      if (response.statusCode == 200) {
        final List data = response.data as List;
        return data.map((e) => WayzPoi.fromJson(e)).toList();
      } else {
        print("Wayz POI 搜索失败: ${response.statusCode}");
        return [];
      }
    } catch (e) {
      print("Wayz POI 调用异常: $e");
      return [];
    }
  }

  static Future<List<WayzPoi>> nearbySearch({
    @required double latitude,
    @required double longitude,
    String keywords,
  }) async {
    try {
      final dio = Dio(BaseOptions(
        headers: {"Content-Type": "application/json"},
        connectTimeout: 10000,
        receiveTimeout: 10000,
      ));

      dio.interceptors.add(LogInterceptor(
        request: true,            // 打印请求行
        requestHeader: true,      // 打印请求头
        requestBody: true,        // 打印请求参数
        responseHeader: false,    // 打印响应头
        responseBody: true,       // 打印响应体
        error: true,              // 打印错误信息
        logPrint: (obj) {
          print("DIO_LOG: $obj"); // 自定义打印方法
        },
      ));

      final queryParams = {
        "access_key": accessKey,
        "location": '$longitude,$latitude',
        "keywords": keywords,
        "radius": '5000',
        "orderby": 'distance_score',
      };

      // 移除 null 值
      queryParams.removeWhere((key, value) => value == null);

      final url = "https://api.newayz.com/location/scenario/v1/nearbysearch";

      final response = await dio.get(url, queryParameters: queryParams);

      if (response.statusCode == 200) {
        final data = response.data;

        if (data is List) {
          return data.map((e) => WayzPoi.fromJson(e)).toList();
        } else {
          print("nearby 返回的不是数组: $data");
          return [];
        }
      } else {
        print("nearby 搜索失败: ${response.statusCode}");
        return [];
      }
    } catch (e) {
      print("nearby 调用异常: $e");
      return [];
    }
  }

}
