import java.util.Optional;
import java.util.UUID;

public class App {
    private final TaskService service;

    public App(TaskService service) throws Exception {
        this.service = service;
    }

    public void run(String... args) throws Exception {
        if (args.length == 0)
            return;
        try {
            switch (args[0]) {
            case "add":
                service.add(new Task(args[1]));
                break;
            case "update":
                service.update(UUID.fromString(args[1]), Optional.of(args[2]), Optional.empty());
                break;
            case "delete":
                service.remove(UUID.fromString(args[1]));
                break;
            case "mark-in-progress":
                service.update(UUID.fromString(args[1]), Optional.empty(), Optional.of(TaskStatus.IN_PROGRESS));
                break;
            case "mark-done":
                service.update(UUID.fromString(args[1]), Optional.empty(), Optional.of(TaskStatus.DONE));
                break;
            case "list":
                if (args.length > 1)
                    service.list(TaskStatus.from(args[1])).forEach(System.out::println);
                else
                    service.list().forEach(System.out::println);
                break;
            default:
                System.out.println("Unsupported input type!!!");
                break;
            }
        } catch (EnumConstantNotPresentException e) {
            System.out.println("Invalid task status! Allowed values are " + TaskStatus.displayValues());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.save();
        }
    }

    public static void main(String[] args) throws Exception {
        new App(
            new TaskService("tasks.json")
        ).run(args);
    }
}