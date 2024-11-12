package com.flab.gettoticket.repository;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class JdbcTemplateGoodsRepository implements GoodsRepository{
    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateGoodsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Goods> selectGoodsList(int startIndex, int amount) {
        String columns = "a.id, c.name AS genre_name, a.title, a.`desc`, a.performance_start_date, a.performance_end_date, a.performance_time, b.name AS location, b.x, b.y";
        String select = "SELECT " + columns + " FROM goods a ";
        String join = "LEFT JOIN place b ON a.place_id = b.id LEFT JOIN genre c ON a.genre_id = c.id ";
        String limit = "LIMIT " + startIndex + ", " + amount;

        String sql = select + join + limit;

        List<Goods> goodsList = jdbcTemplate.query(sql, goodsRowMapper());

        return goodsList.isEmpty() ? null : goodsList;
    }

    @Override
    public Goods selectGoods(String goodsId) {
        String columns = "a.id, c.name AS genre_name, a.title, a.`desc`, a.performance_start_date, a.performance_end_date, a.performance_time, b.name AS location, b.x, b.y";
        String select = "SELECT " + columns + " FROM goods a ";
        String join = "LEFT JOIN place b ON a.place_id = b.id LEFT JOIN genre c ON a.genre_id = c.id ";
        String where = "WHERE a.id = ?";

        String sql = select + join + where;

        log.info("sql: " + sql);

        List<Goods> goodsList = jdbcTemplate.query(sql, goodsRowMapper(), goodsId);

        return goodsList.isEmpty() ? null : goodsList.get(0);
    }

    @Override
    public void insertGoods(Goods goods) {
        String columns = "id, title, `desc`, performance_start_date, performance_end_date, performance_time, genre_id, place_id";
        String sql = "INSERT INTO goods (" + columns + ") VALUES (?,?,?,?,?,?,?,?)";

        String id = goods.getId();
        String title = goods.getTitle();
        String desc = goods.getDesc();
        String performanceStartDate = goods.getPerformanceStartDate();
        String performanceEndDate = goods.getPerformanceEndDate();
        String performanceTime = goods.getPerformanceTime();
        String genreId = goods.getGenreId();
        String placeId = goods.getPlaceId();

        log.info("sql: " + sql);

        jdbcTemplate.update(sql, id, title, desc, performanceStartDate, performanceEndDate, performanceTime, genreId, placeId);
    }

    @Override
    public void updateGoods(Goods goods) {
        String sql = "UPDATE goods SET title=?, `desc`=?, performance_start_date=?, performance_end_date=?, performance_time=?, genre_id=?, place_id=? WHERE id=?";

        String id = goods.getId();
        String title = goods.getTitle();
        String desc = goods.getDesc();
        String performanceStartDate = goods.getPerformanceStartDate();
        String performanceEndDate = goods.getPerformanceEndDate();
        String performanceTime = goods.getPerformanceTime();
        String genreId = goods.getGenreId();
        String placeId = goods.getPlaceId();

        log.info("sql: " + sql);

        jdbcTemplate.update(sql, title, desc, performanceStartDate, performanceEndDate, performanceTime, genreId, placeId, id);
    }

    @Override
    public void deleteGoods(String goodsId) {
        String sql = "DELETE FROM goods WHERE id=?";

        jdbcTemplate.update(sql, goodsId);
    }

    @Override
    public List<Zone> selectZonePrice(String goodsId) {
        String sql = "SELECT id, grade, name, price FROM zone WHERE goods_id = ? ORDER BY grade";

        List<Zone> list = jdbcTemplate.query(sql, zoneRowMapper(), goodsId);

        return list.isEmpty() ? null : list;
    }

    private RowMapper<Goods> goodsRowMapper() {
        return ((rs, rowNum) -> {
          Goods goods = new Goods();
          goods.setId(rs.getString("id"));
          goods.setGenreName(rs.getString("genre_name"));
          goods.setTitle(rs.getString("title"));
          goods.setDesc(rs.getString("desc"));
          goods.setLocation(rs.getString("location"));
          goods.setX(rs.getString("x"));
          goods.setY(rs.getString("y"));
          goods.setPerformanceStartDate(rs.getString("performance_start_date"));
          goods.setPerformanceEndDate(rs.getString("performance_end_date"));
          goods.setPerformanceTime(rs.getString("performance_time"));
          return goods;
        });
    }

    private RowMapper<Zone> zoneRowMapper() {
        return ((rs, rowNum) -> {
            Zone zone = new Zone();
            zone.setId(rs.getString("id"));
            zone.setGrade(rs.getString("grade"));
            zone.setName(rs.getString("name"));
            zone.setPrice(rs.getInt("price"));
            return zone;
        });
    }
}
