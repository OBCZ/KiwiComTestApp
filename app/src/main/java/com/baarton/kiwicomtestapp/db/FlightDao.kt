package com.baarton.kiwicomtestapp.db

import androidx.room.*

@Dao
interface FlightDao {
    //TODO cleanup
    @Query("SELECT * FROM flights")
    suspend fun getAll(): List<Flight>

/*    @Query("SELECT * FROM flights-of-the-day WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Flight>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Flight*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Flight)

    @Delete
    suspend fun delete(user: Flight)

    @Query("DELETE FROM flights")
    suspend fun nuke()

}