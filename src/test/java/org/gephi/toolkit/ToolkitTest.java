package org.gephi.toolkit;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.gephi.appearance.api.AppearanceController;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.GraphController;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.preview.api.PreviewController;
import org.gephi.project.api.ProjectController;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public abstract class ToolkitTest {

    protected final ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);

    protected final GraphController graphController = Lookup.getDefault().lookup(GraphController.class);

    protected final AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);

    protected final FilterController filterController = Lookup.getDefault().lookup(FilterController.class);

    protected final ImportController importController = Lookup.getDefault().lookup(ImportController.class);

    protected final ExportController exportController = Lookup.getDefault().lookup(ExportController.class);

    protected final PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);

    // Utilities

    protected File copyResourcesToTempDir(String fileName, File tempFolder) throws IOException {
        File file;
        try {
            file = new File(getClass().getResource("/org/gephi/toolkit/" + fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        return FileUtil.toFile(
            FileUtil.copyFile(FileUtil.toFileObject(file), FileUtil.toFileObject(tempFolder), fileName));
    }
}
