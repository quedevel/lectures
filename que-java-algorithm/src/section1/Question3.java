package section1;

import java.util.Arrays;
import java.util.Scanner;

public class Question3 {
    public static String solution(String str){
        String answer = "";
        String[] sArr = str.split(" ");
        answer = Arrays.stream(sArr).max((o1, o2) -> {
            if(o1.length() > o2.length()) return 1;
            else if (o1.length() < o2.length()) return -1;
            else return 0;
        }).get();
        return answer;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(solution(str));
    }
}
