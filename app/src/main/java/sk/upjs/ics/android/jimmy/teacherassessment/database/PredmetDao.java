package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PredmetDao {

    @Query("SELECT * FROM predmet")
    List<Predmet> getAllPredmety();

    @Query("SELECT * FROM predmet WHERE id=:idPredmet")
    Predmet getPredmetById(Integer idPredmet);

    @Insert
    void insert(Predmet... predmety);

    @Update
    void update(Predmet... predmety);

    @Delete
    void delete(Predmet... predmety);
}
