package org.devops.cardsapi

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.devops.Application
import org.devops.db.CardRepository
import org.devops.db.CardService
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun testCreateUser() {
        val id = "foo"
        val name = "hei"

/*
        given().post()"/api").
                basic(id, "123")
                .put("/$id")
                .then()
                .statusCode(201)
*/

        assertTrue(cardService.createNewCard(id,name))
        assertTrue(cardRepository.existsById(id))
    }

    /*
    @Test
    fun testGetUser(){

        val id = "foo"
        val name = "hei"
        cardService.createNewCard(id, name)

        given().auth().basic(id, "123")
                .get("/$id")
                .then()
                .statusCode(200)
    }

     */
/*
    @Test
    fun testGetCollection() {
        given().get("/api/cards/collection_$LATEST")
                .then()
                .statusCode(200)
                .body("data.cards.size", Matchers.greaterThan(10))
    }



    @Test
    fun testGetCollectionOldVersion() {
        given().get("/api/cards/collection_v0_002")
                .then()
                .statusCode(200)
                .body("data.cards.size", Matchers.greaterThan(10))
    }

 */
}