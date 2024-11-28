package codefun.load_test.logic.user;

public interface IUserListener
{
    default void onUserConnected(IUser user) {}
    default void onUserDisconnected(IUser user) {}
}