package com.bnppf.kata.online_book_store;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class OnlineBookStoreApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void testDBConnection() throws SQLException {
		try (Connection connection=dataSource.getConnection()){
			assertNotNull(connection);
			log.info("Database connection established");
		}
	}


}
