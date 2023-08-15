package com.dyakov.todolist.di

import com.dyakov.todolist.data.repositories.TodoItemRepositoryImpl
import com.dyakov.todolist.domain.repositories.TodoItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppBindsModule {

    @Binds
    fun bindTodoItemRepositoryImpl(repositoryImpl: TodoItemRepositoryImpl): TodoItemRepository
}