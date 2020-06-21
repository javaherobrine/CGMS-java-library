package connect.websocket;
import java.net.*;
import java.net.http.*;
import java.util.concurrent.*;
public class WSConnection {
	public static WebSocket getWebSocket(String url) throws URISyntaxException, InterruptedException, ExecutionException {
		HttpClient hc=HttpClient.newHttpClient();
		return hc.newWebSocketBuilder().buildAsync(new URI(url), null).get();
	}
}
