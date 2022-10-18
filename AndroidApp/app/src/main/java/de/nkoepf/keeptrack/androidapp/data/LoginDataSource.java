package de.nkoepf.keeptrack.androidapp.data;

import android.security.keystore.UserNotAuthenticatedException;

import java.io.IOException;

import de.nkoepf.keeptrack.androidapp.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            if (username.equals("testUser") && password.equals("pass123")) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "testUser");
                return new Result.Success<>(fakeUser);
            } else {
                return new Result.Error(new UserNotAuthenticatedException("Invalid user credentials"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}