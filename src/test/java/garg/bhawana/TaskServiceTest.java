package garg.bhawana;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TaskServiceTest {
    TaskService cut;
    FileService fileService;
    UUID id;
    

    @BeforeEach
    public void init() throws Exception {
        fileService = new FileService(App.JSON_FILE);
        cut = new TaskService(fileService);
        // add a test task
        id = cut.add("task1");
    }

    @Test
    public void testAdd() throws Exception {
        List<String> expected = List.of(String.format("[%s] todo task1", id.toString()));
        List<String> actual = cut.list();
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    public void testUpdateStatus(TaskStatus status) throws Exception {
        cut.update(id.toString(), null, status.displayValue);
        List<String> actual = cut.list(status.displayValue);
        actual.forEach(s -> Assertions.assertTrue(s.contains(status.displayValue)));
    }

    @Test
    public void testUpdateDescription() throws Exception {
        cut.update(id.toString(), "task2", TaskStatus.IN_PROGRESS.displayValue);
        List<String> expected = List.of(String.format("[%s] in-progress task2", id));

        List<String> actual = cut.list();
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testDelete() throws Exception {
        cut.delete(id.toString());
        List<String> expected = List.of(String.format("[%s] todo task1", id.toString()));
        List<String> actual = cut.list();
        Assertions.assertFalse(actual.containsAll(expected));
    }
}
