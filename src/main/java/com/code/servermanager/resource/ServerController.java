package com.code.servermanager.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import com.code.servermanager.enumeration.Status;
import com.code.servermanager.model.Response;
import com.code.servermanager.model.Server;
import com.code.servermanager.service.implementation.ServerServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {
	private final ServerServiceImpl service;

	@GetMapping("/list")
	public ResponseEntity<Response> getServers() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		return ResponseEntity.ok(
				Response.builder()
						.timeStamp(LocalDateTime.now())
						.data(Map.of("servers", service.list(30)))
						.message("Servers retrieved")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build());
	}

	@GetMapping("/ping/{ipAddress}")
	public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
		Server server = service.ping(ipAddress);
		return ResponseEntity.ok(
				Response.builder()
						.timeStamp(LocalDateTime.now())
						.data(Map.of("server", server))
						.message(server.getStatus() == Status.SERVER_UP ? "Ping success"
								: "Ping failed")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build());
	}

	@PostMapping("/save")
	public ResponseEntity<Response> saveServer(@Valid @RequestBody Server server) {
		return ResponseEntity.ok(
				Response.builder()
						.timeStamp(LocalDateTime.now())
						.data(Map.of("server", service.create(server)))
						.message("Server created")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
		return ResponseEntity.ok(
				Response.builder()
						.timeStamp(LocalDateTime.now())
						.data(Map.of("server", service.get(id)))
						.message("Server retrieved")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
		return ResponseEntity.ok(
				Response.builder()
						.timeStamp(LocalDateTime.now())
						.data(Map.of("deleted", service.delete(id)))
						.message("Server deleted")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build());
	}

	@GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getServerImage(@PathVariable("filename") String fileName) throws IOException {
		return Files.readAllBytes(Paths.get(".\\images\\" + fileName));
	}
}
