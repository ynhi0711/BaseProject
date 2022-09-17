package com.nhinguyen.sample.di

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.nhinguyen.sample.BuildConfig
import com.nhinguyen.sample.core.api.RetrofitService
import com.nhinguyen.sample.core.data.AppDatabase
import com.nhinguyen.sample.core.repository.local.DataDao
import com.nhinguyen.sample.core.repository.local.ILocalDataSource
import com.nhinguyen.sample.core.repository.local.LocalDataSource
import com.nhinguyen.sample.core.repository.remote.IRemoteDataSource
import com.nhinguyen.sample.core.repository.remote.RemoteDataSource
import com.nhinguyen.sample.utils.Constants
import com.nhinguyen.sample.utils.SPUtils
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Suppress("unused")
@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    internal fun provideSPUtils(app: Application, moshi: Moshi): SPUtils {
        return SPUtils(app, moshi)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        defaultHeadersInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(defaultHeadersInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideDefaultHeadersInterceptor(
        app: Application
    ): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            //Add header
            requestBuilder.addHeader("Authorization", "Bearer ${Constants.ACCESS_TOKEN}")
            it.proceed(requestBuilder.build())
        }
    }

    @Singleton
    @Provides
    internal fun provideService(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application, moshi: Moshi): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, "addons.db")
            .fallbackToDestructiveMigration()
            .build()
            .apply { AppDatabase.moshi = moshi }
    }

    @Singleton
    @Provides
    fun provideGameDao(appDatabase: AppDatabase) = appDatabase.gameDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofitService: RetrofitService): IRemoteDataSource =
        RemoteDataSource(retrofitService)

    @Singleton
    @Provides
    fun provideLocalDataSource(dataDao: DataDao): ILocalDataSource =
        LocalDataSource(dataDao)
}
