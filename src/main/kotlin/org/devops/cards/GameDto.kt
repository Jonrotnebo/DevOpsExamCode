package org.devops.cards

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import lombok.Data

@ApiModel(description = "A new Game Question")
@Data
class GameDto(

        @ApiModelProperty("The id Of this resouce")
        var id: String? = null,

        @ApiModelProperty("A Name for The Question")
        var name: String? = null,

        @ApiModelProperty("A Description of the quest")
        var description: String? = null,

        @ApiModelProperty("A value of the card")
        var value: Int? = 0
)