package org.gephi.toolkit;

import org.gephi.appearance.api.AppearanceModel;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.attribute.AttributeEqualBuilder;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder;
import org.gephi.filters.plugin.operator.INTERSECTIONBuilder;
import org.gephi.filters.plugin.partition.PartitionBuilder;
import org.gephi.graph.GraphGenerator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.project.api.Project;
import org.gephi.project.api.Workspace;
import org.junit.Assert;
import org.junit.Test;

public class FilteringTest extends ToolkitTest {

    @Test
    public void testDegreeFilter() {
        Project project = projectController.newProject();

        // Generate tiny graph
        Graph graph = GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph().getGraph();

        //Add one extra node (unconnected)
        Node extraNode = graph.getModel().factory().newNode();
        Assert.assertTrue(graph.addNode(extraNode));

        // Filter
        DegreeRangeBuilder.DegreeRangeFilter degreeFilter = new DegreeRangeBuilder.DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(1, Integer.MAX_VALUE));     //Remove nodes with degree < 1
        GraphView view = filterController.filter(filterController.createQuery(degreeFilter));

        // Get filtered graph and assert
        Graph filteredGraph = graph.getModel().getGraph(view);
        Assert.assertEquals(2, filteredGraph.getNodeCount());
        Assert.assertFalse(filteredGraph.contains(extraNode));
        Assert.assertEquals(1, filteredGraph.getEdgeCount());
    }

    @Test
    public void testPartitionFilter() {
        Project project = projectController.newProject();
        Workspace workspace = project.getCurrentWorkspace();

        // Generate tiny graph
        Graph graph = GraphGenerator.build(workspace).generateTinyGraph().addStringNodeColumn().getGraph();

        // Filter
        AppearanceModel appearanceModel = appearanceController.getModel(workspace);
        PartitionBuilder.NodePartitionFilter
            partitionFilter = new PartitionBuilder.NodePartitionFilter(appearanceModel,
            appearanceModel.getNodePartition(graph.getModel().getNodeTable().getColumn(GraphGenerator.STRING_COLUMN)));
        partitionFilter.unselectAll();
        partitionFilter.addPart(GraphGenerator.STRING_COLUMN_VALUES[0]);
        GraphView view = filterController.filter(filterController.createQuery(partitionFilter));

        // Get filtered graph and assert
        Graph filteredGraph = graph.getModel().getGraph(view);
        Assert.assertEquals(1, filteredGraph.getNodeCount());
        Assert.assertTrue(filteredGraph.contains(graph.getNode(GraphGenerator.FIRST_NODE)));

        // Filter again
        partitionFilter.selectAll();
        partitionFilter.removePart(GraphGenerator.STRING_COLUMN_VALUES[0]);
        GraphView view2 = filterController.filter(filterController.createQuery(partitionFilter));

        // Get filtered graph and assert
        filteredGraph = graph.getModel().getGraph(view2);
        Assert.assertEquals(1, filteredGraph.getNodeCount());
        Assert.assertTrue(filteredGraph.contains(graph.getNode(GraphGenerator.SECOND_NODE)));
    }

    @Test
    public void testAndOperator() {
        Project project = projectController.newProject();
        Workspace workspace = project.getCurrentWorkspace();

        // Generate tiny graph
        Graph graph = GraphGenerator.build(workspace).generateTinyGraph().addStringNodeColumn().getGraph();

        // First query
        DegreeRangeBuilder.DegreeRangeFilter degreeFilter = new DegreeRangeBuilder.DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(1, 1));
        Query degreeQuery = filterController.createQuery(degreeFilter);

        // Second query
        AttributeEqualBuilder.EqualStringFilter.Node equalFilter = new AttributeEqualBuilder.EqualStringFilter.Node(
            graph.getModel().getNodeTable().getColumn(GraphGenerator.STRING_COLUMN)
        );
        equalFilter.init(graph);
        equalFilter.setPattern(GraphGenerator.STRING_COLUMN_VALUES[0]);
        Query equalQuery = filterController.createQuery(equalFilter);

        // Filter
        INTERSECTIONBuilder.IntersectionOperator intersectionOperator = new INTERSECTIONBuilder.IntersectionOperator();
        Query andQuery = filterController.createQuery(intersectionOperator);
        filterController.setSubQuery(andQuery, degreeQuery);
        filterController.setSubQuery(andQuery, equalQuery);
        GraphView view = filterController.filter(andQuery);

        // Get filtered graph and assert
        Graph filteredGraph = graph.getModel().getGraph(view);
        Assert.assertEquals(1, filteredGraph.getNodeCount());
        Assert.assertTrue(filteredGraph.contains(graph.getNode(GraphGenerator.FIRST_NODE)));
    }
}
