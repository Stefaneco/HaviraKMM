package com.example.havira.dish.presentation.edit

import com.example.havira.core.domain.util.Resource
import com.example.havira.core.domain.util.toCommonStateFlow
import com.example.havira.dish.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DishEditViewModel(
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishEditState())
    val state = _state.asStateFlow().toCommonStateFlow()

    fun onEvent(event: DishEditEvent){
        when(event){
            DishEditEvent.BackButtonPressed -> TODO()
            is DishEditEvent.EditDescription -> {
                _state.update { it.copy(
                    desc = event.description
                ) }
            }
            is DishEditEvent.EditDish -> {
                editDish(_state.value, event.onEdit)
            }
            is DishEditEvent.EditTitle -> {
                _state.update { it.copy(
                    title = event.title,
                    isValidDish = isValidDish(it.dishId ,event.title)
                ) }
            }
            DishEditEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
        }
    }

    fun loadDish(dishId: Long){
        if (_state.value.dishId != null) return
        viewModelScope.launch {
            dishInteractors.getDishById(dishId).collect { dishResource ->
                when(dishResource){
                    is Resource.Success -> {
                        _state.update { it.copy(
                            dishId = dishResource.data?.id,
                            title = dishResource.data?.title ?: "",
                            desc = dishResource.data?.desc ?: "",
                            isValidDish = isValidDish(dishResource.data?.id,dishResource.data?.title ?: "")
                        ) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(
                            error = dishResource.throwable?.message ?: "Unknown error"
                        ) }
                    }
                }
            }
        }
    }

    private fun editDish(state: DishEditState, onEdit: () -> Unit){
        if (!state.isValidDish) return
        viewModelScope.launch {
            _state.update { it.copy( isSaving = true ) }

            val result = dishInteractors.editDish(
                id = state.dishId!!,
                title = state.title,
                description = state.desc
            )

            if(result.isFailure) {
                println(result.exceptionOrNull()?.message)
                _state.update { it.copy(
                    error = result.exceptionOrNull()?.message,
                    isSaving = false
                ) }
            }
            else if(result.isSuccess){
                _state.update { it.copy(
                    isSaving = false
                ) }
                onEdit()
            }
        }
    }

    private fun isValidDish(id: Long?, title: String) : Boolean{
        return title.isNotBlank() && id != null
    }
}