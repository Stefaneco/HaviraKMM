package com.piotrkalin.havira.core.domain.util

import kotlinx.datetime.*

object DateTimeUtil {
    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun fromEpochMillis(millis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun formatDate(millis: Long): String {
        return formatDate(fromEpochMillis(millis))
    }

    fun formatDate(dateTime: LocalDateTime): String {
        //val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val month = if(dateTime.monthNumber < 10) "0${dateTime.monthNumber}" else dateTime.monthNumber
        val day = if(dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year
        //val hour = if(dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour
        //val minute = if(dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute

        return buildString {
            append(day)
            append(".")
            append(month)
            append(".")
            append(year)
            //append(", ")
            //append(hour)
            //append(":")
            //append(minute)
        }
    }
}