package garg.bhawana;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AppTest {
    static final UUID TEST_UUID = UUID.randomUUID();
    private App cut;

    @Mock
    TaskService service;
    @Mock
    List<String> list;
    
    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.openMocks(this);
        cut = new App(service);
    }

    @Test
    public void testNoArgs() throws Exception {
        cut.run();
        verifyNoInteractions(service);
    }

    @Test
    public void testAdd() throws Exception {
        doReturn(TEST_UUID).when(service).add(any(Task.class));
        String[] args = {"add", "watch latest movie"};
        cut.run(args);
        verify(service).add(any(Task.class));
        verify(service).save();
    }

    @Test
    public void testUpdate() throws Exception {
        String[] args = {"update", TEST_UUID.toString(), "prepare for interviews"};
        doNothing().when(service).update(TEST_UUID, Optional.of(args[2]), Optional.empty());

        cut.run(args);
        verify(service).update(TEST_UUID, Optional.of(args[2]), Optional.empty());
        verify(service).save();
    }

    @Test
    public void testRemove() throws Exception {
        String[] args = {"delete", TEST_UUID.toString()};
        doNothing().when(service).remove(TEST_UUID);

        cut.run(args);
        verify(service).remove(TEST_UUID);
        verify(service).save();
    }

    @Test
    public void testMarkInProgress() throws Exception {
        String[] args = {"mark-in-progress", TEST_UUID.toString()};
        doNothing().when(service).update(TEST_UUID, Optional.empty(), Optional.of(TaskStatus.IN_PROGRESS));

        cut.run(args);
        verify(service).update(TEST_UUID, Optional.empty(), Optional.of(TaskStatus.IN_PROGRESS));
        verify(service).save();
    }

    @Test
    public void testMarkDone() throws Exception {
        String[] args = {"mark-done", TEST_UUID.toString()};
        doNothing().when(service).update(TEST_UUID, Optional.empty(), Optional.of(TaskStatus.DONE));

        cut.run(args);
        verify(service).update(TEST_UUID, Optional.empty(), Optional.of(TaskStatus.DONE));
        verify(service).save();
    }

    @Test
    public void testList() throws Exception {
        String[] args = {"list"};
        doReturn(list).when(service).list();

        cut.run(args);
        verify(service).list();
        verify(service).save();
    }

    @Test
    public void testListByTodo() throws Exception {
        String[] args = {"list", "todo"};
        doReturn(list).when(service).list(TaskStatus.TODO);

        cut.run(args);
        verify(service).list(TaskStatus.TODO);
        verify(service).save();
    }

    @Test
    public void testListByInProgress() throws Exception {
        String[] args = {"list", "in-progress"};
        doReturn(list).when(service).list(TaskStatus.IN_PROGRESS);

        cut.run(args);
        verify(service).list(TaskStatus.IN_PROGRESS);
        verify(service).save();
    }

    @Test
    public void testListByDone() throws Exception {
        String[] args = {"list", "done"};
        doReturn(list).when(service).list(TaskStatus.DONE);

        cut.run(args);
        verify(service).list(TaskStatus.DONE);
        verify(service).save();
    }

    @Test
    public void testListByUnknownTaskStatus() throws Exception {
        String[] args = {"list", "on-the-run%@$%"};
        cut.run(args);
        verify(service, times(0)).list(any(TaskStatus.class));
        verify(service).save();
    }
}
