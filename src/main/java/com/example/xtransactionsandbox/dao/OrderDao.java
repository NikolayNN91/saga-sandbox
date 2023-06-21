package com.example.xtransactionsandbox.dao;

import com.example.xtransactionsandbox.exception.BadRequestException;
import com.example.xtransactionsandbox.entity.OrderEntity;
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
public class OrderDao {

    private static final String CUSTOMER_COLUMN = "customerId";
    private static final String ID_COLUMN = "id";
    private static final String SELLER_COLUMN = "sellerId";
    private static final String MERCHANDISE_COLUMN = "merchandiseId";
    private static final String QUANTITY_COLUMN = "quantity";

    private final JdbcTemplate postgresqlJdbcTemplate;

    public OrderDao(@Qualifier("postgresqlJdbcTemplate") JdbcTemplate postgresqlJdbcTemplate) {
        this.postgresqlJdbcTemplate = postgresqlJdbcTemplate;
    }

    public Optional<OrderEntity> findById(Long merchandiseId) {
        String query = "select * from orders where id = ?";
        RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
            OrderEntity order = new OrderEntity();
            order.setId(merchandiseId);
            order.setCustomerId(rs.getLong(CUSTOMER_COLUMN));
            order.setSellerId(rs.getLong(SELLER_COLUMN));
            order.setMerchandiseId(rs.getLong(MERCHANDISE_COLUMN));
            order.setQuantity(rs.getInt(QUANTITY_COLUMN));

            return order;
        };
        return Optional.ofNullable(postgresqlJdbcTemplate.queryForObject(query, rowMapper, merchandiseId));
    }

    public OrderEntity insert(OrderEntity entity) {
        validateIdIsNotExist(entity);
        String query = "insert into orders (\"customerId\", \"merchandiseId\", \"sellerId\", \"quantity\") values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, entity.getCustomerId());
                ps.setLong(2, entity.getMerchandiseId());
                ps.setLong(3, entity.getSellerId());
                ps.setInt(4, entity.getQuantity());
                return ps;
            }
        };
        postgresqlJdbcTemplate.update(preparedStatementCreator, keyHolder);
        return createNewFrom((Long) keyHolder.getKeys().get(ID_COLUMN), entity);
    }

    public OrderEntity updateById(OrderEntity order) {
        validateIdExist(order);
        String query = "update orders set \"customerId\" =?, \"merchandiseId\" =?, \"sellerId\" =?, quantity =? where id =?";
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setLong(1, order.getCustomerId());
                ps.setLong(2, order.getMerchandiseId());
                ps.setLong(3, order.getSellerId());
                ps.setInt(4, order.getQuantity());
                ps.setLong(5, order.getId());
                return ps;
            }
        };
        postgresqlJdbcTemplate.update(preparedStatementCreator);
        return order;
    }

    public List<OrderEntity> getAll() {
        String query = "select * from orders";
        RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
            OrderEntity order = new OrderEntity();
            order.setId(rs.getLong(ID_COLUMN));
            order.setCustomerId(rs.getLong(CUSTOMER_COLUMN));
            order.setSellerId(rs.getLong(SELLER_COLUMN));
            order.setMerchandiseId(rs.getLong(MERCHANDISE_COLUMN));
            order.setQuantity(rs.getInt(QUANTITY_COLUMN));

            return order;
        };
        return postgresqlJdbcTemplate.query(query, rowMapper);
    }

    private void validateIdIsNotExist(OrderEntity order) {
        if (order.getId() != null) {
            throw new BadRequestException();
        }
    }

    private void validateIdExist(OrderEntity order) {
        if (order.getId() == null) {
            throw new BadRequestException();
        }
    }

    private OrderEntity createNewFrom(Long id, OrderEntity order) {
        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setId(id);
        newOrderEntity.setCustomerId(order.getCustomerId());
        newOrderEntity.setMerchandiseId(order.getMerchandiseId());
        newOrderEntity.setSellerId(order.getSellerId());
        newOrderEntity.setQuantity(order.getQuantity());
        return newOrderEntity;
    }
}
