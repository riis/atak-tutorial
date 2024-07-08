package com.atakmap.android.plugintemplate.plugin.support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import com.atakmap.android.ipc.AtakBroadcast
import transapps.mapi.MapView
import transapps.maps.plugin.tool.Group
import transapps.maps.plugin.tool.Tool
import transapps.maps.plugin.tool.ToolDescriptor

/**
 * Do not use unless deploying your plugin with a version of ATAK less than 4.5.1.
 */
@Deprecated("")
abstract class AbstractPluginTool
/**
 * Construct an abstract PluginTool
 * @param context the context to use
 * @param shortDescription the short dscription
 * @param description the description
 * @param icon the icon
 * @param action the action
 */(
    private val context: Context,
    private val shortDescription: String,
    private val description: String,
    private val icon: Drawable,
    private val action: String
) : Tool(), ToolDescriptor {
    override fun getDescription(): String {
        return description
    }

    override fun getIcon(): Drawable {
        return icon
    }

    override fun getGroups(): Array<Group> {
        return arrayOf(
            Group.GENERAL
        )
    }

    override fun getShortDescription(): String {
        return shortDescription
    }

    override fun getTool(): Tool {
        return this
    }

    override fun onActivate(
        arg0: Activity,
        arg1: MapView,
        arg2: ViewGroup,
        arg3: Bundle,
        arg4: ToolCallback
    ) {
        // Hack to close the dropdown that automatically opens when a tool
        // plugin is activated.

        if (arg4 != null) {
            arg4.onToolDeactivated(this)
        }

        // Intent to launch the dropdown or tool

        //arg2.setVisibility(ViewGroup.INVISIBLE);
        val i = Intent(action)
        AtakBroadcast.getInstance().sendBroadcast(i)
    }

    override fun onDeactivate(arg0: ToolCallback) {
    }
}
