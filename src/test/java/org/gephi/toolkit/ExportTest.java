package org.gephi.toolkit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.gephi.graph.GraphGenerator;
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.Project;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExportTest extends ToolkitTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testExport() throws IOException {
        Project project = projectController.newProject();

        // Generate tiny graph
        GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph();

        // Export
        File file = tempFolder.newFile("test.gexf");
        exportController.exportFile(file, project.getCurrentWorkspace());
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testExportToWriter() {
        Project project = projectController.newProject();

        // Generate tiny graph
        GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph();

        // Export
        Exporter exporterGraphML = exportController.getExporter("graphml");     //Get GraphML exporter
        StringWriter stringWriter = new StringWriter();
        exportController.exportWriter(stringWriter, (CharacterExporter) exporterGraphML);
        Assert.assertTrue(stringWriter.toString().length() > 0);
    }

    @Test
    public void testExportPdf() {
        Project project = projectController.newProject();

        // Generate tiny graph
        GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph().addRandomPositions();

        //PDF Exporter config and export to Byte array
        PDFExporter pdfExporter = (PDFExporter) exportController.getExporter("pdf");
        pdfExporter.setPageSize(PDRectangle.A0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exportController.exportStream(baos, pdfExporter);
        byte[] pdf = baos.toByteArray();
        Assert.assertTrue(pdf.length > 0);
    }
}
