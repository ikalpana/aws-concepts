package com.fileapis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import com.amazonaws.AmazonClientException;
import com.fileapis.common.Constants;
import com.fileapis.controller.StorageController;
import com.fileapis.service.StorageService;

@SpringBootTest
@TestPropertySource(properties = { "amazon.s3.properties.bucket-name=springbootbucket4" })
public class StorageControllerTest {

	@Mock
	private StorageService service;

	@InjectMocks
	private StorageController storageController;

	private String bucketName;

	private String fileName = "skillset.txt";

	List<String> list = new ArrayList<String>();
	
	URL url;

	@Test
	public void testUpload() throws AmazonClientException, IOException {
		MockMultipartFile file = new MockMultipartFile("filename.txt", "filename.txt", "text/plain",
				"filename.txt".getBytes());
		when(service.checkFiletype(file)).thenReturn(true);
		doNothing().when(service).uploadFile(bucketName, fileName, file.getSize(), file.getContentType(),
				file.getInputStream());
		
		
		String actual = Constants.FILE_UPLOADED_SUCCESSFULL + "\n" + url;
		when(service.getPreSignedUrl(bucketName, fileName)).thenReturn(url);

		ResponseEntity<String> response = storageController.uploadFile(file);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Constants.FILE_UPLOADED_SUCCESSFULL + "\n" + url, response.getBody());

		when(service.checkFiletype(file)).thenReturn(false);

		ResponseEntity<String> response1 = storageController.uploadFile(file);
		assertEquals(HttpStatus.NOT_ACCEPTABLE, response1.getStatusCode());
		assertEquals(Constants.FILE_UPLOADED_FAILED, response1.getBody());
	}

	@Test
	public void testDownloadFile() throws AmazonClientException, IOException {
		ByteArrayOutputStream body = new ByteArrayOutputStream();
		list.add(fileName);
		when(service.listFiles(bucketName)).thenReturn(list);
		when(service.downloadFile(bucketName, fileName)).thenReturn(body);

		ResponseEntity<?> response = storageController.downloadFile(fileName);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(body.toByteArray().getClass(), response.getBody().getClass());

		ResponseEntity<?> response1 = storageController.downloadFile("xfhchfhtf.txt");
		assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
		assertEquals(Constants.FILE_DOES_NOT_EXIST, response1.getBody());

	}

	@Test
	public void testListFiles() {
		when(service.listFiles(bucketName)).thenReturn(list);
		ResponseEntity<?> response1 = storageController.listFiles();
		assertEquals(HttpStatus.NO_CONTENT, response1.getStatusCode());
		assertEquals(Constants.NO_FILES_EXIST, response1.getBody());

		list.add(fileName);
		ResponseEntity<?> response = storageController.listFiles();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(list, response.getBody());
	}

	@Test
	public void testDeleteFile() {
		list.add(fileName);

		when(service.listFiles(bucketName)).thenReturn(list);
		doNothing().when(service).deleteFile(bucketName, fileName);

		ResponseEntity<String> response = storageController.deleteFile(fileName);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Constants.FILE_DELETED_SUCCESSFULL, response.getBody());

		ResponseEntity<String> response1 = storageController.deleteFile("jdjsd.txt");
		assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
		assertEquals(Constants.FILE_DOES_NOT_EXIST, response1.getBody());
	}

}
