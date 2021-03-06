/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocase7;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static ocase7.Category.resultSet;
import static ocase7.Question.pstmt;
import static ocase7.User.pstmt;

/**
 *
 * @author Teilnehmer
 */
public class User {

    static Statement stmt = null;
    static PreparedStatement pstmt = null;
    static ResultSet resultSet = null;

    private int id;
    private String name;
    private String password;
    private Session session;
    private ArrayList<Integer> userSessionList;

    public Session getSession() {
        return session;
    }

    public User(int id, String name, String password, Session userSession) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.session = userSession;
    }

    public User(int userId) {
        this.id = userId;
        this.name = fetchUserNameByUserID(userId);
        this.password = fetchUserPasswordByUserID(userId);
        this.session = new Session();
        this.userSessionList = fetchUserSessionsList(userId);
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        id = getUserByLogin(name, password).getId();
        session = Session.getSession(this);
    }

    public User() {
        session = new Session();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

// Athor LYN & Eric
    public void insertUserAnswerIdIntoDb(User user) {
        final ArrayList<Card> cards = user.getSession().getCardBox().getCards();
        // userAntwortenPerSession speichern
        for (Card card : cards) {
            for (UserAnswer userAnswer : card.getUserAnswers()) {
                // nur gegebene Antworten speichern
                if (userAnswer.isGiven()) {
                    Session2UserAnswer.insert(
                            new Session2UserAnswer(user.getSession().getId(), userAnswer.getId()));
                }
            }
        }
        // FragezurSession speichern
        for (Card card : cards) {
            Session2Question.insert(new Session2Question(user.getSession().getId(), card.getId()));
        }
        // FragezurWiedervorlage/Gecheated speichern
        for (Card card : cards) {
            // gecheateteAntworten speichern
            if (card.isCheated()) {
                // c für cheated
                Again.insert(new Again("c", user.getSession().getId(), card.getId()));
            } else if (card.isFollowUp()) {
                // a für again
                Again.insert(new Again("a", user.getSession().getId(), card.getId()));
            }
        }
//        try {
//            Connection con = MySQLConnection.getConnection();
//            String sql = "INSERT INTO userAnswer (user_id, answer_id) VALUES( ?, ?)";
//            for (int i = 0; i < u.getSession().getCardBox().getCards().size(); i++) {
//                for (int c = 0; c < u.getSession().getCardBox().getCards().get(i).getUserAnswers().size(); c++) {
//                    pstmt = con.prepareStatement(sql);
//                    pstmt.setInt(1, u.getId());
//                    if (u.getSession().getCardBox().getCards().get(i).getUserAnswers().get(i).isGiven()) {
//                        pstmt.setInt(2, u.getSession().getCardBox().getCards().get(i).getUserAnswers().get(c).getId());
//                    }
//                    stmt = con.createStatement();
//                    resultSet = stmt.executeQuery(sql);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage() + " Das Klappt leider noch nicht.");
//        } finally {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//
//                }
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
    }

    public static ArrayList<User> getAll() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            Connection con = MySQLConnection.getConnection();
            String sql = "SELECT * FROM user";
            pstmt = con.prepareStatement(sql);
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                userList.add(new User(resultSet.getInt("id")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return userList;
    }

    public User getUserById(int id) {
        User user = new User(id);
//        try {
//            Connection con = MySQLConnection.getConnection();
//            String sql = "SELECT * FROM user WHERE id = ?";
//            pstmt = con.prepareStatement(sql);
//            pstmt.setInt(1, id);
//            resultSet = pstmt.executeQuery();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", password=" + password + '}';
    }

    private ArrayList<Integer> fetchUserSessionsList(int userID) {
        ArrayList<Integer> sessionsList = new ArrayList<>();
        try {
            Connection con = MySQLConnection.getConnection();
            String sql = "SELECT id FROM session WHERE user_id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                sessionsList.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sessionsList;
    }

    private String fetchUserNameByUserID(int userID) {
        String userName = null;
        try {
            Connection con = MySQLConnection.getConnection();
            String sql = "SELECT name FROM user WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                userName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userName;
    }

    private String fetchUserPasswordByUserID(int userID) {
        String userPassword = null;
        try {
            Connection con = MySQLConnection.getConnection();
            String sql = "SELECT password FROM user WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                userPassword = resultSet.getString("password");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userPassword;
    }

    public User getUserByLogin(String userName, String userPw) {
        User user = null;
        int userId = 0;
        try {
            Connection con = MySQLConnection.getConnection();
            String sql = "SELECT id FROM user WHERE name = ? AND password = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, userPw);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (userId != 0) {
            user = getUserById(userId);
        }

        return user;
    }

}
