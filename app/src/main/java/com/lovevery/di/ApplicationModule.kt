package com.lovevery.di

import com.lovevery.data.remote.MessagesAPIService
import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.usecases.CreateMessageUseCase
import com.lovevery.domain.usecases.CreateMessageUseCaseImpl
import com.lovevery.domain.usecases.GetAllMessagesUseCase
import com.lovevery.domain.usecases.GetAllMessagesUseCaseImpl
import com.lovevery.domain.usecases.GetMessagesByUserUseCase
import com.lovevery.domain.usecases.GetMessagesByUserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/***
 * Note: In more advanced versions of the app we would need to refactor
 * this to provide specific components or have a more advance hierarchy
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =

        Retrofit.Builder()
            .baseUrl("https://abraxvasbh.execute-api.us-east-2.amazonaws.com") // TODO Move to build config
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesGetAllMessagesUseCase(
        messagesRepository: MessagesRepository
    ): GetAllMessagesUseCase =
        GetAllMessagesUseCaseImpl(messagesRepository)

    @Singleton
    @Provides
    fun providesGetMessagesByUserUseCase(
        messagesRepository: MessagesRepository
    ): GetMessagesByUserUseCase=
        GetMessagesByUserUseCaseImpl(messagesRepository)

    @Singleton
    @Provides
    fun providesCreateMessagesUseCase(
        messagesRepository: MessagesRepository
    ): CreateMessageUseCase =
        CreateMessageUseCaseImpl(messagesRepository)

    @Singleton
    @Provides
    fun provideMessageRepository(apiService: MessagesAPIService): MessagesRepository =
        MessagesRepository(apiService)

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MessagesAPIService =
        retrofit.create(MessagesAPIService::class.java)

    @Provides
    @Singleton
    fun provideTimberTree(): Timber.Tree {
        return Timber.DebugTree()
    }

}