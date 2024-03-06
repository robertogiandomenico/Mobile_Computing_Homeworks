package com.example.hw1

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "proPicUri") val proPicUri: String?
)

@Entity(primaryKeys = ["noteTitle", "noteBody"])
data class Note(
    @ColumnInfo(name = "noteTitle") val title: String,
    @ColumnInfo(name = "noteBody") val body: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :name LIMIT 1")
    fun findByName(name: String): User

    @Query("SELECT proPicUri FROM user WHERE uid LIKE :userID LIMIT 1")
    fun getProPicUri(userID: Int): String

    @Query("UPDATE user SET proPicUri = :newUri WHERE uid = :userID")
    fun setProPicUri(userID: Int, newUri: String)

    @Query("UPDATE user SET username = :newUsername WHERE uid = :userID")
    fun setUsername(userID: Int, newUsername: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE noteTitle LIKE :title LIMIT 1")
    fun findByTitle(title: String): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg note: Note)

    @Delete
    fun delete(note: Note)

}

@Database(entities = [User::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.
                    databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "NoteDatabase"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance

                // Insert default if the database is empty
                if (instance.userDao().getAll().isEmpty()) {
                    instance.userDao().insertAll(User(0, "Nick", ""))
                }

                instance
            }
        }
    }
}