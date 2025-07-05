package org.example.persistence;

import org.example.entity.Workspace;
import org.example.enumtype.WorkspaceType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkspaceDAO {

    public void saveWorkspace(Workspace workspace) {
        String sql = "INSERT INTO workspaces (name, type, price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, workspace.getName());
            ps.setString(2, workspace.getType().name());
            ps.setDouble(3, workspace.getPrice());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                workspace.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Workspace> findAll() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM workspaces";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Workspace workspace = new Workspace(
                        rs.getString("name"),
                        WorkspaceType.valueOf(rs.getString("type")),
                        rs.getDouble("price")
                );
                workspace.setId(rs.getInt("id"));
                workspaces.add(workspace);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workspaces;
    }

    public Optional<Workspace> findById(int id) {
        String sql = "SELECT * FROM workspaces WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Workspace workspace = new Workspace(
                        rs.getString("name"),
                        WorkspaceType.valueOf(rs.getString("type")),
                        rs.getDouble("price")
                );
                workspace.setId(rs.getInt("id"));
                return Optional.of(workspace);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean updateWorkspace(int id, Workspace updated) {
        String sql = "UPDATE workspaces SET name = ?, type = ?, price = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, updated.getName());
            ps.setString(2, updated.getType().name());
            ps.setDouble(3, updated.getPrice());
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteWorkspace(int id) {
        String sql = "DELETE FROM workspaces WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
