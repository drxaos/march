import org.rapidoid.http.MediaType;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.On;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        On.address("0.0.0.0").port(3864);

        On.get("/").serve((Req req, Resp resp) -> {
            resp.contentType(MediaType.HTML_UTF_8);
            if (req.clientIpAddress().equals("127.0.0.1")) {
                return new String(convertStreamToByteArray(Server.class.getClassLoader().getResourceAsStream("main.html"), 1024));
            } else {
                return new String(convertStreamToByteArray(Server.class.getClassLoader().getResourceAsStream("no.html"), 1024));
            }
        });

        while (true) {
            Thread.sleep(1000);
        }
    }

    public static byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {

        // check to ensure that file size is not larger than Integer.MAX_VALUE.
        if (size > Integer.MAX_VALUE) {
            return new byte[0];
        }

        byte[] buffer = new byte[(int) size];
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int line = 0;
        // read bytes from stream, and store them in buffer
        while ((line = stream.read(buffer)) != -1) {
            // Writes bytes from byte array (buffer) into output stream.
            os.write(buffer, 0, line);
        }
        stream.close();
        os.flush();
        os.close();
        return os.toByteArray();
    }
}