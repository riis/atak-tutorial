package com.atakmap.android.plugintemplate.plugin.support

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.atakmap.android.maps.MapComponent
import com.atakmap.android.maps.MapView
import com.atakmap.coremap.log.Log
import transapps.maps.plugin.lifecycle.Lifecycle
import java.util.LinkedList

/**
 * Do not use unless deploying your plugin with a version of ATAK less than 4.5.1.
 */
@Deprecated("")
abstract class AbstractPluginLifecycle(
    private val pluginContext: Context,
    component: MapComponent
) : Lifecycle {
    private val overlays: MutableCollection<MapComponent> = LinkedList()
    private var mapView: MapView? = null

    init {
        overlays.add(component)
        //PluginNativeLoader.init(ctx);
    }

    override fun onConfigurationChanged(arg0: Configuration) {
        for (c in this.overlays) c.onConfigurationChanged(arg0)
    }

    override fun onCreate(
        arg0: Activity,
        arg1: transapps.mapi.MapView
    ) {
        if (arg1 == null || arg1.view !is MapView) {
            Log.w(TAG, "This plugin is only compatible with ATAK MapView")
            return
        }
        this.mapView = arg1.view as MapView

        // create components
        val iter = overlays
            .iterator()
        var c: MapComponent
        while (iter.hasNext()) {
            c = iter.next()
            try {
                c.onCreate(pluginContext, arg0.intent, mapView)
            } catch (e: Exception) {
                Log.w(
                    TAG,
                    "Unhandled exception trying to create overlays MapComponent",
                    e
                )
                iter.remove()
            }
        }
    }

    override fun onDestroy() {
        for (c in this.overlays) c.onDestroy(this.pluginContext, this.mapView)
    }

    override fun onFinish() {
        // XXX - no corresponding MapComponent method
    }

    override fun onPause() {
        for (c in this.overlays) c.onPause(this.pluginContext, this.mapView)
    }

    override fun onResume() {
        for (c in this.overlays) c.onResume(this.pluginContext, this.mapView)
    }

    override fun onStart() {
        for (c in this.overlays) c.onStart(this.pluginContext, this.mapView)
    }

    override fun onStop() {
        for (c in this.overlays) c.onStop(this.pluginContext, this.mapView)
    }

    companion object {
        private const val TAG = "AbstractPluginLifecycle"
    }
}
