package com.atakmap.android.plugintemplate.plugin

import android.content.Context
import com.atak.plugins.impl.AbstractPluginLifecycle
import com.atakmap.android.plugintemplate.PluginTemplateMapComponent


/**
 * Please note:
 * Support for versions prior to 4.5.1 can make use of a copy of AbstractPluginLifeCycle shipped with
 * the plugin.
 */
class PluginTemplateLifecycle(ctx: Context) :
    AbstractPluginLifecycle(ctx, PluginTemplateMapComponent()) {
    init {
        PluginNativeLoader.init(ctx)
    }

    companion object {
        private const val TAG = "PluginTemplateLifecycle"
    }
}
