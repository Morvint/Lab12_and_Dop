package Lab12;

public class text {

    public static void main(String[] args) {
        String str = "I LOVE@JAVA";
        String SplitsReceived [] = str.split(" ");
        System.out.print("received -- > ");

        for (String msg : SplitsReceived) {
            System.out.println(msg);
        }
        System.out.println("0 " + SplitsReceived[0]);
        System.out.print("length  -- > ");
        int lg = SplitsReceived.length;
        System.out.print(lg);
//        System.out.println("1 " + SplitsReceived[1]);
    }
}
