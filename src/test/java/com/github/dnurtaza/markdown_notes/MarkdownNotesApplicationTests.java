package com.github.dnurtaza.markdown_notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class MarkdownNotesApplicationTests {

	@Test
	void verifyModulesAndWriteDocs() {
		var modules = ApplicationModules.of(MarkdownNotesApplication.class).verify();

		new Documenter(modules)
				.writeModulesAsPlantUml()
				.writeIndividualModulesAsPlantUml();
	}

}
