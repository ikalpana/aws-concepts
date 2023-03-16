package com.fileapis.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class StorageService {

	@Autowired
	private AmazonS3 s3Client;

	public StorageService(AmazonS3 s3Client) {
		super();
		this.s3Client = s3Client;
	}

	public void uploadFile(final String bucketName, final String fileName, final Long contentLength,
			final String contentType, final InputStream value) throws AmazonClientException {

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);
		metadata.setContentType(contentType);

		s3Client.putObject(bucketName, fileName, value, metadata);

	}

	public ByteArrayOutputStream downloadFile(final String bucketName,final String fileName)
			throws IOException, AmazonClientException {
		S3Object s3Object = s3Client.getObject(bucketName, fileName);

		InputStream inputStream = s3Object.getObjectContent();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		int len;
		byte[] buffer = new byte[4096];
		while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
			outputStream.write(buffer, 0, len);
		}

		return outputStream;
	}

	public List<String> listFiles(final String bucketName) throws AmazonClientException {
		List<String> keys = new ArrayList<>();
		ObjectListing objectListing = s3Client.listObjects(bucketName);

		while (true) {
			List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
			if (objectSummaries.isEmpty()) {
				break;
			}

			objectSummaries.stream().filter(item -> !item.getKey().endsWith("/")).map(S3ObjectSummary::getKey)
					.forEach(keys::add);

			objectListing = s3Client.listNextBatchOfObjects(objectListing);
		}

		return keys;
	}

	public void deleteFile(final String bucketName, final String fileName) throws AmazonClientException {

		s3Client.deleteObject(bucketName, fileName);
	
	}
	
	public URL getPreSignedUrl(final String bucketName,final String fileName){
	    Date expiration = new Date();
	    long expTimeMillis = expiration.getTime();
	    expTimeMillis += 3000 * 60;
	    expiration.setTime(expTimeMillis);
	    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
	            .withMethod(HttpMethod.GET)
	            .withExpiration(expiration);
	    URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
	    return url;
	}
	
	public boolean checkFiletype(MultipartFile file) {
		String fileExtensions = ".pdf,.txt,.docx, .xlsx";
		String fileName = file.getOriginalFilename();
		int lastIndex = fileName.lastIndexOf('.');
		String substring = fileName.substring(lastIndex, fileName.length());
		return fileExtensions.contains(substring.toLowerCase());

	}

}
