package org.gephi.toolkit;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.processor.plugin.DefaultProcessor;
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
}
