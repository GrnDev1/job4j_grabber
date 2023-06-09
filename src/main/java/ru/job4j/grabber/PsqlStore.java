package ru.job4j.grabber;

import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private Connection cnn;

    public static Properties getProperties() {
        Properties config = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO grabber_sc.post(name, text, link, created) "
                + "VALUES (?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;";
        try (PreparedStatement st = cnn.prepareStatement(sql)) {
            st.setString(1, post.getTitle());
            st.setString(2, post.getDescription());
            st.setString(3, post.getLink());
            st.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM grabber_sc.post";
        try (PreparedStatement st = cnn.prepareStatement(sql);
             ResultSet set = st.executeQuery()) {
            while (set.next()) {
                Post post = getPost(set);
                list.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = new Post();
        String sql = "SELECT * FROM grabber_sc.post WHERE id = ?";
        try (PreparedStatement st = cnn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet set = st.executeQuery()) {
                while (set.next()) {
                    post = getPost(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private static Post getPost(ResultSet set) throws SQLException {
        Post post = new Post();
        post.setId(set.getInt("id"));
        post.setTitle(set.getString("name"));
        post.setDescription(set.getString("text"));
        post.setLink(set.getString("link"));
        post.setCreated(set.getTimestamp("created").toLocalDateTime());
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        String link = "https://career.habr.com/vacancies/java_developer";
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = habrCareerParse.list(link);
        try (PsqlStore psqlStore = new PsqlStore(getProperties())) {
            list.forEach(psqlStore::save);
            psqlStore.getAll().forEach(System.out::println);
            System.out.println();
            System.out.println(psqlStore.findById(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}