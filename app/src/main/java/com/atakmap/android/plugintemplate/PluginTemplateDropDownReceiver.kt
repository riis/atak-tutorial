package com.atakmap.android.plugintemplate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.atak.plugins.impl.PluginLayoutInflater
import com.atakmap.android.dropdown.DropDown.OnStateListener
import com.atakmap.android.dropdown.DropDownReceiver
import com.atakmap.android.maps.MapView
import com.atakmap.android.plugintemplate.plugin.R
import com.atakmap.coremap.log.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PluginTemplateDropDownReceiver(
    mapView: MapView?,
    private val pluginContext: Context,
    private val position: StateFlow<Pair<Double, Double>>
) : DropDownReceiver(mapView), OnStateListener {
    // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
    // In this case, using it is not necessary - but I am putting it here to remind
    // developers to look at this Inflator
    private val templateView: View = PluginLayoutInflater.inflate(
        pluginContext,
        R.layout.main_layout, null
    )

    private val positionTextView: TextView = templateView.findViewById(R.id.positionTextView)

    init {
        // Observe position changes
        CoroutineScope(Dispatchers.IO).launch {
            position.collect { pair ->
                val newPositionString = "Position: (${pair.first}, ${pair.second})"
                withContext(Dispatchers.Main) {
                    positionTextView.text = newPositionString
                }
            }
        }
    }

    /**************************** PUBLIC METHODS  */
    public override fun disposeImpl() {
    }

    /**************************** INHERITED METHODS  */
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return

        if (action == SHOW_PLUGIN) {
            Log.d(TAG, "showing plugin drop down")
            showDropDown(
                templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                HALF_HEIGHT, false, this
            )
        }
    }

    override fun onDropDownSelectionRemoved() {
    }

    override fun onDropDownVisible(v: Boolean) {
    }

    override fun onDropDownSizeChanged(width: Double, height: Double) {
    }

    override fun onDropDownClose() {
    }

    companion object {
        val TAG: String = PluginTemplateDropDownReceiver::class.java
            .simpleName

        const val SHOW_PLUGIN: String = "com.atakmap.android.plugintemplate.SHOW_PLUGIN"
    }
}
