import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wzmap_location/wzmap_location.dart';

void main() {
  const MethodChannel channel = MethodChannel('wzmap_location');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });


}
