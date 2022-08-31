package com.coolightman.demessenger.di

import com.coolightman.demessenger.data.database.DB_URL
import com.coolightman.demessenger.data.database.MESSAGES_REF
import com.coolightman.demessenger.data.database.USERS_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance(DB_URL)
    }

    @Provides
    @Singleton
    @Named(MESSAGES_REF)
    fun provideReferenceMessages(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference(MESSAGES_REF)
    }

    @Provides
    @Singleton
    @Named(USERS_REF)
    fun provideReferenceUsers(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference(USERS_REF)
    }
}