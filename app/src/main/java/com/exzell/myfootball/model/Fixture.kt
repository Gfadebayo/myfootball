package com.exzell.myfootball.model

import com.google.gson.annotations.SerializedName

data class Fixture(@SerializedName("idEvent") val id: Int,
                   @SerializedName("intRound") val week: Int,
                   @SerializedName("strHomeTeam") val homeName: String,
                   @SerializedName("strAwayTeam") val awayName: String,
                   @SerializedName("intHomeScore") val homeScore: Int,
                   @SerializedName("intAwayScore") val awayScore: Int,
                   @SerializedName("strStatus") val status: String,
                   @SerializedName("strSeason") val season: String,
                   @SerializedName("dateEvent") val date: String,
                   @SerializedName("idHomeTeam") val homeTeamId: Int,
                   @SerializedName("idAwayTeam") val awayTeamId: Int,
                   @SerializedName("idLeague") val leagueId: Int,
                   @SerializedName("strAwayRedCards") val awayRedCards: String,
                   @SerializedName("strAwayYellowCards") val awayYellowCards: String,
                   @SerializedName("strHomeRedCards") val homeRedCards: String,
                   @SerializedName("strHomeYellowCards") val homeYellowCards: String,
                   @SerializedName("strHomeGoalDetails") val homeScorers: String,
                   @SerializedName("strAwayGoalDetails") val awayScorers: String,
                   @SerializedName("intHomeShots") val homeShots: Int,
                   @SerializedName("intAwayShots") val awayShots: Int) : Lineup() {

    var homeLogoDir: String? = null

    var awayLogoDir: String? = null
}
