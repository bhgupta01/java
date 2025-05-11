import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TaskService {
    private static final TypeReference<List<Task>> TaskListType = new TypeReference<>() {
    };

    private final Path path;
    private final ObjectMapper mapper;
    private final List<Task> content;
    private final String filename;

    public TaskService(String filename) throws Exception {
        this.filename = filename;
        this.path = getOrCreateFile();
        this.mapper = new ObjectMapper();
        this.content = new ArrayList<>();
        deserialize();
    }

    public List<String> list() {
        return content.stream().map(Task::toString).collect(Collectors.toList());
    }

    public List<String> list(TaskStatus status) {
        return content.stream().filter(t -> t.getStatus().equals(status)).map(Task::toString)
                .collect(Collectors.toList());
    }

    public UUID add(Task task) {
        Objects.requireNonNull(task);
        content.add(task);
        return task.getId();
    }

    public void remove(UUID id) {
        Objects.requireNonNull(id);
        content.removeIf(t -> t.getId().equals(id));
    }

    public void update(UUID id, Optional<String> description, Optional<TaskStatus> status) {
        Objects.requireNonNull(id);
        content.stream().filter(t -> t.getId().equals(id)).forEach(t -> {
            if (description.isPresent())
                t.setDescription(description.get());
            if (status.isPresent())
                t.setStatus(status.get());
            t.setUpdatedAt(System.currentTimeMillis());
        });
    }

    public void save() throws Exception {
        try (final OutputStream out = Files.newOutputStream(path)) {
            mapper.writeValue(out, content);
        }
    }

    private Path getOrCreateFile() throws Exception {
        final Path path = Path.of(filename);
        if (!Files.exists(path))
            Files.createFile(path);
        return path;
    }

    private void deserialize() throws Exception {
        try (final InputStream in = Files.newInputStream(path)) {
            final List<Task> existing = mapper.readValue(in, TaskListType);
            content.addAll(existing);
        } catch(MismatchedInputException e) {
            // silently handle this
        }
    }
}
