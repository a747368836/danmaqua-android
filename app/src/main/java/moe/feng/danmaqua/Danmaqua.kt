package moe.feng.danmaqua

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.tencent.mmkv.MMKV
import moe.feng.common.eventshelper.EventsHelper
import moe.feng.common.eventshelper.of
import moe.feng.danmaqua.event.SettingsChangedListener
import moe.feng.danmaqua.model.BlockedTextRule
import moe.feng.danmaqua.util.ext.*

/**
 * Danmaqua constants and settings
 */
object Danmaqua {

    const val EXTRA_PREFIX = "${BuildConfig.APPLICATION_ID}.extra"
    const val EXTRA_DATA = "${EXTRA_PREFIX}.DATA"
    const val EXTRA_ACTION = "${EXTRA_PREFIX}.ACTION"

    const val ACTION_PREFIX = "${BuildConfig.APPLICATION_ID}.action"

    const val NOTI_CHANNEL_ID_STATUS = "status"
    const val NOTI_ID_LISTENER_STATUS = 10

    const val PENDING_INTENT_REQUEST_STOP = 10
    const val PENDING_INTENT_REQUEST_ENTER_MAIN = 11
    const val PENDING_INTENT_REQUEST_RECONNECT = 12

    const val LEFT_PARENTHESIS = "〈｛『〖［《〔「【"
    const val RIGHT_PARENTHESIS = "〉｝『〗］》〕」】"

    const val DEFAULT_FILTER_PATTERN =
        "(?<who>[^$LEFT_PARENTHESIS]*)[$LEFT_PARENTHESIS](?<text>[^$RIGHT_PARENTHESIS]*)[$RIGHT_PARENTHESIS]?"

    object Settings {

        private val mmkv: MMKV get() = MMKV.defaultMMKV()

        fun commit(context: Context? = null, block: Settings.() -> Unit) {
            Settings.block()
            notifyChanged(context)
        }

        fun updateVersionCode(context: Context) {
            try {
                val pi = context.packageManager.getPackageInfo(context.packageName, 0)
                lastVersionCode = pi.versionCode
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update current used version code to settings", e)
            }
        }

        var lastVersionCode: Int by mmkv.intProperty(
            key = "last_version_code", defaultValue = 0
        )

        var introduced: Boolean by mmkv.booleanProperty(
            key = "introduced", defaultValue = false
        )

        var enabledAnalytics: Boolean by mmkv.booleanProperty(
            key = "enabled_analytics", defaultValue = true
        )

        var darkMode: Int by mmkv.intProperty(
            key = "dark_mode", defaultValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        var filterEnabled: Boolean by mmkv.booleanProperty(
            key = "filter_enabled", defaultValue = false
        )

        var filterPattern: String by mmkv.notnullStringProperty(
            key = "filter_pattern_v2", defaultValue = DEFAULT_FILTER_PATTERN
        )

        var blockedTextPatterns: List<BlockedTextRule> by mmkv.jsonArrayProperty(
            key = "filter_blocked_text_rules",
            arrayClass = Array<BlockedTextRule>::class.java
        )

        var floatingBackgroundAlpha: Int by mmkv.intProperty(
            key = "floating_alpha", defaultValue = 255
        )

        var floatingTextSize: Int by mmkv.intProperty(
            key = "floating_text_size", defaultValue = 14
        )

        var floatingTwoLine: Boolean by mmkv.booleanProperty(
            key = "floating_two_line", defaultValue = false
        )

        var floatingTouchToMove: Boolean by mmkv.booleanProperty(
            key = "floating_touch_to_move", defaultValue = true
        )

        fun notifyChanged(context: Context? = null) {
            EventsHelper.getInstance(context)
                .of<SettingsChangedListener>()
                .onSettingsChanged()
        }

    }

}