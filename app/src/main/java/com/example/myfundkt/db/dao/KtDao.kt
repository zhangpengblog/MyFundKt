package com.example.myfundkt.db.dao

import androidx.room.*
import com.example.myfundkt.db.entity.FoudInfoEntity

@Dao
interface KtDao {
    /*发生冲突时覆盖*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFundInfo(vararg foudInfoEntities: FoudInfoEntity?)

    @Query("SELECT code from mydb")
    suspend fun getCodes(): List<String>?

    @Query("SELECT * from mydb WHERE code = :s")
    suspend fun findByCode(s: String): FoudInfoEntity?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFundInfo(vararg foudInfoEntities: FoudInfoEntity?): Int

    @Delete
    suspend fun delete(vararg foudInfoEntities: FoudInfoEntity?)

    @Query("DELETE from mydb")
    suspend fun clear()

    @Query("DELETE FROM mydb WHERE id= :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from mydb")
    suspend fun findAll(): List<FoudInfoEntity?>?
}