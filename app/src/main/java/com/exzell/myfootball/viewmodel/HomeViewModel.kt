package com.exzell.myfootball.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.exzell.myfootball.io.Repository
import com.exzell.myfootball.model.Fixture
import com.exzell.myfootball.model.League
import com.exzell.myfootball.model.Standing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var mRepo: Repository

    var mCurrentSeason: String = ""

    var mSelectedLeague: Int = 0

    private val mLeagues: MutableList<League> = mutableListOf()

    private val mStanding: MutableList<Standing> = mutableListOf()

    private val mResults: MutableList<Fixture> = mutableListOf()

    private val mFixtures: MutableList<Fixture>  = mutableListOf()


    fun clearSavedContent(){
        mStanding.clear()
        mResults.clear()
        mFixtures.clear()
    }

    fun clearLeagueList(){
        mLeagues.clear()
    }

    fun getLeagues(onComplete: (Boolean, List<League>) -> Unit){
        viewModelScope.launch {

            if(mLeagues.isEmpty()) withContext(Dispatchers.IO){
                mRepo.leagueList
            }.apply {

                onComplete.invoke(true, this)
                mLeagues.addAll(this)
            }
            else {
                onComplete.invoke(true, mLeagues)
            }
        }
    }

    fun getLastResults(id: Int, onComplete: (Boolean, List<Fixture>) -> Unit){
        viewModelScope.launch {

            if(mResults.isEmpty()) withContext(Dispatchers.IO){
                mRepo.getLastResults(id)
            }.apply {

                val currentWeek = maxOf { it.week }
                val filtered = filter { it.week == currentWeek }

                mResults.addAll(filtered)
                onComplete.invoke(true, filtered)
            }
            else onComplete.invoke(true, mResults)
        }
    }

    fun getFixtures(id: Int, week: Int, season: String, onComplete: (Boolean, List<Fixture>) -> Unit){
        viewModelScope.launch {

            if(mFixtures.isEmpty()) withContext(Dispatchers.IO){
                mRepo.getFixtures(id, week, season)
            }.apply {
                val currentWeek = maxOf { it.week }
                val filtered = filter { it.week == currentWeek }

                mFixtures.addAll(filtered)
                onComplete.invoke(true, filtered)
            }
            else onComplete.invoke(true, mFixtures)
        }
    }

    fun getTopTeams(id: Int, onComplete: (Boolean, List<Standing>) -> Unit){
        viewModelScope.launch {

            if(mStanding.isEmpty()) withContext(Dispatchers.IO){
                mRepo.getStandingList(id, mRepo.getCurrentSeason(id).apply { mCurrentSeason = this })
            }.apply {

                mStanding.addAll(this)
                onComplete.invoke(true, this)
            }
            else onComplete.invoke(true, mStanding)
        }
    }

    fun setLogoFromStanding(fixtures: List<Fixture>?){

        if(fixtures == null){

            setLogoFromStanding(mResults)
            setLogoFromStanding(mFixtures)
        }else {

            fixtures.forEach { fix ->
                mStanding.find { it.teamId == fix.homeTeamId }?.let {
                    fix.homeLogoDir = it.logoDir
                }

                mStanding.find { it.teamId == fix.awayTeamId }?.let {
                    fix.awayLogoDir = it.logoDir
                }
            }
        }
    }
}