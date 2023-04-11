package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.group.domain.model.Group
import database.GroupEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun GroupEntity.toGroup() : Group{
    return Group(
        id = id,
        ownerId = ownerId,
        joinCode = joinCode,
        name = name,
        created = Instant.fromEpochMilliseconds(created)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}