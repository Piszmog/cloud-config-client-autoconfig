package io.github.piszmog.cloudconfigclient.autoconfig.env.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Object for containing information on the configuration resources to load.
 * <p>
 * Created by Piszmog on 7/21/2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    private String directory;
    private List<String> files;
}
