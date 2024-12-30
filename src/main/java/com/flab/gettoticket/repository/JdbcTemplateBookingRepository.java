package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.BookingSeatDetail;
import com.flab.gettoticket.entity.Booking;
import com.flab.gettoticket.entity.BookingSeat;
import com.flab.gettoticket.enums.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class JdbcTemplateBookingRepository implements BookingRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Booking> selectBookingList(String email) {
        String sql = """
                SELECT 
                    id
                    , status
                    , goods_id
                    , play_time_id
                    , user_id
                    , user_name
                    , booking_at
                FROM booking 
                WHERE user_id=?
                AND status NOT IN ('cancelled')
                ORDER BY booking_at
                """;

        return jdbcTemplate.query(sql, bookingRowMapper(), email);
    }

    @Override
    public Booking selectBooking(long id, String email) {
        String sql = """
                SELECT 
                    id
                    , status
                    , goods_id
                    , play_time_id
                    , user_id
                    , user_name
                    , booking_at
                FROM booking 
                WHERE id=?
                AND user_id=?
                """;

        List<Booking> list = jdbcTemplate.query(sql, bookingRowMapper(), id, email);

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<BookingSeat> selectBookingSeat(long bookingId) {
        String sql = """
                SELECT
                    id
                    , status
                    , seat_id
                FROM booking_seat
                WHERE booking_id=?
                """;

        return jdbcTemplate.query(sql, bookingSeatRowMapper(), bookingId);
    }

    @Override
    public List<BookingSeatDetail> selectBookingSeatDetailList(long bookingId) {
        String sql = """
                SELECT
                    a.id
                    , a.status
                    , c.name AS zone_name
                    , c.grade
                    , c.price
                    , b.name AS seat_name
                    , b.col
                    , b.floor
                FROM booking_seat a
                LEFT JOIN seat b
                ON a.seat_id = b.id
                LEFT JOIN zone c
                ON b.zone_id = c.id
                WHERE booking_id=?
                """;

        return jdbcTemplate.query(sql, bookingSeatDetailMapper(), bookingId);
    }

    @Override
    public int selectValidBookingSeatCount(long bookingId) {
        String sql = """
                SELECT COUNT(*) AS cnt
                FROM booking_seat
                WHERE booking_id=?
                AND status NOT IN ('cancelled')
                """;
        String cnt = jdbcTemplate.queryForObject(sql, String.class, bookingId);

        return cnt != null ? Integer.parseInt(cnt) : 0;
    }

    @Override
    public long selectBookingLastSeq() {
        String sqlForLastSequence = "SELECT LASTVAL(booking_seq) FROM DUAL";
        String bookingLastSeq = jdbcTemplate.queryForObject(sqlForLastSequence, String.class);

        return bookingLastSeq != null ? Long.parseLong(bookingLastSeq) : 0L;
    }

    @Override
    public long selectBookingSeatLastSeq() {
        String sqlForLastSequence = "SELECT LASTVAL(bookingSeat_seq) FROM DUAL";
        String bookingSeatLastSeq = jdbcTemplate.queryForObject(sqlForLastSequence, String.class);

        return bookingSeatLastSeq != null ? Long.parseLong(bookingSeatLastSeq) : 0L;
    }

    @Override
    public void updateBooking(Booking booking) {
        String sql = """
                UPDATE booking SET
                    status=?
                    , update_at=now()
                WHERE id=?
                AND user_id=?
                """;

        long id = booking.getId();
        String userId = booking.getUserId();
        BookingStatus bookingStatus = booking.getStatus();
        int status = bookingStatus.getCode();

        jdbcTemplate.update(sql, status, id, userId);
    }

    @Override
    public void updateBookingSeat(BookingSeat bookingSeat) {
        String sql = """
                UPDATE booking_seat SET
                    status=?
                    , booking_id=?
                    , update_at=now()
                WHERE id=?
                AND user_id=?
                """;

        long id = bookingSeat.getId();
        long bookigId = bookingSeat.getBookingId();
        BookingStatus bookingStatus = bookingSeat.getStatus();
        int status = bookingStatus.getCode();
        String userId = bookingSeat.getUserId();

        jdbcTemplate.update(sql, status, bookigId, id, userId);
    }

    @Override
    public long insertBooking(Booking booking) {
        String sqlForSequence = "SELECT NEXTVAL(booking_seq) FROM DUAL";

        String sql = """
                INSERT INTO booking (
                    id
                    , status
                    , goods_id
                    , play_time_id
                    , user_id
                    , user_name
                )
                VALUES (?,?,?,?,?,?)    
                """;

        String bookingSeq = jdbcTemplate.queryForObject(sqlForSequence, String.class);
        long id = bookingSeq != null ? Long.parseLong(bookingSeq) : 0L;
        BookingStatus bookingStatus = booking.getStatus();
        int status = bookingStatus.getCode();
        long goodsId = booking.getGoodsId();
        long playTimeId = booking.getPlayTimeId();
        String userId = booking.getUserId();
        String userName = booking.getUserName();

        int result = jdbcTemplate.update(sql, id, status, goodsId, playTimeId, userId, userName);
        long seq = 0L;

        if(result > 0) seq = selectBookingLastSeq();

        return seq;
    }

    @Override
    public void insertBookingSeat(BookingSeat bookingSeat) {
        String sqlForSequence = "SELECT NEXTVAL(bookingSeat_seq) FROM DUAL";

        String sql = """
                INSERT INTO booking_seat (
                    id
                    , status
                    , booking_id
                    , seat_id
                    , user_id
                )
                VALUES (?,?,?,?,?)
                """;

        String bookingSeatSeq = jdbcTemplate.queryForObject(sqlForSequence, String.class);
        long id = bookingSeatSeq != null ? Long.parseLong(bookingSeatSeq) : 0L;
        BookingStatus bookingStatus = bookingSeat.getStatus();
        int status = bookingStatus.getCode();
        long bookingId = bookingSeat.getBookingId();
        long seatId = bookingSeat.getSeatId();
        String userId = bookingSeat.getUserId();

        jdbcTemplate.update(sql, id, status, bookingId, seatId, userId);
    }

    private RowMapper<Booking> bookingRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            int status = rs.getInt("status");
            long goodsId = rs.getLong("goods_id");
            long playTimeId = rs.getLong("play_time_id");
            String userid = rs.getString("user_id");
            String userName = rs.getString("user_name");
            LocalDateTime bookingAt = rs.getTimestamp("booking_at").toLocalDateTime();

            BookingStatus bookingStatus = BookingStatus.setCode(status);

            return Booking.builder()
                    .id(id)
                    .status(bookingStatus)
                    .goodsId(goodsId)
                    .playTimeId(playTimeId)
                    .userName(userName)
                    .bookingAt(bookingAt)
                    .build();
        });
    }

    private RowMapper<BookingSeat> bookingSeatRowMapper() {
        return ((rs, rowNum) -> {
            long id = rs.getLong("id");
            int status = rs.getInt("status");
            long seatId = rs.getLong("seat_id");

            BookingStatus bookingStatus = BookingStatus.setCode(status);

            return BookingSeat.builder()
                    .id(id)
                    .status(bookingStatus)
                    .seatId(seatId)
                    .build();
        });
    }

    private RowMapper<BookingSeatDetail> bookingSeatDetailMapper() {
        return ((rs, rowNum) -> {
            long bookingSeatId = rs.getLong("id");
            int bookingSeatStatus = rs.getInt("status");
            String zoneName = rs.getString("zone_name");
            int grade = rs.getInt("grade");
            int price = rs.getInt("price");
            String seatName = rs.getString("seat_name");
            int col = rs.getInt("col");
            int floor = rs.getInt("floor");

            BookingStatus bookingStatus = BookingStatus.setCode(bookingSeatStatus);

            return BookingSeatDetail.builder()
                    .bookingSeatId(bookingSeatId)
                    .bookingSeatStatus(bookingStatus)
                    .zoneName(zoneName)
                    .grade(grade)
                    .price(price)
                    .seatName(seatName)
                    .col(col)
                    .floor(floor)
                    .build();
        });
    }
}
