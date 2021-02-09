package com.example.myfundkt.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myfundkt.db.entity.SortEntity;

@Dao
public interface SortDao {
    /*发生冲突时覆盖*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSort(SortEntity... sortEntities);
    @Query("DELETE from mysort")
    void clear();
}