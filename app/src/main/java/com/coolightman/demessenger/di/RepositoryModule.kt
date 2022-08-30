package com.coolightman.demessenger.di

import com.coolightman.demessenger.data.repository.MessageRepositoryImpl
import com.coolightman.demessenger.data.repository.UserRepositoryImpl
import com.coolightman.demessenger.data.repository.UsersRepositoryImpl
import com.coolightman.demessenger.domain.repository.MessageRepository
import com.coolightman.demessenger.domain.repository.UserRepository
import com.coolightman.demessenger.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideUsersRepository(impl: UsersRepositoryImpl): UsersRepository
}