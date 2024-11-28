package codefun.load_test.logic.user;

public interface IUserManager {
    IUser createUser();
    void destroyUser(IUser user);
    void destroyAllUsers();
}
