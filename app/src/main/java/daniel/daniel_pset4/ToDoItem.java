package daniel.daniel_pset4;

/*
 * Student name: Daniel Oliemans
 * Student number: 11188669
 * University of Amsterdam
 */

public class ToDoItem {
    private int id = -1;
    private int listId;
    private String title;
    private boolean completed;

    public ToDoItem() {}

    public ToDoItem(String title) {
        this.title = title;
    }

    public ToDoItem(int id, String title) {
        this.id    = id;
        this.title = title;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString() { return this.title; }
}
