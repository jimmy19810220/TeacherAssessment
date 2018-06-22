package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Query("SELECT * FROM student")
    List<Student> getAllStudents();

    @Query("SELECT * FROM student WHERE student.id IN (SELECT studentId FROM predmet_student_join AS ps " +
            "WHERE ps.predmetId = :idPredmet)")
    List<Student> getStudentiPredmetu(Integer idPredmet);

    @Query("SELECT * FROM student AS s WHERE s.id IN (SELECT studentId FROM predmet_student_join AS ps " +
            "WHERE ps.predmetId = :idPredmet) " +
            "AND NOT EXISTS(SELECT studentId FROM termin_student_join AS ts WHERE ts.terminId = :idTermin " +
                            "AND ts.studentId = s.id)")
    List<Student> getStudentiPredmetuNeprihlaseniNaTermin(Integer idPredmet, Integer idTermin);

    @Query("SELECT * FROM student WHERE id=:idStudent")
    Student getStudentById(Integer idStudent);

    @Insert
    long insert(Student student);

    @Insert
    void insert(Student... students);

    @Update
    void update(Student... students);

    @Delete
    void delete(Student... students);
}
