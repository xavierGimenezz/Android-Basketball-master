package com.taniafontcuberta.basketball.controller.activities.add_edit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.model.Atleta;
import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.master_detail.AtletaListActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;


import java.util.List;

/**
 * A Add screen that offers Add via username/basketsView.
 */
public class AddEditActivity extends AppCompatActivity implements AtletaCallback {

    // UI references.
    private EditText nameView;
    private  EditText apellidoView;
    private EditText nacionalidadView;
    private DatePicker birthdateView;
    private Bundle extras;

    // ATTR
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String birthdate;
    private String id;

    private View mProgressView;
    private View mAddFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        if (extras.getString("type").equals("add")) {
            setTitle("Add Atleta");
        } else {
            setTitle("Edit Atleta");
        }
        setContentView(R.layout.activity_add_edit);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the Add form.
        nameView = (EditText) findViewById(R.id.athleteName);
        apellidoView = (EditText) findViewById(R.id.apellido);
        nacionalidadView = (EditText) findViewById(R.id.nacionalidad);
        birthdateView = (DatePicker) findViewById(R.id.birthdate);



        Button addButton = (Button) findViewById(R.id.add_edit_button);

        switch (extras.getString("type")) {
            case "add":
                addButton.setText("Add Player");
                break;
            case "edit":
                addButton.setText("Edit Player");

                id = extras.getString("id");
                nameView.setText(AtletaManager.getInstance().getAthlete(id).getNombre());
                apellidoView.setText(AtletaManager.getInstance().getAthlete(id).getApellido());
                nacionalidadView.setText(AtletaManager.getInstance().getAthlete(id).getNacionalidad());



            /* Get birthDate, split by "-", and update datePicker */
                String birthdateGet = AtletaManager.getInstance().getAthlete(id).getFechaNacimiento().toString();
                String[] date = birthdateGet.split("-");
                birthdateView.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));


        }
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd(view);
            }
        });

        mAddFormView = findViewById(R.id.add_edit_form);
        mProgressView = findViewById(R.id.add_edit_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    /**
     * Attempts to log in the account specified by the Add form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual Add attempt is made.
     */
    private void attemptAdd(View v) {
        // Reset errors.
        nameView.setError(null);
        apellidoView.setError(null);
        nacionalidadView.setError(null);

        // Store values at the  Add attempt.
        nombre = nameView.getText().toString();
        apellido = this.apellidoView.getText().toString();
        nacionalidad = this.nacionalidadView.getText().toString();

        String year = String.valueOf(this.birthdateView.getYear());
        String month = String.valueOf(this.birthdateView.getMonth() + 1);
        String day = String.valueOf(this.birthdateView.getDayOfMonth());

        if (this.birthdateView.getDayOfMonth() < 10) {
            day = "0" + day;
        }
        if (this.birthdateView.getMonth() < 10) {
            month = "0" + month;
        }
        birthdate = year + "-" + month + "-" + day;

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            // There was an error; don't attempt Add and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Add attempt.
            showProgress(true);

            atleta.setNombre(nombre);
            atleta.setApellido(apellido);
            atleta.setNacionalidad(nacionalidad);
            atleta.setFechaNacimiento(birthdate);

            if (extras.getString("type").equals("add")) {
                AtletaManager.getInstance().createAthlete(AddEditActivity.this, atleta);
                Toast.makeText(AddEditActivity.this, "Created :  " + atleta.getNombre(), Toast.LENGTH_LONG).show();
            } else {
                atleta.setId(Long.parseLong(id));
                AtletaManager.getInstance().updateAthlete(AddEditActivity.this, atleta);
                Toast.makeText(AddEditActivity.this, "Edited  :   " + atleta.getNombre(), Toast.LENGTH_LONG).show();

            }
            Intent i = new Intent(v.getContext(), AtletaListActivity.class);
            startActivity(i);

        }
    }

    @Override
    public void onSuccess(List<Atleta> atletasList) {}

    @Override
    public void onSucces() {

    }

    @Override
    public void onSucces(Atleta atleta) {
        Toast.makeText(AddEditActivity.this, "Add  :   " + atleta.getNombre(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e("AddEditActivity->", "performAdd->onFailure ERROR " + t.getMessage());

        // TODO: Gestionar los diversos tipos de errores. Por ejemplo, no se ha podido conectar correctamente.
        showProgress(false);
    }

    /**
     * Shows the progress UI and hides the Add form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

