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
                    , sale_yn
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
    public int updateSeatSaleYn(long id, String saleYn) {
        String sql = """
                UPDATE seat SET
                    sale_yn=?
                WHERE id=?
                """;

        return jdbcTemplate.update(sql, saleYn, id);
    }

    private RowMapper<Seat> seatRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int col = rs.getInt("col");
            int floor = rs.getInt("floor");
            String sale_yn = rs.getString("sale_yn");
            char saleYn = sale_yn.charAt(0);
            long playId = rs.getLong("play_id");

            return Seat.builder()
                    .id(id)
                    .name(name)
                    .col(col)
                    .floor(floor)
                    .saleYn(saleYn)
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
