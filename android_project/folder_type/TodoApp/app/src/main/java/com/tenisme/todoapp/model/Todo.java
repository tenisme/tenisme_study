package com.tenisme.todoapp.model;

public class Todo {
    private int todo_id;
    private String title;
    private String date;
    private int completed;

    public Todo() {
    }

    public Todo(int todo_id, String title, String date, int completed) {
        this.todo_id = todo_id;
        this.title = title;
        this.date = date;
        this.completed = completed;
    }

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
