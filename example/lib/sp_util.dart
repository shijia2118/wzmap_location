import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class SpUtil {
  /// app全局配置 eg:theme
  static SharedPreferences sharedPreferences;


  /// 必备数据的初始化操作
  /// 由于是同步操作会导致阻塞,所以应尽量减少存储容量
  static init() async {
    sharedPreferences = await SharedPreferences.getInstance();
  }

  static String getUuid() {
    var uuid = sharedPreferences.getString('uuid');

    if(uuid == null){

      uuid = Uuid().v4();

      setUuid(uuid);
    }

    return uuid;
  }

  static setUuid(String value) {
    return sharedPreferences.setString('uuid', value);
  }
}
