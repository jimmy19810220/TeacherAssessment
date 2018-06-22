package sk.upjs.ics.android.jimmy.teacherassessment.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;
import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "termin_student_join",
        primaryKeys = { "terminId", "studentId" },
        foreignKeys = {
                @ForeignKey(entity = Termin.class,
                        parentColumns = "id",
                        childColumns = "terminId",
                        onDelete = CASCADE),
                @ForeignKey(entity = Student.class,
                        parentColumns = "id",
                        childColumns = "studentId")
        })
public class TerminStudentJoin {

    @NonNull
    public final Integer terminId;
    @NonNull
    public final Integer studentId;

    public TerminStudentJoin(final Integer terminId, final Integer studentId) {
        this.terminId = terminId;
        this.studentId = studentId;
    }




}
