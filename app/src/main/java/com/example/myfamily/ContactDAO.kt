package com.example.myfamily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDAO {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(contactModel: ContactModel)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(contactModelList: List<ContactModel>)


    @Query("SELECT * FROM ContactModel")
    suspend fun getAllContacts():List<ContactModel>
}