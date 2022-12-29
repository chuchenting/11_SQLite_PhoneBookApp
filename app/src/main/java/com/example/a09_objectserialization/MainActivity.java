package com.example.a09_objectserialization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView name_textView;
    private TextView phone_textView;
    private TextView email_textView;

    private EditText name_editText;
    private EditText phone_editText;
    private EditText email_editText;

    private Button add_button;
    private Button modify_button;
    private Button del_button;

    private ListView person_listView;
    private ArrayList<Person> person_list;
    private ArrayAdapter<Person> array_adapter;
    private int selected_item;

    PersonDatabase person_database;
    AlertDialog.Builder builder ;
    private final String file_name = "person_list.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        person_listView = findViewById(R.id.person_listView);
        name_textView = (TextView) findViewById(R.id.name_textView);
        phone_textView = (TextView) findViewById(R.id.phone_textView);
        email_textView = (TextView) findViewById(R.id.email_textView);
        name_editText = (EditText) findViewById(R.id.name_editText);
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        email_editText = (EditText) findViewById(R.id.email_editText);
        add_button = (Button) findViewById(R.id.add_button);
        modify_button = (Button) findViewById(R.id.modify_button);
        del_button = (Button) findViewById(R.id.del_button);

        person_database = new PersonDatabase(this, "address_book.db", 1);
        person_list = new ArrayList<Person>();
        person_database.load(person_list);
        array_adapter = new ArrayAdapter<Person>(this, R.layout.list_item, person_list);

        person_listView.setAdapter(array_adapter);
        array_adapter.notifyDataSetChanged();

        OnItemClickHandler ItemHandler = new OnItemClickHandler();
        person_listView.setOnItemClickListener(ItemHandler);

        ButtonHandler ButtonHandler = new ButtonHandler();
        add_button.setOnClickListener(ButtonHandler);
        modify_button.setOnClickListener(ButtonHandler);
        del_button.setOnClickListener(ButtonHandler);

        builder = new AlertDialog.Builder(MainActivity.this);
    }

    public class OnItemClickHandler implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //get the selected item
            Person p = person_list.get((int) id);

            //create an intent
            name_editText.setText(p.getName());
            phone_editText.setText(p.getPhone());
            email_editText.setText(p.getEmail());
            selected_item = (int) id;


        }//onItemClick
    }//OnItemClickHandler class

    private class ButtonHandler implements View.OnClickListener {
        private class DelButtonHandler implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int which) {
                person_list.remove(selected_item);
                array_adapter.notifyDataSetChanged();
                clearEditText();
                selected_item = -1;
                save();
            }//onClick
        }//DelButtonHandler class

        public void onClick(View view) {
            if (view == add_button) {
                String name = name_editText.getText().toString();
                String phone = phone_editText.getText().toString();
                String email = email_editText.getText().toString();

                if (name.equals("") || phone.equals("")) {
                    builder.setTitle ("Missing information.");
                    builder.setMessage ("Please enter a name and phone number.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    selected_item = -1;
                    return;
                }//if condition


                Person p = new Person(name, phone, email);
                person_list.add(p);
                array_adapter.notifyDataSetChanged();

                person_database.add(p);

                clearEditText();
                save();
                //selected_item = -1;
            }//if

            else if (view == modify_button) {
                if(selected_item != -1) {
                    Person p = person_list.get(selected_item);
                    String name = name_editText.getText().toString();
                    String phone = phone_editText.getText().toString();
                    String email = email_editText.getText().toString();

                    p.set_name(name);
                    p.set_phone(phone);
                    p.set_email(email);

                    array_adapter.notifyDataSetChanged();
                    person_database.update(p);

                    clearEditText();
                }
                selected_item = -1;
                save();
            }//else if

            else if (view == del_button) {
                if(selected_item != -1) {

                    builder.setTitle("Confirm Message.");
                    builder.setMessage("Are you sure you want to delete this item?");
                    DelButtonHandler handler = new DelButtonHandler();
                    builder.setPositiveButton("Yes", handler);
                    builder.setNegativeButton("Cancel", null);
                    person_database.del(name_editText.getText().toString());

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }//else if
        }//onClick
    }//ButtonHandler class

    private void clearEditText() {
        name_editText.setText("");
        phone_editText.setText("");
        email_editText.setText("");
    }//clearEditText

    private void load(){
        try {
            File directory = getExternalFilesDir(null);
            File file = new File(directory, file_name);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));

            Person p = (Person) input.readObject();
            while (p != null) {
                person_list.add(p);
                p = (Person) input.readObject();
            }//while loop

            if (input != null) input.close();
        }catch(Exception e){
            System.out.printf("%s", e.getMessage());
        }
    }//load

    private void save(){
        try{
            File directory = getExternalFilesDir(null);
            File file = new File(directory, file_name);
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));

            for(int i = 0; i < person_list.size(); i++){
                output.writeObject((Person)person_list.get(i));
            }//for loop

            if(output != null){
                output.close();
            }//if condition
        }catch(Exception e){
            System.out.printf("%s", e.getMessage());
        }
    }

    public class PersonDatabase extends SQLiteOpenHelper {
        public PersonDatabase(Context context, String name, int version) {
            super(context, name, null, version);
        }//PersonDatabase()

        private static final String TABLE_PERSON = "person";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_PHONE = "phone";
        private static final String COLUMN_EMAIL = "email";

        public void load (ArrayList<Person> person_list) {
            person_list.clear();

            String query = "SELECT * FROM " + TABLE_PERSON;
            SQLiteDatabase db = this.getWritableDatabase();
            if (db ==null) {return;}


            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {return; }


            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++) {
                //get all fields
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);

                //create a new person object
                Person person = new Person(name, phone, email);
                person_list.add(person);

                //move to the next record
                cursor.moveToNext();
            }//for
        }//load

        public void add(Person person) {
            String insert_str = "insert into " + TABLE_PERSON + " (" +
                    COLUMN_NAME + "," +
                    COLUMN_PHONE + "," +
                    COLUMN_EMAIL + ")" +
                    " values (" + "\"" + person.getName() + "\"," +
                    "\"" + person.getPhone() + "\"," +
                    "\"" + person.getEmail() + "\")";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(insert_str);
            db.close();
        }//add

        public void del(String name) {
            String delete_str = "delete FROM " + TABLE_PERSON + " where " + COLUMN_NAME + " = " + "\"" + name + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(delete_str);
            db.close();
        }//del
        public void update(Person person) {
            String update_str = "update " + TABLE_PERSON + " SET " +
                    COLUMN_NAME + " = " + "\"" + person.getName() + "\"" + "," +
                    COLUMN_PHONE + " = " + "\"" + person.getPhone() + "\"" + "," +
                    COLUMN_EMAIL + " = " + "\"" + person.getEmail() + "\"" + " " +
                    " where " + COLUMN_NAME + " = " + "\"" + person.getName() + "\"";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(update_str);
            db.close();
        }//update

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String CreateTableStr = "CREATE TABLE " + TABLE_PERSON + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PHONE + " TEXT, "
                    + COLUMN_EMAIL + " TEXT" + ")";
            sqLiteDatabase.execSQL(CreateTableStr);
        }//onCreate()

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}//onUpgrade()
    }//PersonDatabase()
}//MainActivity class