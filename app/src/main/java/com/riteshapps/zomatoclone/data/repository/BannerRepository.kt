package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.BannerDao
import com.riteshapps.zomatoclone.data.local.entity.BannerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BannerRepository @Inject constructor(
    private val bannerDao: BannerDao
) {
    fun getAllBanners(): Flow<List<BannerEntity>> {
        return bannerDao.getAllBanners()
    }

    fun getBannerById(bannerId: Long): Flow<BannerEntity?> {
        return bannerDao.getBannerById(bannerId)
    }

    suspend fun insertBanner(banner: BannerEntity): Long {
        return bannerDao.insertBanner(banner)
    }

    suspend fun insertBanners(banners: List<BannerEntity>) {
        bannerDao.insertBanners(banners)
    }

    suspend fun updateBanner(banner: BannerEntity) {
        bannerDao.updateBanner(banner)
    }

    suspend fun deleteBanner(banner: BannerEntity) {
        bannerDao.deleteBanner(banner)
    }

    suspend fun getBannerCount(): Int {
        return bannerDao.getBannerCount()
    }
}
