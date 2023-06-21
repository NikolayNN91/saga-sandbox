package com.example.xtransactionsandbox.dao;

import com.example.xtransactionsandbox.entity.AccountEntity;
import com.example.xtransactionsandbox.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class AccountDao {

    private static final String USER_COLUMN = "userId";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String ID_COLUMN = "id";

    private final JdbcTemplate mysqlDataSource;

    public AccountDao(@Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlDataSource) {
        this.mysqlDataSource = mysqlDataSource;
    }

    public Optional<AccountEntity> findById(Long accountId) {
        String query = "select * from accounts where id = ?";
        RowMapper<AccountEntity> rowMapper = (rs, rowNum) -> {
            AccountEntity account = new AccountEntity();
            account.setId(accountId);
            account.setUserId(rs.getLong(USER_COLUMN));
            account.setAmount(rs.getDouble(AMOUNT_COLUMN));
            return account;
        };
        return Optional.ofNullable(mysqlDataSource.queryForObject(query, rowMapper, accountId));
    }

    public AccountEntity insert(AccountEntity entity) {
        validateIdIsNotExist(entity);
        String query = "insert into accounts (amount, userId) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setDouble(1, entity.getAmount());
                ps.setLong(2, entity.getUserId());
                return ps;
            }
        };
        mysqlDataSource.update(preparedStatementCreator, keyHolder);
        return createNewFrom(keyHolder.getKey().longValue(), entity);
    }

    public AccountEntity update(AccountEntity account) {
        validateIdExist(account);
        String query = "update accounts set amount =?, userId =? where id =?";
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setDouble(1, account.getAmount());
                ps.setLong(2, account.getUserId());
                ps.setLong(3, account.getId());
                return ps;
            }
        };
        mysqlDataSource.update(preparedStatementCreator);
        return account;
    }

    private void validateIdIsNotExist(AccountEntity account) {
        if (account.getId() != null) {
            throw new BadRequestException();
        }
    }

    private void validateIdExist(AccountEntity account) {
        if (account.getId() == null) {
            throw new BadRequestException();
        }
    }

    private AccountEntity createNewFrom(Long id, AccountEntity account) {
        AccountEntity newAccountEntity = new AccountEntity();
        newAccountEntity.setId(id);
        newAccountEntity.setAmount(account.getAmount());
        newAccountEntity.setUserId(account.getUserId());
        return newAccountEntity;
    }

    public List<AccountEntity> findAllByUserId(Long userId) {
        String query = "select * from accounts where userId = ?";
        RowMapper<AccountEntity> rowMapper = (rs, rowNum) -> {
            AccountEntity account = new AccountEntity();
            account.setId(rs.getLong(ID_COLUMN));
            account.setUserId(userId);
            account.setAmount(rs.getDouble(AMOUNT_COLUMN));
            return account;
        };
        return mysqlDataSource.query(query, rowMapper, userId);
    }

    public List<AccountEntity> getAll() {
        String query = "select * from accounts";
        RowMapper<AccountEntity> rowMapper = (rs, rowNum) -> {
            AccountEntity account = new AccountEntity();
            account.setId(rs.getLong(ID_COLUMN));
            account.setUserId(rs.getLong(USER_COLUMN));
            account.setAmount(rs.getDouble(AMOUNT_COLUMN));
            return account;
        };
        return mysqlDataSource.query(query, rowMapper);
    }
}
