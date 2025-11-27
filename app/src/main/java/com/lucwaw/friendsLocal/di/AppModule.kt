package com.lucwaw.friendsLocal.di
import android.app.Application
import androidx.room.Room
import com.lucwaw.friendsLocal.data.PersonRepositoryImpl
import com.lucwaw.friendsLocal.data.PersonDatabase
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePersonDatabase(app: Application): PersonDatabase {
        return Room.databaseBuilder(
            app,
            PersonDatabase::class.java,
            "persons.db"
        ).build()
    }

    @Singleton
    @Provides
    fun providePersonRepository(db: PersonDatabase): PersonRepository {
        return PersonRepositoryImpl(db.dao)
    }
}