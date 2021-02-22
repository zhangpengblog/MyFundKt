package com.example.myfundkt.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfundkt.db.dao.KtDao
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.utils.Conteaxt



@Database(entities = [FoudInfoEntity::class],version = 1,exportSchema = false)
abstract class KtDatabase :RoomDatabase(){
    companion object{
        var dataBase: KtDatabase =
            Room.databaseBuilder(Conteaxt.getContext(),KtDatabase::class.java,"db_kt")
                .fallbackToDestructiveMigration()
                .build()
    }
    abstract fun getDao(): KtDao
}

