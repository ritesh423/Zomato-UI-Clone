package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.ReviewDao
import com.riteshapps.zomatoclone.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDao: ReviewDao
) {
    fun getReviewsByRestaurant(restaurantId: Long): Flow<List<ReviewEntity>> {
        return reviewDao.getReviewsByRestaurant(restaurantId)
    }

    fun getReviewsByUser(userId: Long): Flow<List<ReviewEntity>> {
        return reviewDao.getReviewsByUser(userId)
    }

    fun getReviewById(reviewId: Long): Flow<ReviewEntity?> {
        return reviewDao.getReviewById(reviewId)
    }

    fun getAverageRating(restaurantId: Long): Flow<Float?> {
        return reviewDao.getAverageRating(restaurantId)
    }

    fun getReviewCount(restaurantId: Long): Flow<Int> {
        return reviewDao.getReviewCount(restaurantId)
    }

    fun getRecentReviews(restaurantId: Long, limit: Int = 5): Flow<List<ReviewEntity>> {
        return reviewDao.getRecentReviews(restaurantId, limit)
    }

    suspend fun addReview(review: ReviewEntity): Long {
        return reviewDao.insertReview(review)
    }

    suspend fun updateReview(review: ReviewEntity) {
        reviewDao.updateReview(review)
    }

    suspend fun deleteReview(review: ReviewEntity) {
        reviewDao.deleteReview(review)
    }
}
