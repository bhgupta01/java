package garg.bhawana;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TaskServiceTest {
    String filename = "src/test/resources/tests.json";

    @Test
    public void testAddSaveList() throws Exception {
        final TaskService cut = new TaskService(filename);
        Task task1 = new Task("task1");
        List<String> expected = List.of(task1.toString());
        cut.add(task1);
        cut.save();
        final List<String> actual = cut.list();
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testUpdate() throws Exception {
        final TaskService cut = new TaskService(filename);
        Task task1 = new Task("task1");
        cut.add(task1);
        cut.save();

        cut.update(task1.getId(), Optional.of("task2"), Optional.of(TaskStatus.IN_PROGRESS));
        cut.save();
        List<String> expected = List.of(String.format("[%s] in-progress task2", task1.getId()));

        final List<String> actual = cut.list();
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testRemove() throws Exception {
        final TaskService cut = new TaskService(filename);
        Task task1 = new Task("task1");
        cut.add(task1);
        cut.save();

        cut.remove(task1.getId());
        cut.save();
        List<String> expected = List.of(String.format("[%s] todo task1", task1.getId()));

        final List<String> actual = cut.list();
        Assertions.assertFalse(actual.containsAll(expected));
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    public void testListByStatus(TaskStatus status) throws Exception {
        final TaskService cut = new TaskService(filename);
        Task task1 = new Task("task1");
        task1.setStatus(status);
        cut.add(task1);
        cut.save();
        final List<String> actual = cut.list(status);
        actual.forEach(s -> Assertions.assertTrue(s.contains(status.displayValue)));
    }
}
