package com.test.testmaksat.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomDatabase;

import java.util.List;

public class UserDialogsListData {
    private List<UserData> list;

    public UserDialogsListData(List<UserData> list) {
        this.list = list;
    }

    public List<UserData> getList() {
        return list;
    }


/*

    @Database(entities = {UserDialogsListData.UserData.class}, version = 1)
    public abstract class DataBase extends RoomDatabase {
        public abstract com.test.testmaksat.data.UserDialogsListDataDAO userDialogsListDataDAO();
    }
*/

    @Entity(tableName = "UserData")
    public static class UserData {
        private String userName;
        private @PrimaryKey int userId;
        private String lastMessage;

        public UserData(String userName, int userId, String lastMessage) {
            this.userName = userName;
            this.userId = userId;
            this.lastMessage = lastMessage;
        }

        public UserData() {
            this.userName = userName;
            this.userId = userId;
            this.lastMessage = lastMessage;
        }

        public String getUserName() {
            return userName;
        }

        public int getUserId() {
            return userId;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}
