package com.example.kiroku.user.service;

import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.dto.JoinDto;

public interface JoinService {
    public JoinStatus joinUser(JoinDto.JoinRequest joinRequest);
    public boolean checkDuplicateId(String userId);
    public boolean checkDuplicateNickname(String nickname);
    public boolean withdrawal(String userId);
}
