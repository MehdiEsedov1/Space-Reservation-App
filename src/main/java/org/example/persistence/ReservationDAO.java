package org.example.persistence;

import org.example.entity.Interval;
import org.example.entity.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public void saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (name, space_id, start_time, end_time) VALUES (?, ?, ?, ?)";

        Interval interval = reservation.getInterval();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getName());
            ps.setInt(2, reservation.getSpaceId());
            ps.setTimestamp(3, new Timestamp(interval.getStartTime().getTime()));
            ps.setTimestamp(4, new Timestamp(interval.getEndTime().getTime()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT id, name, space_id, start_time, end_time FROM reservations";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int spaceId = rs.getInt("space_id");

                Interval interval = new Interval.IntervalBuilder()
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .build();

                Reservation reservation = new Reservation(name, spaceId, interval);
                reservation.setId(rs.getInt("id"));
                list.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
