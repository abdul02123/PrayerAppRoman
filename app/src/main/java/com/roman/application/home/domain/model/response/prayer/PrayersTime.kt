package com.roman.application.home.domain.model.response.prayer

import com.roman.application.util.enums.DateFormat
import kotlinx.parcelize.IgnoredOnParcel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PrayersTime(
    val fajr: Date,
    val shuruq: Date,
    val duhur: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date
) {

    @IgnoredOnParcel
    private val timeFormat = SimpleDateFormat(DateFormat.HH_MM_A.identifierName, Locale.getDefault())

    fun getCurrentPrayer(): Pair<Pair<String, String>, Pair<String, String>> {
        val currentTime = Date()
        return when {
            currentTime.after(fajr) && currentTime.before(shuruq) -> Pair(
                Pair(
                    "Fajr",
                    timeFormat.format(fajr)
                ), Pair("Shuruq", timeFormat.format(shuruq))
            )

            currentTime.after(shuruq) && currentTime.before(duhur) -> Pair(
                Pair(
                    "Shuruq",
                    timeFormat.format(shuruq)
                ), Pair("Duhur", timeFormat.format(duhur))
            )

            currentTime.after(duhur) && currentTime.before(asr) -> Pair(
                Pair(
                    "Duhur",
                    timeFormat.format(duhur)
                ), Pair("Asr", timeFormat.format(asr))
            )

            currentTime.after(asr) && currentTime.before(maghrib) -> Pair(
                Pair(
                    "Asr",
                    timeFormat.format(asr)
                ), Pair("Maghrib", timeFormat.format(maghrib))
            )

            currentTime.after(maghrib) && currentTime.before(isha) -> Pair(
                Pair(
                    "Maghrib",
                    timeFormat.format(maghrib)
                ), Pair("Isha", timeFormat.format(isha))
            )

            else -> Pair(
                Pair("Isha", timeFormat.format(isha)),
                Pair("Fajr", timeFormat.format(fajr))
            )
        }
    }

}

fun List<String>.toPrayersModel(date: String): PrayersTime {
    val timeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val fajr = timeFormat.parse("$date ${this[0]}")
    val shuruq = timeFormat.parse("$date ${this[1]}")
    val duhur = timeFormat.parse("$date ${this[2]}")
    val asr = timeFormat.parse("$date ${this[3]}")
    val maghrib = timeFormat.parse("$date ${this[4]}")
    val isha = timeFormat.parse("$date ${this[5]}")
    return PrayersTime(fajr!!, shuruq!!, duhur!!, asr!!, maghrib!!, isha!!)
}
