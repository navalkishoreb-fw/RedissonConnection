import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RedissonClientConnection {

    // Create logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(RedissonClientConnection.class);

    public static void main(String[] args) throws InterruptedException {
        // Configure Redisson
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");

        // Create Redisson client
        RedissonClient redisson = Redisson.create(config);
        logger.info("Connected to Redis");

        // Get a lock on a key
        RLock lock = redisson.getLock("myLockKey");

        if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
            try {
                logger.info("Lock acquired, doing work...");
            } finally {
                lock.unlock();
                logger.info("Lock released.");
            }
        } else {
            logger.warn("Could not acquire the lock.");
        }

        // Shutdown Redisson client
        redisson.shutdown();
        logger.info("Redisson client shutdown.");
    }
}
