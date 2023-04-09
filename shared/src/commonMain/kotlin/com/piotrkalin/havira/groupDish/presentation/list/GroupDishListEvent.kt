package com.piotrkalin.havira.groupDish.presentation.list

sealed class GroupDishListEvent {
    object ShareJoinCode : GroupDishListEvent()
    object CloseBottomSheet : GroupDishListEvent()
    object OpenBottomSheet : GroupDishListEvent()
    object LeaveGroup : GroupDishListEvent()
}
