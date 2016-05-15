package daniel.daniel_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/*
This class contains functions to control to-do lists and to-dos in the database.
 */
public class ToDoManager extends SQLiteOpenHelper {
    private static ToDoManager instance;

    // Strings used for query actions
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    public static final String TABLE_LIST = "TODO_LIST";
    public static final String TABLE_ITEM = "TODO_ITEM";

    // Get singleton
    public static synchronized ToDoManager getInstance(Context context) {
        if (instance == null) {
            instance = new ToDoManager(context.getApplicationContext());
        }
        return instance;
    }

    // Constructor
    public ToDoManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*
    Creates a table for to-do lists and to-do items
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Set query strings
        String createListTableQuery = "CREATE TABLE " + TABLE_LIST + "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "title TEXT "+
                ")";

        String createItemTableQuery = "CREATE TABLE " + TABLE_ITEM + "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "list_id INTEGER, "+
                "title TEXT, "+
                "is_completed INTEGER"+
                ")";

        // Create tables
        db.execSQL(createListTableQuery);
        db.execSQL(createItemTableQuery);
    }


    /*
     When upgraded, the old table is dropped.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEM);
    }

    /*
    Add a list in the database
    */
    public void saveTodoList(ToDoList todoList) {

        // Set an contentvalues object with the title
        ContentValues values = new ContentValues();
        values.put("title", todoList.getId());

        SQLiteDatabase db = getWritableDatabase();
        // If to-do list id is negative (-1), set new id and insert in database
        if (todoList.getId() < 0) {
            long listId = db.insert(TABLE_LIST, null, values);
            todoList.setId(((int) listId));
        }
        // If id is positive, to-do list exists and is updated
        else {
            db.update(TABLE_LIST, values, "id = ?", new String[] {String.valueOf(todoList.getId())});
        }

        // Close database
        db.close();
    }

    /*
      Add a to-do to the database
    */
    public void saveToDoItem(ToDoItem todoItem) {

        // Set an contentvalues object with the title, id and if it is completed
        ContentValues values = new ContentValues();
        values.put("title", todoItem.getId());
        values.put("list_id", todoItem.getListId());
        values.put("completed", (todoItem.isCompleted()) ? 1 : 0);

        SQLiteDatabase db = getWritableDatabase();
        // If to-do id is negative (-1), set new id and insert in database
        if (todoItem.getId() < 0) {
            long itemId = db.insert(TABLE_ITEM, null, values);
            todoItem.setId(((int) itemId));
        }
        // If id is positive, to-do list exists and is updated
        else {
            db.update(TABLE_ITEM, values, "id = ?", new String[] {String.valueOf(todoItem.getId())});
        }

        // Close database
        db.close();
    }

    /*
     Remove a list from database
    */
    public void removeList (int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LIST, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /*
     Remove a to-do from database
    */
    public void removeItem (int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ITEM, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /*
     Get all to-do lists from database
    */
    public ArrayList<ToDoList> getTodoLists(){

        // Create new arraylist and get database
        ArrayList<ToDoList> todoLists = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // String for query action
        String query = "SELECT * FROM " + TABLE_LIST + " WHERE 1";

        // Create cursor
        Cursor c = db.rawQuery(query, null);
        // Set cursor to first
        if (c.moveToFirst()) {
            // Loop through database
            do {
                // Create a new list
                ToDoList list = new ToDoList(c.getInt(0), c.getString(1));
                // Add list to arraylist
                todoLists.add(list);
                // If there is a next, go to next list
            } while (c.moveToNext()) ;
        }
        // Close database and cursor and return the arraylist of lists
        db.close();
        c.close();
        return todoLists;
    }

    /*
     Get all to-do list items from the database
    */
    public ArrayList<ToDoItem> retrieveTodoItemsByListId(int listId){
        // Create new arraylist and get database
        ArrayList<ToDoItem> todoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // String for query action
        String query = "SELECT * FROM " + TABLE_ITEM + " WHERE list_id = "+ Integer.valueOf(listId).toString();

        // Create cursor
        Cursor c = db.rawQuery(query, null);

        // Set cursor to first
        if (c.moveToFirst()) {
            // Loop through database
            do {
                // Create a new to-do
                ToDoItem item = new ToDoItem();
                item.setId(c.getInt(c.getColumnIndex("id")));
                item.setListId(c.getInt(c.getColumnIndex("list_id")));
                item.setTitle(c.getString(c.getColumnIndex("title")));
                boolean itemDone = (c.getInt(c.getColumnIndex("completed")) == 1);
                item.setCompleted(itemDone);
                // Add to-do to arraylist
                todoItems.add(item);
                // If there is a next, go to next to-do
            } while (c.moveToNext()) ;
        }

        // Close database and cursor and return the arraylist of to-dos
        db.close();
        c.close();
        return todoItems;
    }
}