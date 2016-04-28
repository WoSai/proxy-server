package com.wosai.upay.proxy.upay.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.util.Digest;

/**
 * 新增文raw方式上传接口
 * @author qi
 *
 */
@Component
public class RawUpayHttpClient extends UpayHttpClient {
	
	private static final int BUFFER=4*1024;
	
	private RestTemplate restTemplate;

	@Autowired
	public RawUpayHttpClient(RestTemplate restTemplate,
			ObjectMapper objectMapper) {
		super(restTemplate, objectMapper);
		this.restTemplate=restTemplate;
	}

	/**
	 * raw方式提交body数据
	 * @param principal
	 * @param secretKey
	 * @param url
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> call(String principal, String secretKey, String url, String body) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] raw=body.getBytes();

        baos.write(raw);
        baos.write(secretKey.getBytes());
        String signature = Digest.md5(baos.toByteArray());
        try {
            return restTemplate.execute(url, HttpMethod.POST,
                                        new SignRequestCallback(raw, principal+" "+signature),null);
        }catch(RestClientException ex) {
            throw new IOException("rest client i/o error", ex);
        }
    }

	/**
	 * 上传附件
	 * @param principal
	 * @param secretKey
	 * @param url
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> call(String principal, String secretKey, String url, File body) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(body));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte data[] = new byte[BUFFER];
        while ((bis.read(data, 0, BUFFER)) != -1) {
            baos.write(secretKey.getBytes());   
        }
        byte[] raw=Arrays.copyOf(baos.toByteArray(), baos.size());
        
        baos.write(secretKey.getBytes());
        String signature = Digest.md5(baos.toByteArray());
        try {
            return restTemplate.execute(url, HttpMethod.POST,
                                        new SignRequestCallback(raw, principal+" "+signature),null);
        }catch(RestClientException ex) {
            throw new IOException("rest client i/o error", ex);
        }
    }
}
