package com.kwemrj.pillreminder.di

import android.content.Context
import androidx.room.Room
import com.kwemrj.pillreminder.data.local.DrugDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDrugDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        DrugDatabase::class.java,
        "pill_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDrugDao(db: DrugDatabase) = db.drugDao()

}