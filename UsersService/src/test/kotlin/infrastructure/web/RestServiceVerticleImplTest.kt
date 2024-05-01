package infrastructure.web

import infrastructure.web.handlers.UserHandler
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk

class RestServiceTest : ShouldSpec({
    context("Having a REST service") {
        val port = 8080
        val userHandler = mockk<UserHandler>()

        should("start the service") {
            shouldNotThrowAny {
                RestService(port, userHandler).init()
            }
        }
    }
})