package com.lucwaw.friendsLocal.data

import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PersonRepositoryImpl(
    private val dao: PersonDao
): PersonRepository {

    override suspend fun insertPerson(person: Person): Long {
        return dao.insertPerson(person.toPersonEntity())
    }

    override suspend fun deletePerson(person: Person) {
        dao.deletePerson(person.toPersonEntity())
    }

    override fun getPersons(): Flow<List<Person>> {
        return dao.getPersons().map { spots ->
            spots.map { it.toPerson() }
        }
    }

    override suspend fun getPersonById(id: Long): Person? {
        return dao.getPersonById(id)?.toPerson()
    }
}