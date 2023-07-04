package com.example.xtransactionsandbox.repository.mysql;

import com.example.xtransactionsandbox.entity.jpa.mysql.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {

    List<AccountJpaEntity> findAllByUserId(Long userId);
}
