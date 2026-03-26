package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    fun getReviewById(reviewId: Long): Flow<ReviewEntity?>

    @Query("SELECT * FROM reviews WHERE restaurantId = :restaurantId ORDER BY timestamp DESC")
    fun getReviewsByRestaurant(restaurantId: Long): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE userId = :userId ORDER BY timestamp DESC")
    fun getReviewsByUser(userId: Long): Flow<List<ReviewEntity>>

    @Query("SELECT AVG(rating) FROM reviews WHERE restaurantId = :restaurantId")
    fun getAverageRating(restaurantId: Long): Flow<Float?>

    @Query("SELECT COUNT(*) FROM reviews WHERE restaurantId = :restaurantId")
    fun getReviewCount(restaurantId: Long): Flow<Int>

    @Query("SELECT * FROM reviews WHERE restaurantId = :restaurantId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentReviews(restaurantId: Long, limit: Int): Flow<List<ReviewEntity>>

    @Query("SELECT COUNT(*) FROM reviews WHERE restaurantId = :restaurantId AND rating >= :minRating")
    fun getReviewCountByMinRating(restaurantId: Long, minRating: Float): Flow<Int>
}
