package com.exzell.myfootball.model

import com.google.gson.annotations.SerializedName

abstract class Lineup {

    @SerializedName("strHomeFormation")
    var homeFormation: String = ""

    @SerializedName("strAwayFormation")
    var awayFormation: String = ""

    @SerializedName("strHomeLineupGoalkeeper")
    var homeKeeper: String = ""

    @SerializedName("strHomeLineupDefense")
    var homeDefense: String = ""

    @SerializedName("strHomeLineupMidfield")
    var homeMidfield: String = ""

    @SerializedName("strHomeLineupForward")
    var homeForward: String = ""

    @SerializedName("strHomeLineupSubstitutes")
    var homeSubstitutes: String = ""

    @SerializedName("strAwayLineupGoalkeeper")
    var awayKeeper: String = ""

    @SerializedName("strAwayLineupDefense")
    var awayDefense: String = ""

    @SerializedName("strAwayLineupMidfield")
    var awayMidfield: String = ""

    @SerializedName("strAwayLineupForward")
    var awayForward: String = ""

    @SerializedName("strAwayLineupSubstitutes")
    var awaySubstitutes: String = ""
}