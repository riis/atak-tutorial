package com.atakmap.android.plugintemplate

import android.content.Context
import android.content.Intent
import com.atakmap.android.dropdown.DropDownMapComponent
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter
import com.atakmap.android.maps.MapView
import com.atakmap.android.plugintemplate.plugin.R
import com.atakmap.coremap.log.Log
import io.mavsdk.MavsdkEventQueue
import io.mavsdk.System
import io.mavsdk.mavsdkserver.MavsdkServer
import io.mavsdk.telemetry.Telemetry
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PluginTemplateMapComponent : DropDownMapComponent() {
    private var pluginContext: Context? = null

    private var ddr: PluginTemplateDropDownReceiver? = null

    private val mavsdkServer = MavsdkServer()
    private var drone: System? = null
    private var isMavsdkServerRunning = false
    private val disposables: MutableList<Disposable> = mutableListOf()

    private val _position = MutableStateFlow(Pair(0.toDouble(), 0.toDouble()))
    private val position : StateFlow<Pair<Double, Double>> = _position

    override fun onCreate(
        context: Context, intent: Intent,
        view: MapView
    ) {
        context.setTheme(R.style.ATAKPluginTheme)
        super.onCreate(context, intent, view)
        pluginContext = context

        ddr = PluginTemplateDropDownReceiver(
            view, context, position
        )

        Log.d(TAG, "registering the plugin filter")
        val ddFilter = DocumentedIntentFilter()
        ddFilter.addAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN)
        registerDropDownReceiver(ddr, ddFilter)

        if (!isMavsdkServerRunning) {
            runMavsdkServer()
        }
    }

    override fun onDestroyImpl(context: Context, view: MapView) {
        super.onDestroyImpl(context, view)
        destroyMavsdkServer()
    }

    private fun runMavsdkServer() {
        MavsdkEventQueue.executor().execute {
            val mavsdkServerPort: Int = mavsdkServer.run("udp://:14550")
            drone = System(
                BACKEND_IP_ADDRESS,
                mavsdkServerPort
            )

            disposables.add(
                drone!!.telemetry.getFlightMode().distinctUntilChanged()
                    .subscribe(Consumer<Telemetry.FlightMode> { flightMode: Telemetry.FlightMode ->
                        Log.d(TAG, "flight mode: $flightMode")
                    })
            )
            disposables.add(
                drone!!.telemetry.getArmed().distinctUntilChanged()
                    .subscribe(Consumer<Boolean> { armed: Boolean ->
                        Log.d(TAG,"armed: $armed")
                    })
            )
            disposables.add(
                drone!!.telemetry.getPosition()
                    .subscribe(Consumer<Telemetry.Position> { position: Telemetry.Position ->
                        val latLng: Pair<Double, Double> = Pair(position.latitudeDeg, position.longitudeDeg)
                        Log.d(TAG, "position: (${latLng.first}, ${latLng.second})")
                        _position.update { latLng }
                    })
            )
            isMavsdkServerRunning = true
        }
    }

    private fun destroyMavsdkServer() {
        MavsdkEventQueue.executor().execute {
            for (disposable in disposables) {
                disposable.dispose()
            }
            disposables.clear()
            drone?.dispose()
            drone = null
            mavsdkServer.stop()
            mavsdkServer.destroy()

            isMavsdkServerRunning = false
        }
    }

    companion object {
        // phone 192.168.2.15
        // pc
        private const val TAG = "PluginTemplateMapComponent"
        private const val BACKEND_IP_ADDRESS: String = "127.0.0.1"
    }
}
