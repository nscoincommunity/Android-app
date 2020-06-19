package com.stocksexchange.core.formatters

import com.stocksexchange.core.utils.extensions.getFractionalPartPrecision
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import kotlin.math.abs

class NumberFormatter constructor(locale: Locale) {


    companion object {

        private const val ONE_HUNDRED_MILLIONTH = 0.00000001
        private const val ONE_THOUSANDTH = 0.001
        private const val ONE_HUNDREDTH = 0.01
        private const val ONE_TENTH = 0.1
        private const val ONE = 1
        private const val TEN = 10.0
        private const val ONE_HUNDRED = 100.0
        private const val ONE_THOUSAND = 1_000.0
        private const val TEN_THOUSAND = 10_000.0
        private const val ONE_HUNDRED_THOUSAND = 100_000.0
        private const val ONE_MILLION = 1_000_000.0
        private const val TEN_MILLION = 10_000_000.0
        private const val ONE_HUNDRED_MILLION = 100_000_000.0
        private const val ONE_BILLION = 1_000_000_000.0
        private const val TEN_BILLION = 10_000_000_000.0
        private const val ONE_HUNDRED_BILLION = 100_000_000_000.0
        private const val ONE_TRILLION = 1_000_000_000_000.0

        private const val DECIMAL_SEPARATOR_PERIOD = '.'

        private const val DEFAULT_GROUPING_SIZE = 3

        private const val DEFAULT_FIXED_PRICE_MAX_LENGTH = 10
        private const val DEFAULT_VOLUME_MAX_LENGTH = 7
        private const val DEFAULT_DAILY_PRICE_CHANGE_MAX_LENGTH = 6

        const val FORMATTER_GROUPING_SYMBOL = ' '
        const val FORMATTER_DECIMAL_SYMBOL = '.'
        private const val FORMATTER_DIGIT_SYMBOL = '0'

        private const val THOUSAND_ABBREVIATION_SYMBOL = "K"
        private const val MILLION_ABBREVIATION_SYMBOL = "M"
        private const val BILLION_ABBREVIATION_SYMBOL = "B"

        private val ABBREVIATION_SYMBOLS = listOf(
            THOUSAND_ABBREVIATION_SYMBOL,
            MILLION_ABBREVIATION_SYMBOL,
            BILLION_ABBREVIATION_SYMBOL
        )


        fun containsAbbreviationSymbol(value: String): Boolean {
            for(abbreviationSymbol in ABBREVIATION_SYMBOLS) {
                if(value.contains(abbreviationSymbol)) {
                    return true
                }
            }

            return false
        }

    }


    private val mFormatter: DecimalFormat = (NumberFormat.getInstance(locale) as DecimalFormat)

    private val mDefaultFormatter: DecimalFormat = (NumberFormat.getInstance(locale) as DecimalFormat)

    private var mIsGroupingEnabled: Boolean = true



    init {
        // Disabling the grouping in the formatter
        // since we are doing it manually due to the fact
        // that this flag is useless when the pattern
        // contains grouping delimiters
        mFormatter.isGroupingUsed = false
        mFormatter.roundingMode = RoundingMode.FLOOR
        val symbols = mDefaultFormatter.decimalFormatSymbols
        symbols.decimalSeparator = FORMATTER_DECIMAL_SYMBOL
        symbols.groupingSeparator = FORMATTER_GROUPING_SYMBOL
        mFormatter.decimalFormatSymbols = symbols

        val defaultSymbols = mDefaultFormatter.decimalFormatSymbols
        defaultSymbols.decimalSeparator = FORMATTER_DECIMAL_SYMBOL
        mDefaultFormatter.decimalFormatSymbols = defaultSymbols

        mDefaultFormatter.isGroupingUsed = false
        mDefaultFormatter.roundingMode = RoundingMode.FLOOR
    }


    private fun format(payload: Payload): String {
        mFormatter.applyPattern(payload.pattern)
        return mFormatter.format(payload.value)
    }


    private fun defaultFormat(payload: Payload): String {
        mDefaultFormatter.applyPattern(payload.pattern)
        return mDefaultFormatter.format(payload.value)
    }
    

