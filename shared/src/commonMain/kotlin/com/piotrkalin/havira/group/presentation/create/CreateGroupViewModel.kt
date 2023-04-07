package com.piotrkalin.havira.group.presentation.create

import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateGroupViewModel(
    private val groupInteractors: GroupInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(CreateGroupState())
    val state = _state.asStateFlow().toCommonStateFlow()

    init {

    }

    fun onEvent(event: CreateGroupEvent){
        when(event){
            CreateGroupEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
            CreateGroupEvent.BackButtonPressed -> {}
            is CreateGroupEvent.EditGroupName -> {
                _state.update { it.copy(
                    groupName = event.name,
                    isValidGroup = isValidGroup(event.name)
                ) }
            }
            CreateGroupEvent.CreateGroup -> {
                if (!_state.value.isValidGroup) return
                viewModelScope.launch {
                    _state.update { it.copy(
                        isCreating = true
                    ) }
                    groupInteractors.createGroup(name = _state.value.groupName).collect { result ->
                        if(result.isSuccess){
                            _state.update { it.copy(
                                isCreating = false,
                                joinCode = result.getOrNull()?.joinCode,
                                groupId = result.getOrNull()?.id,
                                isCreated = true
                            ) }
                        }
                        else if (result.isFailure) {
                            _state.update { it.copy(
                                isCreating = false,
                                error = result.exceptionOrNull()?.message
                            ) }
                        }
                    }
                }
            }
            is CreateGroupEvent.NavigateToCreatedGroup -> {}
        }
    }

    private fun isValidGroup(name : String) : Boolean {
        return name.isNotBlank()
    }
}