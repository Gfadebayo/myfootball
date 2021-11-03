package com.exzell.myfootball.model

import com.google.gson.annotations.SerializedName

data class Team(@SerializedName("idTeam") val id: Int,
                @SerializedName("strTeam") val name: String,
                @SerializedName("") val shortName: String,
                @SerializedName("strTeamBadge") val logoDir: String,
                @SerializedName("intRank") val position: Int,
                @SerializedName("strForm") val form: String,
                @SerializedName("intPlayed") val played: Int,
                @SerializedName("intWin") val win: Int,
                @SerializedName("intDraw") val draw: Int,
                @SerializedName("intLoss") val loss: Int,
                @SerializedName("intGoalDifference") val gd: Int,
                @SerializedName("intGoalFor") val gf: Int,
                @SerializedName("intGoalAgainst") val ga: Int)