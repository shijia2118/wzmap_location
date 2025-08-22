import 'package:flutter/material.dart';
import 'package:wzmap_location/wzmap_location.dart';
import 'package:wzmap_location_example/location_widget.dart';
import 'package:wzmap_location_example/sp_util.dart';

void main()async {

  WidgetsFlutterBinding.ensureInitialized();

  await SpUtil.init();

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void dispose() {
    WzmapLocation.stopLocation();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('维智定位插件测试')),
        body:Center(
          child: LocationWidget(),
        ),
      ),
    );
  }
}
