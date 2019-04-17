package cc.hiroga.ktorLambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.security.SecureRandom
import java.util.*

/**
 * Created by sakemotoshinya on 16/11/25.
 */
public class App {
    public fun handleRequest(request: RequestClass, context: Context): String {
        val lambdaLogger = context.getLogger()
        lambdaLogger.log(request.httpMethod?: "no http method...")

        return withTestApplication({
            module()
        }) {
            with(handleRequest(HttpMethod.Get, "/")) {
                return@withTestApplication response.status().toString()
            }
        }
    }
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("My Example Blog2", ContentType.Text.Html)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("BlogAppKt"), module = Application::module).start()
}
