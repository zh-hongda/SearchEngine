package testsolr;


import org.junit.Test;

import java.util.Date;

public class GetTime {

    @Test
    public void getTimes(){
        Date date = new Date();
        System.out.println((long) date.getTime());
    }
}
