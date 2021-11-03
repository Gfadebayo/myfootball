package com.exzell.myfootball.io

import com.exzell.myfootball.model.Fixture
import com.exzell.myfootball.model.League
import com.exzell.myfootball.model.Standing
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * The api seems to put the array inside a json object
 * which gives Gson trouble when trying to create the list
 * instead we use this class to tell Gson to start with the object
 * then put the list into it
 */
class LeagueCollection{
    @SerializedName("leagues")
    val leagues: List<League>? = null
        get() = field!!.filter {
            it.strSport == "Soccer"
        }
}

/**
 * Check [LeagueCollection]
 */
class StandingCollection{
    @SerializedName("table")
    val standings: List<Standing>? = null
}

class SeasonCollection{
    @SerializedName("seasons")
    val seasons: List<Season>? = null

    fun getCurrentSeason(): String{
        return Collections.max(seasons) { s1, s2 ->
            s1.strSeason.split("-".toRegex())[1].compareTo(s2.strSeason.split("-".toRegex())[1])
        }.strSeason
    }

    data class Season(val strSeason: String)
}

class FixtureCollection{
    @SerializedName("events")
    val fixtures: List<Fixture>? = null
}