package org.devops.db

import org.devops.RestAPICards
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType
import kotlin.random.Random

@Repository
interface CardRepository : CrudRepository<Cards, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Cards u where u.id = :id")
    fun lockedFind(@Param("id") id: String) : Cards?

}

@Service
@Transactional
class CardService(
        private val cardRepository: CardRepository
) {

    private val logger = LoggerFactory.getLogger(RestAPICards::class.java.name)

    fun getAll() : MutableIterable<Cards> {

        return cardRepository.findAll()
    }


    fun findByIdEager(id: String) : Cards? {

        val card = cardRepository.findById(id).orElse(null)

        if (card == null){
            logger.info("Card find by id does not exist.")
        }

        return card
    }

    fun createNewCard(id: String, name: String, description: String) : Boolean{
        if(cardRepository.existsById(id)){
            return false
        }

        val randomValue = Random.nextInt(0, 100);
        logger.info("Card Value for card: $id, has value $randomValue")

        val card = Cards(id = id, name = name, description = description, value = randomValue)

        cardRepository.save(card)
        return true
    }

}