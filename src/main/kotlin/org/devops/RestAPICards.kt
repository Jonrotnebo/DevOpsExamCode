package org.devops

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.devops.cards.GameDto
import org.devops.db.CardService
import org.devops.db.Cards
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
    private val getTimerCards = Timer.builder("long.get.timer").description("Record Time of get function").tag("us", "listCards").register(meterRegistry)

    private val cardsSummary = DistributionSummary.builder("response.size")
            .description("Cards Destribution")
            .baseUnit("byte")
            .scale(100.0).register(meterRegistry)


    //@Autowired
    //private lateinit var meterRegistry: MeterRegistry

    @Autowired
    private lateinit var cardService: CardService


    @ApiOperation("Return info on all cards in the game")
    @GetMapping(path = ["/collection"])
    @Timed("listCards")
    fun ListCards() : ResponseEntity<List<Cards>> {

        //val timerTask
        val collection = cardService.getAll()

        //cardsSummary.record(collection.sumByDouble {  })
        getCounter.increment()

        logger.info("ListCards{} called. Returning cards. Returning: ${collection.count()} Cards")

        //logger.info("List Cards Time: Returning timer ${getTimerCards.mean()}")
        logger.info("Number of calls to get. ${getCounter.count()}")
        return ResponseEntity.status(200).body(collection.toList())
    }


    @ApiOperation("Adding own Cards to a List")
    @PostMapping(path= ["/collection"], consumes = ["application/json"], produces = ["application/json"])
    @Timed("createCard")
    fun createCard(@RequestBody dto: GameDto) : ResponseEntity<Cards>{
        logger.debug("Created user, in function: {}", createCounter.count())
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
            logger.debug("Created monster after create: ${createCounter.count()} Creation calls")
            logger.info("Create Card Sucsess.")
            return ResponseEntity.status(201).build()
        }

    }


}