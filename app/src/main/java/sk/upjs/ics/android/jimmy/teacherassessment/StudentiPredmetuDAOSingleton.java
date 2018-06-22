package sk.upjs.ics.android.jimmy.teacherassessment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jimmy on 20.03.2018.
 */

public enum StudentiPredmetuDAOSingleton {

    INSTANCE;

    private List<StudentPredmetu> studentiPredmetuList = new ArrayList<StudentPredmetu>();

    private int idGenerator = 0;

    StudentiPredmetuDAOSingleton() {

        StudentPredmetu student1 = new StudentPredmetu();
        student1.setId(1);
        student1.setMeno("Janko");
        student1.setPriezvisko("Hrasko");
        student1.setPlneMeno(student1.getMeno() + " " + student1.getPriezvisko());

        StudentPredmetu student2 = new StudentPredmetu();
        student2.setId(2);
        student2.setMeno("Ferko");
        student2.setPriezvisko("Mrkvicka");
        student2.setPlneMeno(student2.getMeno() + " " + student2.getPriezvisko());

        StudentPredmetu student3 = new StudentPredmetu();
        student3.setId(3);
        student3.setMeno("Ratafak");
        student3.setPriezvisko("Plachta");
        student3.setPlneMeno(student3.getMeno() + " " + student3.getPriezvisko());

        StudentPredmetu student4 = new StudentPredmetu();
        student4.setId(4);
        student4.setMeno("Ferdo");
        student4.setPriezvisko("Mravec");
        student4.setPlneMeno(student4.getMeno() + " " + student4.getPriezvisko());

        saveOrUpdate(student1);
        saveOrUpdate(student2);
        saveOrUpdate(student3);
        saveOrUpdate(student4);


        StudentPredmetu student5 = new StudentPredmetu();
        student5.setId(5);
        student5.setMeno("Peter");
        student5.setPriezvisko("Skaredy");
        student5.setPlneMeno(student5.getMeno() + " " + student5.getPriezvisko());

        StudentPredmetu student6 = new StudentPredmetu();
        student6.setId(6);
        student6.setMeno("Miska");
        student6.setPriezvisko("Marienkova");
        student6.setPlneMeno(student6.getMeno() + " " + student6.getPriezvisko());

        StudentPredmetu student7 = new StudentPredmetu();
        student7.setId(7);
        student7.setMeno("Kuko");
        student7.setPriezvisko("Danculovsky");
        student7.setPlneMeno(student7.getMeno() + " " + student7.getPriezvisko());

        saveOrUpdate(student5);
        saveOrUpdate(student6);
        saveOrUpdate(student7);

        StudentPredmetu student8 = new StudentPredmetu();
        student8.setId(8);
        student8.setMeno("Lester");
        student8.setPriezvisko("Nygaard");
        student8.setPlneMeno(student8.getMeno() + " " + student8.getPriezvisko());

        StudentPredmetu student9 = new StudentPredmetu();
        student9.setId(9);
        student9.setMeno("Lorne");
        student9.setPriezvisko("Malvo");
        student9.setPlneMeno(student9.getMeno() + " " + student9.getPriezvisko());

        saveOrUpdate(student8);
        saveOrUpdate(student9);

    }


