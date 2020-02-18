package persist;

import exceptions.CrudException;
import model.Course;
import model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JdbcReviewCrudPersisterTest {
  private Connection conn;
  private JdbcCourseCrudPersister courseCrud;
  private JdbcReviewCrudPersister reviewCrud;
  private Course course;

  private String getResourcesPath() {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    return resourceDirectory.toFile().getAbsolutePath();
  }

  @Before
  public void setUp() throws SQLException {
    final String URI = "jdbc:sqlite:" + getResourcesPath() + "/db/Test.db";
    conn = DriverManager.getConnection(URI);

    String sql;
    Statement st = conn.createStatement();

    sql = "DROP TABLE IF EXISTS Courses;";
    st.execute(sql);

    sql = "DROP TABLE IF EXISTS Reviews;";
    st.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS Courses(" +
            "id INTEGER PRIMARY KEY, name VARCHAR(30), url VARCHAR(100));";
    st.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS Reviews(" +
            "id INTEGER PRIMARY KEY, courseId INTEGER NOT NULL,rating INTEGER NOT NULL, comment VARCHAR(500), FOREIGN KEY (courseId) REFERENCES Courses(id));";
    st.execute(sql);

    sql = "PRAGMA foreign_keys = ON;";
    st.execute(sql);
    // TODO: Implement me!

    courseCrud = new JdbcCourseCrudPersister(conn);
    reviewCrud = new JdbcReviewCrudPersister(conn);

    Course c1 = new Course("oose", "jhu-oose.com");
    Course c2 = new Course("Intro os", "jhu-os.com");
    Course c3 = new Course("Data Structures", "jhu-ds.com");
    courseCrud.create(c1);
    courseCrud.create(c2);
    courseCrud.create(c3);

  }

  @Test
  public void createReviewChangesId() throws SQLException {
    Review review = new Review(1, 1, "good");
    assertEquals(0, review.getId());
    reviewCrud.create(review);
    assertNotEquals(0, review.getId());

    // TODO: Implement me!
  }

  @Test
  public void readReviewWorks() {
    Review r1 = new Review(1, 1, "good");
    reviewCrud.create(r1);
    Review r2 = reviewCrud.read(r1.getId());
    assertEquals(r1, r2);

    // TODO: Implement me!
  }

  @Test
  public void updateReviewWorks() {
    Review r1 = new Review(1, 1, "good");
    reviewCrud.create(r1);

    r1.setRating(3);
    reviewCrud.update(r1);

    Review r2 = reviewCrud.read(r1.getId());
    assertEquals(r1, r2);
    // TODO: Implement me!
  }

  @Test
  public void deleteReviewWorks() {
    Review r1 = new Review(1, 1, "good");
    reviewCrud.create(r1);

    reviewCrud.delete(r1.getId());
    Review r2 = reviewCrud.read(r1.getId());
    assertNull(r2);
    // TODO: Implement me!
  }

  @Test
  public void readReviewsWorks() {
    Review r1 = new Review(1, 3, "good");
    Review r2 = new Review(2, 2, "okay");
    Review r3 = new Review(1, 1, "bad");

    reviewCrud.create(r1);
    reviewCrud.create(r2);
    reviewCrud.create(r3);

    List<Review> results = reviewCrud.readAll(1);
    assertTrue(results.contains(r1));
    assertTrue(results.contains(r3));
    assertFalse(results.contains(r2));

    // TODO: Implement me!
  }

  @Test (expected = CrudException.class)
  public void addingReviewToNonExistingCourseFails() {
    Review review = new Review(5,2,"cool");
    reviewCrud.create(review);


    // TODO: Implement me!
    //  By non-existing course, it means when adding a review, 
    //  use a course_id that does not exists in the Courses table.
  }

  @After
  public void tearDown() throws SQLException {
    conn.close();
  }
}