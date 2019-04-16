package io.pivotal.pal.tracker;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;
    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry any) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into time_entries (project_id, user_id, date, hours) values (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setLong(1, any.getProjectId());
            statement.setLong(2, any.getUserId());
            statement.setDate(3, Date.valueOf(any.getDate()));
            statement.setInt(4, any.getHours());
            return statement;
        }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {

        try {
            return (TimeEntry) this.jdbcTemplate.queryForObject("select * from time_entries where id = ?", new Object[]{timeEntryId}, new BeanPropertyRowMapper(TimeEntry.class));
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return this.jdbcTemplate.query("select * from time_entries", new RowMapper(){

            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TimeEntry(rs.getLong("id"), rs.getLong("project_id"), rs.getLong("user_id"), rs.getDate("date").toLocalDate(), rs.getInt("hours"));
            }

        });
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
        this.jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "update time_entries set project_id = ? , user_id = ? , date = ?, hours = ? where id = ?"
            );
            statement.setLong(1, any.getProjectId());
            statement.setLong(2, any.getUserId());
            statement.setDate(3, Date.valueOf(any.getDate()));
            statement.setInt(4, any.getHours());
            statement.setLong(5, eq);
            return statement;
        });
        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        this.jdbcTemplate.update("delete from time_entries where id = ?", new Object[]{timeEntryId});
    }
}
