package connect.tcp;
import java.io.*;
import java.net.*;
import java.util.*;
public class Server implements Closeable {
	public static HashMap<String,Socket> sockets=new HashMap<>();
	/**
	 * ServerClientInterface[0]Ϊ��������ServerClientInterface[1]Ϊ�����
	 */
	public static HashMap<Socket,ServerClientInterface[]> threads=new HashMap<>();
	public int serverPort;
	public ServerSocket s;
	/**
	 * ����쿴�����������ˣ�
	 * @throws IOException
	 */
	public void open() throws IOException {
		this.s = new ServerSocket(this.serverPort);
	}
	/**
	 * ����ָ���Ķ˿ڴ���������
	 * @param port �˿ں�
	 */
	public Server(int port) {
		this.serverPort = port;
	}
	/**
	 * Ŷ��������������
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.s.close();
	}
	/**
	 * ʹ��ServerThread�������׽��ֲſ���ʹ�ô˷���
	 */
	public synchronized static void disconnection(String playerName) throws IOException {
		Socket soc=sockets.remove(playerName);
		ServerClientInterface[] ss=threads.remove(soc);
		soc.shutdownInput();
		soc.shutdownOutput();
		soc.close();
		ss[0].close();
		ss[1].close();
	}
}