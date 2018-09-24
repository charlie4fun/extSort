import com.extSort.Main;
import com.extSort.Producer;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

public class main {
    @Test
    public void testConcatenate() {
        Properties props = Main.getSettings();

        String unsortedFileLocation = props.getProperty("workingDirectory") + props.getProperty("unsortedFileName");

        Producer gen = new Producer(unsortedFileLocation, props.getProperty("dataType"));
        gen.generate(32L);

        File file = new File(unsortedFileLocation);

        file.delete();
    }
}
