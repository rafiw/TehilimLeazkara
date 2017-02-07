package com.solve.it;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TehilimLeazkaraActivity extends Activity implements Constants {
    StringBuilder sb;
    String[] kyt;
    Button thilim;
    Button elMale;
    Button kadishY;
    Button kadishD;
    Button entButton;
    Button ashkava;
    Button tfilaLeiluy;
    SharedPreferences prefs;
    Intent view;
    EditText name;
    int selection = 0;
    TextView configedNusach;
    EditText parName;
    ToggleButton tg;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(TehilimLeazkaraActivity.this);
        setContentView(R.layout.main);
        setWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selection = Integer.parseInt(prefs.getString(getString(R.string.pref_list_key), "0"));
        configedNusach.setText(getResources().getString(R.string.def_nusach) +
                getResources().getStringArray(R.array.listOptions)[selection]);
    }

    public void sendTextToView(String str) {
        //attach to bundle
        //font
        String fontType = prefs.getString(getResources().getString(R.string.pref_font_list), "0");
        //fontSize
        String fontSize = prefs.getString(getResources().getString(R.string.pref_font_size), DEFAULT_FONT_SIZE);
        try {
            if (Integer.parseInt(fontSize) == 0
                    || Integer.parseInt(fontSize) > 50
                    || Integer.parseInt(fontSize) < 1) {
                fontSize = DEFAULT_FONT_SIZE;
                Toast.makeText(this, R.string.error_small,
                        Toast.LENGTH_LONG).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_small,
                    Toast.LENGTH_LONG).show();
        }
        // colors
        boolean textBlack = prefs.getBoolean(getString(R.string.pref_black_text), true);
        // show nikud
        boolean showNikud = prefs.getBoolean(getString(R.string.pref_is_font_enabled), true);
        //fix right to left
        boolean rightToLeft = prefs.getBoolean(getString(R.string.pref_right_to_left), true);

        if (!showNikud) {
            str = removeNikud(str);
        }
        view = new Intent("com.solve.it.THILIM");

        view.putExtra(FONT_TYPE, fontType);
        view.putExtra(FONT_SIZE, fontSize);
        view.putExtra(TEXT_BLACK, textBlack);
        view.putExtra(RIGHT_TO_LEFT, rightToLeft);
        //text to show
        view.putExtra(TEXT_KEY, str);
        //start view
        startActivity(view);
    }

    private void setArray() {
        sb = new StringBuilder(300);
        String[] a_b = getResources().getStringArray(R.array.alef_bet);

        sb.append(getResources().getString(R.string.perek) + " " + a_b[11] + "\"" + a_b[2] + "\n"
                + getResources().getString(R.string.tehilimLg) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[8] + "\"" + a_b[6] + "\n"
                + getResources().getString(R.string.tehilimTz) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[9] + "\"" + a_b[6] + "\n"
                + getResources().getString(R.string.tehilimYz) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[15] + "\"" + a_b[1] + "\n"
                + getResources().getString(R.string.tehilimAb) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[22] + "\"" + a_b[1] + "\n"
                + getResources().getString(R.string.tehilimTza) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[18] + "\"" + a_b[3] + "\n"
                + getResources().getString(R.string.tehilimKd) + "\n\n");
        sb.append(getResources().getString(R.string.perek) + " " + a_b[18] + "\"" + a_b[11] + "\n"
                + getResources().getString(R.string.tehilimKl) + "\n\n");
    }

    private String removeNikud(String sb2) {
        return sb2.replaceAll(getResources().getString(R.string.nikud), "");
    }


    private void setWidgets() {
        //for nusach
        selection = Integer.parseInt(prefs.getString(getString(R.string.pref_list_key), "0"));
        thilim = (Button) findViewById(R.id.okBbutton);
        name = (EditText) findViewById(R.id.name);
        parName = (EditText) findViewById(R.id.par_name);
        ashkava = (Button) findViewById(R.id.ashkava_button);
        elMale = (Button) findViewById(R.id.elMaleRachamim);
        tg = (ToggleButton) findViewById(R.id.son_daughter);
        kadishY = (Button) findViewById(R.id.kadishYatomButton);
        kadishD = (Button) findViewById(R.id.kadishDerabananButton);
        tfilaLeiluy = (Button) findViewById(R.id.tfilaLeiluy);
        thilim.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setArray();
                String nametoProsess = new String(name.getText().toString().replace(" ", "")
                        + getResources().getString(R.string.neshama));
                getTehilim(nametoProsess);
                sendTextToView(sb.toString());
                sb.delete(0, sb.length());
            }
        });

        entButton = (Button) findViewById(R.id.entranceButton);
        entButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String string;
                if (selection == 0)
                    string = getResources().getString(R.string.enterAshkenaz);
                else
                    string = getResources().getString(R.string.enterSfardi);
                sendTextToView(string);
            }
        });

        tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ashkava.setText(getResources().getString(R.string.ashkava_label_m));
                else
                    ashkava.setText(getResources().getString(R.string.ashkava_label_w));
            }
        });

        elMale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0 || parName.getText().length() == 0) {
                    Toast.makeText(v.getContext(), R.string.error_missing_nams, Toast.LENGTH_LONG).show();
                    sendTextToView(getResources().getString(R.string.elMaleGeneric));
                } else {
                    StringBuilder el = new StringBuilder();
                    el.append(getResources().getString(R.string.elMaleBeginGeneric));
                    String str;
                    if (tg.isChecked()) {
                        str = " " + name.getText().toString() + " " +
                                getResources().getString(R.string.son) + " " +
                                parName.getText().toString() + " ";
                    } else {
                        str = " " + name.getText().toString() + " " +
                                getResources().getString(R.string.girl) + " " +
                                parName.getText().toString() + " ";
                    }
                    el.append(str);
                    if (tg.isChecked())
                        el.append(getResources().getString(R.string.elMaleMan));
                    else
                        el.append(getResources().getString(R.string.elMaleWoman));
                    sendTextToView(el.toString());
                }
            }
        });

        ashkava.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tg.isChecked())
                    sendTextToView(getResources().getString(R.string.ashkava_m));
                else
                    sendTextToView(getResources().getString(R.string.ashkava_w));
            }
        });

        kadishY.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str;
                switch (selection) {
                    case 1:
                        str = getResources().getString(R.string.kadishYatomSfard);
                        break;
                    case 2:
                        str = getResources().getString(R.string.kadishYatomEdot);
                        break;
                    case 3:
                        str = getResources().getString(R.string.kadishyatomTeiman);
                        break;
                    case 0:
                    default:
                        str = getResources().getString(R.string.kadishYatomAshkenaz);
                        break;
                }
                if (str != null) {
                    sendTextToView(str);
                }

            }
        });
        tfilaLeiluy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder(3050);
                sb.append(getResources().getString(R.string.tfila_generic_start));
                sb.append(name.getText().toString() + " ");
                if (tg.isChecked()) {
                    //son
                    sb.append(getResources().getString(R.string.son) + " ");
                    sb.append(parName.getText().toString() + " ");
                    sb.append(getResources().getString(R.string.tfilat_leiluy_m));
                } else {
                    //girl
                    sb.append(getResources().getString(R.string.girl) + " ");
                    sb.append(parName.getText().toString() + " ");
                    sb.append(getResources().getString(R.string.tfilat_leiluy_w));
                }
                sendTextToView(sb.toString());
            }
        });
        kadishD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str;
                switch (selection) {
                    case 1:
                        str = getResources().getString(R.string.kadishDSfard);
                        break;
                    case 2:
                        str = getResources().getString(R.string.kadishDEdot);
                        break;
                    case 3:
                        str = getResources().getString(R.string.kadishDTeiman);
                        break;
                    case 0:
                    default:
                        str = getResources().getString(R.string.kadishDAshkenaz);
                        break;
                }
                if (str != null) {
                    sendTextToView(str);
                }
            }
        });
        configedNusach = (TextView) findViewById(R.id.configed_nusach);
        configedNusach.setText(getResources().getString(R.string.def_nusach) +
                getResources().getStringArray(R.array.listOptions)[selection]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.first_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id._about:
                Intent i = new Intent("com.solve.it.ABOUT");
                startActivity(i);
                return true;
            case R.id._settings:
                Intent j = new Intent("com.solve.it.PREFS");
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getTehilim(String name) {
        kyt = getResources().getStringArray(R.array.kytn);
        for (int i = 0; i < name.length(); i++)
            sb.append(getKyt(Character.toString(name.charAt(i))));
    }

    private String getKyt(String letter) {
        String[] a_b = getResources().getStringArray(R.array.alef_bet);
        for (int i = 0; i < a_b.length; i++)
            if (a_b[i].contains(letter))
                return letter + "\n" + kyt[i] + "\n\n";
        return "";
    }

}