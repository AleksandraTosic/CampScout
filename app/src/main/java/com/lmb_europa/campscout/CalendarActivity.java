package com.lmb_europa.campscout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;

import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

//promeni import na ovo ako nece ova klasa da radi...pola androida radi na drugi import, pola na prvi, jedno radi sigurno
//import android.icu.util.Calendar;
import java.util.Calendar;

import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class CalendarActivity extends Activity implements View.OnClickListener { //ovde user bira datum...mislim da je ovo jedina klasa koja...recimo da radi...vrlo kompletno

    Button btnStartDatePicker, btnEndDatePicker, btnConfirm;
    EditText txtStartDate, txtEndDate;
    private int mYear, mMonth, mDay;

    private int stDay, stMonth, stYear, enDay, enMonth, enYear;

    String sDates, eDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnStartDatePicker=(Button)findViewById(R.id.btn_start_date);
        btnEndDatePicker=(Button)findViewById(R.id.btn_end_date);
        txtStartDate=(EditText)findViewById(R.id.in_date);
        txtEndDate=(EditText)findViewById(R.id.in_time);

        txtStartDate.setFocusable(false);
        txtEndDate.setFocusable(false);

        btnConfirm.setOnClickListener(this);
        btnStartDatePicker.setOnClickListener(this);
        btnEndDatePicker.setOnClickListener(this);

        String title = getIntent().getStringExtra("checkApp");

        if (title.equals("FIRST_TIME")) {
            View yourView = findViewById(R.id.btn_confirm);

            new SimpleTooltip.Builder(this)
                    .anchorView(yourView)
                    .text("Choose dates you want to be shown, and confirm to view the exact spots on a map!")
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .transparentOverlay(false)
                    .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                    .overlayOffset(0)
                    .build()
                    .show();
        }

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Wrong date format!")
                .setMessage("It seams like you have selected a wrong date format!.\nPlease change your end date date")
                .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        if (v == btnStartDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            stDay = mDay;
            stMonth = mMonth;
            stYear = mYear;

            DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            sDates = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog1.show();
        }
        if (v == btnEndDatePicker) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            enDay = mDay;
            enMonth = mMonth;
            enYear = mYear;

            DatePickerDialog datePickerDialog2 = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                             /*if (enYear >= stYear) {
                                 if ((enMonth > stMonth) || (enMonth == stMonth && enDay > stDay)) {*/

                                         txtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year); //predomislila sam se, ovo nesto jebe klasu da radi kompletno
                                         eDates = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                 /*}
                             }
                            else {
                                txtEndDate.getText().clear(); //ideja je ako se zajebe unos da se EditText ocisti i da korisnik ponovo unese datum
                                showAlert();
                            }*///ne radi
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog2.show();
        }
        if (v == btnConfirm)
        {
            Intent i = new Intent(CalendarActivity.this, AvailableSpotsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("startDate", sDates);
            bundle.putString("endDate", eDates);
            i.putExtras(bundle);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }
}
