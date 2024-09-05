package com.feedbox.infrastructure.mysql.post.respoitory;

import com.feedbox.infrastructure.mysql.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
}
