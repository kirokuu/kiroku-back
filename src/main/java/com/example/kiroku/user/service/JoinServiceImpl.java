package com.example.kiroku.user.service;

import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.JoinDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService{

    private final UserService userService;

    @Override
    public JoinStatus joinUser(JoinDto joinRequest) {
        User user = joinRequest.ofUser();
        boolean duplicateId = !checkDuplicateId(user.getUserId());
        boolean duplicateNickname = !checkDuplicateNickname(user.getNickname());
        User userToJoin = userService.findUser(user.getUserId());

        if(duplicateId) return JoinStatus.DUPLICATE_ID;
        else if(duplicateNickname) return JoinStatus.DUPLICATE_NICKNAME;
        else if(!userToJoin.isEmpty()) return JoinStatus.ALREADY_JOIN;

        userService.saveUser(user);
        return JoinStatus.SUCCESS;
    }

    @Override
    public boolean checkDuplicateId(String userId) {
        User user = userService.findUser(userId);
        return user.isEmpty();
    }

    @Override
    public boolean checkDuplicateNickname(String nickname) {
        User user = userService.findUserByNickname(nickname);
        return user.isEmpty();
    }

    @Override
    public boolean withdrawal(String userId) {
        User user = userService.findUser(userId);
        if(user.isEmpty()) {
            return false;
        }else {
            userService.deleteUser(user);
            return true;
        }
    }
}
