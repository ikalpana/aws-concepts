package com.fileapis.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import static java.net.HttpURLConnection.HTTP_OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fileapis.common.Constants;
import com.fileapis.service.StorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/s3bucketstorage")
public class StorageController {

	@Value("${amazon.s3.properties.bucket-name}")
	private String bucketName;

	@Autowired
	private StorageService service;

	public StorageController(StorageService service) {
		super();
		this.service = service;
	}

	@RequestMapping(value = "/",method = RequestMethod.GET)
	@ApiOperation(value = "List Files")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ResponseEntity.class, message = "Files fetched successfully"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The bucket does not exist"),
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, response = ResponseEntity.class, message = "No files Found in the bucket !") })
	public ResponseEntity<?> listFiles() {

		List<String> body = service.listFiles(bucketName);
		if (!body.isEmpty()) {
			log.info("Files found in bucket({}): {}", bucketName, body);
			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		log.info("Files not found in the bucket({}): {}", bucketName);
		return new ResponseEntity<>(Constants.NO_FILES_EXIST, HttpStatus.NO_CONTENT);
	}

	@PostMapping("/upload")
	@ApiOperation(value = "Upload Files")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ResponseEntity.class, message = "File uploaded successfully"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The bucket does not exist"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_ACCEPTABLE, response = ResponseEntity.class, message = "File upload failed please check the file extention") })
	@SneakyThrows(IOException.class)
	public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {

		String fileName = file.getOriginalFilename();
		if (service.checkFiletype(file)) {
			service.uploadFile(bucketName, fileName, file.getSize(), file.getContentType(), file.getInputStream());
			URL url = service.getPreSignedUrl(bucketName, fileName);
			log.info("File uploaded to bucket({}): {}", bucketName, fileName);
			return new ResponseEntity<>(Constants.FILE_UPLOADED_SUCCESSFULL+"\n"+url, HttpStatus.OK);

		}
		log.info("Files upload failed to the bucket({}): {}", bucketName, fileName);
		return new ResponseEntity<>(Constants.FILE_UPLOADED_FAILED, HttpStatus.NOT_ACCEPTABLE);
	}

	@SneakyThrows
	@ApiOperation(value = "Download Files")
	@GetMapping("/download/{fileName}")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ResponseEntity.class, message = "File downloaded successfully."),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The bucket does not exist"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The given file does not exist !") })
	public ResponseEntity<?> downloadFile(@PathVariable("fileName") String fileName) {
		List<String> files = service.listFiles(bucketName);
		
		if (files.contains(fileName)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=" + fileName);
			ByteArrayOutputStream body = service.downloadFile(bucketName, fileName);
			log.info("File downloaded from bucket({}): {}", bucketName, fileName);
			return ResponseEntity.status(HTTP_OK).headers(headers).body(body.toByteArray());
		}
		log.info("The given file does not exist in the bucket({}): {}", bucketName, fileName);
		return new ResponseEntity<>(Constants.FILE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/delete/{fileName}")
	@ApiOperation(value = "Delete Files")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ResponseEntity.class, message = "File deleted successfully."),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The bucket does not exist"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = ResponseEntity.class, message = "The given file does not exist !") })
	public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName) {
		if (service.listFiles(bucketName).contains(fileName)) {
			service.deleteFile(bucketName, fileName);
			log.info("File deleted from bucket({}): {}", bucketName, fileName);
			return new ResponseEntity<>(Constants.FILE_DELETED_SUCCESSFULL, HttpStatus.OK);

		}
		log.info("The given file does not exist in the bucket({}): {}", bucketName, fileName);
		return new ResponseEntity<>(Constants.FILE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
	}
	
	

}
