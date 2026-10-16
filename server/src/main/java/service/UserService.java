package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService (UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (registerRequest == null) {
            throw new BadRequestException("Error: bad request");
        }
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (username == null || password == null || email == null || username.isBlank() || password.isBlank() || email.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(username);
        if (user != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        String authToken = generateToken();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedException, BadRequestException, DataAccessException {
        if (loginRequest == null) {
            throw new BadRequestException("Error: bad request");
        }
        String username = loginRequest.username();
        String password = loginRequest.password();
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(username);
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String userPassword = user.password();
        if (!Objects.equals(userPassword, password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = generateToken();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);

        return new LoginResult(username, authToken);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (logoutRequest == null) {
            throw new BadRequestException("Error: bad request");
        }
        String authToken = logoutRequest.authToken();
        if (authToken == null || authToken.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuth(authToken);
    }
}
