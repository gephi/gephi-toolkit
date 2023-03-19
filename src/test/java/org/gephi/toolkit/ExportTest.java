/*
 * Copyright 2008-2023 Gephi
 * Website : https://gephi.org/
 *
 * This file is part of Gephi.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 3 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * https://gephi.org/developers/license/
 * or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License files at
 * /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 3, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 3] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 3 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 3 code and therefore, elected the GPL
 * Version 3 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Portions Copyrighted 2023 Gephi
 */
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
