package cn.adcc.authorization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public class CommonsTests {

    @Test
    void testThrows() {
        Optional<String> emptyOpt = Optional.empty();
        assertThrows(NoSuchElementException.class,
                () -> emptyOpt.get());
        assertThrows(NullPointerException.class,
                () -> Optional.of(null));
        assertDoesNotThrow(() -> {
            Optional.ofNullable(null);
        });
    }

    @Test
    void TestEquals() {
        Optional<String> opt = Optional.ofNullable("ming");
        assertTrue(opt.isPresent());
        assertEquals("ming", opt.get());
        opt.ifPresent(o -> assertEquals("ming", o));
    }

    @Test
    void TestMap() {
        Optional<String> opt = Optional.ofNullable(null);
        //opt.get();
        //opt.filter(o -> "s".equals(o));
        System.out.println(opt.filter(o -> o.equals("s")));
    }
}
