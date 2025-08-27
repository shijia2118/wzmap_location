import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionUtil {

  static location(BuildContext context, {Function action}) async {
    PermissionStatus status = await Permission.location.status;

    if (!status.isGranted) {
      status = await Permission.location.request();
    }
    if (status.isGranted) {
      await action();
    }
  }
}