package com.example.myfundkt.ui.fragment.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfundkt.db.DbRepository
import com.example.myfundkt.db.entity.FoudInfoEntity

class EditViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var repository: DbRepository = DbRepository()
    private val _foudInfoEntity = MutableLiveData<FoudInfoEntity>()
     val foudInfoEntity : LiveData<FoudInfoEntity> = _foudInfoEntity

    fun find(code:String){
        _foudInfoEntity.value = repository.FindByCode(code)
    }
    fun update(foundInfoEntity: FoudInfoEntity){
        repository.Update(foundInfoEntity)
    }


}


