package garg.bhawana;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AppTest {
    static final UUID TEST_UUID = UUID.randomUUID();
    App cut;
    static PrintStream out;
    static PrintStream err;

    @Mock
    TaskService ts;
    @Mock
    List<String> list;
    @Mock
    PrintStream outMock;
    @Mock
    PrintStream errMock;

    @BeforeAll
    static void mockPrintStreams() {
        out = System.out;
        err = System.err;
    }

    @AfterAll
    static void resetPrintStreams() {
        System.setOut(out);
        System.setErr(err);
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.openMocks(this);
        System.setOut(outMock);
        System.setErr(errMock);
        cut = new App(ts);
    }

    @Test
    public void testNoArgs() throws Exception {
        cut.run();
        verify(outMock).println("Missing command");
        verifyNoInteractions(ts);
    }

    @Test
    public void testAdd() throws Exception {
        String[] args = { "add", "watch latest movie" };
        doReturn(TEST_UUID).when(ts).add(args[1]);
        cut.run(args);
        verify(outMock).println(TEST_UUID);
        verify(ts).add(args[1]);
    }

    @Test
    public void testUpdate() throws Exception {
        String[] args = { "update", TEST_UUID.toString(), "prepare for interviews" };
        doNothing().when(ts).update(args[1], args[2], null);

        cut.run(args);
        verify(ts).update(args[1], args[2], null);
    }

    @Test
    public void testDelete() throws Exception {
        String[] args = { "delete", TEST_UUID.toString() };
        doNothing().when(ts).delete(args[1]);

        cut.run(args);
        verify(ts).delete(args[1]);
    }

    @Test
    public void testMarkInProgress() throws Exception {
        String[] args = { "mark-in-progress", TEST_UUID.toString() };
        doNothing().when(ts).update(args[1], null, "in-progress");

        cut.run(args);
        verify(ts).update(args[1], null, "in-progress");
    }

    @Test
    public void testMarkDone() throws Exception {
        String[] args = { "mark-done", TEST_UUID.toString() };
        doNothing().when(ts).update(args[1], null, "done");

        cut.run(args);
        verify(ts).update(args[1], null, "done");
    }

    @Test
    public void testList() throws Exception {
        String[] args = { "list" };
        doReturn(list).when(ts).list();

        cut.run(args);
        verify(ts).list();
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    public void testListByTaskStatus(TaskStatus status) throws Exception {
        String[] args = { "list", status.displayValue };
        doReturn(list).when(ts).list(args[1]);

        cut.run(args);
        verify(ts).list(args[1]);

    }

    @Test
    public void testListByUnknownTaskStatus() throws Exception {
        String[] args = { "list", "on-the-run%@$%" };
        cut.run(args);
        verify(ts).list(anyString());
    }

    @Test
    public void testUnknownCommandArgument() throws Exception {
        String[] args = { "build-me-a-robot" };
        cut.run(args);
        verify(outMock).println("Unsupported command");
        verifyNoInteractions(ts);
    }
}
