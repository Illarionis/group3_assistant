package engine;

public final class Account {
    private final String id, username, password;

    public Account(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static Account accountOf(String s) {
        final String[] segments = s.split("\n");
        return new Account(segments[0], segments[1], segments[2]);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return id + "\n" + username + "\n" + password;
    }
}
