package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcTemplatePlayRepository implements PlayRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePlayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LocalDate> selectPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                    SELECT play_at FROM play_time
                    WHERE goods_id = ? AND play_at BETWEEN ? AND ?
                    GROUP BY play_at
                    ORDER BY play_at
                    """;

        List<LocalDate> list = jdbcTemplate.query(sql, playAtRowMapper(), goodsId, startDate, endDate);

        return list;
    }

    @Override
    public List<PlayTime> selectTimeTable(long goodsId, LocalDate playAt) {
        String sql = """
                    SELECT play_at, play_order, play_time, id FROM play_time
                    WHERE goods_id = ? AND play_at = ?
                    ORDER BY play_order, play_time
                    """;

        List<PlayTime> list = jdbcTemplate.query(sql, timeTableRowMapper(), goodsId, playAt);

        return list;
    }

    @Override
    public List<SeatCountDTO> selectSeatCount(long playTimeId) {
        String sql = """
                    SELECT b.grade, b.name, count(b.name) AS `count`
                    FROM seat a
                    LEFT JOIN zone b
                    ON a.goods_id = b.goods_id AND a.zone_id = b.id
                    WHERE a.time_id = ?
                    GROUP BY b.name
                    ORDER BY a.name
                    """;

        List<SeatCountDTO> list = jdbcTemplate.query(sql, seatCountRowMapper(), playTimeId);

        return list;
    }

    private RowMapper<LocalDate> playAtRowMapper() {
        return ((rs, rowNum) -> {
            return rs.getDate("play_at").toLocalDate();
        });
    }

    private RowMapper<PlayTime> timeTableRowMapper() {
        return ((rs, rowNum) -> {
            PlayTime playTime = new PlayTime();
            playTime.setPlayAt(rs.getDate("play_at").toLocalDate());
            playTime.setPlayOrder(rs.getInt("play_order"));
            playTime.setPlayTime(rs.getInt("play_time"));
            playTime.setPlayTimeId(rs.getLong("id"));
            return playTime;
        });
    }

    private RowMapper<SeatCountDTO> seatCountRowMapper() {
        return ((rs, rowNum) -> {
            SeatCountDTO dto = new SeatCountDTO();
            dto.setGrade(rs.getString("grade"));
            dto.setZoneName(rs.getString("name"));
            dto.setCount(rs.getInt("count"));
            return dto;
        });
    }
}
