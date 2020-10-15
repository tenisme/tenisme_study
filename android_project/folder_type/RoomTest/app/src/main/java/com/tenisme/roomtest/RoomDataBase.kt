package com.tenisme.roomtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoEntity::class], version = 1, exportSchema = false)
abstract class MemoDatabase: RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object { // DB 인스턴스를 싱글톤으로 사용하기 위해 static 선언과 같은 companion object 를 생성

        private var INSTANCE: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase? {
            if (INSTANCE == null) { // 생성되어있는 DB 인스턴스가 없으면 새롭게 작성
                synchronized(MemoDatabase::class) { // 여러 스레드가 접근하지 못하도록 synchronized 로 설정
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, // Room.databaseBuilder 로 DB 인스턴스를 생성
                        MemoDatabase::class.java, "memo"
                    ).fallbackToDestructiveMigration() // fallbackToDestructiveMigration : 데이터베이스가 갱신될 때 기존의 테이블을 버리고 새로 사용하도록 설정
                        .build()
                }
            }
            return INSTANCE // DB 인스턴스를 리턴
        }
    }
}