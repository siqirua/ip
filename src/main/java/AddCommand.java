public class AddCommand implements Command{
    private final Task task;

    AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public TaskList execute(TaskList tasks, Ui ui, Storage storage) throws Exception{
        ui.showMessage(" Got it. I've added this task:\n");
        TaskList newTasks = tasks.add(task);
        ui.showMessage("    " + task + "\n");
        ui.showMessage("Now you have " + Integer.toString(tasks.getTasks().size()) +
                " tasks in the list.\n");
        storage.save(tasks);
        return newTasks;
    }
}
