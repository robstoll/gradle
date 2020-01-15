import java.time.format.*;
import java.time.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;

public class DumpOpenFiles {
    public static void main(String[] args) throws Exception {
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(ZonedDateTime.now());
        String dumpFile = new File(time + ".filehandles").getAbsolutePath().replace('\\', '/');
        String port = new String(Files.readAllBytes(new File("build/port.txt").toPath())).trim();
        try (BufferedReader response = new BufferedReader(new InputStreamReader(new URL("http://localhost:" + port + "/" + dumpFile).openStream()))) {
            String line = null;
            while ((line = response.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
