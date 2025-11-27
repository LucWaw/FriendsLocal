package com.lucwaw.friendsLocal.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PersonEntity::class],
    version = 1
)
abstract class PersonDatabase: RoomDatabase() {

    abstract val dao: PersonDao
}