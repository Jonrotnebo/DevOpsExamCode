package org.devops

import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.devops.cards.GameDto
import org.devops.db.CardService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Api(value = "/api/cards", description = "Operation on the cards existing in the game")
@RequestMapping(path = ["/api/cards"])
@RestController
class RestAPICards() {
    companion object{
        const val LATEST = "v1_000"
    }
    var cardsCreated = 0

    val logger = LoggerFactory.getLogger(RestAPICards::class.java.name)

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    @Autowired
    private lateinit var cardService: CardService


    @ApiOperation("Return info on all cards in the game")
    @GetMapping(
            path = ["/collection"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLatest() : ResponseEntity<Void> {

        val collection =  cardService.getAll()

        return ResponseEntity.status(200).build()
    }
/*
    @ApiOperation("Old-version endpoints. Will automatically redirect to most recent version")
    @GetMapping(path = [
        "/collection_v0_001",
        "/collection_v0_002",
        "/collection_v0_003"
    ])
    fun getOld() : ResponseEntity<Void> {

        return ResponseEntity.status(301)
                .location(URI.create("/api/cards/collection_$LATEST"))
                .build()
    }

 */


    @ApiOperation("Adding own Cards to a List")
    @PostMapping(path= ["/collection"], consumes = ["application/json"], produces = ["application/json"])
    fun createCard(@RequestBody dto: GameDto) : ResponseEntity<Void>{
        if (dto.id == null || dto.name == null) {
            logger.error("create user, id or name is null.")
            return ResponseEntity.status(400).build()
        }

        val ok = cardService.createNewCard(dto.id!!, dto.name!!)

        meterRegistry.counter("txcount", "name", dto.name).increment();
        meterRegistry.gauge("numberOfUsers", cardsCreated)

        if (!ok){
            logger.error("Creation of user failed.")
            return ResponseEntity.status(400).build()
        } else{
            ++cardsCreated
            logger.info("Create Card Sucsess")
            return ResponseEntity.status(201).build()
        }


    }
}