package com.example.chaos.mobiledevelopmentcw_4a;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainMenu extends AppCompatActivity {

    /***
     * Declarations (UI)
     ***/
    Button but1, but2, but5, but6, but8;
    ListView lstVw1;
    ListViewAdapter lstVwAda;
    EditText name, init, pasPer, txtInit;
    TextView txtName, txtPasPer;
    private ViewFlipper switcher;

    /***
     * Declarations (Lists)
     ***/
    public ArrayList<String> nameLst = new ArrayList<>();
    public ArrayList<Integer> initLst = new ArrayList<>();
    public ArrayList<Integer> pasPerLst = new ArrayList<>();
    public ArrayList<String> tempNameLst = new ArrayList<>();
    public ArrayList<Integer> tempInitLst = new ArrayList<>();
    public ArrayList<Integer> tempPasPerLst = new ArrayList<>();


    /***
     * Declarations (Variables)
     ***/
    static String[] nameArr;
    String[] tempNameArr;
    static Integer[] initArr, pasPerArr, tempInitArr, tempPasPerArr;
    int count;
    private String tempName;
    private Integer tempInit, tempPasPer;

    /***
     * When the application loads
     ***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*** Resets the lists ***/
        nameLst.clear();
        initLst.clear();
        pasPerLst.clear();

        /*** Sets up all UI components ***/
        switcher = (ViewFlipper) findViewById(R.id.ViewFlipper);
        //but1 = (Button) findViewById(R.id.butAdd);
        but2 = (Button) findViewById(R.id.butSort);
        but5 = (Button) findViewById(R.id.butConfirm);
        but6 = (Button) findViewById(R.id.butCancel);
        but8 = (Button) findViewById(R.id.butInitTracker);
        txtName = (TextView) findViewById(R.id.txtName);
/*
        txtPasPer = (TextView) findViewById(R.id.txtPasPer);
*/
        txtInit = (EditText) findViewById(R.id.txtInitiative);
        name = (EditText) findViewById(R.id.editName);
        init = (EditText) findViewById(R.id.editInitiative);
        //pasPer = (EditText) findViewById(R.id.editPasPer);
        lstVw1 = (ListView) findViewById(R.id.mainListView);

        //Can we remove this from outside the Initiative Tracker?

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearInputs();
                if (count > 0) {
                    ArraysToList();
                }
                switcher.showNext();
            }
        });


        /*** Switch to Add Combatant screen ***/
        /*
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearInputs();
                if (count > 0) {
                    ArraysToList();
                }
                switcher.showNext();
            }
        }); */

        /*** Sort all combatants by Initiative (High -> Low) ***/
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 1) {
                    SortEverything();
                }
            }
        });

        /*** Return to Initiative Order - adding the combatant in the process ***/
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name == null) {
                    MissingName();
                } else if (init == null) {
                    MissingInit();
                } /*else if (pasPer == null) {
                    MissingPasPer();
                }*/ else {
                    PrepForArrays();
                    switcher.showPrevious();
                    AddToArrays();
                }
            }
        });

        /*** Return to main view without adding a combatant ***/
        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.showPrevious();
            }
        });

        /*** Goes to the initiative tracker screen ***/
        but8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.TrackerView)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * Resets input fields
     ***/
    public void ClearInputs() {
        name.setText("");
        init.setText("");
        /*pasPer.setText("");*/
    }

    /***
     * Gets new data into Lists for adding to arrays
     ***/
    public void PrepForArrays() {
        /*** Gets inputs and places them into temporary holders ***/
        tempName = name.getText().toString();
        tempInit = Integer.parseInt(init.getText().toString());
        /*tempPasPer = Integer.parseInt(pasPer.getText().toString());*/

        /*** Adds the temporary holders into the relevant lists ***/
        nameLst.add(tempName);
        initLst.add(tempInit);
        /*pasPerLst.add(tempPasPer);*/

        /*** Overwrites arrays with List data ***/
        nameArr = nameLst.toArray(new String[0]);
        initArr = initLst.toArray(new Integer[0]);
        /*pasPerArr = pasPerLst.toArray(new Integer[0]);*/
    }

    /***
     * Passes arrays into ListViewAdapter
     ***/
    public void AddToArrays() {
        lstVwAda = new com.example.chaos.mobiledevelopmentcw_4a.ListViewAdapter(this, nameArr, /*pasPerArr,*/ initArr, nameLst, initLst/*, pasPerLst*/);
        lstVw1.setAdapter(lstVwAda);

        /*** Gets number of entries ***/
        count = nameArr.length;

        /*** Displays number of entries ***/
        Toast.makeText(this, "Combatants present: " + count, Toast.LENGTH_SHORT).show();
    }

    /***
     * Overwrites lists with Array data
     ***/
    public void ArraysToList() {
        nameLst.clear();
        initLst.clear();
        /*pasPerLst.clear();*/

        nameLst = new ArrayList(Arrays.asList(nameArr));
        initLst = new ArrayList(Arrays.asList(initArr));
        /*pasPerLst = new ArrayList(Arrays.asList(pasPerArr));*/
    }

    /***
     * Displays a toast when called
     ***/
    public void MissingName() {
        Toast.makeText(this, "Please fill in the 'Name' section", Toast.LENGTH_SHORT).show();
    }

    /***
     * Displays a toast when called
     ***/
    public void MissingInit() {
        Toast.makeText(this, "Please fill in the 'Initiative' section", Toast.LENGTH_SHORT).show();
    }

    /***
     * Displays a toast when called
     ***/
    public void MissingPasPer() {
        Toast.makeText(this, "Please fill in the 'Passive Perception' section", Toast.LENGTH_SHORT).show();
    }

    public void SortEverything() {
        tempNameArr = Arrays.copyOf(nameArr, nameArr.length);
        tempInitArr = Arrays.copyOf(initArr, initArr.length);
        /*tempPasPerArr = Arrays.copyOf(pasPerArr, pasPerArr.length);*/

        Arrays.sort(tempInitArr, Collections.<Integer>reverseOrder());

        tempNameLst.clear();
        tempInitLst.clear();
        /*tempPasPerLst.clear();*/

        int len1, len2;

        len1 = tempInitArr.length;
        len2 = initLst.size();

        for (int i = 0; i < len1; i++) {
            for (int x = 0; x < len2; x++) {
                if (initLst.get(x) == tempInitArr[i]) {
                    tempNameLst.add(nameLst.get(x));
                    /*tempPasPerLst.add(pasPerLst.get(x));*/

                    initLst.remove(x);
                    nameLst.remove(x);
                    /*pasPerLst.remove(x);*/

                    len1 = tempInitArr.length;
                    len2 = initLst.size();
                }
            }
        }

        nameArr = tempNameLst.toArray(new String[0]);
        /*pasPerArr = tempPasPerLst.toArray(new Integer[0]);*/
        initArr = Arrays.copyOf(tempInitArr, tempInitArr.length);

        ArraysToList();
        AddToArrays();
    }
}
