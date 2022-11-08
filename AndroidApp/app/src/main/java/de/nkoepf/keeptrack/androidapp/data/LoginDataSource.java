package de.nkoepf.keeptrack.androidapp.data;

import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import org.openapitools.client.ApiException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.nkoepf.keeptrack.androidapp.data.model.LoggedInUser;
import de.nkoepf.keeptrack.api.UserApi;
import de.nkoepf.keeptrack.model.LoginRequest;
import de.nkoepf.keeptrack.model.LoginResponse;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource {

    private final UserApi userApi;


    public LoginDataSource() {
        this.userApi = new UserApi();
    }

    public Result<LoggedInUser> login(String username, String password) {


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);

        try {
            LoginResponse loginResponse = userApi.login(loginRequest);
            Log.i("LoginDataSource", loginResponse.toString());

        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
        }

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