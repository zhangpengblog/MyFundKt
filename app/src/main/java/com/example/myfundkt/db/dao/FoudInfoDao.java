package com.example.myfundkt.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfundkt.db.entity.FoudInfoEntity;

import java.util.List;

@Dao
public interface FoudInfoDao {
    /*发生冲突时覆盖*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoudInfo(FoudInfoEntity...foudInfoEntities);

    @Query("SELECT code from mydb")
    List<String> getCodes();

    @Query ("SELECT * from mydb WHERE code = :s")
    FoudInfoEntity FindByCode(String[] s);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int UpdateFoudInfo(FoudInfoEntity...foudInfoEntities);

    @Delete
    void delete(FoudInfoEntity ...foudInfoEntities);

    @Query ("DELETE from mydb")
    void clear();

    @Query ("DELETE FROM mydb WHERE id= :id")
    void deleteById(int id);

    @Query ("SELECT * from mydb")
    List<FoudInfoEntity> findAll();
}