    //TODO: vyberie sa zoznam studentov podla idcka predmetu vyucujuceho (zatial data natvrdo)
    public List<StudentPredmetu> getStudentiPredmetu(int idPredmet) {

//        List<StudentPredmetu> studentiPredmetuList = new ArrayList<>(this.studentiPredmetuList);
        StudentiPredmetuDAOSingleton dao = StudentiPredmetuDAOSingleton.INSTANCE;
        this.studentiPredmetuList = dao.studentiPredmetuList;

        switch (idPredmet) {
            case 1:
                this.studentiPredmetuList = studentiPredmetuList.subList(0,4);
                return studentiPredmetuList;
            case 2:
                this.studentiPredmetuList = studentiPredmetuList.subList(4,7);
                return studentiPredmetuList;
            case 3:
                this.studentiPredmetuList = studentiPredmetuList.subList(7,studentiPredmetuList.size());
                return studentiPredmetuList;

            default:
                return null;
        }


//        switch (idPredmet) {
//            case 1:
//
//                StudentPredmetu student1 = new StudentPredmetu();
//                student1.setId(1);
//                student1.setMeno("Janko");
//                student1.setPriezvisko("Hrasko");
//                student1.setPlneMeno(student1.getMeno() + " " + student1.getPriezvisko());
//
//                StudentPredmetu student2 = new StudentPredmetu();
//                student2.setId(2);
//                student2.setMeno("Ferko");
//                student2.setPriezvisko("Mrkvicka");
//                student2.setPlneMeno(student2.getMeno() + " " + student2.getPriezvisko());
//
//                StudentPredmetu student3 = new StudentPredmetu();
//                student3.setId(3);
//                student3.setMeno("Ratafak");
//                student3.setPriezvisko("Plachta");
//                student3.setPlneMeno(student3.getMeno() + " " + student3.getPriezvisko());
//
//                StudentPredmetu student4 = new StudentPredmetu();
//                student4.setId(4);
//                student4.setMeno("Ferdo");
//                student4.setPriezvisko("Mravec");
//                student4.setPlneMeno(student4.getMeno() + " " + student4.getPriezvisko());
//
//                studentiPredmetuList.add(student1);
//                studentiPredmetuList.add(student2);
//                studentiPredmetuList.add(student3);
//                studentiPredmetuList.add(student4);
//
//
//                return studentiPredmetuList;
//
//            case 2:
//
//                StudentPredmetu student5 = new StudentPredmetu();
//                student5.setId(5);
//                student5.setMeno("Peter");
//                student5.setPriezvisko("Skaredy");
//                student5.setPlneMeno(student5.getMeno() + " " + student5.getPriezvisko());
//
//                StudentPredmetu student6 = new StudentPredmetu();
//                student6.setId(6);
//                student6.setMeno("Miska");
//                student6.setPriezvisko("Marienkova");
//                student6.setPlneMeno(student6.getMeno() + " " + student6.getPriezvisko());
//
//                StudentPredmetu student7 = new StudentPredmetu();
//                student7.setId(7);
//                student7.setMeno("Kuko");
//                student7.setPriezvisko("Danculovsky");
//                student7.setPlneMeno(student7.getMeno() + " " + student7.getPriezvisko());
//
//                studentiPredmetuList.add(student5);
//                studentiPredmetuList.add(student6);
//                studentiPredmetuList.add(student7);
//
//                return studentiPredmetuList;
//
//            case 3:
//
//                StudentPredmetu student8 = new StudentPredmetu();
//                student8.setId(8);
//                student8.setMeno("Lester");
//                student8.setPriezvisko("Nygaard");
//                student8.setPlneMeno(student8.getMeno() + " " + student8.getPriezvisko());
//
//                StudentPredmetu student9 = new StudentPredmetu();
//                student9.setId(9);
//                student9.setMeno("Lorne");
//                student9.setPriezvisko("Malvo");
//                student9.setPlneMeno(student9.getMeno() + " " + student9.getPriezvisko());
//
//                studentiPredmetuList.add(student8);
//                studentiPredmetuList.add(student9);
//
//                return studentiPredmetuList;
//
//        }

//        return studentiPredmetuList;

    }

    public StudentPredmetu getStudentPredmetu(int studentId) {
        for (StudentPredmetu studentPredmetu : this.studentiPredmetuList) {
            if (studentPredmetu.getId() == studentId) {
                return studentPredmetu;
            }
        }
        return null;
    }


    public void saveOrUpdate(StudentPredmetu studentPredmetu) {
        if (studentPredmetu.getId() == null) {
            studentPredmetu.setId(getGeneratedId());
            this.studentiPredmetuList.add(studentPredmetu);
        } else {
            Iterator<StudentPredmetu> iterator = this.studentiPredmetuList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                StudentPredmetu s = iterator.next();
                if (s.getId().equals(studentPredmetu.getId())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            this.studentiPredmetuList.add(index, studentPredmetu);
        }
    }

    private int getGeneratedId() {
        idGenerator = this.studentiPredmetuList.size() + 2;
        return idGenerator;
    }

    public void delete(StudentPredmetu studentPredmetu) {
        Iterator<StudentPredmetu> iterator = this.studentiPredmetuList.iterator();
        while(iterator.hasNext()) {
            StudentPredmetu s = iterator.next();
            if(s.getId() == studentPredmetu.getId()) {
                iterator.remove();
            }
        }
    }

}
