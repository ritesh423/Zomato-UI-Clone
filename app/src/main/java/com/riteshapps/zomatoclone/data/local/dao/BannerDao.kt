package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.BannerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: BannerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanners(banners: List<BannerEntity>)

    @Update
    suspend fun updateBanner(banner: BannerEntity)

    @Delete
    suspend fun deleteBanner(banner: BannerEntity)

    @Query("SELECT * FROM banners WHERE id = :bannerId")
    fun getBannerById(bannerId: Long): Flow<BannerEntity?>

    @Query("SELECT * FROM banners ORDER BY id")
    fun getAllBanners(): Flow<List<BannerEntity>>

    @Query("SELECT COUNT(*) FROM banners")
    suspend fun getBannerCount(): Int
}
