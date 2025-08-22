class WayzPoi {
  final String id;
  final String name;
  final String code;
  final String type;
  final double distance; // km
  final double relevance;
  final String geoPoint;
  final WayzAddress address;
  final List<WayzCategory> categories;

  WayzPoi({
    this.id,
    this.name,
    this.code,
    this.type,
    this.distance,
    this.relevance,
    this.geoPoint,
    this.address,
    this.categories,
  });

  factory WayzPoi.fromJson(Map<String, dynamic> json) {
    return WayzPoi(
      id: json["id"],
      name: json["name"],
      code: json["code"],
      type: json["type"],
      distance: (json["distance"] as num)?.toDouble(),
      relevance: (json["relevance"] as num)?.toDouble(),
      geoPoint: json["geoPoint"],
      address: json["address"] != null
          ? WayzAddress.fromJson(json["address"])
          : null,
      categories: (json["categories"] as List)
          ?.map((e) => WayzCategory.fromJson(e))
          ?.toList() ??
          [],
    );
  }
}

class WayzAddress {
  final String name;
  final List<WayzContext> context;

  WayzAddress({this.name, this.context});

  factory WayzAddress.fromJson(Map<String, dynamic> json) {
    return WayzAddress(
      name: json["name"],
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

class WayzCategory {
  final String id;
  final String name;

  WayzCategory({this.id, this.name});

  factory WayzCategory.fromJson(Map<String, dynamic> json) {
    return WayzCategory(
      id: json["id"],
      name: json["name"],
    );
  }
}
