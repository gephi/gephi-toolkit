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
