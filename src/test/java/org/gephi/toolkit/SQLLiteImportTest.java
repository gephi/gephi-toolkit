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
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.database.drivers.SQLiteDriver;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.plugin.database.EdgeListDatabaseImpl;
import org.gephi.io.importer.plugin.database.ImporterEdgeList;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.Project;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SQLLiteImportTest extends ToolkitTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testImport() throws IOException {
        Project project = projectController.newProject();

        File file = copyResourcesToTempDir("lesmiserables.sqlite3", tempFolder.getRoot());
        Assert.assertTrue(file.exists());

        // Import database
        EdgeListDatabaseImpl db = new EdgeListDatabaseImpl();
        db.setHost(file.getAbsolutePath());
        db.setDBName("");
        db.setSQLDriver(new SQLiteDriver());
        db.setNodeQuery("SELECT nodes.id AS id, nodes.label AS label FROM nodes");
        db.setEdgeQuery(
            "SELECT edges.source AS source, edges.target AS target, edges.label AS label, edges.weight AS weight FROM edges");
        ImporterEdgeList edgeListImporter = new ImporterEdgeList();
        Container container = importController.importDatabase(db, edgeListImporter);
        container.getLoader().setAllowAutoNode(false);      //Don't create missing nodes
        container.getLoader().setEdgeDefault(EdgeDirectionDefault.UNDIRECTED);   //Force UNDIRECTED

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), project.getCurrentWorkspace());

        //See if graph is well imported
        UndirectedGraph graph = graphController.getGraphModel(project.getCurrentWorkspace()).getUndirectedGraph();
        Assert.assertEquals(77, graph.getNodeCount());
        Assert.assertEquals(254, graph.getEdgeCount());
    }
}
