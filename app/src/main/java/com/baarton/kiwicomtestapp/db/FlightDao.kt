package com.baarton.kiwicomtestapp.db

import androidx.room.*
import com.baarton.kiwicomtestapp.data.Flight

@Dao
interface FlightDao {

    @Query("SELECT * FROM flights")
    suspend fun getAll(): List<Flight>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Flight)

    @Query("DELETE FROM flights")
    suspend fun nuke()

}