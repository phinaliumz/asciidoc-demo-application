package org.samikallio.asciidocdemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;




@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class AsciiDocDemoApplication {

	@RequestMapping("/pdf-demo")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello, world!");
		System.out.println("Request mapping");
		return model;

	}

	@GetMapping(path = { "/pdf-demo/{input}" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] getPdf(@PathVariable("input") String input) throws IOException, DocumentException {
		System.out.println("Parameter is -> " + input);
		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put("input", input);
		File tmpFile = File.createTempFile("asciidoc-demo-pdf", ".xhtml");
		Writer writer = new FileWriter(tmpFile);
		JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/demo-pdf-template.twig");
		JtwigModel model = JtwigModel.newModel().with("input", input);

		StringReader stringReader = new StringReader(template.render(model));
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		Options options = new Options();
		options.setBackend("xhtml5");
		options.setHeaderFooter(true);
		asciidoctor.convert(stringReader, writer, options);
		writer.close();
		writer = null;
		ITextRenderer pdfRenderer = new ITextRenderer();
		pdfRenderer.setDocument(tmpFile);
		pdfRenderer.layout();
		ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
		pdfRenderer.createPDF(bOutputStream);
		bOutputStream.close();

		return bOutputStream.toByteArray();
	}

	public static void main(String[] args) {
		SpringApplication.run(AsciiDocDemoApplication.class, args);
	}
}
