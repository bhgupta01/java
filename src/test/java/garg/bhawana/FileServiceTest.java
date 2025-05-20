package garg.bhawana;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileServiceTest {
    FileService cut;
    @BeforeEach
    public void init() throws Exception {
        cut = new FileService(App.JSON_FILE);
    }

    @Test
    public void testWriteAndRead() throws Exception {
        List<Task> content = new ArrayList<>();
        content.add(new Task("dummy task"));
        cut.write(content);
        content.clear();
        cut.read(content);
        Assertions.assertTrue(content.size() > 0);
    }
}
