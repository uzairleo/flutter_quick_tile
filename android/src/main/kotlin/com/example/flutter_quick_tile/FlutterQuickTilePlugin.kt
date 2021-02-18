package com.example.flutter_quick_tile

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import kotlin.concurrent.thread


/** FlutterQuickTilePlugin */
public class FlutterQuickTilePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_quick_tile")
    channel.setMethodCallHandler(this);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_quick_tile")
      channel.setMethodCallHandler(FlutterQuickTilePlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}



class MyTileService: TileService() {
  override fun onTileAdded() {
    super.onTileAdded()
    qsTile.state = Tile.STATE_INACTIVE
    qsTile.updateTile()
  }

  override fun onClick() {
    super.onClick()
    if(qsTile.state == Tile.STATE_INACTIVE) {
      // Turn on
      qsTile.state = Tile.STATE_ACTIVE
      qsTile.label="keep fluttering"
      startVibrating()
    } else {
      // Turn off
      qsTile.state = Tile.STATE_INACTIVE
      stopVibrating()
      qsTile.label="Hi leo!"
    }
    // Update looks
    qsTile.updateTile()
  }

  fun startVibrating() {
//    launch {
//      while(qsTile.state == Tile.STATE_ACTIVE) {
//        (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
//                .vibrate(1000)
//        delay(1000)
//      }
//    }
  }

  fun stopVibrating() {
    (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).cancel()
  }
}
