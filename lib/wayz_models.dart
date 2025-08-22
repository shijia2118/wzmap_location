class WayzResponse {
  final String id;
  final String asset;
  final WayzLocation location;

  WayzResponse({this.id, this.asset, this.location});

  factory WayzResponse.fromJson(Map<String, dynamic> json) {
    return WayzResponse(
      id: json["id"],
      asset: json["asset"],
      location: json["location"] != null
          ? WayzLocation.fromJson(json["location"])
          : null,
    );
  }
}

class WayzLocation {
  final int timestamp;
  final WayzAddress address;
  final WayzPlace place;
  final WayzPosition position;

  WayzLocation({this.timestamp, this.address, this.place, this.position});

  factory WayzLocation.fromJson(Map<String, dynamic> json) {
    return WayzLocation(
      timestamp: json["timestamp"],
      address: json["address"] != null
          ? WayzAddress.fromJson(json["address"])
          : null,
      place: json["place"] != null ? WayzPlace.fromJson(json["place"]) : null,
      position: json["position"] != null
          ? WayzPosition.fromJson(json["position"])
          : null,
    );
  }
}

class WayzAddress {
  final String name;
  final int level;
  final List<WayzContext> context;

  WayzAddress({this.name, this.level, this.context});

  factory WayzAddress.fromJson(Map<String, dynamic> json) {
    return WayzAddress(
      name: json["name"],
      level: json["level"],
      context: (json["context"] as List)
          ?.map((e) => WayzContext.fromJson(e))
          ?.toList() ??
          [],
    );
  }
}

class WayzContext {
  final String code;
  final String name;
  final String type;

  WayzContext({this.code, this.name, this.type});

  factory WayzContext.fromJson(Map<String, dynamic> json) {
    return WayzContext(
      code: json["code"],
      name: json["name"],
      type: json["type"],
    );
  }
}

class WayzPlace {
  final String id;
  final String name;
  final String type;
  final List<WayzCategory> categories;

  WayzPlace({this.id, this.name, this.type, this.categories});

  factory WayzPlace.fromJson(Map<String, dynamic> json) {
    return WayzPlace(
      id: json["id"],
      name: json["name"],
      type: json["type"],
      categories: (json["categories"] as List)
          ?.map((e) => WayzCategory.fromJson(e))
          ?.toList() ??
          [],
    );
  }
}

class WayzCategory {
  final int id;
  final String name;

  WayzCategory({this.id, this.name});

  factory WayzCategory.fromJson(Map<String, dynamic> json) {
    return WayzCategory(
      id: json["id"],
      name: json["name"],
    );
  }
}

class WayzPosition {
  final int timestamp;
  final WayzPoint point;
  final String source;
  final double accuracy;
  final String spatialReference;
  final double confidence;

  WayzPosition({
    this.timestamp,
    this.point,
    this.source,
    this.accuracy,
    this.spatialReference,
    this.confidence,
  });

  factory WayzPosition.fromJson(Map<String, dynamic> json) {
    return WayzPosition(
      timestamp: json["timestamp"],
      point: json["point"] != null ? WayzPoint.fromJson(json["point"]) : null,
      source: json["source"],
      accuracy: (json["accuracy"] as num)?.toDouble(),
      spatialReference: json["spatialReference"],
      confidence: (json["confidence"] as num)?.toDouble(),
    );
  }
}

class WayzPoint {
  final double longitude;
  final double latitude;
  final double altitude;

  WayzPoint({this.longitude, this.latitude, this.altitude});

  factory WayzPoint.fromJson(Map<String, dynamic> json) {
    return WayzPoint(
      longitude: (json["longitude"] as num)?.toDouble(),
      latitude: (json["latitude"] as num)?.toDouble(),
      altitude: (json["altitude"] as num)?.toDouble(),
    );
  }
}
