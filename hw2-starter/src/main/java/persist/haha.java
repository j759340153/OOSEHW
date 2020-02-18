package persist;

import model.Course;
import exceptions.CrudException;
import model.Review;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;


public class haha {
    public static String getResourcesPath() {
        Path resourceDirectory = Paths.get("src", "main","java","persist", "resources");
        return resourceDirectory.toFile().getAbsolutePath();
    }

    public static void main(String[] args) throws SQLException{
        Course c1 = new Course("oose", "jhu-oose.com");
        Course c2 = new Course("Intro os", "jhu-os.com");
        Course c3 = new Course("Data Structures", "jhu-ds.com");
        final String URI = "jdbc:sqlite:" + getResourcesPath() + "/db/Test.db";
        Connection conn = DriverManager.getConnection(URI);

        String sql;
        Statement st = conn.createStatement();
        sql = "DROP TABLE IF EXISTS Courses;";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Courses(" +
                "id INTEGER PRIMARY KEY, name VARCHAR(30), url VARCHAR(100));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Reviews(" +
                "id INTEGER PRIMARY KEY, courseId INTEGER,rating INTEGER, comment VARCHAR(500), FOREIGN KEY (courseId) REFERENCES Courses(id));";
        st.execute(sql);

        sql = "PRAGMA foreign_keys = ON;";
        st.execute(sql);

        JdbcCourseCrudPersister courseCrud = new JdbcCourseCrudPersister(conn);
        Course course = new Course(null, null);
        courseCrud.create(course);









    }
}
