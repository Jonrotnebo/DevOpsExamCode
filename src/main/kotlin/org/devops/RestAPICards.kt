package org.devops

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.devops.cards.GameDto
import org.devops.db.CardService
import org.devops.db.Cards
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Api(value = "/cards", description = "Operation on the cards existing in the game")
@RequestMapping(path = ["/cards"])
@RestController
class RestAPICards(
        @Autowired
        private val meterRegistry: MeterRegistry

) {

    private val logger = LoggerFactory.getLogger(RestAPICards::class.java.name)

    private val createCounter = Counter.builder("count.create").description("Cards created").register(meterRegistry)
    private val getCounter = Counter.builder("count.get").description("get calls").register(meterRegistry)


    //@Autowired
    //private lateinit var meterRegistry: MeterRegistry

    @Autowired
    private lateinit var cardService: CardService


    @ApiOperation("Return info on all cards in the game")
    @GetMapping(path = ["/collection"])
    fun ListCards() : ResponseEntity<List<Cards>> {

        val collection =  cardService.getAll()
        getCounter.increment()

        return ResponseEntity.status(200).body(collection.toList())
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
    fun createCard(@RequestBody dto: GameDto) : ResponseEntity<Cards>{
        logger.debug("Created monster, in function: {}", createCounter.count())
        if (dto.id == null || dto.name == null) {
            logger.error("create user, id or name is null.")
            return ResponseEntity.status(400).build()
        }

        val ok = cardService.createNewCard(dto.id!!, dto.name!!)

        meterRegistry.counter("txcount", "name", dto.name).increment();

        if (!ok){
            logger.error("Creation of user failed.")
            return ResponseEntity.status(400).build()
        } else{

            createCounter.increment()
            logger.debug("Created monster after create: {}", createCounter.count())
            logger.info("Create Card Sucsess.")
            return ResponseEntity.status(201).build()
        }

    }


}