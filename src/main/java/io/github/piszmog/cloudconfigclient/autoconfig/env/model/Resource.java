package io.github.piszmog.cloudconfigclient.autoconfig.env.model;

import lombok.Data;

import java.util.List;

/**
 * Object for containing information on the configuration resources to load.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
@Data
public class Resource
{
    // ============================================================
    // Class Attributes:
    // ============================================================

    private String directory;
    private List<String> files;
}
