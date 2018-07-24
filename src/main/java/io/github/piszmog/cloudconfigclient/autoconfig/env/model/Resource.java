package io.github.piszmog.cloudconfigclient.autoconfig.env.model;

import java.util.List;

/**
 * Object for containing information on the configuration resources to load.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
public class Resource
{
    // ============================================================
    // Class Attributes:
    // ============================================================

    private String directory;
    private List<String> files;

    // ============================================================
    // Public Methods:
    // ============================================================

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory( final String directory )
    {
        this.directory = directory;
    }

    public List<String> getFiles()
    {
        return files;
    }

    public void setFiles( final List<String> files )
    {
        this.files = files;
    }
}
