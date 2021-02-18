
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterQuickTile {
  static const MethodChannel _channel =
      const MethodChannel('flutter_quick_tile');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
