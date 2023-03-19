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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.io.processor.plugin.MergeProcessor;
import org.gephi.project.api.Project;
import org.junit.Assert;
import org.junit.Test;

public class ImportTest extends ToolkitTest {

    @Test
    public void testImport() throws IOException, URISyntaxException {
        Project project = projectController.newProject();

        //Import file
        File file = new File(getClass().getResource("/org/gephi/toolkit/lesmiserables.gml").toURI());
        Container container = importController.importFile(file);
        container.getLoader().setEdgeDefault(EdgeDirectionDefault.DIRECTED);   //Force DIRECTED
        container.getLoader().setAllowAutoNode(false);  //Don't create missing nodes

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), project.getCurrentWorkspace());

        //Assert
        GraphModel graphModel = graphController.getGraphModel(project.getCurrentWorkspace());
        Assert.assertEquals(77, graphModel.getGraph().getNodeCount());
        Assert.assertEquals(254, graphModel.getGraph().getEdgeCount());
    }

    @Test
    public void testImportSlices() throws IOException, URISyntaxException {
        Project project = projectController.newProject();

        Container[] containers = new Container[3];
        for (int i = 0; i < 3; i++) {
            File file = new File(getClass().getResource("/org/gephi/toolkit/timeframe" + (i + 1) + ".gexf").toURI());
            containers[i] = importController.importFile(file);
        }

        // Process the container using the MergeProcessor
        importController.process(containers, new MergeProcessor(), project.getCurrentWorkspace());

        // Assert proper merging
        GraphModel graphModel = graphController.getGraphModel(project.getCurrentWorkspace());
        Assert.assertEquals(4, graphModel.getGraph().getNodeCount());
        Assert.assertEquals(4, graphModel.getGraph().getEdgeCount());

        // Assert proper timestamps
        Assert.assertArrayEquals(new double[] {2007, 2008, 2009}, graphModel.getGraph().getNode("1").getTimestamps(),
            0);
        Assert.assertArrayEquals(new double[] {2008, 2009}, graphModel.getGraph().getNode("4").getTimestamps(), 0);
        Assert.assertArrayEquals(new double[] {2007, 2008}, graphModel.getGraph().getEdge("x1").getTimestamps(), 0);

        // Assert proper attributes
        Assert.assertEquals(12, graphModel.getGraph().getNode("1").getAttribute("price", 2007));
        Assert.assertEquals(8, graphModel.getGraph().getNode("4").getAttribute("price", 2008));
//        Assert.assertEquals(1, graphModel.getGraph().getEdge("x1").getWeight(2007), 0);
//        Assert.assertEquals(4, graphModel.getGraph().getEdge("x1").getWeight(2008), 0);
    }
}
