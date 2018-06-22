package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TerminStudentJoinDao {

    @Insert
    void insert(TerminStudentJoin terminStudentJoin);

    @Query("SELECT * FROM termin INNER JOIN termin_student_join ON " +
            "termin.id=termin_student_join.terminId " +
            "WHERE termin_student_join.studentId=:studentId")
    List<Termin> getTerminyStudenta(final Integer studentId);

    @Query("SELECT * FROM student INNER JOIN termin_student_join ON " +
            "student.id=termin_student_join.studentId WHERE " +
            "termin_student_join.terminId=:terminId")
    List<Student> getStudentiTerminu(final Integer terminId);

    @Query("SELECT * FROM termin_student_join")
    List<TerminStudentJoin> getAllTerminyStudentov();


}
