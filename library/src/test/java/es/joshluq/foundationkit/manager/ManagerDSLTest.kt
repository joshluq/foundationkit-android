package es.joshluq.foundationkit.manager

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ManagerDSLTest {

    private class TestContextConfig(val apiKey: String, val context: Context) : ManagerConfig

    private class TestContextConfigBuilder(override val context: Context) : ContextConfigBuilder<TestContextConfig> {
        var apiKey: String = ""
        override fun build() = TestContextConfig(apiKey, context)
    }

    private class TestContextManager : Manager<TestContextConfig>() {
        val configInstance get() = config
        
        companion object : ContextManagerFactory<TestContextManager, TestContextConfig, TestContextConfigBuilder> {
            override val builder = object : ManagerBuilder<TestContextConfig, TestContextManager> {
                override fun build(config: TestContextConfig): TestContextManager {
                    return TestContextManager().apply { this.config = config }
                }
            }
            override fun createBuilder(context: Context) = TestContextConfigBuilder(context)
        }
    }

    private class TestPureConfig(val precision: Int) : ManagerConfig

    private class TestPureConfigBuilder : ConfigBuilder<TestPureConfig> {
        var precision: Int = 2
        override fun build() = TestPureConfig(precision)
    }

    private class TestPureManager : Manager<TestPureConfig>() {
        val configInstance get() = config

        companion object : ManagerFactory<TestPureManager, TestPureConfig, TestPureConfigBuilder> {
            override val builder = object : ManagerBuilder<TestPureConfig, TestPureManager> {
                override fun build(config: TestPureConfig): TestPureManager {
                    return TestPureManager().apply { this.config = config }
                }
            }
            override fun createBuilder() = TestPureConfigBuilder()
        }
    }

    @Test
    fun `context-aware manager should be initialized correctly using DSL`() {
        val fakeContext = mockk<Context>(relaxed = true, relaxUnitFun = true) {
            every { applicationContext } returns this
        }
        val expectedApiKey = "test-api-key"

        val manager = TestContextManager.build(fakeContext) {
            apiKey = expectedApiKey
        }

        assertNotNull(manager)
        assertEquals(expectedApiKey, manager.configInstance.apiKey)
        assertEquals(fakeContext, manager.configInstance.context)
    }

    @Test
    fun `pure manager should be initialized correctly using DSL without context`() {
        val expectedPrecision = 5

        val manager = TestPureManager.build {
            precision = expectedPrecision
        }

        assertNotNull(manager)
        assertEquals(expectedPrecision, manager.configInstance.precision)
    }

}
