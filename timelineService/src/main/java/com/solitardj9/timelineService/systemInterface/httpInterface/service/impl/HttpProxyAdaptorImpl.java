package com.solitardj9.timelineService.systemInterface.httpInterface.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.solitardj9.timelineService.systemInterface.httpInterface.model.HttpProxyAdaptorParamEnum.RestTemplateRequestFactoryMode;
import com.solitardj9.timelineService.systemInterface.httpInterface.service.HttpProxyAdaptor;

@Service("httpProxyAdaptor")
public class HttpProxyAdaptorImpl implements HttpProxyAdaptor {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpProxyAdaptorImpl.class);
	
	@Autowired
	RestTemplate restTemplate;
	
//	@Value("${httpInterface.httpProxyAdaptor.restTemplate.requestFactory.mode}")
//	private String mode;
//	
//	@Value("${httpInterface.httpProxyAdaptor.restTemplate.requestFactory.readTimeout}")
//	private Integer readTimeout;
	
	
	@Bean()
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		//
		try {
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext;
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry =
					RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf)
					.register("http", new PlainConnectionSocketFactory())
					.build();
			
			BasicHttpClientConnectionManager connectionManager = 
					new BasicHttpClientConnectionManager(socketFactoryRegistry);
			
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
					.setConnectionManager(connectionManager).build();
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			
			/*for file download*/
			//if (mode.equals(RestTemplateRequestFactoryMode.FILE.getMode())) {
			//	requestFactory.setReadTimeout(readTimeout);
			//}

			return new RestTemplate(requestFactory);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			logger.error("[HttpProxyAdaptor].restTemplate : error = " + e.toString());
		}

		return new RestTemplate();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<String> executeHttpProxy(String scheme, String url, String path, Map<String, Object> queryParams, HttpMethod method, HttpHeaders headers, String body) {
		//
		if (url != null) {
			url = scheme + "://" + url;
			UriComponentsBuilder uriComponentsBuilder = makeUriComponentsBuilder(scheme, url, path);
			
			if (queryParams != null) {
				for (Entry<String, Object> entry : queryParams.entrySet()) {
					uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
				}
			}
			
			URI uri = uriComponentsBuilder.scheme(scheme).build().encode().toUri();
			
			HttpEntity<String> requestEntity = new HttpEntity(body, headers);
			
			ResponseEntity<String> responseEntity = null;
			
			try {
				responseEntity = restTemplate.exchange(uri, method, requestEntity, String.class);
				return responseEntity;
			}
			catch (HttpClientErrorException e) {
	    		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	    	}
			catch (HttpServerErrorException e) {
	    		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	    	}
	    	catch (Exception e) {
	    		return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
		}
		return new ResponseEntity("url is null", HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public File executeHttpProxyForFile(String scheme, String url, String path, Map<String, Object> queryParams, HttpHeaders headers, String body, String fileName) {
		//
		File file = null;
		if (url != null) {
			url = scheme + "://" + url;
			UriComponentsBuilder uriComponentsBuilder = makeUriComponentsBuilder(scheme, url, path);
			
			if (queryParams != null) {
				for (Entry<String, Object> entry : queryParams.entrySet()) {
					uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
				}
			}
			
			URI uri = uriComponentsBuilder.scheme(scheme).build().encode().toUri();
			
			if (headers == null) {
				headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			}
			
			body = null;
			
			//HttpEntity<String> requestEntity = new HttpEntity(body, headers);
			
			file = (File) restTemplate.execute(uri, 
													HttpMethod.GET,
													requestCallback(headers),
													clientHttpResponse -> {
														//File ret = File.createTempFile(fileName, "tmp");
														File ret = File.createTempFile(fileName, "tmp");
														StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
														return ret;
													});
			return file;
		}
		
		return file;
	}
	
	private RequestCallback requestCallback(final HttpHeaders headers) {
	    return clientHttpRequest -> {
	        clientHttpRequest.getHeaders().addAll(headers);
	    };
	}
	
	private UriComponentsBuilder makeUriComponentsBuilder(String scheme, String url, String path) {
		//
		return UrlHostUtil.makeUriComponentsBuilder(scheme, url, path);
	}
}