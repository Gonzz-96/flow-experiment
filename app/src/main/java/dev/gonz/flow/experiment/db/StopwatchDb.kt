package dev.gonz.flow.experiment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StopwatchState::class], version = 1)
abstract class StopwatchDb : RoomDatabase() {
    abstract fun dao(): StopwatchStateDao

    companion object {
        @Volatile
        private var DB: StopwatchDb? = null

        fun getDb(context: Context): StopwatchDb {
            synchronized(this) {
                if (DB == null) {
                    DB = Room.databaseBuilder(
                        context,
                        StopwatchDb::class.java,
                        "stopwatch.db"
                    ).build()
                }

                return DB!!
            }
        }
    }
}
