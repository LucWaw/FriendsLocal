package com.lucwaw.friendsLocal.domain.repository

import com.lucwaw.friendsLocal.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    suspend fun insertPerson(spot: Person)

    suspend fun deletePerson(spot: Person)

    fun getPersons(): Flow<List<Person>>
}