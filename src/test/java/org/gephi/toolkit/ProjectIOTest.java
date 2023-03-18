package org.gephi.toolkit;

import java.io.File;
import java.io.IOException;
import org.gephi.graph.GraphGenerator;
import org.gephi.project.api.Project;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ProjectIOTest extends ToolkitTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testSave() throws IOException {
        Project project = projectController.newProject();

        // Generate tiny graph
        GraphGenerator.build(project.getCurrentWorkspace()).generateTinyGraph();

        // Save
        File fileCreatedEmpty = tempFolder.newFile("test.gephi");
        File fileNotExisting = new File(tempFolder.getRoot(), "test2.gephi");
        projectController.saveProject(project, fileCreatedEmpty);
        projectController.saveProject(project, fileNotExisting);
        projectController.closeCurrentProject();

        Assert.assertTrue(fileCreatedEmpty.length() > 0);
        Assert.assertTrue(fileNotExisting.length() > 0);
        assertTinyGraph(fileCreatedEmpty);
        assertTinyGraph(fileNotExisting);
    }

    private void assertTinyGraph(File file) {
        projectController.closeCurrentProject();
        Project project = projectController.openProject(file);
        Assert.assertEquals(2, graphController.getGraphModel(project.getCurrentWorkspace()).getGraph().getNodeCount());
    }
}
