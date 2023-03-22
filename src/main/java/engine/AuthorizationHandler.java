package engine;

public interface AuthorizationHandler {
    boolean handle(String username, String password);
}
