package daniel.daniel_pset4;

/*
 * Student name: Daniel Oliemans
 * Student number: 11188669
 * University of Amsterdam
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Main screen activity where users enter Lists for to-dos
 */
public class MainActivity extends AppCompatActivity {

    // Declaring variables
    EditText inputBox;
    ListView todoList;
    ArrayAdapter<ToDoList> todoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        //Giving context to the variables by telling AS what IDs or context to look at
        todoList = (ListView) findViewById(R.id.todoList);
        inputBox = (EditText) findViewById(R.id.inputBox);
        final ToDoManager todoManager = ToDoManager.getInstance(this);
        ArrayList<ToDoList> toDoLists = todoManager.getTodoLists();

        // Set the adapter
        todoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                toDoLists);
        todoList.setAdapter(todoListAdapter);

        // Sets parameters to advance to the next screen
        final Intent itemList = new Intent(this, ItemList.class);
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Determines item clicked
                ToDoList todo = (ToDoList) todoList.getItemAtPosition(position);

                // And goes to the next screen
                itemList.putExtra("list_id", todo.getId());
                startActivity(itemList);
            }
        });

        // Setting an OnItemLongClickListener here to keep listening whether the user has long
        // pressed an item to delete it
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoList todo = (ToDoList) todoList.getItemAtPosition(position);
                // Deletes the item from the database
                todoManager.removeItem(todo.getId());
                // Deletes the item from the ToDoList
                todoListAdapter.remove(todo);
                return false;
            }
        });
    }

    /*
     * Function that waits for the user to give input in the EditText after pressing the "Add"
     * button and adds the user's input
     */
    public void createTodo(View view) {
        // Get the user input from the input box below
        String input = String.valueOf(inputBox.getText());

        // Checks whether entered characters are valid
        if (!input.matches("[a-zA-Z1-9\\s]+")) {
            Toast.makeText(this, "Please enter valid characters.", Toast.LENGTH_SHORT).show();
        } else {
            // If the entered characters are valid, add the user's input to the database
            final ToDoManager toDoManager = ToDoManager.getInstance(this);
            ToDoList todoList = new ToDoList(inputBox.getText().toString());
            toDoManager.saveToDoItem(todoList);

            // And add them to the ListView afterwards
            todoListAdapter.add(todoList);
            inputBox.setText("");

        }
    }
}

