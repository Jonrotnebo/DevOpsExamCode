package devopsDemoRest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class AccountController {

}
fun main(args: Array<String>) {
    SpringApplication.run(AccountController::class.java, *args)
}