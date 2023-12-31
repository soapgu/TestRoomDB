package com.soapgu.testroomdb.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.soapgu.testroomdb.model.Record;

@Database(entities = {Record.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final int limit = 10;
    public abstract RecordDao recordDao();
}
