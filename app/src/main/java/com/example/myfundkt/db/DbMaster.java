package com.example.myfundkt.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myfundkt.db.dao.FoudInfoDao;
import com.example.myfundkt.db.dao.SortDao;
import com.example.myfundkt.db.entity.FoudInfoEntity;
import com.example.myfundkt.db.entity.SortEntity;
import com.example.myfundkt.utils.Conteaxt;


@Database(entities = {FoudInfoEntity.class, SortEntity.class}, version = 1, exportSchema = false)
public abstract class DbMaster extends RoomDatabase {
    private static DbMaster INSTANCE;

    public static synchronized DbMaster getDbMaster() {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder (Conteaxt.getContext (), DbMaster.class, "my_found")
                    .build ();
        }
        return INSTANCE;
    }

    public abstract FoudInfoDao getFoudInfoDao();
    public abstract SortDao getSortDao();
}
