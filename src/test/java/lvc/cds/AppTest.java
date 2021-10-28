package lvc.cds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.Collections;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        assertEquals(1, 1);
    }

    public static void main(String[] args){
        String s = "-hello hello- sna hello hello";
        System.out.println(s.contains("-hello sna"));
       System.out.println(s);
        ArrayList<ArrayList<String>> sd = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();
        a.add("I");
        a.add("am");
        a.add("cool");
        a.add("yuh");
        sd.add(a);

        ArrayList<String> b = new ArrayList<>();
        b.add("I");
        b.add("am");
        b.add("cool");
        b.add("yuh");
        //b.add("right");
        
        //System.out.println(sd.contains(b));
        
    }
}
