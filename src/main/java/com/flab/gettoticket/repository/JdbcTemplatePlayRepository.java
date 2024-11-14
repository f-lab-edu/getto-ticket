package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import com.flab.gettoticket.model.Zone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTemplatePlayRepository implements PlayRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePlayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> selectPlayAtList(String goodsId, String startDate, String endDate) {
        String select = "SELECT play_at FROM play_time";
        String where = "WHERE goods_id = ? AND play_at BETWEEN ? AND ? GROUP BY play_at ORDER BY play_at";
        String sql = select + " " + where;

        List<String> list = jdbcTemplate.query(sql, playAtRowMapper(), goodsId, startDate, endDate);

        return list.isEmpty() ? null : list;
    }

    @Override
    public List<PlayTime> selectTimeTable(String goodsId, String playAt) {
        String select = "SELECT play_at, play_order, play_time, id FROM play_time";
        String where = "WHERE goods_id = ? AND play_at = ? ORDER BY play_order, play_time";
        String sql = select + " " + where;

        List<PlayTime> list = jdbcTemplate.query(sql, timeTableRowMapper(), goodsId, playAt);

        return list.isEmpty() ? null : list;
    }

    @Override
    public List<SeatCountDTO> selectSeatCount(String timeId) {
        String select = "SELECT b.grade, b.name, count(b.name) AS `count` FROM seat a";
        String join = "LEFT JOIN zone b ON a.goods_id = b.goods_id AND a.zone_id = b.id";
        String where = "WHERE a.time_id = ? GROUP BY b.name ORDER BY a.name";
        String sql = select + " " + join + " " + where;

        List<SeatCountDTO> list = jdbcTemplate.query(sql, seatCountRowMapper(), timeId);

        return list.isEmpty() ? null : list;
    }

    private RowMapper<String> playAtRowMapper() {
        return ((rs, rowNum) -> {
            return rs.getString("play_at");
        });
    }

    private RowMapper<PlayTime> timeTableRowMapper() {
        return ((rs, rowNum) -> {
            PlayTime playTime = new PlayTime();
            playTime.setPlayAt(rs.getString("play_at"));
            playTime.setPlayOrder(rs.getInt("play_order"));
            playTime.setPlayTime(rs.getInt("play_time"));
            playTime.setPlayTimeId(rs.getString("id"));
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
