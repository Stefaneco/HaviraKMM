package com.piotrkalin.havira.groupDish.presentation.list

sealed class GroupDishListEvent {
    object ShareJoinCode : GroupDishListEvent()
    object CloseBottomSheet : GroupDishListEvent()
    object OpenBottomSheet : GroupDishListEvent()
    class LeaveGroup(val onSuccess: () -> Unit  = {}) : GroupDishListEvent()
    class DisbandGroup(val onSuccess: () -> Unit  = {}) : GroupDishListEvent()
}
