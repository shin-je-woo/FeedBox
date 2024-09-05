package com.feedbox.application.post.port.out;

import java.util.List;

public interface UserDataPort {

    String getCategoryNameByCategoryId(Long categoryId);
    String getUserNameByUserId(Long userId);
    List<Long> getFollowerIdsByUserId(Long userId);
}
