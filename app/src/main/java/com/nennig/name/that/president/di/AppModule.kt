package com.nennig.name.that.president.di

import android.content.Context
import com.nennig.name.that.president.data.repository.PresidentRepositoryImpl
import com.nennig.name.that.president.domain.repository.PresidentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePresidentRepository(
        @ApplicationContext context: Context
    ): PresidentRepository {
        return PresidentRepositoryImpl(context)
    }
} 