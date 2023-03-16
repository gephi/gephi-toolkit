package org.gephi.toolkit;

import org.gephi.io.generator.plugin.DynamicGraph;
import org.gephi.io.generator.plugin.RandomGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.Workspace;
import org.gephi.project.io.utils.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.openide.util.Lookup;

public class GeneratorTest extends ToolkitTest {

    @Test
    public void testGenerate() {
        Workspace workspace = Utils.newWorkspace();

        //Generate a new random graph into a container
        Container container = Lookup.getDefault().lookup(Container.Factory.class).newContainer();
        RandomGraph randomGraph = new RandomGraph();
        randomGraph.setNumberOfNodes(500);
        randomGraph.setWiringProbability(0.005);
        randomGraph.generate(container.getLoader());

        //Append container to graph structure
        importController.process(container, new DefaultProcessor(), workspace);

        Assert.assertEquals(randomGraph.getNumberOfNodes(),
            graphController.getGraphModel(workspace).getGraph().getNodeCount());
    }

    @Test
    public void testGenerateDynamicGraph() {
        Workspace workspace = Utils.newWorkspace();

        //Generate dynamic graph into workspace
        Container container = Lookup.getDefault().lookup(Container.Factory.class).newContainer();

        DynamicGraph dynamicGraph = new DynamicGraph();
        dynamicGraph.generate(container.getLoader());
        importController.process(container, new DefaultProcessor(), workspace);

        Assert.assertTrue(graphController.getGraphModel(workspace).isDynamic());
    }
}
