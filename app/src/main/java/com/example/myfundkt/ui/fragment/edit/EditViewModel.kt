package com.example.myfundkt.ui.fragment.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.utils.ktDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val _foudInfoEntity = MutableLiveData<FoudInfoEntity>()
    val foudInfoEntity: LiveData<FoudInfoEntity> = _foudInfoEntity

    fun find(code: String) {
//        _foudInfoEntity.value = repository.FindByCode(code)
        viewModelScope.launch(Dispatchers.IO) {

            _foudInfoEntity.postValue(ktDao.findByCode(code))
        }
    }

    fun update(foundInfoEntity: FoudInfoEntity) {
//        repository.Update(foundInfoEntity)
        viewModelScope.launch {

            ktDao.updateFundInfo(foundInfoEntity)
        }
    }


}


