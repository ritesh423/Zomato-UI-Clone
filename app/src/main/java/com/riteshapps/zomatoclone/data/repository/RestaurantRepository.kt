package com.riteshapps.zomatoclone.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.riteshapps.zomatoclone.data.local.dao.RestaurantDao
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val restaurantDao: RestaurantDao
) {
    fun getAllRestaurants(): Flow<List<RestaurantEntity>> {
        return restaurantDao.getAllRestaurants()
    }

    fun getAllOpenRestaurants(): Flow<List<RestaurantEntity>> {
        return restaurantDao.getAllOpenRestaurants()
    }

    fun getRestaurantById(restaurantId: Long): Flow<RestaurantEntity?> {
        return restaurantDao.getRestaurantById(restaurantId)
    }

    suspend fun getRestaurantByIdSync(restaurantId: Long): RestaurantEntity? {
        return restaurantDao.getRestaurantByIdSync(restaurantId)
    }

    fun getRestaurantsByCategory(categoryId: Long): Flow<List<RestaurantEntity>> {
        return restaurantDao.getRestaurantsByCategory(categoryId)
    }

    fun searchRestaurants(query: String): Flow<List<RestaurantEntity>> {
        return restaurantDao.searchRestaurants(query)
    }

    fun getVegOnlyRestaurants(): Flow<List<RestaurantEntity>> {
        return restaurantDao.getVegOnlyRestaurants()
    }

    fun getRestaurantsByMinRating(minRating: Float): Flow<List<RestaurantEntity>> {
        return restaurantDao.getRestaurantsByMinRating(minRating)
    }

    fun getTopRatedRestaurants(limit: Int = 10): Flow<List<RestaurantEntity>> {
        return restaurantDao.getTopRatedRestaurants(limit)
    }

    fun getRestaurantsPaged(): Flow<PagingData<RestaurantEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { restaurantDao.getRestaurantsPaged() }
        ).flow
    }

    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long {
        return restaurantDao.insertRestaurant(restaurant)
    }

    suspend fun updateRestaurant(restaurant: RestaurantEntity) {
        restaurantDao.updateRestaurant(restaurant)
    }

    suspend fun deleteRestaurant(restaurant: RestaurantEntity) {
        restaurantDao.deleteRestaurant(restaurant)
    }

    suspend fun getRestaurantCount(): Int {
        return restaurantDao.getRestaurantCount()
    }
}
