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
        db.setEdgeQuery("SELECT edges.source AS source, edges.target AS target, edges.label AS label, edges.weight AS weight FROM edges");
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
