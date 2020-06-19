package com.stocksexchange.android.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.database.converters.base.BaseConverter
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.factories.ThemeFactory
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.core.utils.EncryptionUtil
import org.koin.core.get

/**
 * Converters used for converting different type of data
 * from and to the database.
 */
object Converters : BaseConverter() {


    @TypeConverter
    @JvmStatic
    fun fromAuthenticationSessionDuration(authenticationSessionDuration: AuthenticationSessionDuration): String {
        return authenticationSessionDuration.name
    }


    @TypeConverter
    @JvmStatic
    fun toAuthenticationSessionDuration(name: String): AuthenticationSessionDuration {
        return AuthenticationSessionDuration.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromLanguage(language: Language): String {
        return language.name
    }


    @TypeConverter
    @JvmStatic
    fun toLanguage(name: String): Language {
        return Language.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromFiatCurrency(fiatCurrency: FiatCurrency): String {
        return fiatCurrency.name
    }


    @TypeConverter
    @JvmStatic
    fun toFiatCurrency(name: String): FiatCurrency {
        return FiatCurrency.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromCandleStickStyle(candleStickStyle: CandleStickStyle): String {
        return candleStickStyle.name
    }


    @TypeConverter
    @JvmStatic
    fun toCandleStickStyle(name: String): CandleStickStyle {
        return CandleStickStyle.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromDecimalSeparator(decimalSeparator: DecimalSeparator): String {
        return decimalSeparator.name
    }


    @TypeConverter
    @JvmStatic
    fun toDecimalSeparator(name: String): DecimalSeparator {
        return DecimalSeparator.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromDepthChartLineStyle(depthChartLineStyle: DepthChartLineStyle): String {
        return depthChartLineStyle.name
    }


    @TypeConverter
    @JvmStatic
    fun toDepthChartLineStyle(name: String): DepthChartLineStyle {
        return DepthChartLineStyle.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromGroupingSeparator(groupingSeparator: GroupingSeparator): String {
        return groupingSeparator.name
    }


    @TypeConverter
    @JvmStatic
    fun toGroupingSeparator(name: String): GroupingSeparator {
        return GroupingSeparator.valueOf(name)
    }


    @TypeConverter
    @JvmStatic
    fun fromPinCode(pinCode: PinCode): String {
        return get<EncryptionUtil>().encrypt(pinCode.code)
    }


    @TypeConverter
    @JvmStatic
    fun toPinCode(code: String): PinCode {
        return PinCode(get<EncryptionUtil>().decrypt(code))
    }


    @TypeConverter
    @JvmStatic
    fun fromTheme(theme: Theme): Int {
        return theme.id
    }


    @TypeConverter
    @JvmStatic
    fun toTheme(id: Int): Theme {
        return get<ThemeFactory>().getThemeForId(id)
    }


    @TypeConverter
    @JvmStatic
    fun fromTransactionAddressData(transactionAddressData: TransactionAddressData?): String? {
        return transactionAddressData?.let { get<Gson>().toJson(it) }
    }


    @TypeConverter
    @JvmStatic
    fun toTransactionAddressData(json: String?): TransactionAddressData? {
        return json?.let { get<Gson>().fromJson(it) }
    }


    @TypeConverter
    @JvmStatic
    fun fromReferralProgram(referralProgram: ReferralProgram?): String? {
        return referralProgram?.let { get<Gson>().toJson(it) }
    }


    @TypeConverter
    @JvmStatic
    fun toReferralProgram(json: String?): ReferralProgram {
        return (json?.let { get<Gson>().fromJson<ReferralProgram>(it) } ?: ReferralProgram.STUB)
    }


    @TypeConverter
    @JvmStatic
    fun fromOrderTradeList(list: List<Order.Trade>): String {
        return get<Gson>().toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun toOrderTradeList(json: String?): List<Order.Trade> {
        return json?.let { get<Gson>().fromJson<List<Order.Trade>>(it) } ?: listOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromOrderFeeList(list: List<Order.Fee>): String {
        return get<Gson>().toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun toOrderFeeList(json: String?): List<Order.Fee> {
        return json?.let { get<Gson>().fromJson<List<Order.Fee>>(it) } ?: listOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromOrderbookOrderList(list: List<OrderbookOrder>): String {
        return get<Gson>().toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun toOrderbookOrderList(json: String?): List<OrderbookOrder> {
        return json?.let { get<Gson>().fromJson<List<OrderbookOrder>>(it) } ?: listOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromMultiTransactionAddressList(list: List<TransactionAddressData>): String {
        return get<Gson>().toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun toMultiTransactionAddressList(json: String?): List<TransactionAddressData> {
        return json?.let { get<Gson>().fromJson<List<TransactionAddressData>>(it) } ?: listOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromProtocolList(list: List<Protocol>): String {
        return get<Gson>().toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun toProtocolList(json: String?): List<Protocol> {
        return json?.let { get<Gson>().fromJson<List<Protocol>>(it) } ?: listOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromStringBooleanMap(map: Map<String, Boolean>?): String {
        return get<Gson>().toJson(map)
    }


    @TypeConverter
    @JvmStatic
    fun toStringBooleanMap(json: String?): Map<String, Boolean> {
        return json?.let { get<Gson>().fromJson<Map<String, Boolean>>(it) } ?: mapOf()
    }


    @TypeConverter
    @JvmStatic
    fun fromStringDoubleMap(map: Map<String, Double>?): String {
        return get<Gson>().toJson(map)
    }


    @TypeConverter
    @JvmStatic
    fun toStringDoubleMap(json: String?): Map<String, Double> {
        return json?.let { get<Gson>().fromJson<Map<String, Double>>(it) } ?: mapOf()
    }


}