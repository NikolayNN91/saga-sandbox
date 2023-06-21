package com.example.xtransactionsandbox.dao;

import com.example.xtransactionsandbox.entity.MerchandiseEntity;
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
public class StockDao {

    private static final String NAME_COLUMN = "name";
    private static final String PRICE_COLUMN = "price";
    private static final String SELLER_COLUMN = "seller";
    private static final String QUANTITY_COLUMN = "quantity";
    private static final String ID_COLUMN = "id";

    private final JdbcTemplate postgresqlJdbcTemplate;

    public StockDao(@Qualifier("postgresqlJdbcTemplate") JdbcTemplate postgresqlJdbcTemplate) {
        this.postgresqlJdbcTemplate = postgresqlJdbcTemplate;
    }

    public Optional<MerchandiseEntity> findById(Long merchandiseId) {
        String query = "select * from merchandises where id = ?";
        RowMapper<MerchandiseEntity> rowMapper = (rs, rowNum) -> {
            MerchandiseEntity merchandise = new MerchandiseEntity();
            merchandise.setId(merchandiseId);
            merchandise.setName(rs.getString(NAME_COLUMN));
            merchandise.setPrice(rs.getDouble(PRICE_COLUMN));
            merchandise.setSeller(rs.getLong(SELLER_COLUMN));
            merchandise.setQuantity(rs.getInt(QUANTITY_COLUMN));

            return merchandise;
        };
        return Optional.ofNullable(postgresqlJdbcTemplate.queryForObject(query, rowMapper, merchandiseId));
    }

    public MerchandiseEntity insert(MerchandiseEntity entity) {
        validateIdIsNotExist(entity);
        String query = "insert into merchandises (\"name\", price, seller, quantity) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setDouble(2, entity.getPrice());
                ps.setLong(3, entity.getSeller());
                ps.setInt(4, entity.getQuantity());
                return ps;
            }
        };
        postgresqlJdbcTemplate.update(preparedStatementCreator, keyHolder);
        return createNewFrom((Long) keyHolder.getKeys().get(ID_COLUMN), entity);
    }

    public MerchandiseEntity update(MerchandiseEntity merchandise) {
        validateIdExist(merchandise);
        String query = "update merchandises set \"name\" =?, price =?, seller =?, quantity =? where id =?";
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, merchandise.getName());
                ps.setDouble(2, merchandise.getPrice());
                ps.setLong(3, merchandise.getSeller());
                ps.setInt(4, merchandise.getQuantity());
                ps.setLong(5, merchandise.getId());
                return ps;
            }
        };
        postgresqlJdbcTemplate.update(preparedStatementCreator);
        return merchandise;
    }

    public List<MerchandiseEntity> getAll() {
        String query = "select * from merchandises";
        RowMapper<MerchandiseEntity> rowMapper = (rs, rowNum) -> {
            MerchandiseEntity merchandise = new MerchandiseEntity();
            merchandise.setId(rs.getLong(ID_COLUMN));
            merchandise.setName(rs.getString(NAME_COLUMN));
            merchandise.setPrice(rs.getDouble(PRICE_COLUMN));
            merchandise.setSeller(rs.getLong(SELLER_COLUMN));
            merchandise.setQuantity(rs.getInt(QUANTITY_COLUMN));

            return merchandise;
        };
        return postgresqlJdbcTemplate.query(query, rowMapper);
    }

    private void validateIdIsNotExist(MerchandiseEntity merchandise) {
        if (merchandise.getId() != null) {
            throw new BadRequestException();
        }
    }

    private void validateIdExist(MerchandiseEntity merchandise) {
        if (merchandise.getId() == null) {
            throw new BadRequestException();
        }
    }

    private MerchandiseEntity createNewFrom(Long id, MerchandiseEntity merchandise) {
        MerchandiseEntity newMerchandiseEntity = new MerchandiseEntity();
        newMerchandiseEntity.setId(id);
        newMerchandiseEntity.setName(merchandise.getName());
        newMerchandiseEntity.setQuantity(merchandise.getQuantity());
        newMerchandiseEntity.setSeller(merchandise.getSeller());
        newMerchandiseEntity.setPrice(merchandise.getPrice());
        return newMerchandiseEntity;
    }
}
