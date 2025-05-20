package garg.bhawana;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class TaskService {
    private final FileService fileService;

    public TaskService(FileService fileService) throws Exception {
        this.fileService = fileService;
    }

    public List<String> list() throws Exception {
        final List<Task> content = new ArrayList<>();
        fileService.read(content);
        return content.stream()
                .map(Task::toString)
                .collect(Collectors.toList());
    }

    public List<String> list(String status) throws Exception {
        Objects.requireNonNull(status);

        final TaskStatus taskStatus = TaskStatus.from(status);
        final List<Task> content = new ArrayList<>();
        fileService.read(content);
        return content.stream()
                .filter(t -> t.getStatus().equals(taskStatus))
                .map(Task::toString)
                .collect(Collectors.toList());
    }

    public UUID add(String description) throws Exception {
        Objects.requireNonNull(description);

        final Task task = new Task(description);
        final List<Task> content = new ArrayList<>();
        fileService.read(content);
        content.add(task);
        fileService.write(content);
        return task.getId();
    }

    public void delete(String id) throws Exception {
        Objects.requireNonNull(id);

        final UUID uuid = UUID.fromString(id);
        final List<Task> content = new ArrayList<>();
        fileService.read(content);
        content.removeIf(t -> t.getId().equals(uuid));
        fileService.write(content);
    }

    public void update(String id, String description, String status) throws Exception {
        Objects.requireNonNull(id);
        if (description == null && status == null)
            return;

        final UUID uuid = UUID.fromString(id);
        final List<Task> content = new ArrayList<>();
        fileService.read(content);
        content.stream()
                .filter(t -> t.getId().equals(uuid))
                .forEach(t -> {
                    if (description != null)
                        t.setDescription(description);
                    if (status != null)
                        t.setStatus(TaskStatus.from(status));
                    t.setUpdatedAt(System.currentTimeMillis());
                });
        fileService.write(content);
    }
}
