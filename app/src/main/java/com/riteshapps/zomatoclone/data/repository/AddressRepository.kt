package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.AddressDao
import com.riteshapps.zomatoclone.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepository @Inject constructor(
    private val addressDao: AddressDao
) {
    fun getAddressesByUser(userId: Long): Flow<List<AddressEntity>> {
        return addressDao.getAddressesByUser(userId)
    }

    fun getAddressById(addressId: Long): Flow<AddressEntity?> {
        return addressDao.getAddressById(addressId)
    }

    fun getDefaultAddress(userId: Long): Flow<AddressEntity?> {
        return addressDao.getDefaultAddress(userId)
    }

    suspend fun getDefaultAddressSync(userId: Long): AddressEntity? {
        return addressDao.getDefaultAddressSync(userId)
    }

    fun getAddressCount(userId: Long): Flow<Int> {
        return addressDao.getAddressCount(userId)
    }

    suspend fun addAddress(address: AddressEntity): Long {
        return addressDao.insertAddress(address)
    }

    suspend fun updateAddress(address: AddressEntity) {
        addressDao.updateAddress(address)
    }

    suspend fun deleteAddress(address: AddressEntity) {
        addressDao.deleteAddress(address)
    }

    suspend fun setAsDefault(userId: Long, addressId: Long) {
        addressDao.setAsDefault(userId, addressId)
    }
}
