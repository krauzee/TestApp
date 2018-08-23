package com.test.testmaksat.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {UserDialogsListData.UserData.class}, version = 1)
    public abstract class DataBase extends RoomDatabase {
        public abstract UserDialogsListDataDAO userDialogsListDataDAO();
    }
