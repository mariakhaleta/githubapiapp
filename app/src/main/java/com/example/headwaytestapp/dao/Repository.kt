package com.example.headwaytestapp.dao

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "repositories_list")
data class Repository(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("language") val language: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("created_at") val date: String?,
) {
    @Ignore val correctFormatDate = date.toFormattedDate()
}
private const val RESPONSE_DATE_FORMAT = "yyyy-MM-DD'T'HH:MM:SS" //2022-05-20T13:27:58Z
private const val NATIVE_DATE_FORMAT = "dd MMMM yyyy"

private val parser = SimpleDateFormat(RESPONSE_DATE_FORMAT, Locale.ENGLISH)
private val formatter = SimpleDateFormat(NATIVE_DATE_FORMAT, Locale.ENGLISH)
private fun String?.toFormattedDate(): String? = parser.parse(this!!)?.let { formatter.format(it) }?.lowercase()