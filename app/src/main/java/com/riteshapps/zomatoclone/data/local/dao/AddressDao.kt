package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("SELECT * FROM addresses WHERE id = :addressId")
    fun getAddressById(addressId: Long): Flow<AddressEntity?>

    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC")
    fun getAddressesByUser(userId: Long): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    fun getDefaultAddress(userId: Long): Flow<AddressEntity?>

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultAddressSync(userId: Long): AddressEntity?

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultAddresses(userId: Long)

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :addressId")
    suspend fun setDefaultAddress(addressId: Long)

    @Transaction
    suspend fun setAsDefault(userId: Long, addressId: Long) {
        clearDefaultAddresses(userId)
        setDefaultAddress(addressId)
    }

    @Query("SELECT COUNT(*) FROM addresses WHERE userId = :userId")
    fun getAddressCount(userId: Long): Flow<Int>
}
