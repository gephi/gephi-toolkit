package org.gephi.toolkit;

import org.gephi.graph.GraphGenerator;
import org.gephi.graph.api.Graph;
import org.gephi.project.api.Project;
import org.gephi.statistics.plugin.Modularity;
import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest extends ToolkitTest {

    @Test
    public void testStatistics() {
        Project project = projectController.newProject();

        // Generate a new random graph into a container
        Graph graph = GraphGenerator.build(project.getCurrentWorkspace()).generateSmallRandomGraph().getGraph();

        // Run modularity algorithm - community detection
        Modularity modularity = new Modularity();
        modularity.setRandom(true);
        modularity.setUseWeight(true);
        modularity.setResolution(0.8);
        modularity.execute(graph);

        // Assert
        Assert.assertTrue(modularity.getModularity() != 0);
        graph.getNodes().forEach(node -> Assert.assertNotNull(node.getAttribute(Modularity.MODULARITY_CLASS)));
    }
}
