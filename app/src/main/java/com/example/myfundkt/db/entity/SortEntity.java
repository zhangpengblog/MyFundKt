package com.example.myfundkt.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mysort")
public class SortEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;


    public boolean CYE;
    public boolean CYSY;
    public boolean CYSYL;
    public boolean ZDF;
    public boolean GSSY;

    public SortEntity(boolean CYE) {
        this.CYE = CYE;
    }

}
