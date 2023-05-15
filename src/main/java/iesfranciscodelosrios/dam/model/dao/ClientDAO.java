package iesfranciscodelosrios.dam.model.dao;

import iesfranciscodelosrios.dam.model.connections.Connect;
import iesfranciscodelosrios.dam.model.domain.Client;
import iesfranciscodelosrios.dam.model.domain.ColorTestResult;

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements DAO<Client> {

    private final static String FINDALL = "SELECT * FROM client";
    private final static  String FINDBYID = "SELECT * FROM client WHERE id_client = ?";
    private final static  String INSERT = "INSERT INTO client(id_client, name, surname, telephone, email, password, colorTestResult) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final static  String UPDATE = "UPDATE client SET name = ?, surname = ?, telephone = ?, email = ?, password = ?, colorTestResult = ? WHERE id_client = ?";
    private final static  String DELETE = "DELETE FROM client WHERE id_client = ?";

    private Connection conn;

    public ClientDAO(Connection conn) { this.conn = conn; }

    public ClientDAO() { this.conn = Connect.getConnect(); }

    @Override
    public List<Client> findAll() throws SQLException {
        List<Client> result = new ArrayList<>();
        try(PreparedStatement pst = this.conn.prepareStatement(FINDALL)) {
            try(ResultSet res = pst.executeQuery()) {
                while(res.next()) {
                    Client client = new Client();
                    client.setId_person(res.getInt("id_client"));
                    client.setName(res.getString("name"));
                    client.setSurname(res.getString("surname"));
                    client.setTelephone(res.getString("telephone"));
                    client.setEmail(res.getString("email"));
                    client.setPassword(res.getString("password"));

                    String resultString = res.getString("colorTestResult");
                    ColorTestResult ctr = null;
                    for(ColorTestResult r: ColorTestResult.values()) {
                        if(r.name().equalsIgnoreCase(resultString)) {
                            ctr = r;
                            break;
                        }
                    }

                    if (result != null) {
                        client.setColorTestResult(ctr);

                    }
                    result.add(client);
                }
            }
        }
        return result;
    }

    @Override
    public Client findById(int id_client) throws SQLException {
        Client result = null;
        try(PreparedStatement pst = this.conn.prepareStatement(FINDBYID)) {
            pst.setInt(1, id_client);
            try(ResultSet res = pst.executeQuery()) {
                if(res.next()) {
                    Client client = new Client();
                    client.setId_person(res.getInt("id_client"));
                    client.setName(res.getString("name"));
                    client.setSurname(res.getString("surname"));
                    client.setTelephone(res.getString("telephone"));
                    client.setEmail(res.getString("email"));
                    client.setPassword(res.getString("password"));

                    String resultString = res.getString("colorTestResult");
                    ColorTestResult ctr = null;
                    for(ColorTestResult r: ColorTestResult.values()) {
                        if(r.name().equalsIgnoreCase(resultString)) {
                            ctr = r;
                            break;
                        }
                    }

                    if (result != null) {
                        client.setColorTestResult(ctr);
                    }

                    result = client;
                }
            }
        }
        return result;
    }

    public Client findByEmail(String email) throws SQLException {
        Client result = null;
        String query = "SELECT * FROM client WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Client();
                    result.setId_person(resultSet.getInt("id_client"));
                    result.setName(resultSet.getString("name"));
                    result.setSurname(resultSet.getString("surname"));
                    result.setTelephone(resultSet.getString("telephone"));
                    result.setEmail(resultSet.getString("email"));
                    result.setPassword(resultSet.getString("password"));
                    String resultString = resultSet.getString("colorTestResult");
                    ColorTestResult ctr = null;
                    for (ColorTestResult r : ColorTestResult.values()) {
                        if (r.name().equalsIgnoreCase(resultString)) {
                            ctr = r;
                            break;
                        }
                    }
                    result.setColorTestResult(ctr);
                }
            }
        }
        return result;
    }

    public boolean checkIfIdExists(int id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id_client = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean checkIfEmailExists(String email) throws SQLException {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Client save(Client entity) throws SQLException {
        Client result = new Client();
        if (entity != null) {
            Client client = findById(entity.getId_person());
            if (client == null) {
                try (PreparedStatement pst = this.conn.prepareStatement(INSERT)) {
                    pst.setInt(1, entity.getId_person());
                    pst.setString(2, entity.getName());
                    pst.setString(3, entity.getSurname());
                    pst.setString(4, entity.getTelephone());
                    pst.setString(5, entity.getEmail());
                    pst.setString(6, entity.getPassword());
                    pst.setString(7, entity.getColorTestResult().name());
                    pst.executeUpdate();
                }
            }
            result = entity;
        }
        return result;
    }

    @Override
    public Client update(Client entity) throws SQLException {
        Client result = new Client();
        if (entity != null) {
            Client client = findById(entity.getId_person());
            if (client != null) {
                try(PreparedStatement pst = this.conn.prepareStatement(UPDATE)) {
                    pst.setString(1, entity.getName());
                    pst.setString(2, entity.getSurname());
                    pst.setString(3, entity.getTelephone());
                    pst.setString(4, entity.getEmail());
                    pst.setString(5, entity.getPassword());
                    pst.setString(6, entity.getColorTestResult().name());
                    pst.setInt(7, entity.getId_person());
                    pst.executeUpdate();
                }
            }
            result = entity;
        }
        return result;
    }

    @Override
    public void delete(Client entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = this.conn.prepareStatement(DELETE)) {
                pst.setInt(1, entity.getId_person());
                pst.executeUpdate();
            }
        }
    }



    @Override
    public void close() throws Exception {

    }
}
