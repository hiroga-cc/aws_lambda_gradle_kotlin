package cc.hiroga.ktorLambda

import cc.hiroga.ktorLambda.module
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import junit.framework.Assert
import org.junit.Test

class ApplicationTest {
    @Test
    fun testRequest() = withTestApplication({
        module()
    }) {
        with(handleRequest(HttpMethod.Get, "/")) {
            Assert.assertEquals(HttpStatusCode.OK, response.status())
            Assert.assertEquals("Hi, this is Hiroaki!", response.content)
        }
    }
}