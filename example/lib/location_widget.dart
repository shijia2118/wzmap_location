import 'dart:async';

import 'package:flutter/material.dart';
import 'package:wzmap_location/wzmap_location.dart';
import 'package:wzmap_location_example/permission_util.dart';

import 'location_page.dart';

class LocationWidget extends StatefulWidget {
  LocationWidget({
    Key key,
  }) : super(key: key);

  @override
  _LocationWidgetState createState() => _LocationWidgetState();
}

class _LocationWidgetState extends State<LocationWidget> {
  String _location;

  @override
  initState() {
    WidgetsBinding.instance.addPostFrameCallback((t) {
      _getLocation();
    });
    super.initState();
  }

  
  _getLocation() async {

    await PermissionUtil.location(context,action: () async{

      await WzmapLocation.startLocation(onLocation: (location){

        print(location);

        setState(() {
          _location = location.address;
        });

      },
        onError: (error){
          print('>>>>>>>>>>>location error:$error');
        },
      );
    },);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      color: Colors.transparent,
      margin: EdgeInsets.only(left: 20),
      child: GestureDetector(
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => LocationPage(_location??'')),
          ).then((value) {
            if(value != null && value is String){
              setState(() {
                _location = value;
              });
            }
          });

        },
        child: Row(
          children: [
            Container(
              width: 182,
              child: Text(
                _location==null||_location.isEmpty ? '温州鹿城区绣山路321号温州市政府':_location,
                overflow: TextOverflow.ellipsis,
                maxLines: 1,
                style: TextStyle(color: Colors.black, fontSize: 14),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
