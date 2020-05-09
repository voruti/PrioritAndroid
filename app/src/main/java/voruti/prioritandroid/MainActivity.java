package voruti.prioritandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

import voruti.priorit.Item;
import voruti.priorit.PrioritManager;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 2107635;
    private Item currentItem;
    private PrioritManager manager;
    private FloatingActionButton fab;
    private boolean isListShown;
    private DetailFragment detailFragment;
    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isListShown) {
                    Item item = new Item();
                    if (manager.addItem(item)) {
                        currentItem = item;

                        NavHostFragment.findNavController(listFragment)
                                .navigate(R.id.action_ListFragment_to_DetailFragment);
                    } else {
                        Log.println(Log.ERROR, "voruti", "Error on adding new item");
                    }
                } else {
                    Item item = detailFragment.saveCurrent();
                    if (!manager.updateItem(item))
                        Log.println(Log.ERROR, "voruti", "Error on saving item");
                }
            }
        });

        ensurePermissions();

        File externalStorageDirectory = this.getExternalFilesDir(null);
        try {
            manager = new PrioritManager(externalStorageDirectory);
        } catch (IOException e) {
            Log.println(Log.ERROR, "voruti", "PrioritManager threw IOException; " + e.getMessage() + ", " + e.getCause());
            e.printStackTrace();

            // from https://stackoverflow.com/q/2663491 :
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("There was an error: " + e.getMessage() + ", " + e.getCause())
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });
            AlertDialog error = builder.create();
            error.show();
            return;
        }
    }

    public void ensurePermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, WRITE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    MainActivity.this.finish();
                }
                break;
        }
    }

    PrioritManager getManager() {
        return manager;
    }

    Item getCurrentItem() {
        return currentItem;
    }

    void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    void setDetailFragment(DetailFragment detailFragment) {
        this.detailFragment = detailFragment;
    }

     void setListFragment(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    void switchTo(boolean isListShown) {
        this.isListShown = isListShown;
        if (isListShown)
            fab.setImageResource(android.R.drawable.ic_menu_add);
        else
            fab.setImageResource(android.R.drawable.ic_menu_save);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
     */
}
