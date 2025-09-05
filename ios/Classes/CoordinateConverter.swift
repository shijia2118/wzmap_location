import CoreLocation

class CoordinateConverter {
    static let pi = 3.1415926535897932384626
    static let a = 6378245.0
    static let ee = 0.00669342162296594323
    
    static func transformLat(x: Double, y: Double) -> Double {
        var ret = -100.0 + 2.0 * x + 3.0 * y
        ret += 0.2 * y * y + 0.1 * x * y
        ret += 0.2 * sqrt(abs(x))
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * sin(y * pi) + 40.0 * sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * sin(y / 12.0 * pi) + 320 * sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }
    
    static func transformLon(x: Double, y: Double) -> Double {
        var ret = 300.0 + x + 2.0 * y
        ret += 0.1 * x * x + 0.1 * x * y
        ret += 0.1 * sqrt(abs(x))
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * sin(x * pi) + 40.0 * sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * sin(x / 12.0 * pi) + 300.0 * sin(x / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }
    
    static func outOfChina(lat: Double, lon: Double) -> Bool {
        if lon < 72.004 || lon > 137.8347 {
            return true
        }
        if lat < 0.8293 || lat > 55.8271 {
            return true
        }
        return false
    }
    
    /// WGS-84 → GCJ-02
    static func wgs2gcj(wgsLat: Double, wgsLon: Double) -> CLLocationCoordinate2D {
        if outOfChina(lat: wgsLat, lon: wgsLon) {
            return CLLocationCoordinate2D(latitude: wgsLat, longitude: wgsLon)
        }
        var dLat = transformLat(x: wgsLon - 105.0, y: wgsLat - 35.0)
        var dLon = transformLon(x: wgsLon - 105.0, y: wgsLat - 35.0)
        let radLat = wgsLat / 180.0 * pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        let sqrtMagic = sqrt(magic)
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi)
        dLon = (dLon * 180.0) / (a / sqrtMagic * cos(radLat) * pi)
        let mgLat = wgsLat + dLat
        let mgLon = wgsLon + dLon
        return CLLocationCoordinate2D(latitude: mgLat, longitude: mgLon)
    }
}
