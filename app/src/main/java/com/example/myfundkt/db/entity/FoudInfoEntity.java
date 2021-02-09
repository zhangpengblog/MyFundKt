package com.example.myfundkt.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mydb")
public class FoudInfoEntity {
    @PrimaryKey(autoGenerate = true)
    public int id  ;

    public String code; // 号码
    public double quantity;//持有份额
    public double cost; //成本

    public FoudInfoEntity(String code,double quantity,double cost){
        this.code=code;
        this.quantity=quantity;
        this.cost=cost;
    }


}