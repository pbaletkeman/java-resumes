package ca.letkeman.resumes;

// import javax.annotation.Resource; // for Spring Boot 2
import ca.letkeman.resumes.service.FilesStorageService;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceApplication  implements CommandLineRunner {
	@Resource
	FilesStorageService storageService;

	@Value("${upload.path}")
	private String root;

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {

		storageService.init(root);
	}
}
