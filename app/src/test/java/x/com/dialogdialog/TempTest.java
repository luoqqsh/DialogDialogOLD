package x.com.dialogdialog;

/**
 * 各种临时测试，执行with coverage
 */
public class TempTest {
    public static void main(String[] args) {
        System.out.println("start");
        System.out.println(calculateSize(100000));
        System.out.println("end");
    }

    private static int calculateSize(int numElements) {
        int initialCapacity = 8;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>> 1);
            System.out.println(initialCapacity);
            initialCapacity |= (initialCapacity >>> 2);
            System.out.println(initialCapacity);
            initialCapacity |= (initialCapacity >>> 4);
            System.out.println(initialCapacity);
            initialCapacity |= (initialCapacity >>> 8);
            System.out.println(initialCapacity);
            initialCapacity |= (initialCapacity >>> 16);
            System.out.println(initialCapacity);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }
}
