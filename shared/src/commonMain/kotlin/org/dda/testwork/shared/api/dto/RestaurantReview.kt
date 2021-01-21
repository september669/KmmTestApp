package org.dda.testwork.shared.api.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable()
data class RestaurantReview(
    @SerialName("DateAdded")
    @Serializable(with = SerializerLocalDateTime::class)
    val dateAdded: LocalDateTime,
    @SerialName("IsPositive")
    val isPositive: Boolean,
    @SerialName("Message")
    val _message: String,
    @SerialName("RestaurantName")
    val restaurantName: String,
    @SerialName("UserFIO")
    val _userFIO: String
) {

    val id: String by lazy {
        toString()
    }

    val userFIO by lazy {
        _userFIO.trim()
    }

    val message by lazy {
        _message.trim()
    }

}


object SerializerLocalDateTime : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return decoder.decodeString().toLocalDateTime()
    }

}