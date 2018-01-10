package hit.phishingtester.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public final class HostUtils {
	
	private HostUtils() {
		
	}
	
	/**
	 * Looks up the IP address of a host (DNS resolving)
	 * @param host to resolve
	 * @return IP address of the host
	 * @throws IOException 
	 */
	public static String getIPAddressOfHost(String host) throws IOException {
		InetAddress address = InetAddress.getByName(host);
		if(address.isReachable(2000)) {
			return address.getHostAddress();	
		}
		return null;
	}
	
	public static boolean checkHttpAvailability(String host) {
		try(Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, 80), 1500);
			return true;
		} catch(IOException e) {
			return false;
		}
	}
	
	public static boolean checkHttpsAvailability(String host) {
		try(Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, 443), 1500);
			return true;
		} catch(IOException e) {
			return false;
		}
	}
	
	
	
	/**
	 * Extracts the full host from URL (removes protocol for example)
	 * @param url
	 * @return full host
	 * @throws URISyntaxException
	 */
	public static String extractFQDN(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String host = uri.getHost();
		
		return host;
	}
	
	public static String extractHostWithNoSubDomain(String url) {
		String host = (url.split("\\."))[0];
		
		return host;
	}
	/**
	 * Extracts the domain name from address that starts with "www"
	 * For instance, google.co.il from www.google.co.il
	 * @param hostWithWWW
	 * @return host without www. at beginning
	 */
	public static String extractDomain(String hostWithWWW) {
		String domain = hostWithWWW.startsWith("www.") ? hostWithWWW.substring(4) : hostWithWWW;
		
		return domain;
	}
}
