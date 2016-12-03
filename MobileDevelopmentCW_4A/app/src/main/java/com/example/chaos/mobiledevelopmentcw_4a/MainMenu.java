package com.example.chaos.mobiledevelopmentcw_4a;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainMenu extends AppCompatActivity{

    /***
     * Declarations (UI)
     ***/
    Button but1, but2, but5, but6, but8, but9;
    ListView lstVw1;
    ListViewAdapter lstVwAda;
    EditText name, init, txtInit;
    TextView txtName, txtRSSLoc;
    private ViewFlipper switcher;

    /***
     * Declarations (Lists)
     ***/
    public ArrayList<String> nameLst = new ArrayList<>();
    public ArrayList<Integer> initLst = new ArrayList<>();
    public ArrayList<Integer> pasPerLst = new ArrayList<>();
    public ArrayList<String> tempNameLst = new ArrayList<>();
    public ArrayList<Integer> tempInitLst = new ArrayList<>();


    /***
     * Declarations (Variables)
     ***/
    static String[] nameArr;
    String[] tempNameArr;
    static Integer[] initArr, tempInitArr;
    int count;
    private String tempName;
    private Integer tempInit;

    /***
     * Declarations (Database)
     ***/
    InitiativeDB userInitiatives;

    /***
     * Declarations (Other)
     ***/
    SharedPreferences initSharedPrefs;
    initSaveData initSDPrefs;
    SharedPreferences mySharedPrefs;
    FragmentManager fmAboutDialogue;

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
        but1 = (Button) findViewById(R.id.butBack_Tracker);
        but2 = (Button) findViewById(R.id.butSort);
        but5 = (Button) findViewById(R.id.butConfirm);
        but6 = (Button) findViewById(R.id.butBack_AddComb);
        but8 = (Button) findViewById(R.id.butInitTracker);
        but9 = (Button) findViewById(R.id.butBack_Prefs);
        txtName = (TextView) findViewById(R.id.txtName);
        txtInit = (EditText) findViewById(R.id.txtInitiative);
        txtRSSLoc = (TextView) findViewById(R.id.txtRSSLocation);
        name = (EditText) findViewById(R.id.editName);
        init = (EditText) findViewById(R.id.editInitiative);
        lstVw1 = (ListView) findViewById(R.id.mainListView);

        /*** Load any saved preferences ***/
        initSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadSavedPreferences();
        Log.e("n","SDOutput msg");

        /***Shared Preferences ***/
        mySharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        initSDPrefs = new initSaveData(mySharedPrefs);
        initSDPrefs.setDefaultPrefs();

        /*** Dialogue Fragment ***/
        fmAboutDialogue = this.getFragmentManager();

        /*** Handles a floating button to add combatants ***/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearInputs();
                if (count > 0) {
                    ArraysToList();
                }
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.addCombatant)));

            }
        });

        /*** Database handler ***/
        userInitiatives = new InitiativeDB();

        InitiativeDBMgr dbInitMgr = new InitiativeDBMgr(this,"initiative.s3db",null,1); // Lab 5
        try {
            dbInitMgr.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //userInitiatives = dbInitMgr.findName(userInitiatives.getName());


        /*** Return to Home Screen ***/
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.MainMenu)));
            }
        });

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
                } else {
                    PrepForArrays();
                    switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.TrackerView)));
                    AddToArrays();
                }
            }
        });

        /*** Return to main view without adding a combatant ***/
        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.TrackerView)));
            }
        });

        /*** Goes to the initiative tracker screen ***/
        but8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.TrackerView)));
            }
        });

        but9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.MainMenu)));
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                // About Dialogue;
                DialogFragment initAboutDlg = new initAboutDialogue();
                initAboutDlg.show(fmAboutDialogue, "menu");
                return true;
            case R.id.action_settings:
                switcher.setDisplayedChild(switcher.indexOfChild(findViewById(R.id.PrefsView)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /***
     * Resets input fields
     ***/
    public void ClearInputs() {
        name.setText("");
        init.setText("");
    }

    /***
     * Gets new data into Lists for adding to arrays
     ***/
    public void PrepForArrays() {
        /*** Gets inputs and places them into temporary holders ***/
        tempName = name.getText().toString();
        tempInit = Integer.parseInt(init.getText().toString());

        /*** Adds the temporary holders into the relevant lists ***/
        nameLst.add(tempName);
        initLst.add(tempInit);

        /*** Overwrites arrays with List data ***/
        nameArr = nameLst.toArray(new String[0]);
        initArr = initLst.toArray(new Integer[0]);
    }

    /***
     * Passes arrays into ListViewAdapter
     ***/
    public void AddToArrays() {
        lstVwAda = new com.example.chaos.mobiledevelopmentcw_4a.ListViewAdapter(this, nameArr, initArr, nameLst, initLst);
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

        nameLst = new ArrayList(Arrays.asList(nameArr));
        initLst = new ArrayList(Arrays.asList(initArr));
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

    public void SortEverything() {
        tempNameArr = Arrays.copyOf(nameArr, nameArr.length);
        tempInitArr = Arrays.copyOf(initArr, initArr.length);

        Arrays.sort(tempInitArr, Collections.<Integer>reverseOrder());

        tempNameLst.clear();
        tempInitLst.clear();

        int len1, len2;

        len1 = tempInitArr.length;
        len2 = initLst.size();

        for (int i = 0; i < len1; i++) {
            for (int x = 0; x < len2; x++) {
                if (initLst.get(x) == tempInitArr[i]) {
                    tempNameLst.add(nameLst.get(x));

                    initLst.remove(x);
                    nameLst.remove(x);

                    len1 = tempInitArr.length;
                    len2 = initLst.size();
                }
            }
        }

        nameArr = tempNameLst.toArray(new String[0]);
        initArr = Arrays.copyOf(tempInitArr, tempInitArr.length);

        ArraysToList();
        AddToArrays();
    }

    private void loadSavedPreferences()
    {
        txtRSSLoc.setText(txtRSSLoc.getText() + initSharedPrefs.getString("init_RSSFeed", "https://www.reddit.com/r/DnD.rss"));

    }
}
