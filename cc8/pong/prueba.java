import java.net.InetAddress;
 public class prueba{
	public static void main(String argv[]) throws Exception {
		System.out.println(InetAddress.getLocalHost().toString().split("/")[1]);
	}
 }