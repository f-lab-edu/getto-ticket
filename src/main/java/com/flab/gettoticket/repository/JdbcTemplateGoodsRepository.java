package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.Zone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class JdbcTemplateGoodsRepository implements GoodsRepository{
    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateGoodsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Goods> selectGoodsList(int limit, long offset) {
        String sql = """
                    SELECT 
                        a.id
                       , c.name AS genre_name
                       , a.title
                       , a.`desc`
                       , a.performance_start_date
                       , a.performance_end_date
                       , a.performance_time
                       , b.name AS location
                       , b.x
                       , b.y
                       , c.id AS genre_id
                       , b.id AS place_id
                    FROM goods a
                    LEFT JOIN place b ON a.place_id = b.id
                    LEFT JOIN genre c ON a.genre_id = c.id
                    WHERE a.id > ?
                    ORDER BY a.id
                    LIMIT ?
                    """;

        List<Goods> goodsList = jdbcTemplate.query(sql, goodsRowMapper(), offset, limit);

        return goodsList;
    }

    @Override
    public Goods selectGoods(long id) {
        String sql = """
                SELECT 
                    a.id
                    , c.name AS genre_name
                    , a.title
                    , a.`desc`
                    , a.performance_start_date
                    , a.performance_end_date
                    , a.performance_time
                    , b.name AS location
                    , b.x
                    , b.y 
                FROM goods a
                LEFT JOIN place b ON a.place_id = b.id 
                LEFT JOIN genre c ON a.genre_id = c.id
                WHERE a.id = ?
                """;

        List<Goods> goodsList = jdbcTemplate.query(sql, goodsRowMapper(), id);

        return goodsList.get(0);
    }

    @Transactional
    @Override
    public int insertGoods(Goods goods) {
        String sqlForSequence = "SELECT NEXTVAL(goods_seq) FROM DUAL";

        String sql = """
                    INSERT INTO goods (
                        id
                        , title
                        , `desc`
                        , performance_start_date
                        , performance_end_date
                        , performance_time
                        , genre_id
                        , place_id
                    ) 
                    VALUES (?,?,?,?,?,?,?,?)
                    """;

        String goodsSeq = jdbcTemplate.queryForObject(sqlForSequence, String.class);
        long id = Integer.valueOf(goodsSeq);
        String title = goods.getTitle();
        String desc = goods.getDesc();
        LocalDate performanceStartDate = goods.getPerformanceStartDate();
        LocalDate performanceEndDate = goods.getPerformanceEndDate();
        String performanceTime = goods.getPerformanceTime();
        long genreId = goods.getGenreId();
        long placeId = goods.getPlaceId();

        return jdbcTemplate.update(sql, id, title, desc, performanceStartDate, performanceEndDate, performanceTime, genreId, placeId);
    }

    @Override
    public int updateGoods(Goods goods) {
        String sql = """
                    UPDATE goods SET 
                        title=?
                         ,`desc`=?
                         , performance_start_date=?
                         , performance_end_date=?
                         , performance_time=?
                         , genre_id=?
                         , place_id=? 
                    WHERE id=?
                    """;

        long id = goods.getId();
        String title = goods.getTitle();
        String desc = goods.getDesc();
        LocalDate performanceStartDate = goods.getPerformanceStartDate();
        LocalDate performanceEndDate = goods.getPerformanceEndDate();
        String performanceTime = goods.getPerformanceTime();
        long genreId = goods.getGenreId();
        long placeId = goods.getPlaceId();

        return jdbcTemplate.update(sql, title, desc, performanceStartDate, performanceEndDate, performanceTime, genreId, placeId, id);
    }

    @Override
    public int deleteGoods(long id) {
        String sql = "DELETE FROM goods WHERE id=?";

        return jdbcTemplate.update(sql, id);
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

    private RowMapper<Goods> goodsRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            String genreName = rs.getString("genre_name");
            String title = rs.getString("title");
            String desc = rs.getString("desc");
            LocalDate performanceStartDate = rs.getDate("performance_start_date").toLocalDate();
            LocalDate performanceEndDate = rs.getDate("performance_end_date").toLocalDate();
            String performanceTime = rs.getString("performance_time");
            String location = rs.getString("location");
            String x = rs.getString("x");
            String y = rs.getString("y");
            long genreId = rs.getLong("genreId");
            long placeId = rs.getLong("placeId");

            return new Goods(id, genreName, title, desc, performanceStartDate, performanceEndDate, performanceTime, location, x, y, genreId, placeId);
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
