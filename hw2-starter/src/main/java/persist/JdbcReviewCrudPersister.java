package persist;

import exceptions.CrudException;
import model.Course;
import model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcReviewCrudPersister implements CrudPersister<Review> {

  private Connection conn;

  public JdbcReviewCrudPersister(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void create(Review review) throws CrudException {
    String sql = "INSERT INTO Reviews(courseId, rating, comment) VALUES (?, ?, ?)";
    PreparedStatement pst = null;
    try {
      pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      pst.setInt(1, review.getCourseId());
      pst.setInt(2, review.getRating());
      pst.setString(3, review.getComment());
      pst.executeUpdate();

      ResultSet rs = pst.getGeneratedKeys();
      rs.next();
      review.setId(rs.getInt(1));
    } catch (SQLException e) {
      throw new CrudException("Unable to create the review", e);
    }
  }

  @Override
  public Review read(int id) throws CrudException {
    String sql = "SELECT * FROM Reviews WHERE id = ?;";
    PreparedStatement pst = null;
    Review review = null;
    try {
      pst = conn.prepareStatement(sql);
      pst.setInt(1, id);
      ResultSet rs = pst.executeQuery();

      if (!rs.next()) return null;

      review = new Review(
              rs.getInt("courseId"),
              rs.getInt("rating"),
              rs.getString("comment")
      );
      review.setId(rs.getInt("id"));
    } catch (SQLException e) {
      throw new CrudException("Unable to read the course", e);
    }
    // TODO: Implement me!

    return review;
  }

  @Override
  public void update(Review review) throws CrudException {
    String sql = "UPDATE Reviews SET courseId = ?, rating = ?, comment = ? WHERE id = ?;";
    PreparedStatement pst = null;
    try {
      pst = conn.prepareStatement(sql);
      pst.setInt(1, review.getCourseId());
      pst.setInt(2, review.getRating());
      pst.setString(3, review.getComment());
      pst.setInt(4, review.getId());
      pst.executeUpdate();
    } catch (SQLException e) {
      throw new CrudException("Unable to update the review", e);
    }

    // TODO: Implement me!
  }

  @Override
  public void delete(int id) throws CrudException {
    String sql = "DELETE FROM Reviews WHERE id = ?;";
    PreparedStatement pst = null;
    try {
      pst = conn.prepareStatement(sql);
      pst.setInt(1, id);
      pst.executeUpdate();
    } catch (SQLException e) {
      throw new CrudException("Unable to delete review", e);
    }
    // TODO: Implement me!
  }

  /**
   * Return a list of all the reviews for a course with the given id.
   *
   * @param courseId the id (primary key) of a course.
   * @return list of reviews.
   */
  public List<Review> readAll(int courseId) {
    List<Review> reviews = new ArrayList<>();



    String sql = "SELECT * FROM Reviews WHERE courseId = ?";
    PreparedStatement pst = null;
    try {

      pst = conn.prepareStatement(sql);
      pst.setInt(1, courseId);
      ResultSet rs =pst.executeQuery();
      while (rs.next()) {
        Review review = new Review(
                rs.getInt("courseId"),
                rs.getInt("rating"),
                rs.getString("comment")
        );
        review.setId(rs.getInt("id"));
        reviews.add(review);

      }


    } catch (SQLException e) {
      throw new CrudException("Unable to read all reviews", e);
    }

    
    // TODO: Implement me!

    return reviews;
  }
}