    fun formatVolume(value: Double): String = format(getVolumePayload(value))


    fun formatDailyVolume(value: Double): String = format(getDailyVolumePayload(value))


    fun formatFixedPrice(value: Double): String = format(getFixedPricePayload(value))


    fun formatFixedPriceInFiatCurrency(value: Double, maxLengthSubtrahend: Int): String {
        return format(getFixedPriceInFiatCurrencyPayload(value, maxLengthSubtrahend))
    }


    fun formatLastPriceChange(value: Double): String = format(getLastPriceChangePayload(value))


    fun formatDailyPriceChange(value: Double): String = format(getDailyPriceChangePayload(value))


    fun formatPrice(value: Double): String = format(getPricePayload(value))


    fun formatInputAlertPrice(value: Double): String = defaultFormat(getAlertPricePayload(value))


    fun formatPriceInFiatCurrency(value: Double): String = format(getAmountPayload(value))


    fun formatLimitPrice(value: Double): String = format(getLimitPricePayload(value))


    fun formatMinOrderAmount(value: Double): String = format(getFixedPricePayload(value))


    fun formatFeePercent(value: Double): String = format(getPercentPayload(value))


    fun formatVerificationFee(value: Double): String = format(getVerificationFeePayload(value))


    fun formatTransactionFee(value: Double): String = formatAmount(value)


    fun formatBalance(value: Double): String = format(getBalancePayload(value))


    fun formatAmount(value: Double): String = format(getAmountPayload(value))


    fun formatOrderbookPrice(value: Double, maxLength: Int) = format(getAdvancedFixedPricePayload(value, maxLength))


    fun formatOrderbookAmount(value: Double, maxLength: Int) = format(getAdvancedAmountPayload(value, maxLength))


    fun formatTradeHistoryPrice(value: Double, maxLength: Int) = format(getAdvancedFixedPricePayload(value, maxLength))


    fun formatTradeHistoryAmount(value: Double, maxLength: Int) = format(getAdvancedAmountPayload(value, maxLength))


    fun formatPercentSpread(value: Double) = format(getPercentPayload(value))


    fun formatTradeFormPrice(value: Double, maxLength: Int) = format(getTradeFormPricePayload(value, maxLength))


    /**
     * Parses a string to a double.
     *
     * @param source The string to parse
     * @param default The default value to return if parsing could not be done
     *
     * @return The parsed double or the default value
     */
    fun parse(source: String, default: Double = 0.0): Double {
        if(source.isBlank()) {
            return default
        }

        return try {
            with(mFormatter) {
                applyPattern(getParsingPattern(source))
                parse(source)
            }.toDouble()
        } catch(exception: ParseException) {
            default
        } catch(exception: IllegalArgumentException) {
            default
        }
    }


    fun setDecimalSeparator(separator: Char) {
        val symbols = getDecimalFormatSymbols()
        symbols.decimalSeparator = separator
        setDecimalFormatSymbols(symbols)
    }


    fun getDecimalSeparator(): Char {
        return getDecimalFormatSymbols().decimalSeparator
    }


    fun getGroupingSeparator(): Char {
        return getDecimalFormatSymbols().groupingSeparator
    }


    fun setGroupingEnabled(isGroupingEnabled: Boolean) {
        this.mIsGroupingEnabled = isGroupingEnabled
    }


    private fun setDecimalFormatSymbols(symbols: DecimalFormatSymbols) {
        mFormatter.decimalFormatSymbols = symbols
    }


    private fun getDecimalFormatSymbols(): DecimalFormatSymbols {
        return mFormatter.decimalFormatSymbols
    }


    private fun getVolumePayload(value: Double): Payload {
        return when {
            (value < TEN) -> getAdvancedVolumePayload(value, 1)
            (value < ONE_HUNDRED) -> getAdvancedVolumePayload(value, 2)
            (value < ONE_THOUSAND) -> getAdvancedVolumePayload(value, 3)
            (value < TEN_THOUSAND) -> getAdvancedVolumePayload(value, 4)
            (value < ONE_HUNDRED_THOUSAND) -> getAdvancedVolumePayload(value, 5)
            (value < ONE_MILLION) -> getAdvancedVolumePayload(value, 6)
            (value < TEN_MILLION) -> getAdvancedVolumePayload(value, 7)
            (value < ONE_HUNDRED_MILLION) -> getAdvancedVolumePayload(value, 8)
            (value < ONE_BILLION) -> getAdvancedVolumePayload(value, 9)

            else -> getAdvancedVolumePayload(value, 10)
        }
    }


