package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = { Predmet.class, Student.class, PredmetStudentJoin.class, Termin.class,
                        TerminStudentJoin.class}, version = 8)
public abstract class UcitelDatabase extends RoomDatabase {

    private static final String DB_NAME = "ucitelDatabase.db";
    private static volatile UcitelDatabase instance;

    public static synchronized UcitelDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UcitelDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                UcitelDatabase.class,
                DB_NAME).fallbackToDestructiveMigration().build();
    }

    public abstract PredmetDao getPredmetDao();

    public abstract StudentDao getStudentDao();

    public abstract PredmetStudentJoinDao getPredmetStudentJoinDao();

    public abstract TerminDao getTerminDao();

    public abstract TerminStudentJoinDao getTerminStudentJoinDao();

}
