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

import java.awt.Color;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.api.Partition;
import org.gephi.appearance.api.PartitionFunction;
import org.gephi.appearance.api.RankingFunction;
import org.gephi.appearance.plugin.PartitionElementColorTransformer;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.palette.Palette;
import org.gephi.appearance.plugin.palette.PaletteManager;
import org.gephi.graph.GraphGenerator;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.project.api.Project;
import org.junit.Assert;
import org.junit.Test;

public class AppearanceTest extends ToolkitTest {

    @Test
    public void testPartition() {
        Project project = projectController.newProject();
        AppearanceModel appearanceModel = appearanceController.getModel(project.getCurrentWorkspace());

        // Generate graph
        Graph graph =
            GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph().addStringNodeColumn().getGraph();

        // Get partition function
        Column column = graph.getModel().getNodeTable().getColumn(GraphGenerator.STRING_COLUMN);
        Function func = appearanceModel.getNodeFunction(column, PartitionElementColorTransformer.class);
        Partition partition = ((PartitionFunction) func).getPartition();

        // Set palette
        Palette palette = PaletteManager.getInstance().randomPalette(2);
        partition.setColors(graph, palette.getColors());

        // Transform
        appearanceController.transform(func);

        // Assert
        Assert.assertEquals(palette.getColors()[0], graph.getNode(GraphGenerator.FIRST_NODE).getColor());
        Assert.assertEquals(palette.getColors()[1], graph.getNode(GraphGenerator.SECOND_NODE).getColor());
    }

    @Test
    public void testRanking() {
        Project project = projectController.newProject();
        AppearanceModel appearanceModel = appearanceController.getModel(project.getCurrentWorkspace());

        // Generate graph
        Graph graph = GraphGenerator.build(project.getCurrentWorkspace()).generateSmallRandomGraph().getGraph();

        // Get ranking function and transform
        Function degreeRanking = appearanceModel.getNodeFunction(graph.getModel().defaultColumns()
            .degree(), RankingElementColorTransformer.class);
        RankingElementColorTransformer degreeTransformer = degreeRanking.getTransformer();
        Color[] colors = new Color[] {new Color(0xFEF0D9), new Color(0xB30000)};
        degreeTransformer.setColors(colors);
        degreeTransformer.setColorPositions(new float[] {0f, 1f});
        appearanceController.transform(degreeRanking);

        // Assert
        Number minValue = ((RankingFunction) degreeRanking).getRanking().getMinValue(graph);
        Number maxValue = ((RankingFunction) degreeRanking).getRanking().getMaxValue(graph);
        for (Node node : graph.getNodes()) {
            int degree = graph.getDegree(node);
            if (degree == minValue.intValue()) {
                Assert.assertEquals(colors[0], node.getColor());
            } else if (degree == maxValue.intValue()) {
                Assert.assertEquals(colors[1], node.getColor());
            } else {
                Assert.assertNotEquals(colors[0], node.getColor());
                Assert.assertNotEquals(colors[1], node.getColor());
            }
        }
    }
}
