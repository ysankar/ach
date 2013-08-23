package com.bfds.ach.calc.repositories;


import com.google.common.base.Preconditions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@Repository
public class RevisionInfoJdbcRepository implements RevisionInfoRepository, InitializingBean {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = "transactionManager")
    public Long newRevisionInfo(final String user) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into revision_info (timestamp, user_name) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, new Date().getTime());
                ps.setString(2, user);
                return ps;
            }
        }, holder);
        Long revisionId =  holder.getKey().longValue();
        Preconditions.checkNotNull(revisionId != null && revisionId > 0, "Invalid revisionId %s", revisionId);
        return revisionId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(dataSource, "dataSource is null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
