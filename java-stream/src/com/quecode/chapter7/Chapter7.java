package com.quecode.chapter7;

import com.quecode.chapter6.model.User;

import java.util.Optional;

/**
 * 7.1 Optional
 * 7.2 Optional 응용
 */
public class Chapter7 {
    public static void main(String[] args) {

        /**
         * 7.1 Optional
         */
        User user1 = new User().setId(1001).setName("Alice").setVerified(false);
        User user2 = new User().setId(1001).setName("Alice").setVerified(false).setEmailAddress(Optional.of("alice@innotree.com"));

        // NullPointerException 발생
        // System.out.println("Same? :" + userEquals(user1, user2));

        String someEmail = "some@email.com";
        String nullEamil = null;

        Optional<String> maybeEmail = Optional.of(someEmail);
        Optional<String> maybeEmail2 = Optional.empty();
        Optional<String> maybeEmail3 = Optional.ofNullable(someEmail);
        Optional<String> maybeEmail4 = Optional.ofNullable(nullEamil);

        String mail = maybeEmail.get();
        System.out.println("mail = " + mail);

        if(maybeEmail2.isPresent()){
            System.out.println("maybeEmail2 = " + maybeEmail2.get());
        }

        String defaultEmail = "default@email.com";
        String email2 = maybeEmail2.orElse(defaultEmail);
        System.out.println("email2 = " + email2);

        String email3 = maybeEmail2.orElseGet(()->defaultEmail);
        System.out.println("email3 = " + email3);

        //String email4 = maybeEmail2.orElseThrow(() -> new RuntimeException("email not present"));
        //System.out.println("email4 = " + email4);
        System.out.println();
        System.out.println();

        /**
         * 7.2 Optional 응용
         */
        Optional<User> maybeUser = Optional.ofNullable(maybeGetUser(true));
        maybeUser.ifPresent(System.out::println);

        Optional<Integer> maybeId = Optional.ofNullable(maybeGetUser(true)).map(User::getId);
        maybeId.ifPresent(System.out::println);

        String userName = Optional.ofNullable(maybeGetUser(false))
                .map(User::getName)
                .map(name -> "The name is "+name)
                .orElse("Name is Empty");
        System.out.println("userName = " + userName);

    }

    public static User maybeGetUser(boolean returnUser){
        if(returnUser){
            return new User().setId(1001).setName("Alice").setVerified(false);
        }
        return null;
    }

    public static boolean userEquals(User u1, User u2){
        return u1.getId() == u2.getId()
                && u1.getName().equals(u2.getName())
                && u1.getEmailAddress().equals(u2.getEmailAddress())
                && u1.isVerified() == u2.isVerified();
    }
}
