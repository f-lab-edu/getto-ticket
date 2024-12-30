package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class JdbcTemplateSeatRepository implements SeatRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateSeatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Seat> selectSeatList(long goodsId, long playId) {
        String sql = """
                SELECT 
                    id
                    , name
                    , col
                    , floor
                    , status_code
                    , play_id
                FROM seat
                WHERE goods_id=?
                AND play_id=?
                ORDER BY floor, col, name
                """;

        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), goodsId, playId);

        return list;
    }

    @Override
    public Seat selectSeat(long seatId) {
        String sql = """
                SELECT 
                    id
                    , name
                    , col
                    , floor
                    , status_code
                    , play_id
                FROM seat
                WHERE id=?
                ORDER BY floor, col, name
                """;

        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), seatId);

        Seat seat = new Seat();

        if(list.isEmpty() || list == null) {
            return seat;
        }

        return list.get(0);
    }

    @Override
    public List<Zone> selectZonePrice(long id) {
        String sql = """
                    SELECT 
                        id
                        , grade
                        , name
                        , price 
                    FROM zone 
                    WHERE goods_id = ? 
                    ORDER BY grade
                    """;

        List<Zone> list = jdbcTemplate.query(sql, zoneRowMapper(), id);

        return list;
    }

    @Override
    public int updateSeatStatusCode(long id, int statusCode) {
        String sql = """
                UPDATE seat SET
                    status_code=?
                WHERE id=?
                """;

        return jdbcTemplate.update(sql, statusCode, id);
    }

    private RowMapper<Seat> seatRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int col = rs.getInt("col");
            int floor = rs.getInt("floor");
            int status_code = rs.getInt("status_code");
            long playId = rs.getLong("play_id");

            return Seat.builder()
                    .id(id)
                    .name(name)
                    .col(col)
                    .floor(floor)
                    .statusCode(status_code)
                    .playId(playId)
                    .build();
        });
    }

    private RowMapper<Zone> zoneRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            String grade = rs.getString("grade");
            String name = rs.getString("name");
            int price = rs.getInt("price");

            return new Zone(id, grade, name, price);
        });
    }
}
