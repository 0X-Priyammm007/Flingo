package flashcards;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardService {

    public void addFlashcard(int userId, String front, String back, String direction) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO flashcards (user_id, front_text, back_text, language_direction) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, front);
            stmt.setString(3, back);
            stmt.setString(4, direction);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Add flashcard failed: " + e.getMessage());
        }
    }

    public List<Flashcard> getFlashcards(int userId, String direction) {
        List<Flashcard> cards = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM flashcards WHERE user_id = ? AND language_direction = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, direction);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cards.add(new Flashcard(
                    rs.getInt("card_id"),
                    rs.getInt("user_id"),
                    rs.getString("front_text"),
                    rs.getString("back_text"),
                    rs.getString("language_direction")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get flashcards failed: " + e.getMessage());
        }
        return cards;
    }

    public void updateFlashcard(int cardId, String front, String back) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE flashcards SET front_text = ?, back_text = ? WHERE card_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, front);
            stmt.setString(2, back);
            stmt.setInt(3, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update flashcard failed: " + e.getMessage());
        }
    }

    public void deleteFlashcard(int cardId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM flashcards WHERE card_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete flashcard failed: " + e.getMessage());
        }
    }
}