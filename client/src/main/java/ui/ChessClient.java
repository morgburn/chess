package ui;

import chess.ChessGame;
import client.ResponseException;
import client.ServerFacade;
import model.GameData;

import java.util.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private List<GameData> listedGames = new ArrayList<>();

    public ChessClient(int port) {
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. ♕");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print("Something went wrong. Please try again.");
            }
        }
        System.out.print(RESET_TEXT_COLOR);
        System.out.println();
    }

    private String eval(String input) {
        String[] tokens = input.split(" +");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase(Locale.ROOT) : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + "] >>> " + SET_TEXT_COLOR_GREEN);
    }

    private String register(String... params) {
        if (state == State.LOGGED_IN) {
            return "You are logged in. Logout to perform this command.";
        }
        if (params.length == 3) {
            try {
                server.register(params[0], params[1], params[2]);
                state = State.LOGGED_IN;
                return "Registered and logged in as " + params[0];
            }
            catch (ResponseException exception) {
                if (exception.statusCode() == 403) {
                    return "Username already taken.";
                } else {
                    return "Something went wrong. Try again.";
                }
            }
        }
        return "Expected: <USERNAME> <PASSWORD> <EMAIL>";
    }

    private String login(String... params) {
        if (state == State.LOGGED_IN) {
            return "You are logged in. Logout to perform this command.";
        }
        if (params.length == 2) {
            try {
                server.login(params[0], params[1]);
                state = State.LOGGED_IN;
                return "Logged in as " + params[0] + "\n" + help();
            }
            catch (ResponseException exception) {
                if (exception.statusCode() == 401) {
                    return "Wrong username or password";
                } else {
                    return "Something went wrong. Try again.";
                }
            }
        }
        return "Expected: <USERNAME> <PASSWORD>";
    }

    private String createGame(String... params) {
        if (state == State.LOGGED_OUT) {
            return "You must log in.";
        }
        if (params.length == 1) {
            try {
                server.createGame(params[0]);
                return "Created game: " + params[0];
            }
            catch (ResponseException exception) {
                return "Something went wrong. Try again.";
            }
        }
        return "Expected: <NAME>";
    }

    private String listGames() {
        if (state == State.LOGGED_OUT) {
            return "You must log in.";
        }
        try {
            Collection<GameData> games = server.listGames();
            listedGames = new ArrayList<>(games);
            if (games.isEmpty()) {
                return "No games have been created.";
            }
            var result = new StringBuilder();
            int gameNumber = 1;
            for (GameData game : listedGames) {
                result.append(gameNumber).append(". Game Name: ").append(game.gameName())
                        .append(" | White Player: ").append(usernameDisplay(game.whiteUsername()))
                        .append(" | Black Player: ").append(usernameDisplay(game.blackUsername()))
                        .append("\n");
                gameNumber++;
            }
            return result.toString();
        } catch (ResponseException exception) {
            return "Something went wrong. Try again.";
        }
    }

    private String usernameDisplay(String username) {
        return Objects.requireNonNullElse(username, "Open");
    }

    private String joinGame(String... params) {
        if (state == State.LOGGED_OUT) {
            return "You must log in.";
        }
        if (params.length == 2) {
            try {
                ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                int gameNumber = Integer.parseInt(params[0]);

                if (gameNumber < 1 || gameNumber > listedGames.size()) {
                    return "Invalid game ID number.";
                }

                int gameID = listedGames.get(gameNumber - 1).gameID();
                server.joinGame(color, gameID);
                return "Joined game as " + params[1];
            } catch (IllegalArgumentException ex) {
                return "Please provide a valid game ID and chess color.";
            } catch (ResponseException ex) {
                if (ex.statusCode() == 403) {
                    return "Color is already taken.";
                } else {
                    return "Something went wrong. Try again.";
                }
            }
        }
        return "Expected: <ID> [WHITE|BLACK]";
    }

    private String observeGame(String... params) {
        return "Not implemented yet.";
    }

    private String logout(String... params) {
        return "Not implemented yet.";
    }

    private String help() {
        if (state == State.LOGGED_OUT) {
            return SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>"
                    + SET_TEXT_COLOR_MAGENTA + " - create an account\n"
                    + SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>"
                    + SET_TEXT_COLOR_MAGENTA + " - play chess\n"
                    + SET_TEXT_COLOR_BLUE + "quit"
                    + SET_TEXT_COLOR_MAGENTA + " - exit\n"
                    + SET_TEXT_COLOR_BLUE + "help"
                    + SET_TEXT_COLOR_MAGENTA + " - display possible command options\n";
        }
        return SET_TEXT_COLOR_BLUE + "create <NAME>"
                + SET_TEXT_COLOR_MAGENTA + " - create a new game\n"
                + SET_TEXT_COLOR_BLUE + "list"
                + SET_TEXT_COLOR_MAGENTA + " - list all games\n"
                + SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]"
                + SET_TEXT_COLOR_MAGENTA + " - join a game\n"
                + SET_TEXT_COLOR_BLUE + "observe <ID>"
                + SET_TEXT_COLOR_MAGENTA + " - observe a game\n"
                + SET_TEXT_COLOR_BLUE + "logout"
                + SET_TEXT_COLOR_MAGENTA + " - stop playing chess\n"
                + SET_TEXT_COLOR_BLUE + "quit"
                + SET_TEXT_COLOR_MAGENTA + " - exit\n"
                + SET_TEXT_COLOR_BLUE + "help"
                + SET_TEXT_COLOR_MAGENTA + " - display possible command options\n";
    }
}
