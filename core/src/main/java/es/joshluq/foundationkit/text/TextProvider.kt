package es.joshluq.foundationkit.text

/**
 * A provider for text that can be used in ViewModels or domain models without a dependency on Android Context.
 * It allows for dynamic strings, resource-based string references, and empty text.
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
     * Represents a string resource reference by ID.
     * Use this for localized strings from resources.
     *
     * @property resId The resource ID of the string.
     * @property args Optional arguments for string formatting.
     */
    class Resource(
        val resId: Int,
        vararg val args: Any
    ) : TextProvider {
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
}
