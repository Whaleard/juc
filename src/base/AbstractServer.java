package base;

import java.util.logging.Logger;

/**
 * @author Mr.MC
 */
public abstract class AbstractServer {

    protected Logger logger;

    public AbstractServer() {
        this.logger = Logger.getLogger(super.getClass().getName());
    }
}
