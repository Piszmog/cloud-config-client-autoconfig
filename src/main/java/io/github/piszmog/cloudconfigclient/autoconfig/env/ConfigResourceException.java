package io.github.piszmog.cloudconfigclient.autoconfig.env;

/**
 * Exception thrown when an error occurs when loading configuration resource files.
 * <p>
 * Created by Piszmog on 5/5/2018
 */
public class ConfigResourceException extends RuntimeException
{
    /**
     * Creates a new configuration resource exception.
     */
    public ConfigResourceException()
    {
    }

    /**
     * Creates a new configuration resource exception.
     *
     * @param message the message associated with the exception
     */
    public ConfigResourceException( final String message )
    {
        super( message );
    }

    /**
     * Creates a new configuration resource exception.
     *
     * @param message the message associated with the exception
     * @param cause   the cause of th exception
     */
    public ConfigResourceException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Creates a new configuration resource exception.
     *
     * @param cause the cause of th exception
     */
    public ConfigResourceException( final Throwable cause )
    {
        super( cause );
    }

    /**
     * Creates a new configuration resource exception.
     *
     * @param message            the message associated with the exception
     * @param cause              the cause of th exception
     * @param enableSuppression  determines if suppression is enabled
     * @param writableStackTrace determines if the stack is writeable
     */
    public ConfigResourceException( final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
