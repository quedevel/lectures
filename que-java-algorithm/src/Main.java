import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int result = 0; // 결과값
        String str = sc.nextLine(); // 첫번째 입력값
        char[] cArr = str.toLowerCase().toCharArray();
        char c = sc.nextLine().toLowerCase().charAt(0); // 두번째 입력값
        for (char c1 : cArr) {
            if (c1 == c){
                result++;
            }
        }
        System.out.println(result);
    }
}
