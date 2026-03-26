package com.riteshapps.zomatoclone.data.dataModule

import android.content.Context
import androidx.room.Room
import com.riteshapps.zomatoclone.data.local.AppDatabase
import com.riteshapps.zomatoclone.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideRestaurantDao(database: AppDatabase): RestaurantDao = database.restaurantDao()

    @Provides
    fun provideMenuItemDao(database: AppDatabase): MenuItemDao = database.menuItemDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao = database.cartDao()

    @Provides
    fun provideWishlistDao(database: AppDatabase): WishlistDao = database.wishlistDao()

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao = database.orderDao()

    @Provides
    fun provideReviewDao(database: AppDatabase): ReviewDao = database.reviewDao()

    @Provides
    fun provideAddressDao(database: AppDatabase): AddressDao = database.addressDao()

    @Provides
    fun provideBannerDao(database: AppDatabase): BannerDao = database.bannerDao()
}