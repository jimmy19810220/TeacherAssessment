package sk.upjs.ics.android.jimmy.teacherassessment.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "predmet_student_join",
        primaryKeys = { "predmetId", "studentId" },
        foreignKeys = {
                @ForeignKey(entity = Predmet.class,
                        parentColumns = "id",
                        childColumns = "predmetId",
                        onDelete = CASCADE),
                @ForeignKey(entity = Student.class,
                        parentColumns = "id",
                        childColumns = "studentId",
                        onDelete = CASCADE)
        })
public class PredmetStudentJoin {

    @NonNull
    public final Integer predmetId;
    @NonNull
    public final Integer studentId;

    public PredmetStudentJoin(final Integer predmetId, final Integer studentId) {
        this.predmetId = predmetId;
        this.studentId = studentId;
    }




}
