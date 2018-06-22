package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface PredmetStudentJoinDao {

    @Insert
    void insert(PredmetStudentJoin predmetStudentJoin);

    @Query("SELECT * FROM predmet INNER JOIN predmet_student_join ON " +
            "predmet.id=predmet_student_join.predmetId " +
            "WHERE predmet_student_join.studentId=:studentId")
    List<Predmet> getPredmetyStudenta(final Integer studentId);

    @Query("SELECT * FROM student INNER JOIN predmet_student_join ON " +
            "student.id=predmet_student_join.studentId WHERE " +
            "predmet_student_join.predmetId=:predmetId")
    List<Student> getStudentiPredmetu(final Integer predmetId);

    @Query("SELECT * FROM predmet_student_join")
    List<PredmetStudentJoin> getAllPredmetyStudentov();


}
