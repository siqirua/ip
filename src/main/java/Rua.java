public class Rua {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Rua(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            ui.showMessage("Load successfully. Now you have " + Integer.toString(tasks.getTasks().size()) +
                    " tasks in the list.\n");
            ui.showMessage(tasks.toString());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                tasks = c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.toString());
            } finally {
                ui.showLine();
            }
        }
    }
    public static void main(String[] args) {
        new Rua("src/main/data/tasks.txt").run();
    }
}