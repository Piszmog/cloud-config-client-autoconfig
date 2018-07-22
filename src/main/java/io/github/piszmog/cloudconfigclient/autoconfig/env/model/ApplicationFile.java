package io.github.piszmog.cloudconfigclient.autoconfig.env.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Piszmog on 7/21/2018
 */
@Data
public class ApplicationFile
{
    @NotBlank( message = "Directory Path must not be null or blank." )
    private String directoryPath;
    @NotBlank( message = "File name must not be null or blank." )
    private String fileName;
}
