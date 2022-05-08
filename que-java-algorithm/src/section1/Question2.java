package section1;

import java.util.Scanner;

public class Question2 {
    public static String solution(String str){
        String answer = "";
        char[] cArr = str.toCharArray();
        for (char c : cArr) {
            answer += Character.isLowerCase(c)? Character.toUpperCase(c) : Character.toLowerCase(c);
        }
        return answer;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(solution(str));
    }
}
