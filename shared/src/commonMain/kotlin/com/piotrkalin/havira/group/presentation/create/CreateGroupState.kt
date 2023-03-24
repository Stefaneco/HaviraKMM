package com.piotrkalin.havira.group.presentation.create

/*data class CreateGroupState (
    val groupName: String = "Pomidorki",
    val error : String? = null,
    val isValidGroup : Boolean = false,
    val isCreating : Boolean = false,
    val isCreated: Boolean = true,
    val joinCode : String? = "null123"
)*/

data class CreateGroupState (
    val groupName: String = "",
    val error : String? = null,
    val isValidGroup : Boolean = false,
    val isCreating : Boolean = false,
    val isCreated: Boolean = false,
    val joinCode : String? = null,
    val groupId : Long? = null
)
