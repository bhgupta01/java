import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

public class AppTest {
    String filename = "tests.json";

    @Test
    public void testAddSaveList() throws Exception {
        TaskService service = mock();
        App cut = new App(service);
        cut.run();
        verifyNoInteractions(service);
    }
}
