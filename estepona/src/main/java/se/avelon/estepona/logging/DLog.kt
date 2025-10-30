package se.avelon.estepona.logging

import android.util.Log

class DLog {
    companion object {
        fun forTag(clazz: Class<*>): String {
            return "@@.${clazz.simpleName}"
        }

        fun method(tag: String, o: Any) {
            Log.d(tag, o.toString())
        }

        fun info(tag: String, o: Any) {
            Log.i(tag, o.toString())
        }
    }
}