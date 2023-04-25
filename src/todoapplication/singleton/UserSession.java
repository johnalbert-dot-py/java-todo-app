package todoapplication.singleton;

public class UserSession {

  public static int userId;

  public static void setUserId(int id) {
    userId = id;
  }

  public static int getUserId() {
    return userId;
  }

  public static void destroy() {
    userId = 0;
  }

}
