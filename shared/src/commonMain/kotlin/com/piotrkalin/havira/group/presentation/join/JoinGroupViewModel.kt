package com.piotrkalin.havira.group.presentation.join

import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JoinGroupViewModel(
    private val groupInteractors: GroupInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(JoinGroupState())
    val state = _state.asStateFlow().toCommonStateFlow()

    fun onEvent(event: JoinGroupEvent){
        when(event){
            JoinGroupEvent.BackButtonPressed -> TODO()
            is JoinGroupEvent.EditJoinCode -> {
                val filteredInput = event.code.filter { !it.isWhitespace() }
                _state.update { it.copy(
                    joinCode = filteredInput,
                    isValidCode = isValidJoinCode(filteredInput)
                ) }
            }
            JoinGroupEvent.JoinGroup -> {
                _state.update { it.copy(
                    isJoining = true
                ) }
               viewModelScope.launch {
                   groupInteractors.joinGroup(_state.value.joinCode).collect{ result ->
                        if (result.isSuccess){
                            _state.update { it.copy(
                                isJoining = false,
                                group = result.getOrNull()
                            )}
                        }
                        else if (result.isFailure){
                            _state.update { it.copy(
                                isJoining = false,
                                error = result.exceptionOrNull()?.message
                            )}
                        }
                   }
               }
            }
            JoinGroupEvent.NavigateToJoinedGroup -> TODO()
            JoinGroupEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
        }
    }

    private fun isValidJoinCode(code : String) : Boolean {
        return code.length > 5
    }
}