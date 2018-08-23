package com.test.testmaksat.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface UserDialogsListDataDAO {

    @Insert
    void insertAll(List<UserDialogsListData.UserData> data);

    @Query("DELETE FROM UserData")
    void deleteAll();

    @Update
    void updateAll(UserDialogsListData userDialogsListData);


    @Query("SELECT * FROM userdata")
    List<UserDialogsListData.UserData> getAll();

}
