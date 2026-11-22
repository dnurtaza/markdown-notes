package com.github.dnurtaza.markdown_notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MarkdownNotesApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(MarkdownNotesApplication.class, args);
	}
}