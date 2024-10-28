package codefun.load_test.logic.user;

public interface IUserListener
{
    default void onUserStart(User user) {}

    /**
     * User stop
     * @param user can be null
     */
    default void onUserStop(User user) {}
}