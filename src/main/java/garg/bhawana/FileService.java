package garg.bhawana;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class FileService {
    static final TypeReference<List<Task>> TaskListType = new TypeReference<>() {
    };

    private final Path path;
    private final ObjectMapper mapper;
    private final String filename;

    public FileService(String filename) throws Exception {
        this.filename = filename;
        this.path = getOrCreateFile();
        this.mapper = new ObjectMapper();
    }

    public void read(List<Task> content) throws Exception {
        try (final InputStream in = Files.newInputStream(path)) {
            content.addAll(mapper.readValue(in, TaskListType));
        } catch (MismatchedInputException e) {
            // do nothing
        }
    }

    public void write(List<Task> content) throws Exception {
        try (final OutputStream out = Files.newOutputStream(path)) {
            mapper.writeValue(out, content);
        }
    }

    private Path getOrCreateFile() throws Exception {
        final Path path = Path.of(filename);
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-rw-rw-");
        if (!Files.exists(path)) {
            FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(permissions);
            Files.createFile(path, attribute);
        } else {
            Files.setPosixFilePermissions(path, permissions);
        }
        return path;
    }
}
