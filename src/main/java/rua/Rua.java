package rua;

import java.time.format.DateTimeParseException;

import rua.command.Command;
import rua.common.Parser;
import rua.common.Storage;
import rua.common.StringLogger;
import rua.common.Ui;
import rua.task.TaskList;


/**
 * A chatbot that helps manage tasks.
 */
public class Rua {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Initialises the chatbot.
     */
    public Rua() {
        ui = new Ui();
        storage = new Storage("./data/");
        try {
            tasks = new TaskList(storage.load());
            ui.showMessage("Load successfully. Now you have " + tasks.getTasks().size()
                    + " tasks in the list.\n");
            ui.showMessage(tasks.toString());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Constructs a chatbot Rua object.
     *
     * @param filePath The path to the file which stores the tasks.
     */
    public Rua(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            final String loadSuccessfulMessage = "Load successfully. Now you have " + tasks.getTasks().size()
                    + " tasks in the list.\n";
            ui.showMessage(loadSuccessfulMessage);
            ui.showMessage(tasks.toString());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public static void main(String[] args) {
        new Rua("src/main/data/tasks.txt").run();
    }

    /**
     * Runs the chatbot.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                ui.showLine(); // show the divider line ("_______")
                tasks = c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.toString());
            } finally {
                ui.showLine();
            }
        }
    }

    public Ui getUi() {
        return ui;
    }

    /**
     * Gets response of the chatbot from the String Logger
     *
     * @param input The string input by the user.
     * @return A string represents the response from the chatbot.
     */
    public String getResponse(String input) {
        StringLogger.clear();
        try {
            Command c = Parser.parse(input);
            tasks = c.execute(tasks, ui, storage);
        } catch (DateTimeParseException e) {
            ui.showParsingError();
        } catch (Exception e) {
            ui.showError(e.toString());
        }
        return StringLogger.getLog();
    }
}
