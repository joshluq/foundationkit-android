package es.joshluq.foundationkit.text

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Resolves the [TextProvider] into a [String] using the provided [Context].
 *
 * @param context The Android context used to resolve resources.
 * @return The resolved string.
 */
fun TextProvider.asString(context: Context): String {
    return when (this) {
        is TextProvider.Dynamic -> value
        is TextProvider.Resource -> {
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
        is TextProvider.Empty -> ""
    }
}

/**
 * Resolves the [TextProvider] into a [String] within a Composable function.
 *
 * @return The resolved string.
 */
@Composable
fun TextProvider.asString(): String {
    return when (this) {
        is TextProvider.Dynamic -> value
        is TextProvider.Resource -> {
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
        is TextProvider.Empty -> ""
    }
}
