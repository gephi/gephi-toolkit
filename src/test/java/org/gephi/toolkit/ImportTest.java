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
        Assert.assertEquals(1, graphModel.getGraph().getEdge("x1").getWeight(2007), 0);
        Assert.assertEquals(4, graphModel.getGraph().getEdge("x1").getWeight(2008), 0);
    }
}
