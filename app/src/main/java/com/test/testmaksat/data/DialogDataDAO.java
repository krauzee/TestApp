package com.test.testmaksat.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DialogDataDAO {

    @Insert
    void insert(DialogData data);

    @Query("DELETE FROM DialogData")
    void deleteAll();

    @Query("SELECT * FROM DialogData")
    List<DialogData> getAll();

    @Query("SELECT * FROM DialogData WHERE userId = 'ОН ДОЛЖЕН РАВНЯТЬСЯ АЙДИШНИКУ ИТЕМА, ПО КОТОРОМУ ТАПНУЛИ, НО КАК ЭТО СЮДА ВКОРЯЧИТЬ??'")
    void getDialogById(DialogData userId);



}
