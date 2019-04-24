package cc.hiroga.ktorLambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.httpMethod
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication

/**
 * Created by @hiroga_cc.
 * Original by sakemotoshinya on 16/11/25.
 */
public class App {
    public fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val lambdaLogger = context.getLogger()
        lambdaLogger.log("event: $event")

        val responseEvent = APIGatewayProxyResponseEvent()

        withTestApplication({
            module()
        }) {
            handleRequest(HttpMethod.Post, "/") {
                setBody(listOf("userId" to "test1").toString())
            }.apply {
                responseEvent.statusCode = response.status()?.value
            }
        }
        return responseEvent
    }
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            println(call.request.httpMethod)
            call.respondText("My Example Blog2", ContentType.Text.Html)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("BlogAppKt"), module = Application::module).start()
}
