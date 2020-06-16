package connect.http;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.*;
import java.net.*;
public class HttpSocket {
	public static final HttpClient DEFAULT_CLIENT=HttpClient.newHttpClient();
	public static byte[] get(String url,int timeout,String... header) throws IOException, InterruptedException {
		HttpRequest request=HttpRequest.newBuilder().headers(header).GET().expectContinue(true).timeout(Duration.ofMillis(timeout)).build();
		return readHttp(DEFAULT_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream()));
	}
	private static byte[] readHttp(HttpResponse<InputStream> response) throws IOException {
		HttpHeaders headers=response.headers();
		Map<String,List<String>> map=headers.map();
		String encode=map.get("Content-Encoding").get(0);
		byte[] temp;
		InputStream is=response.body();
		switch(encode) {
		case "br":
		case "identity":
			temp=is.readAllBytes();
			break;
		case "gzip":
			GZIPInputStream gis=new GZIPInputStream(is);
			temp=gis.readAllBytes();
			break;
		case "deflate":
			InflaterInputStream dis=new InflaterInputStream(is);
			temp=dis.readAllBytes();
			break;
		default:
			temp=is.readAllBytes();	
			break;
		}
		is.close();
		return temp;
	}
	public static byte[] post(String url,String data,int timeout,String...header) throws IOException, InterruptedException {
		HttpRequest request=HttpRequest.newBuilder().headers(header).POST(HttpRequest.BodyPublishers.ofString(data)).expectContinue(true).timeout(Duration.ofMillis(timeout)).build();
		return readHttp(DEFAULT_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream()));
	}
	public static byte[] postASync(Taskable task,String url,String data,int timeout,String... header) throws Exception {
		HttpRequest request=HttpRequest.newBuilder().headers(header).POST(HttpRequest.BodyPublishers.ofString(data)).expectContinue(true).timeout(Duration.ofMillis(timeout)).build();
		CompletableFuture<HttpResponse<InputStream>> httpTask=DEFAULT_CLIENT.sendAsync(request, BodyHandlers.ofInputStream());
		task.task();
		return readHttp(httpTask.get());
	}
}
