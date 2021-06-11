package app.component;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CircleComp {
    private static final int LINES_PER_SIDE = 1;
    private static final int LINE_LENGTH = 50;
    private static final int CIRCLE_LENGTH = 25;
    private static final int MIN_COUNT_EXIT = 3;
    private static final int MAX_COUNT_EXIT = 5;

    private static final int MIN_AUTO_SPEED = 1;
    private static final int MAX_AUTO_SPEED = 3;

    @Autowired
    public CircleComp() {
    }

    public int getLinesPerSide(){
        return LINES_PER_SIDE;
    }

    public int getLineLength(){
        return LINE_LENGTH;
    }

    public int getCircleLength(){
        return CIRCLE_LENGTH;
    }

    public long getCurrentTimeInSeconds(){
        return Instant.now().getEpochSecond();
    }

    public int getMinExit(){return MIN_COUNT_EXIT;}

    public int getMaxExit(){return MAX_COUNT_EXIT;}

    public int getMinAutoSpeed(){return MIN_AUTO_SPEED;}

    public int getMaxAutoSpeed(){return MAX_AUTO_SPEED;}
}
