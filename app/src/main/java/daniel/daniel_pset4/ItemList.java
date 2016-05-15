package daniel.daniel_pset4;

/*
 * Student name: Daniel Oliemans
 * Student number: 11188669
 * University of Amsterdam
 */

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/*
 * Second screen to add to-dos in
 */
public class ItemList extends AppCompatActivity {

    ArrayAdapter<ToDoItem> todoItemArrayAdapter;
    ListView todoListItem;
    int listId;
    EditText inputBoxItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        // Initialize the objects for later use
        todoListItem = (ListView) findViewById(R.id.todoListItem);
        inputBoxItem = (EditText) findViewById(R.id.inputBoxItem);

        // Retrieves info from previous window
        Intent intent = getIntent();
        listId = intent.getIntExtra("list_id", 666);

        // Creating the array list for the DBHelper to write into
        final ToDoManager toDoManager = ToDoManager.getInstance(this);
        final ArrayList<ToDoItem> toDoItemArrayList = toDoManager.retrieveTodoItemsByListId(listId);

        todoItemArrayAdapter = new ItemAdapter(this, android.R.layout.simple_list_item_1,
                toDoItemArrayList);
        todoListItem.setAdapter(todoItemArrayAdapter);

        // Setting an OnItemLongClickListener here to keep listening whether the user has long
        // pressed an item to delete it
        todoListItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem todo = (ToDoItem) todoListItem.getItemAtPosition(position);
                // Deletes the item from the database
                toDoManager.removeItem(todo.getId());
                // Deletes the item from the ToDoList
                todoItemArrayAdapter.remove(todo);
                return false;
            }
        });

        // Sets on click listener to
        todoListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemTextView = (TextView) view.findViewById(android.R.id.text1);
                ToDoItem item = (ToDoItem) todoListItem.getItemAtPosition(position);

                if (item.isCompleted()) {
                    itemTextView.setPaintFlags(0);
                    item.setCompleted(false);
                } else {
                    itemTextView.setPaintFlags(itemTextView.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);
                    item.setCompleted(true);
                }
                toDoManager.saveToDoItem(item);
            }
        });
    }

    public void createTodoItem(View view) {
        // Get the user input from the input box below
        String input = String.valueOf(inputBoxItem.getText());

        // Checks whether entered characters are valid
        if (!input.matches("[a-zA-Z1-9\\s]+")) {
            Toast.makeText(this, "Please enter valid characters.", Toast.LENGTH_SHORT).show();
        } else {
            // If the entered characters are valid, add the user's input to the database
            ToDoManager toDoManager = ToDoManager.getInstance(this);
            ToDoItem toDoItem = new ToDoItem(inputBoxItem.getText().toString());

            toDoItem.setListId(listId);
            toDoItem.setCompleted(false);
            toDoManager.saveToDoItem(toDoItem);

            // And add them to the ListView afterwards
            todoItemArrayAdapter.add(toDoItem);
            inputBoxItem.setText("");
        }
    }
}
