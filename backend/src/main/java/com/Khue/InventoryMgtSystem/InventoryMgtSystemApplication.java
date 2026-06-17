package com.Khue.InventoryMgtSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class InventoryMgtSystemApplication { 	

	static {
		// Fix: Tomcat 11 giới hạn số lượng phần (parts) trong multipart request.
		// Form sản phẩm có 10+ trường + 1 ảnh → vượt giới hạn mặc định.
		System.setProperty("org.apache.tomcat.util.http.Parameters.MAX_COUNT", "10000");
	}

	@PostConstruct
	public void init() {
		// Set timezone to Vietnam time for the entire system
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryMgtSystemApplication.class, args);
	}

}
