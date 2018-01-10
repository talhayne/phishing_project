package hit.phishingtester.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public final class HTMLUtils {
	private static final WebClient webClient = new WebClient();
	
	static {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	}
	private HTMLUtils() {

	}

	public static String checkTitle(String url) {
		String titleText = null;

		try {
			final HtmlPage webPage = webClient.getPage(url);
			titleText = webPage.getTitleText();
		} catch (Exception e) {
			System.out.println("URL " + url + " is not available");
		}

		return titleText;
	}
}
