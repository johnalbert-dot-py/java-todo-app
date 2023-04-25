package todoapplication.types;

public class ToDoItem {
  public int id;
  public String title;
  public String description;
  public String due_date;
  public boolean completed;
  public String created_at;
  public String updated_at;
  public int user_id;

  public ToDoItem(String title, String description, String due_date, boolean completed, String created_at,
      String updated_at, int user_id) {
    this.title = title;
    this.description = description;
    this.due_date = due_date;
    this.completed = completed;
    this.created_at = created_at;
    this.updated_at = updated_at;
    this.user_id = user_id;
  }

  public ToDoItem(int id, String title, String description, String due_date, boolean completed, String created_at,
      String updated_at, int user_id) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.due_date = due_date;
    this.completed = completed;
    this.created_at = created_at;
    this.updated_at = updated_at;
    this.user_id = user_id;
  }

  public ToDoItem() {

  }

}
