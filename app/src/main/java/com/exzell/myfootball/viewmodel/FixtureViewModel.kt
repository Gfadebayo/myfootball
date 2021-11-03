package com.exzell.myfootball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exzell.myfootball.io.Repository
import com.exzell.myfootball.model.Fixture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FixtureViewModel : ViewModel() {

    @Inject
    lateinit var mRepo: Repository

    fun getFixtureDetails(id: Int, onComplete: (Fixture?) -> Unit){
        viewModelScope.launch {
            onComplete.invoke(withContext(Dispatchers.IO){
                mRepo.getFixture(id)
            })
        }
    }
}