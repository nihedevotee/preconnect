import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:preconnect/tools/platform_channels.dart';

abstract final class AdvisingBackground {
  static const MethodChannel _channel = MethodChannel(
    PlatformChannels.advisingBackground,
  );

  static Future<bool> start({
    String title = 'Advising Helper',
    String message = 'Monitoring sections in background...',
  }) async {
    if (kIsWeb) return false;
    try {
      final ok = await _channel.invokeMethod<bool>('start', <String, dynamic>{
        'title': title,
        'message': message,
      });
      return ok ?? false;
    } catch (_) {
      return false;
    }
  }

  static Future<bool> update({
    required String title,
    required String message,
  }) async {
    if (kIsWeb) return false;
    try {
      final ok = await _channel.invokeMethod<bool>('update', <String, dynamic>{
        'title': title,
        'message': message,
      });
      return ok ?? false;
    } catch (_) {
      return false;
    }
  }

  static Future<bool> stop() async {
    if (kIsWeb) return false;
    try {
      final ok = await _channel.invokeMethod<bool>('stop');
      return ok ?? false;
    } catch (_) {
      return false;
    }
  }

  static Future<void> setKeepAwake(bool enable) async {
    if (kIsWeb) return;
    try {
      await _channel.invokeMethod<bool>('setKeepAwake', <String, dynamic>{
        'enable': enable,
      });
    } catch (_) {}
  }
}
