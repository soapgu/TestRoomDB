package com.soapgu.testroomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.soapgu.testroomdb.model.Record;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecordDao {
    @Query("SELECT * from records LIMIT :limit")
    public Single<List<Record>> loadAllRecords( int limit );

    @Query("SELECT EXISTS(SELECT 1 FROM records LIMIT 1)")
    public Single<Boolean> loadRecordCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertRecord(Record record);

    @Update
    public Completable updateRecord(Record record);


    @Query("SELECT * FROM records WHERE id = :id")
    public Single<Record> loadRecordById(int id);

    @Delete
    public Completable deleteRecords(List<Record> records);
}
