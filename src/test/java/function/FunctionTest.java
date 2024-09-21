package function;

import java.util.Random;
import java.util.UUID;

import static com.querydsl.core.types.Ops.MathOps.RANDOM;
import static software.amazon.ion.SystemSymbols.MAX_ID;

public class FunctionTest {

    private static final long MIN_ID = 1000000000L; // 10자리 최소값
    private static final long MAX_ID = 9999999999L; // 10자리 최대값
    private static final Random RANDOM = new Random();

    /**
     * 10자리 long 타입 식별코드를 생성합니다.
     * @return 생성된 식별코드
     */
    public static long generateUniqueId() {
        return MIN_ID + (long) (RANDOM.nextDouble() * (MAX_ID - MIN_ID));
    }

    public static void main(String[] args) {
        System.out.println("Generated Unique ID: " + generateUniqueId());
    }
}
