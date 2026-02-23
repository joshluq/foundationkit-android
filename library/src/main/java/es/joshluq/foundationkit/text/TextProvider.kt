package es.joshluq.foundationkit.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A provider for text that can be used in ViewModels without a dependency on the Android Context.
 * It allows for dynamic strings, resource-based strings with arguments, and empty text.
 */
sealed interface TextProvider {

    /**
     * Represents a dynamic string that doesn't come from resources.
     * Use this for API responses, calculated values, etc.
     *
     * @property value The raw string value.
     */
    data class Dynamic(val value: String) : TextProvider

    /**
     * Represents a string resource.
     * Use this for localized strings from res/values/strings.xml.
     *
     * @property resId The resource ID of the string.
     * @property args Optional arguments for string formatting.
     */
    class Resource(
        @param:StringRes val resId: Int,
        vararg val args: Any
    ) : TextProvider {
        // Since vararg is used, we need to manually implement equals and hashCode for data consistency
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Resource) return false
            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    /**
     * Represents an empty string.
     */
    data object Empty : TextProvider

    /**
     * Resolves the [TextProvider] into a [String] using the provided [Context].
     *
     * @param context The Android context used to resolve resources.
     * @return The resolved string.
     */
    fun asString(context: Context): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> {
                // Optimization to avoid array copy for the common case of no format arguments.
                if (args.isEmpty()) {
                    context.getString(resId)
                } else {
                    // Suppressing because the spread operator is unavoidable here,
                    // and the performance hit is an acceptable trade-off for this API's flexibility.
                    @Suppress("SpreadOperator")
                    context.getString(resId, *args)
                }
            }
            is Empty -> ""
        }
    }

    /**
     * Resolves the [TextProvider] into a [String] within a Composable function.
     *
     * @return The resolved string.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> {
                // Optimization to avoid array copy for the common case of no format arguments.
                if (args.isEmpty()) {
                    stringResource(resId)
                } else {
                    // Suppressing because the spread operator is unavoidable here,
                    // and the performance hit is an acceptable trade-off for this API's flexibility.
                    @Suppress("SpreadOperator")
                    stringResource(resId, *args)
                }
            }
            is Empty -> ""
        }
    }
}
