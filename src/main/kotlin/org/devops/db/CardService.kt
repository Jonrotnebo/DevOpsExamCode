package org.devops.db

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

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

    fun getAll() : MutableIterable<Cards> {


        return cardRepository.findAll()
    }


    fun findByIdEager(id: String) : Cards? {

        val card = cardRepository.findById(id).orElse(null)

        return card
    }

    fun createNewCard(id: String, name: String) : Boolean{
        if(cardRepository.existsById(id)){
            return false
        }


        val card = Cards(id = id, name = name)

        cardRepository.save(card)
        return true
    }

}