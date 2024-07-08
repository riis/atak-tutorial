package com.atakmap.android.plugintemplate.plugin

import android.content.Context
import java.io.File

/**
 * Boilerplate code for loading native.
 */
object PluginNativeLoader {
    private const val TAG = "NativeLoader"
    private var ndl: String? = null

    /**
     * If a plugin wishes to make use of this class, they will need to copy it into their plugin.
     * The classloader that loads this class is a key component of getting System.load to work
     * properly.   If it is desirable to use this in a plugin, it will need to be a direct copy in a
     * non-conflicting package name.
     */
    @Synchronized
    fun init(context: Context) {
        if (ndl == null) {
            try {
                ndl = context.packageManager
                    .getApplicationInfo(
                        context.packageName,
                        0
                    ).nativeLibraryDir
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "native library loading will fail, unable to grab the nativeLibraryDir from the package name"
                )
            }
        }
    }

    /**
     * Security guidance from our recent audit:
     * Pass an absolute path to System.load(). Avoid System.loadLibrary() because its behavior
     * depends upon its implementation which often relies on environmental features that can be
     * manipulated. Use only validated, sanitized absolute paths.
     */
    fun loadLibrary(name: String?) {
        if (ndl != null) {
            val lib = (ndl + File.separator
                    + System.mapLibraryName(name))
            if (File(lib).exists()) {
                System.load(lib)
            }
        } else {
            throw IllegalArgumentException("NativeLoader not initialized")
        }
    }
}
