package section1;

import java.util.Scanner;

public class Question2 {
    public static String solution(String str){
        String answer = "";
        char[] cArr = str.toCharArray();
        // a : 97 z : 122
        // A : 65 Z : 90
        for (char c : cArr) {
            int i = (int)c;
            String temp = i <= 122 && i >= 97? String.valueOf(c).toUpperCase() : String.valueOf(c).toLowerCase();
            answer += temp;
        }
        return answer;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(solution(str));
    }
}
