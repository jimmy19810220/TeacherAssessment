package sk.upjs.ics.android.jimmy.teacherassessment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Student;

public class Utils {

    public static final String DATE_EN_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final DateFormat DF_EN = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
    private static final DateFormat DF_SK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY);
//    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern( DATE_FORMAT );

    private static final String JAZYK_SK = "SK";
    private static final String JAZYK_EN = "EN";


    public static void showConfirmationDialog(String message, final DialogInterface.OnClickListener positiveListener,
                                       final DialogInterface.OnClickListener negativeListener, AppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                positiveListener.onClick(dialogInterface, which);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (negativeListener != null) {
                    negativeListener.onClick(dialogInterface, which);
                }
            }
        });
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.create().show();
    }


    public static void sendMail(AppCompatActivity activity, String... emailAdresses) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        if (emailAdresses == null) {
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        }
        else {
            i.putExtra(Intent.EXTRA_EMAIL, emailAdresses);
        }
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            activity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity.getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    protected static String getFormatedDate(Date date, String kodJazyk) {
        if (JAZYK_EN.equals(kodJazyk)) {
            return DF_EN.format(date);
        } else {
            return DF_SK.format(date);
        }
    }

    /**
     * datum v tvare "dd.MM.yyyy"
     */
//    public static String getDate( Date d ) {
//        if ( d == null )
//            return "";
//        Instant tempInstant = Instant.ofEpochMilli( d.getTime() );
//        return dateFormat.format( java.time.ZonedDateTime.ofInstant(tempInstant, java.time.ZoneId.systemDefault() ) );
//    }


    public static void showDialogPreVyberZnamkyStudenta(final int indexStudent, Context context,
                                                        final List<Student> studentiList,
                                                        final ListView listViewStudenti) {
        //TODO: tu asi pridu znamky z AISu
        final CharSequence[] items = context.getResources().getTextArray(R.array.grades_array);
//            final int checkedItem = ActivitySettings.getDpRozvrhSemester(myActivity).equals(DataProvider.SEMESTER_ZIMNY) ? 0 : 1;


        final int[] checknutaZnamkaIndex = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.znamka);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checknutaZnamkaIndex[0] = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: volanie na server a ulozenie do DB

                studentiList.get(indexStudent).setZnamka(items[checknutaZnamkaIndex[0]].toString());

                //refreshnenem zoznam studentov
                ((AdapterZoznamuStudentov) listViewStudenti.getAdapter()).notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }



}
