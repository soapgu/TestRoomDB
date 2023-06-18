package com.soapgu.testroomdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.soapgu.testroomdb.dao.AppDatabase;
import com.soapgu.testroomdb.model.Record;

import java.util.Date;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    TextView tv_count,tv_names;
    EditText txt_name;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControl();
    }

    private void initControl(){
        db = Room.databaseBuilder(this.getApplicationContext() , AppDatabase.class,"test-db")
                .build();
        tv_count = findViewById(R.id.tv_count);
        tv_names = findViewById(R.id.tv_names);
        txt_name = findViewById(R.id.txt_name);

        this.findViewById(R.id.btn_query).setOnClickListener( v -> {
            disposables.add( db.recordDao().loadAllRecords()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe( records -> {
                                tv_count.setText( String.valueOf( records.size() ) );
                                String names = records.stream().map(t->t.name).collect(Collectors.joining(","));
                                tv_names.setText( names );
                            },
                            throwable -> Logger.e(throwable,"load records error")) );
        } );

        this.findViewById(R.id.btn_add).setOnClickListener( v-> {
            Record record = new Record();
            record.name = txt_name.getText().toString();
            Date date = new Date();
            record.createTime = date.getTime() / 1000;
            disposables.add( db.recordDao().insertRecord( record )
                    .subscribeOn(Schedulers.io())
                    .subscribe( ()-> Logger.i("save record ok"),
                            throwable -> Logger.e( throwable, "save record error" )));
        } );
    }
}