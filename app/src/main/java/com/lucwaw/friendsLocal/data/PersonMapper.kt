package com.lucwaw.friendsLocal.data

import com.lucwaw.friendsLocal.domain.model.Person

fun PersonEntity.toPerson(): Person {
    return Person(
        lat = lat,
        lng = lng,
        id = id,
        firstName = firstName,
        lastName = lastName,
        address = address
    )
}

fun Person.toPersonEntity(): PersonEntity {
    return PersonEntity(
        lat = lat,
        lng = lng,
        id = id,
        firstName = firstName,
        lastName = lastName,
        address = address
    )
}