package org.devops.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
class Cards (

    @get:Id
    @get:NotNull
    var id: String? = null,

    var name: String? = null,

    var description: String? = null,

    var value: Int? = 0

)