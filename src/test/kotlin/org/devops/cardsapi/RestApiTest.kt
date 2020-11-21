package org.devops.cardsapi

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.devops.Application
import org.devops.db.CardRepository
import org.devops.db.CardService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(Application::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiTest {

    @LocalServerPort
    protected var port = 0

    @PostConstruct
    fun init(){
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath="/cards/collection"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Autowired
    private lateinit var cardService: CardService

    @Autowired
    private lateinit var cardRepository: CardRepository

    @BeforeEach
    fun initTest() {
        cardRepository.deleteAll()
    }

    @Test
    fun testCreateCard() {
        val id = "foo"
        val name = "hei"
        val description = "Testing a mania card"
        cardService.createNewCard(id = id, name = name, description = description)

        assertTrue(cardRepository.existsById(id))
    }


    @Test
    fun testGetCollection() {
        val id = "foo"
        val name = "hei"
        val description = "Testing a mania card"
        cardService.createNewCard(id, name, description)
        cardService.createNewCard(name, id, description)
        assertTrue(cardRepository.existsById(id))
        assertTrue(cardRepository.existsById(name))

        given().get()
                .then()
                .statusCode(200)

    }
}