    private fun getAdvancedVolumePayload(value: Double, numOfIntegerPartDigits: Int): Payload {
        return getAdvancedPayloadWithoutTrimming(
            value,
            DEFAULT_VOLUME_MAX_LENGTH,
            numOfIntegerPartDigits
        )
    }


    private fun getDailyVolumePayload(value: Double): Payload {
        return getAdvancedAmountPayload(value, DEFAULT_FIXED_PRICE_MAX_LENGTH)
    }


    private fun getFixedPricePayload(value: Double): Payload {
        return getAdvancedFixedPricePayload(value, DEFAULT_FIXED_PRICE_MAX_LENGTH)
    }


    private fun getFixedPriceInFiatCurrencyPayload(value: Double, maxLengthSubtrahend: Int): Payload {
        return getAdvancedFixedPricePayload(
            value,
            (DEFAULT_FIXED_PRICE_MAX_LENGTH - maxLengthSubtrahend)
        )
    }


    private fun getLastPriceChangePayload(value: Double): Payload {
        val signChar: String
        val signNumber: Double

        when {
            (value > 0.0) -> {
                signChar = "+"
                signNumber = 1.0
            }

            (value == 0.0) -> {
                signChar = ""
                signNumber = 1.0
            }

            else -> {
                signChar = ""
                signNumber = -1.0
            }
        }

        val pricePayload = getFixedPricePayload(abs(value))

        return Payload((signChar + pricePayload.pattern), (signNumber * pricePayload.value))
    }


    private fun getDailyPriceChangePayload(value: Double): Payload {
        val absValue = abs(value)
        val payload = when {
            (absValue < TEN) -> getAdvancedDailyPriceChangePayload(value, 1)
            (absValue < ONE_HUNDRED) -> getAdvancedDailyPriceChangePayload(value, 2)
            (absValue < ONE_THOUSAND) -> getAdvancedDailyPriceChangePayload(value, 3)
            (absValue < TEN_THOUSAND) -> getAdvancedDailyPriceChangePayload(value, 4)
            (absValue < ONE_HUNDRED_THOUSAND) -> getAdvancedDailyPriceChangePayload(value, 5)
            (absValue < ONE_MILLION) -> getAdvancedDailyPriceChangePayload(value, 6)
            (absValue < TEN_MILLION) -> getAdvancedDailyPriceChangePayload(value, 7)
            (absValue < ONE_HUNDRED_MILLION) -> getAdvancedDailyPriceChangePayload(value, 8)
            (absValue < ONE_BILLION) -> getAdvancedDailyPriceChangePayload(value, 9)

            else -> getAdvancedDailyPriceChangePayload(value, 10)
        }
        val sign = when {
            value > 0.0 -> "+"
            else -> ""
        }

        return Payload("$sign${payload.pattern}'%'", payload.value)
    }


    private fun getAdvancedDailyPriceChangePayload(value: Double, numOfIntegerPartDigits: Int): Payload {
        return getAdvancedPayloadWithoutTrimming(
            value,
            DEFAULT_DAILY_PRICE_CHANGE_MAX_LENGTH,
            numOfIntegerPartDigits
        )
    }


