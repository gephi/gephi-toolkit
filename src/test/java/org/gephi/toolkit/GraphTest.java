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

import java.io.IOException;
import java.net.URISyntaxException;
import org.gephi.graph.api.Configuration;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.TimeRepresentation;
import org.gephi.project.api.Project;
import org.gephi.project.api.Workspace;
import org.junit.Assert;
import org.junit.Test;

public class GraphTest extends ToolkitTest {

    @Test
    public void testDefaultConfig() {
        Workspace workspace = projectController.openNewWorkspace();

        GraphModel graphModel = graphController.getGraphModel(workspace);
        Assert.assertEquals(TimeRepresentation.INTERVAL, graphModel.getConfiguration().getTimeRepresentation());
        projectController.closeCurrentProject();
    }

    @Test
    public void testCustomConfig() {
        Configuration config = Configuration.builder().timeRepresentation(TimeRepresentation.TIMESTAMP).build();
        Workspace workspace = projectController.openNewWorkspace(config);

        GraphModel graphModel = graphController.getGraphModel(workspace);
        Assert.assertEquals(TimeRepresentation.TIMESTAMP, graphModel.getConfiguration().getTimeRepresentation());
        projectController.closeCurrentProject();
    }

    @Test
    public void testCustomConfigOtherMethod() {
        // Also opens a new workspace, but with default config
        Project project = projectController.newProject();
        Assert.assertNotNull(project.getCurrentWorkspace());

        // Checks that the workspace has the default config
        GraphModel graphModel = graphController.getGraphModel(projectController.getCurrentWorkspace());
        Assert.assertEquals(TimeRepresentation.INTERVAL, graphModel.getConfiguration().getTimeRepresentation());

        // Creates a new workspace with custom config, but does not open it
        Configuration config = Configuration.builder().timeRepresentation(TimeRepresentation.TIMESTAMP).build();
        Workspace workspace = projectController.newWorkspace(project, config);
        Assert.assertNotEquals(project.getCurrentWorkspace(), workspace);

        // Checks that the workspace has the custom config
        graphModel = graphController.getGraphModel(workspace);
        Assert.assertEquals(TimeRepresentation.TIMESTAMP, graphModel.getConfiguration().getTimeRepresentation());

        Assert.assertEquals(2, project.getWorkspaces().size());
        projectController.closeCurrentProject();
    }
}
