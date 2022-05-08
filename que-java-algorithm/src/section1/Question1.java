package section1;

import java.util.Scanner;

public class Question1 {
    public static int solution(String str, char c){
        int answer = 0;
        char[] cArr = str.toLowerCase().toCharArray();
        for (char c1 : cArr) {
            if (c1 == c){
                answer++;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine(); // 첫번째 입력값
        char c = sc.nextLine().toLowerCase().charAt(0); // 두번째 입력값
        System.out.println(solution(str, c));
    }
}
