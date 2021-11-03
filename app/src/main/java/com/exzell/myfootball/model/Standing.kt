package com.exzell.myfootball.model

import com.google.gson.annotations.SerializedName

data class Standing(@SerializedName("idStanding") val id: Int,
                    @SerializedName("idTeam") val teamId: Int,
                    @SerializedName("strTeam") val name: String,
                    @SerializedName("strTeamBadge") val logoDir: String,
                    @SerializedName("intRank") val position: Int,
                    @SerializedName("strForm") val form: String,
                    @SerializedName("intPlayed") val played: Int,
                    @SerializedName("intWin") val win: Int,
                    @SerializedName("intDraw") val draw: Int,
                    @SerializedName("intLoss") val loss: Int,
                    @SerializedName("intGoalDifference") val gd: Int,
                    @SerializedName("intGoalFor") val gf: Int,
                    @SerializedName("intGoalAgainst") val ga: Int,
                    @SerializedName("") val point: Int)