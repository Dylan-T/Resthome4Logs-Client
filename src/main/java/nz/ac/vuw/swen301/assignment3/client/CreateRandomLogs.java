package nz.ac.vuw.swen301.assignment3.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * creates random logs (using random messages and levels) in an infinite loop at a rate of  1 LogEvent per second.
 * Logging is set up to use the Resthome4LogsAppender appender
 */
public class CreateRandomLogs {

    /**
     *
     */
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger("test1");
            logger.addAppender(new Resthome4LogsAppender(2));
            Level[] levels = {Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL};
            String[] messages = {"message0","message1","message2","message3","message4"};
            Random random = new Random();

            while(true) {
                int randL = random.nextInt(6);
                int randM = random.nextInt(5);

                logger.log(levels[randL], messages[randM]);
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
