package es.joshluq.foundationkit.error

import es.joshluq.foundationkit.text.TextProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class AppErrorTest {

    @Test
    fun `toTextProvider produces correct messages for each AppError subtype`() {
        val noInternet = AppError.NoInternet()
        assertEquals("No internet connection", (noInternet.toTextProvider() as TextProvider.Dynamic).value)

        val timeout = AppError.Timeout()
        assertEquals("Operation timed out", (timeout.toTextProvider() as TextProvider.Dynamic).value)

        val validation = AppError.Validation(reason = "cannot be blank", field = "email")
        assertEquals("Validation error in email: cannot be blank", (validation.toTextProvider() as TextProvider.Dynamic).value)

        val unexpected = AppError.Unexpected(IllegalStateException("boom"))
        assertEquals("boom", (unexpected.toTextProvider() as TextProvider.Dynamic).value)
    }

    @Test
    fun `toListStateError maps AppError directly to ListState Error`() {
        val error = AppError.NotFound(resource = "user_123")
        val state = error.toListStateError()

        assertEquals("Resource not found: user_123", (state.message as TextProvider.Dynamic).value)
    }
}
