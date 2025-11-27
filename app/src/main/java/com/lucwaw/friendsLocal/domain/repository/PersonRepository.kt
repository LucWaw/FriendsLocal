package com.lucwaw.friendsLocal.domain.repository

import com.lucwaw.friendsLocal.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    suspend fun insertPerson(person: Person): Long

    suspend fun deletePerson(person: Person)

    fun getPersons(): Flow<List<Person>>

    suspend fun getPersonById(id: Long): Person?
}