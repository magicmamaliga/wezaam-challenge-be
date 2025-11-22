package com.wezaam.withdrawal.user;


import java.util.List;

public class UserToDTOFactory {

    public static UserDTO createUserDTOFromUser(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getMaxWithdrawalAmount()
        );
    }

    public static List<UserDTO> createUserDTOListFromUserList(List<User> users) {
        return users.stream().map(UserToDTOFactory::createUserDTOFromUser).toList();
    }
}
