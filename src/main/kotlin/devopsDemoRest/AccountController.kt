package devopsDemoRest

import org.springframework.boot.SpringApplication

object DemoApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(Controller::class.java, *args)
    }
}