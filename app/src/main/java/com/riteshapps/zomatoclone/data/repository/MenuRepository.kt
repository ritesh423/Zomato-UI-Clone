package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.MenuItemDao
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepository @Inject constructor(
    private val menuItemDao: MenuItemDao
) {
    fun getMenuItemsByRestaurant(restaurantId: Long): Flow<List<MenuItemEntity>> {
        return menuItemDao.getMenuItemsByRestaurant(restaurantId)
    }

    fun getMenuItemById(menuItemId: Long): Flow<MenuItemEntity?> {
        return menuItemDao.getMenuItemById(menuItemId)
    }

    suspend fun getMenuItemByIdSync(menuItemId: Long): MenuItemEntity? {
        return menuItemDao.getMenuItemByIdSync(menuItemId)
    }

    fun getMenuItemsByCategory(restaurantId: Long, category: String): Flow<List<MenuItemEntity>> {
        return menuItemDao.getMenuItemsByCategory(restaurantId, category)
    }

    fun getCategoriesForRestaurant(restaurantId: Long): Flow<List<String>> {
        return menuItemDao.getCategoriesForRestaurant(restaurantId)
    }

    fun getVegMenuItems(restaurantId: Long): Flow<List<MenuItemEntity>> {
        return menuItemDao.getVegMenuItems(restaurantId)
    }

    fun getBestSellers(restaurantId: Long): Flow<List<MenuItemEntity>> {
        return menuItemDao.getBestSellers(restaurantId)
    }

    fun searchMenuItems(query: String): Flow<List<MenuItemEntity>> {
        return menuItemDao.searchMenuItems(query)
    }

    fun getAvailableMenuItems(restaurantId: Long): Flow<List<MenuItemEntity>> {
        return menuItemDao.getAvailableMenuItems(restaurantId)
    }

    suspend fun insertMenuItem(menuItem: MenuItemEntity): Long {
        return menuItemDao.insertMenuItem(menuItem)
    }

    suspend fun insertMenuItems(menuItems: List<MenuItemEntity>) {
        menuItemDao.insertMenuItems(menuItems)
    }

    suspend fun updateMenuItem(menuItem: MenuItemEntity) {
        menuItemDao.updateMenuItem(menuItem)
    }

    suspend fun deleteMenuItem(menuItem: MenuItemEntity) {
        menuItemDao.deleteMenuItem(menuItem)
    }
}
