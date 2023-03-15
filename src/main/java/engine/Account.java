package engine;

import design.Recoverable;

public final class Account implements Recoverable {
    private final String[] s = new String[3];

    public Account(String id, String username, String password) {
        s[0] = id;
        s[1] = username;
        s[2] = password;
    }

    public Account(String snapshot) {
        recover(snapshot);
    }

    public boolean authorize(String username, String password) {
        return s[1].equals(username) && s[2].equals(password);
    }

    public String getId() {
        return s[0];
    }

    public String getUsername() {
        return s[1];
    }

    @Override
    public String snapshot() {
        /* *
         * Task:
         * Store the id, username and password in such a fashion that the
         * recover(String) method can use to restore the account.
         * */
        return null;
    }

    @Override
    public void recover(String snapshot) {
        /* *
         * Task:
         * Use the snapshot string to assign the id, username and passworld values.
         * */
    }
}