    private fun getPricePayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload(getFormattingPatternWithTrimming(value, 1, 8), value)
            (value < ONE_HUNDRED) -> Payload(getFormattingPatternWithTrimming(value, 2, 7), value)
            (value < ONE_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 3, 6), value)
            (value < TEN_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 4, 5), value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 5, 4), value)
            (value < ONE_MILLION) -> Payload(getFormattingPatternWithTrimming(value, 6, 3), value)
            (value < TEN_MILLION) -> Payload(getFormattingPatternWithTrimming(value, 7, 2), value)

            else -> Payload(getFormattingPatternWithTrimming(value, 8, 1), value)
        }
    }


    private fun getAlertPricePayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 1, 8), value)
            (value < ONE_HUNDRED) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 2, 7), value)
            (value < ONE_THOUSAND) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 3, 6), value)
            (value < TEN_THOUSAND) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 4, 4), value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 5, 3), value)
            (value < ONE_MILLION) -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 6, 2), value)

            else -> Payload(getFormattingPatternWithOutIntegerSeparatorTrimming(value, 7, 0), value)
        }
    }


    private fun getLimitPricePayload(value: Double): Payload {
        return if(value > 0.0) {
            getFixedPricePayload(value)
        } else {
            getPricePayload(value)
        }
    }


    private fun getPercentPayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload("0.00'%'", value)
            (value < ONE_HUNDRED) -> Payload("00.00'%'", value)
            (value < ONE_THOUSAND) -> Payload("000.0'%'", value)

            else -> Payload("0000'%'", value)
        }
    }


    private fun getVerificationFeePayload(value: Double): Payload {
        return when {
            (value >= ONE) -> getPercentPayload(value)
            (value >= ONE_TENTH) -> Payload("0.0'%'", value)
            (value >= ONE_HUNDREDTH) -> Payload("0.00'%'", value)
            (value >= ONE_THOUSANDTH) -> Payload("0.000'%'", value)

            else -> Payload("0'%'", value)
        }
    }


    private fun getBalancePayload(value: Double): Payload {
        return if(value <= 0.0) {
            Payload("0.00000000", value)
        } else {
            getAmountPayload(value)
        }
    }


    private fun getAmountPayload(value: Double): Payload {
        if(value <= 0.0) {
            return Payload(getFormattingPatternWithTrimming(value, 1, 8), value)
        }

        return when {
            (value < ONE_HUNDRED_MILLIONTH) -> Payload("0.000E00", value)
            (value < TEN) -> Payload(getFormattingPatternWithTrimming(value, 1, 8), value)
            (value < ONE_HUNDRED) -> Payload(getFormattingPatternWithTrimming(value, 2, 7), value)
            (value < ONE_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 3, 6), value)
            (value < TEN_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 4, 5), value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload(getFormattingPatternWithTrimming(value, 5, 4), value)
            (value < ONE_MILLION) -> Payload(getFormattingPatternWithTrimming(value, 6, 3), value)
            (value < TEN_MILLION) -> Payload(getFormattingPatternWithTrimming(value, 7, 2), value)
            (value < ONE_HUNDRED_MILLION) -> Payload(getFormattingPatternWithTrimming(value, 8, 1), value)
            (value < ONE_BILLION) -> Payload(getFormattingPatternWithTrimming(value, 9, 0), value)
            (value < TEN_BILLION) -> Payload(getFormattingPatternWithTrimming(value, 10, 0), value)
            (value < ONE_HUNDRED_BILLION) -> Payload(getFormattingPatternWithTrimming(value, 11, 0), value)
            (value < ONE_TRILLION) -> Payload(getFormattingPatternWithTrimming(value, 12, 0), value)

            else -> Payload("0.000E+00", value)
        }
    }


    private fun getAdvancedFixedPricePayload(value: Double, maxLength: Int): Payload {
        return when {
            (value < TEN) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 1)
            (value < ONE_HUNDRED) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 2)
            (value < ONE_THOUSAND) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 3)
            (value < TEN_THOUSAND) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 4)
            (value < ONE_HUNDRED_THOUSAND) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 5)
            (value < ONE_MILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 6)
            (value < TEN_MILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 7)
            (value < ONE_HUNDRED_MILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 8)
            (value < ONE_BILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 9)
            (value < TEN_BILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 10)
            (value < ONE_HUNDRED_BILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 11)
            (value < ONE_TRILLION) -> getAdvancedPayloadWithoutTrimming(value, maxLength, 12)

            else -> getAdvancedPayloadWithoutTrimming(value, maxLength, 13)
        }
    }


    private fun getAdvancedAmountPayload(value: Double, maxLength: Int): Payload {
        return when {
            (value < TEN) -> getAdvancedPayloadWithTrimming(value, maxLength, 1)
            (value < ONE_HUNDRED) -> getAdvancedPayloadWithTrimming(value, maxLength, 2)
            (value < ONE_THOUSAND) -> getAdvancedPayloadWithTrimming(value, maxLength, 3)
            (value < TEN_THOUSAND) -> getAdvancedPayloadWithTrimming(value, maxLength, 4)
            (value < ONE_HUNDRED_THOUSAND) -> getAdvancedPayloadWithTrimming(value, maxLength, 5)
            (value < ONE_MILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 6)
            (value < TEN_MILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 7)
            (value < ONE_HUNDRED_MILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 8)
            (value < ONE_BILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 9)
            (value < TEN_BILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 10)
            (value < ONE_HUNDRED_BILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 11)
            (value < ONE_TRILLION) -> getAdvancedPayloadWithTrimming(value, maxLength, 12)

            else -> getAdvancedPayloadWithTrimming(value, maxLength, 13)
        }
    }


    private fun getAdvancedPayloadWithTrimming(value: Double, maxLength: Int,
                                               numOfIntegerPartDigits: Int): Payload {
        return getAdvancedPayload(value, maxLength, numOfIntegerPartDigits, true)
    }


    private fun getAdvancedPayloadWithoutTrimming(value: Double, maxLength: Int,
                                                  numOfIntegerPartDigits: Int): Payload {
        return getAdvancedPayload(value, maxLength, numOfIntegerPartDigits, false)
    }


    private fun getAdvancedPayload(value: Double, maxLength: Int,
                                   numOfIntegerPartDigits: Int, trimTrailingZeros: Boolean,
                                   abbreviation: String = ""): Payload {
        val integerPart = getIntegerPart(numOfIntegerPartDigits)

        if(integerPart.length > maxLength) {
            return getAdvancedLargePayload(value, maxLength, trimTrailingZeros)
        }

        // Calculating the maximum number of fractional part digits by subtracting
        // from the maximum length the length of integer part, the length of prefix multiplied by
        // 2 (2 was added cause abbreviations tend to take width of up to 2 digits), and 1
        // (fractional part delimiter)
        val maxNumOfFractionalPartDigits = (maxLength - integerPart.length - (abbreviation.length * 2) - 1)
        val defaultPrecision = if(trimTrailingZeros) 0 else maxNumOfFractionalPartDigits
        val fractionalPart = getFractionalPart(
            value,
            maxNumOfFractionalPartDigits,
            defaultPrecision,
            trimTrailingZeros
        )

        return Payload((integerPart + fractionalPart + abbreviation), value)
    }


    private fun getAdvancedLargePayload(value: Double, maxLength: Int, trimTrailingZeros: Boolean): Payload {
        return when {
            (value < ONE_MILLION) -> getAdvancedPayload((value / ONE_THOUSAND), maxLength, 3, trimTrailingZeros, THOUSAND_ABBREVIATION_SYMBOL)
            (value < TEN_MILLION) -> getAdvancedPayload((value / ONE_MILLION), maxLength, 1, trimTrailingZeros, MILLION_ABBREVIATION_SYMBOL)
            (value < ONE_HUNDRED_MILLION) -> getAdvancedPayload((value / ONE_MILLION), maxLength, 2, trimTrailingZeros, MILLION_ABBREVIATION_SYMBOL)
            (value < ONE_BILLION) -> getAdvancedPayload((value / ONE_MILLION), maxLength, 3, trimTrailingZeros, MILLION_ABBREVIATION_SYMBOL)
            (value < TEN_BILLION) -> getAdvancedPayload((value / ONE_BILLION), maxLength, 1, trimTrailingZeros, BILLION_ABBREVIATION_SYMBOL)
            (value < ONE_HUNDRED_BILLION) -> getAdvancedPayload((value / ONE_BILLION), maxLength, 2, trimTrailingZeros, BILLION_ABBREVIATION_SYMBOL)
            (value < ONE_TRILLION) -> getAdvancedPayload((value / ONE_BILLION), maxLength, 3, trimTrailingZeros, BILLION_ABBREVIATION_SYMBOL)

            else -> Payload("0.000E+00", value)
        }
    }


    private fun getTradeFormPricePayload(value: Double, maxLength: Int): Payload {
        val initialPayload = getAdvancedFixedPricePayload(value, maxLength)

        return if(containsAbbreviationSymbol(initialPayload.pattern)) {
            getAmountPayload(value)
        } else {
            initialPayload
        }
    }


    private fun getFormattingPatternWithTrimming(value: Double, numOfIntegerPartDigits: Int,
                                                 maxNumOfFractionalPartDigits: Int, useIntegerPartSeparator: Boolean = true): String {
        return getFormattingPattern(
            value,
            numOfIntegerPartDigits,
            maxNumOfFractionalPartDigits,
            true,
            useIntegerPartSeparator
        )
    }


    private fun getFormattingPatternWithOutIntegerSeparatorTrimming(
        value: Double, numOfIntegerPartDigits: Int, maxNumOfFractionalPartDigits: Int): String {
        return getFormattingPattern(
            value,
            numOfIntegerPartDigits,
            maxNumOfFractionalPartDigits,
            trimTrailingZeros = true,
            useIntegerPartSeparator = false
        )
    }


    private fun getFormattingPatternWithoutTrimming(value: Double, numOfIntegerPartDigits: Int,
                                                    maxNumOfFractionalPartDigits: Int): String {
        return getFormattingPattern(
            value,
            numOfIntegerPartDigits,
            maxNumOfFractionalPartDigits,
            false
        )
    }


    private fun getFormattingPattern(value: Double, numOfIntegerPartDigits: Int,
                                     maxNumOfFractionalPartDigits: Int, trimTrailingZeros: Boolean,
                                     useIntegerPartSeparator: Boolean = true): String {
        val integerPart = getIntegerPart(numOfIntegerPartDigits, useIntegerPartSeparator)
        val fractionalPart = getFractionalPart(value, maxNumOfFractionalPartDigits, 0, trimTrailingZeros)

        return (integerPart + fractionalPart)
    }


    private fun getIntegerPart(numOfDigits: Int, useIntegerPartSeparator: Boolean = true): String {
        val integerPart = StringBuilder()

        for(index in 1..numOfDigits) {
            if(mIsGroupingEnabled && (index > 1) && (((index - 1) % DEFAULT_GROUPING_SIZE) == 0) && useIntegerPartSeparator) {

                integerPart.append(",")
            }

            integerPart.append("0")
        }

        return integerPart.reverse().toString()
    }


    private fun getFractionalPart(value: Double, maxPrecision: Int,
                                  defaultPrecision: Int, trimTrailingZeros: Boolean): String {
        return value.getFractionalPartPrecision(maxPrecision, defaultPrecision, trimTrailingZeros)
            .takeIf { it > 0 }
            ?.let { ".${"0".repeat(it)}" }
            ?: ""
    }


    private fun getParsingPattern(source: String): String {
        val pattern = StringBuilder()
        val groupingSeparator = getGroupingSeparator()
        val decimalSeparator = getDecimalSeparator()

        source.forEachIndexed { _, char ->
            pattern.append(when(char) {
                groupingSeparator -> FORMATTER_GROUPING_SYMBOL
                decimalSeparator -> FORMATTER_DECIMAL_SYMBOL

                else -> FORMATTER_DIGIT_SYMBOL
            })
        }

        return pattern.toString().trim(FORMATTER_GROUPING_SYMBOL, FORMATTER_DECIMAL_SYMBOL)
    }


    fun <T> toApiDataFormatter(block: (NumberFormatter.() -> T)): T {
        val decimalSeparator = getDecimalSeparator()

        setGroupingEnabled(false)
        setDecimalSeparator(DECIMAL_SEPARATOR_PERIOD)

        val result = block(this)

        setGroupingEnabled(true)
        setDecimalSeparator(decimalSeparator)

        return result
    }


    data class Payload(
        val pattern: String,
        val value: Double
    )


}