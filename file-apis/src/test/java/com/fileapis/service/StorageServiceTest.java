package com.fileapis.service;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fileapis.service.StorageService;

@SpringBootTest
public class StorageServiceTest {

	@Mock
	private AmazonS3 s3Client;

	@InjectMocks
	private StorageService storageservice;

	private String bucketName = "springbootbucket4";
	private String fileName = "skillset.txt";

	@Test
	public void testUploadFile() throws IOException {

		Long contentLength = 5L;
		String contentType = "Content";
		InputStream value = new FileInputStream(
				"C:\\Users\\manisai\\OneDrive - Wavelabs Technologies Pvt Ltd\\Documents\\skillset.txt");

		ObjectMetadata objMetadata = Mockito.mock(ObjectMetadata.class);
		objMetadata.setContentLength(contentLength);
		objMetadata.setContentType(contentType);

		when(s3Client.putObject(bucketName, fileName, value, objMetadata)).thenReturn(new PutObjectResult());
		storageservice.uploadFile(bucketName, fileName, contentLength, contentType, value);

	}

	@Test
	public void testDeleteFile() {

		doNothing().when(s3Client).deleteObject(bucketName, fileName);
		storageservice.deleteFile(bucketName, fileName);
		verify(s3Client, times(1)).deleteObject(bucketName, fileName);

	}

	@Test
	public void testListFiles() {
		List<String> keys = new ArrayList<>();
		ObjectListing object = new ObjectListing();
		when(s3Client.listObjects(bucketName)).thenReturn(object);
		when(s3Client.listNextBatchOfObjects(object)).thenReturn(object);
		
		List<String> actual = storageservice.listFiles(bucketName);
		assertEquals(keys, actual);
	}
	
	@Test
	public void testDownloadFile() throws AmazonClientException, IOException {
		
		S3Object object = new S3Object();
		when(s3Client.getObject(bucketName, fileName)).thenReturn(object);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		assertEquals(outputStream, storageservice.downloadFile(bucketName, fileName));

	}
	
	@Test
	public void testGetPreSignedUrl(){
		storageservice.getPreSignedUrl(bucketName, fileName);
	    verify(s3Client, times(1)).generatePresignedUrl(any());
	}
	
	@Test
    public void testCheckFileType() {
        MockMultipartFile file = new MockMultipartFile("skillset.txt", "skillset.txt", "text/plain",
                "skillset.txt".getBytes());
        MockMultipartFile file1 = new MockMultipartFile("Screenshot (1).png", "Screenshot (1).png", "text/plain",
                "Screenshot (1).png".getBytes());

        assertTrue(storageservice.checkFiletype(file));
        assertFalse(storageservice.checkFiletype(file1));
    }



}
