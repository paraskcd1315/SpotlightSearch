package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

internal class DateExpressionParser {
    private val weekdaysEs = listOf("domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado")
    private val weekdaysEn = listOf("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday")
    private val monthsEs = listOf(
        "enero", "febrero", "marzo", "abril", "mayo", "junio",
        "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
    )
    private val monthsEn = listOf(
        "january", "february", "march", "april", "may", "june",
        "july", "august", "september", "october", "november", "december"
    )
    private val relativeDays = mapOf(
        "mañana" to 1, "tomorrow" to 1,
        "ayer" to -1, "yesterday" to -1,
        "hoy" to 0, "today" to 0
    )
    private val englishRelativeDays = setOf("tomorrow", "yesterday", "today")
    private val ordinalsEn = mapOf("first" to 1, "second" to 2, "third" to 3, "fourth" to 4)
    private val ordinalsEs = mapOf("primer" to 1, "primero" to 1, "segundo" to 2, "tercero" to 3, "cuarto" to 4)

    private val namedDayEs =
        Regex("""^(el\s*)?(lunes|martes|miércoles|jueves|viernes|sábado|domingo)(\s+que\s+viene|\s+pasado)?$""")
    private val namedDayEn =
        Regex("""^(next|last)?\s*(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$""")
    private val ordinalDayEn = Regex(
        """^(first|second|third|fourth|last)\s+(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\s+of\s+""" +
            """(january|february|march|april|may|june|july|august|september|october|november|december)$"""
    )
    private val ordinalDayEs = Regex(
        """^(primer|primero|segundo|tercero|cuarto|último)\s+(lunes|martes|miércoles|jueves|viernes|sábado|domingo)\s+de\s+""" +
            """(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)$"""
    )
    private val dateLiteralEn = Regex(
        """^(january|february|march|april|may|june|july|august|september|october|november|december)\s*(\d{1,2})$"""
    )
    private val dateLiteralEs = Regex(
        """^el\s*(\d{1,2})\s*de\s*(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)$"""
    )
    private val dateMath = Regex(
        """^(today|hoy|tomorrow|mañana|yesterday|ayer|yesteryear|añopasado)\s*([+\-])\s*(\d+)\s*""" +
            """(days?|días?|weeks?|semanas?|months?|meses?|years?|años?)$"""
    )

    fun parse(raw: String): CalculationHit? {
        val input = raw.lowercase(Locale.getDefault()).trim()

        namedDayEs.matchEntire(input)?.let { match ->
            val offset = match.groupValues[3].trim()
            return hit(namedDay(weekdaysEs.indexOf(match.groupValues[2]), offset == "pasado", offset == "que viene"), false)
        }
        namedDayEn.matchEntire(input)?.let { match ->
            val offset = match.groupValues[1]
            return hit(namedDay(weekdaysEn.indexOf(match.groupValues[2]), offset == "last", offset == "next"), true)
        }
        ordinalDayEn.matchEntire(input)?.let { match ->
            val date = ordinalWeekday(
                nth = if (match.groupValues[1] == "last") null else ordinalsEn[match.groupValues[1]] ?: 1,
                weekday = weekdaysEn.indexOf(match.groupValues[2]),
                month = monthsEn.indexOf(match.groupValues[3])
            )
            return hit(date, true)
        }
        ordinalDayEs.matchEntire(input)?.let { match ->
            val date = ordinalWeekday(
                nth = if (match.groupValues[1].contains("último")) null else ordinalsEs[match.groupValues[1]] ?: 1,
                weekday = weekdaysEs.indexOf(match.groupValues[2]),
                month = monthsEs.indexOf(match.groupValues[3])
            )
            return hit(date, false)
        }
        dateLiteralEn.matchEntire(input)?.let { match ->
            return hit(nextOccurrence(monthsEn.indexOf(match.groupValues[1]), match.groupValues[2].toInt()), true)
        }
        dateLiteralEs.matchEntire(input)?.let { match ->
            return hit(nextOccurrence(monthsEs.indexOf(match.groupValues[2]), match.groupValues[1].toInt()), false)
        }
        relativeDays[input]?.let { days ->
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, days)
            return hit(calendar.time, input in englishRelativeDays)
        }
        dateMath.matchEntire(input)?.let { match -> return dateArithmetic(match) }
        return null
    }

    private fun namedDay(weekday: Int, past: Boolean, next: Boolean): Date {
        val today = Calendar.getInstance()
        var delta = weekday - (today.get(Calendar.DAY_OF_WEEK) - 1)
        if (past && delta >= 0) delta -= 7
        if (next && delta <= 0) delta += 7
        today.add(Calendar.DATE, delta)
        return today.time
    }

    private fun ordinalWeekday(nth: Int?, weekday: Int, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        if (nth == null) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            while (calendar.get(Calendar.DAY_OF_WEEK) - 1 != weekday) calendar.add(Calendar.DAY_OF_MONTH, -1)
            return calendar.time
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        var matches = 0
        while (matches < nth) {
            if (calendar.get(Calendar.DAY_OF_WEEK) - 1 == weekday) matches++
            if (matches < nth) calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.time
    }

    private fun nextOccurrence(month: Int, day: Int): Date {
        val today = Calendar.getInstance()
        var year = today.get(Calendar.YEAR)
        val passed = today.get(Calendar.MONTH) > month ||
            (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) > day)
        if (passed) year += 1
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 12, 0, 0)
        return calendar.time
    }

    private fun dateArithmetic(match: MatchResult): CalculationHit {
        val base = match.groupValues[1]
        val sign = if (match.groupValues[2] == "+") 1 else -1
        val amount = match.groupValues[3].toInt() * sign
        val unit = match.groupValues[4]
        val calendar = Calendar.getInstance()
        when (base) {
            "tomorrow", "mañana" -> calendar.add(Calendar.DATE, 1)
            "yesterday", "ayer" -> calendar.add(Calendar.DATE, -1)
            "yesteryear", "añopasado" -> calendar.add(Calendar.YEAR, -1)
        }
        when {
            unit.startsWith("day") || unit.startsWith("día") -> calendar.add(Calendar.DATE, amount)
            unit.startsWith("week") || unit.startsWith("semana") -> calendar.add(Calendar.DATE, amount * 7)
            unit.startsWith("month") || unit.startsWith("mes") -> calendar.add(Calendar.MONTH, amount)
            unit.startsWith("year") || unit.startsWith("año") -> calendar.add(Calendar.YEAR, amount)
        }
        return hit(calendar.time, base in setOf("today", "tomorrow", "yesterday", "yesteryear"))
    }

    private fun hit(date: Date, english: Boolean): CalculationHit {
        val locale = if (english) Locale.ENGLISH else Locale.getDefault()
        val formatted = SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm", locale).format(date)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        return CalculationHit(answer = formatted, kind = CalculationKind.DATE)
    }
}
