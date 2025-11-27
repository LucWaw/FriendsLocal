package com.lucwaw.friendsLocal.data

import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PersonRepositoryImpl(
    private val dao: PersonDao
): PersonRepository {

    override suspend fun insertPerson(spot: Person) {
        dao.insertPerson(spot.toPersonEntity())
    }

    override suspend fun deletePerson(spot: Person) {
        dao.deletePerson(spot.toPersonEntity())
    }

    override fun getPersons(): Flow<List<Person>> {
        return dao.getPersons().map { spots ->
            spots.map { it.toPerson() }
        }
    }
}