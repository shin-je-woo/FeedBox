package com.feedbox.userdata.adapter;

import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.userdata.client.UserDataClient;
import com.feedbox.userdata.dto.CategoryResponse;
import com.feedbox.userdata.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDataAdapter implements UserDataPort {

    private final UserDataClient userDataClient;

    @Override
    public String getCategoryNameByCategoryId(Long categoryId) {
        CategoryResponse categoryResponse = userDataClient.getCategoryById(categoryId);
        if(categoryResponse == null) return null;
        return categoryResponse.getName();
    }

    @Override
    public String getUserNameByUserId(Long userId) {
        UserResponse userResponse = userDataClient.getUserById(userId);
        if (userResponse == null) return null;
        return userResponse.getName();
    }

    @Override
    public List<Long> getFollowerIdsByUserId(Long userId) {
        return userDataClient.getFollowerIdsByUserId(userId);
    }
}
