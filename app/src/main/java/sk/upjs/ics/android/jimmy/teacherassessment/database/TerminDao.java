package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TerminDao {

    @Query("SELECT * FROM termin")
    List<Termin> getAllTerminy();

//    @Query("SELECT * FROM student WHERE student.id IN (SELECT studentId FROM predmet_student_join " +
//            "WHERE predmet_student_join.predmetId = :idPredmet)")
//    List<Termin> getStudentiPredmetu(Integer idPredmet);

    @Query("SELECT * FROM termin WHERE id=:idTermin")
    Termin getTerminById(Integer idTermin);

    @Query("SELECT * FROM termin WHERE idPredmet=:idPredmet")
    List<Termin> getTerminyByPredmet(Integer idPredmet);

    @Insert
    long insert(Termin termin);

    @Insert
    void insert(Termin... termin);

    @Update
    void update(Termin... termin);

    @Delete
    int delete(Termin... termin);

}
