package com.exzell.myfootball.model

import com.google.gson.annotations.SerializedName

data class League(@SerializedName("idLeague") val id: Int,
                  @SerializedName("strLeague") val name: String,
                  @SerializedName ("strBadge") val logoDir: String,
                  @SerializedName("strCurrentSeason") val currentSeason: String,
                  @SerializedName("intFormedYear") val establishedYear: Int,
                  @SerializedName("strCountry") val country: String,
                  @SerializedName("strWebsite") val website: String,
                  @SerializedName("strDescriptionEN") val description: String,
                  val strSport: String,)
