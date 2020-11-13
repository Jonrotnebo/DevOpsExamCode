package devopsDemoRest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
 class Controller {


    var logger: Logger = LoggerFactory.getLogger(Controller::class.java)

    @GetMapping(path = ["/ping"])
    fun ping(): String? {
        //logger.info("Testing logz.io! PING Function Call")
        return "pong"
    }

    @GetMapping(path = ["/pong"])
    fun pong(): String? {
        //logger.info("Testing logz.io! Pong Function Call")
        return "ping"
    }

    @GetMapping(path = ["/pingPong"])
    fun pingPong(): String? {
        //logger.info("Testing logz.io! PINGPONG Function Call")
        return "pingPing"
    }

}