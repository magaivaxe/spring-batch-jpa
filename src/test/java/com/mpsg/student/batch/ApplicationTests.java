package com.mpsg.student.batch;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests extends Assertions {

	@Test
	void contextLoads() {
		assertThat(1).isEqualTo(1);
	}

}