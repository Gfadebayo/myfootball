package com.exzell.myfootball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exzell.myfootball.io.Repository
import com.exzell.myfootball.model.Standing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StandingViewModel : ViewModel() {

    @Inject
    lateinit var mRepo: Repository

    fun getStanding(leagueId: Int, season: String, onComplete: (List<Standing>) -> Unit){
        viewModelScope.launch {
            onComplete.invoke(withContext(Dispatchers.IO) {
                mRepo.getStandingList(leagueId, season)
            })
        }
    }
}