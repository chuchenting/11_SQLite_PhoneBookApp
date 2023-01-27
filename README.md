# 11_SQLite_PhoneBookApp
2022-1 Android Studio Programming Practice

**Launch Android Virtual Device in advance!!**
 
Step 1: Launch the AVD manager and start a previously configured AVD.  
Step 2: Open a Terminal or Command-Prompt window.  
Step 3: Change to the following directory   
```
<SDK Directory>\AppData\Local\Android\sdk\platform-tools 
```

Step 4: connect to emulator by adb.exe tool and set as super user (su)  
```
.\adb -e shell
```

Step 5: Type "su".
```
su
```

Step 6: change to the working directory of your App. 
```
cd com.example.your_project_name
```

Step 7: start the SQLite tool by creating a database 
```
sqlite3 address_book.db
```

Step 8: create a table named "person" with four fields. 
```
create table person (id integer primary key autoincrement, name text, phone text, email text);
```

Step 9: add three records to the table. 
```
insert into person(name, phone, email) values ("cory" , "0921" , corich@gmail.com);
```

