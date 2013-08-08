package cz.jiripinkas.jsitemapgenerator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	/**
	 * HTTP GET to URL, return status
	 * 
	 * @param url
	 *            URL
	 * @return status code (for example 200)
	 */
	public static int get(String url) throws Exception {

		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST either fully consume the response content or abort
		// request
		// execution by calling HttpGet#releaseConnection().

		int resultCode = 0;
		DefaultHttpClient httpclient = null;
		HttpGet httpGet = null;
		try {
			httpclient = new DefaultHttpClient();
			httpGet = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpGet);

			resultCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity);

		} catch (Exception ex) {
			throw new Exception("error sending HTTP GET to this URL: " + url);
		} finally {
			httpGet.releaseConnection();
		}
		return resultCode;
	}
}
