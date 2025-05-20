package garg.bhawana;

public class App {
    public static final String JSON_FILE = "../../tasks.json";

    private final TaskService service;

    public App(TaskService service) throws Exception {
        this.service = service;
    }

    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Missing command");
            return;
        }
        try {
            switch (args[0]) {
                case "add":
                    System.out.println(service.add(args[1]));
                    break;
                case "update":
                    service.update(args[1], args[2], null);
                    break;
                case "delete":
                    service.delete(args[1]);
                    break;
                case "mark-in-progress":
                    service.update(args[1], null, "in-progress");
                    break;
                case "mark-done":
                    service.update(args[1], null, "done");
                    break;
                case "list":
                    if (args.length > 1)
                        service.list(args[1]).forEach(System.out::println);
                    else
                        service.list().forEach(System.out::println);
                    break;
                default:
                    System.out.println("Unsupported command");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        final FileService fs = new FileService(JSON_FILE);
        final TaskService ts = new TaskService(fs);
        new App(ts).run(args);
    }
}