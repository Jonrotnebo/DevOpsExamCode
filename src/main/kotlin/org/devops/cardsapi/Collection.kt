package org.devops.cardsapi

import org.devops.cards.CardDto
import org.devops.cards.CollectionDto
import org.devops.cards.Rarity.*

object Collection {

    fun get(): CollectionDto{

        val dto = CollectionDto()

        dto.prices.run {
            put(BRONZE, 100)
            put(SILVER, 500)
            put(GOLD, 1_000)
            put(PINK_DIAMOND, 100_000)
        }

        dto.prices.forEach { dto.millValues[it.key] = it.value / 4 }

        dto.rarityProbabilities.run {
            put(SILVER, 0.10)
            put(GOLD, 0.01)
            put(PINK_DIAMOND, 0.001)
            put(BRONZE, 1 - get(SILVER)!! -get(GOLD)!! - get(PINK_DIAMOND)!!)
        }
        addCards(dto)

        return dto
    }

    private fun addCards(dto: CollectionDto){

        dto.cards.run {
            add(CardDto("c000", "Green Mold", "lore ipsum", BRONZE))
            add(CardDto("c001", "Opera Singer", "lore ipsum", BRONZE))
            add(CardDto("c002", "Not Stitch", "lore ipsum", BRONZE))
            add(CardDto("c003", "Assault Hamster", "lore ipsum", BRONZE))
            add(CardDto("c004", "WTF?!?", "lore ipsum", BRONZE))
            add(CardDto("c005", "Stupid Lump", "lore ipsum", BRONZE))
            add(CardDto("c006", "Sad Farter", "lore ipsum", BRONZE))
            add(CardDto("c007", "Smelly Tainter", "lore ipsum", BRONZE))
            add(CardDto("c008", "Hårboll", "lore ipsum", BRONZE))
            add(CardDto("c009", "Blue Horned", "lore ipsum", BRONZE))
            add(CardDto("c010", "Børje McTrumf", "lore ipsum", SILVER))
            add(CardDto("c011", "Exa Nopass", "lore ipsum", SILVER))
            add(CardDto("c012", "Dick Tracy", "lore ipsum", SILVER))
            add(CardDto("c013", "Marius Mario", "lore ipsum", SILVER))
            add(CardDto("c014", "Patrick Stew", "lore ipsum", SILVER))
            add(CardDto("c015", "Fluffy The Hugger of Death", "lore ipsum", GOLD))
            add(CardDto("c016", "Gary The Wise", "lore ipsum", GOLD))
            add(CardDto("c017", "Grump-Grump The Grumpy One", "lore ipsum", GOLD))
            add(CardDto("c018", "Bert-ho-met The Polite Guy", "lore ipsum", GOLD))
            add(CardDto("c019", "Bengt The Destroyer", "lore ipsum", PINK_DIAMOND))
        }

        assert(dto.cards.size == dto.cards.map { it.cardId }.toSet().size)
        assert(dto.cards.size == dto.cards.map { it.name }.toSet().size)
    }
}