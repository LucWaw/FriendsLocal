package com.lucwaw.friendsLocal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(spot: PersonEntity): Int

    @Delete
    suspend fun deletePerson(spot: PersonEntity)

    @Query("SELECT * FROM personentity")
    fun getPersons(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM personentity WHERE id = :id")
    suspend fun getPersonById(id: Int): PersonEntity?
}