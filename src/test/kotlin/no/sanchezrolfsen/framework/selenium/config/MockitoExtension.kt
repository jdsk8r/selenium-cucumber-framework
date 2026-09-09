package no.sanchezrolfsen.framework.selenium.config

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.mock
import java.lang.reflect.Parameter

class MockitoExtension : TestInstancePostProcessor, ParameterResolver {

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        MockitoAnnotations.openMocks(testInstance)
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.isAnnotationPresent(Mock::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any =
        getMock(parameterContext.parameter, extensionContext)

    private fun getMock(parameter: Parameter, extensionContext: ExtensionContext): Any =
        getMock(parameter, extensionContext, parameter.type)

    private fun <T : Any> getMock(parameter: Parameter, extensionContext: ExtensionContext, mockType: Class<T>): T {
        val mocks = extensionContext.getStore(Namespace.create(MockitoExtension::class.java, mockType))
        val mockName = getMockName(parameter)

        return if (mockName != null) {
            mocks.computeIfAbsent(mockName, { mock(mockType, mockName) }, mockType)
        } else {
            mocks.computeIfAbsent(mockType.canonicalName, { mock(mockType) }, mockType)
        }
    }

    private fun getMockName(parameter: Parameter): String? {
        val explicitMockName = parameter.getAnnotation(Mock::class.java).name.trim()
        return when {
            explicitMockName.isNotEmpty() -> explicitMockName
            parameter.isNamePresent -> parameter.name
            else -> null
        }
    }
}
