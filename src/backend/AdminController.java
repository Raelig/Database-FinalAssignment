package backend;

import java.sql.*;

public class AdminController {
    Connection con;

    public boolean update(String table, String primaryKey, String attributes, String newValues) {
        connect();
        String[] attributeQueries = attributes.split(",");
        String[] newValueQueries = newValues.split(",");
        String pk = "";

        if (table.toLowerCase() == "customer") {
            pk = "customer_id";
        } else if (table.toLowerCase() == "driver") {
            pk = "driver_id";
        } else if (table.toLowerCase() == "travel") {
            pk = "travel_id";
        } else if (table.toLowerCase() == "customertravel") {
            pk = "booking_id";
        } else if (table.toLowerCase() == "city") {
            pk = "city_name";
        }

        if (pk == "") {
            return false;
        }

        if (pk.equals("city_name")) {
            System.out.println("we do this one");
            for (int i = 0; i < attributeQueries.length; i++) {
                try {
                    boolean isInt = false;
                    int intValue = 0;
                    try {
                        intValue = Integer.parseInt(newValueQueries[i]);
                        isInt = true;
                    } catch (Exception e) {
                        isInt = false;
                    }
                    if (isInt) {
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = ? WHERE " + pk + " = ?");
                        statement.setInt(1, intValue);
                        statement.setString(2, primaryKey);
                        statement.execute();
                    } else {
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = ? WHERE " + pk + " = ?");
                        statement.setString(1, newValueQueries[i]);
                        statement.setString(2, primaryKey);
                        statement.execute();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    disconnect();
                    return false;
                }
            }
        }else if(pk.equals("booking_id")) {
            try {
            PreparedStatement beginStatement = con.prepareStatement("BEGIN");
            beginStatement.execute();
            for (int i = 0; i < attributeQueries.length; i++) {

                    boolean isInt = false;
                    int intValue = 0;
                    try {
                        intValue = Integer.parseInt(newValueQueries[i]);
                        isInt = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        isInt = false;
                    }
                    int primKey = Integer.parseInt(primaryKey);
                    if (isInt) {
                        if(attributeQueries[i].equals("nbr_of_seats_booked")){
                            System.out.println("hello");
                            PreparedStatement selectSeatStatement = con.prepareStatement("SELECT nbr_of_seats_booked, travel_id FROM customertravel WHERE booking_id = ?");
                            selectSeatStatement.setInt(1,primKey);
                            int seatsdiff = 0;
                            int travelId = 0;
                            ResultSet seatRes = selectSeatStatement.executeQuery();
                            while (seatRes.next()){
                                seatsdiff = seatRes.getInt(1);
                                travelId = seatRes.getInt(2);
                            }
                            seatsdiff = (seatsdiff - Integer.parseInt(newValueQueries[i]));
                            PreparedStatement updatetravelStatement = con.prepareStatement("UPDATE Travel SET travel_seatsavailable = travel_seatsavailable + ? WHERE travel_id = ?");
                            updatetravelStatement.setInt(1,seatsdiff);
                            updatetravelStatement.setInt(2, travelId);
                            updatetravelStatement.execute();
                        }
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = ? WHERE " + pk + " = ?");
                        statement.setInt(1, intValue);
                        statement.setInt(2, primKey);
                        statement.execute();

                    } else {
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = " + "'" + newValueQueries[i] + "'" + " WHERE " + pk + " = ?");
                        statement.setInt(1, primKey);
                        statement.execute();

                    }

                }
                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
                return false;
            }

        }
        else {
            for (int i = 0; i < attributeQueries.length; i++) {
                try {
                    boolean isInt = false;
                    int intValue = 0;
                    try {
                        intValue = Integer.parseInt(newValueQueries[i]);
                        isInt = true;
                    } catch (Exception e) {
                        isInt = false;
                    }
                    int primKey = Integer.parseInt(primaryKey);
                    if (isInt) {
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = ? WHERE " + pk + " = ?");
                        statement.setInt(1, intValue);
                        statement.setInt(2, primKey);
                        statement.execute();
                    } else {
                        PreparedStatement statement = con.prepareStatement("UPDATE " + table + " SET " + attributeQueries[i] + " = " + "'" + newValueQueries[i] + "'" + " WHERE " + pk + " = ?");
                        statement.setInt(1, primKey);
                        statement.execute();

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    disconnect();
                    return false;
                }
            }
        }
        disconnect();
        return true;
    }


    public String showInfo(String table) {
        connect();
        StringBuffer result = new StringBuffer();
        System.out.println(table);

        if (table.toLowerCase() == "customer") {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM " + table);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    result.append("customer_id: " + res.getInt(1) + " customer_fname: "
                            + res.getString(2) + " customer_lname: "
                            + res.getString(3) + " customer_address: "
                            + res.getString(4) + " customer_email: "
                            + res.getString(5) + " customer_phonenumber: "
                            + res.getString(6) + " customer_zipcode: "
                            + res.getInt(7) + " customer_city:"
                            + res.getString(8) + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
            }
        } else if (table.toLowerCase() == "driver") {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM " + table);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    result.append("driver_id: " + res.getInt(1) + " driver_personnumber: "
                            + res.getString(2) + " driver_fname: "
                            + res.getString(3) + " driver_lname: "
                            + res.getString(4) + " driver_address: "
                            + res.getString(5) + " driver_telephonenumber: "
                            + res.getString(6) + " driver_zipcode: "
                            + res.getInt(7) + " driver_city:"
                            + res.getString(8) + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
            }
        } else if (table.toLowerCase() == "travel") {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM " + table);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    result.append("travel_id: " + res.getInt(1) + " travel_from: "
                            + res.getString(2) + " travel_to: "
                            + res.getString(3) + " travel_departure: "
                            + res.getTimestamp(4) + " travel_arrival: "
                            + res.getTimestamp(5) + " travel_price: "
                            + res.getInt(6) + " travel_seatsavailable: "
                            + res.getInt(7) + " travel_seatsamount: "
                            + res.getInt(8) + " travel_driverid: "
                            + res.getInt(9) + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
            }
        } else if (table.toLowerCase() == "customertravel") {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM " + table);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    result.append("booking_id: " + res.getInt(1) + " customer_id: "
                            + res.getInt(2) + " travel_id: "
                            + res.getInt(3) + " nbr_of_seats_booked: "
                            + res.getInt(4) + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
            }
        } else if (table.toLowerCase() == "city") {
            try {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM " + table);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    result.append("city_name: " + res.getString(1) + " city_countryname: "
                            + res.getString(2) + " city_streetaddress: "
                            + res.getString(3) + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                disconnect();
            }

        }
        disconnect();
        return result.toString();
    }

    public boolean delete(String table, String primaryKey) {
        connect();
        String pk = "";

        if (table.toLowerCase() == "customer") {
            pk = "customer_id";
        } else if (table.toLowerCase() == "driver") {
            pk = "driver_id";
        } else if (table.toLowerCase() == "travel") {
            pk = "travel_id";
        } else if (table.toLowerCase() == "customertravel") {
            pk = "booking_id";
        } else if (table.toLowerCase() == "city") {
            pk = "city_name";
        }

        if (pk == "") {
            return false;
        }

        try {
            if (pk == "city_name") {
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();
                PreparedStatement travelStatement = con.prepareStatement("SELECT travel_id FROM Travel WHERE travel_from = ?");
                travelStatement.setString(1, primaryKey);
                ResultSet res = travelStatement.executeQuery();
                while (res.next()) {
                    int travelId = res.getInt(1);
                    PreparedStatement customerTravelDelete = con.prepareStatement("DELETE FROM CustomerTravel WHERE travel_id = ?");
                    customerTravelDelete.setInt(1, travelId);
                    customerTravelDelete.execute();
                    PreparedStatement travelDelete = con.prepareStatement("DELETE FROM Travel WHERE travel_id = ?");
                    travelDelete.setInt(1, travelId);
                    travelDelete.execute();
                }


                PreparedStatement travelToStatement = con.prepareStatement("SELECT travel_id FROM Travel WHERE travel_to = ?");
                travelToStatement.setString(1, primaryKey);
                ResultSet resTo = travelToStatement.executeQuery();
                while (resTo.next()) {
                    int travelId = resTo.getInt(1);
                    PreparedStatement customerTravelDelete = con.prepareStatement("DELETE FROM CustomerTravel WHERE travel_id = ?");
                    customerTravelDelete.setInt(1, travelId);
                    customerTravelDelete.execute();
                    PreparedStatement travelToDelete = con.prepareStatement("DELETE FROM Travel WHERE travel_id = ?");
                    travelToDelete.setInt(1, travelId);
                    travelToDelete.execute();

                }
                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + "LOWER(" + pk + ") = LOWER(?)");
                statement.setString(1, primaryKey);
                statement.execute();
                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();
            } else if (pk == "booking_id") {
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();

                int primKey = Integer.parseInt(primaryKey);

                PreparedStatement statement1 = con.prepareStatement("SELECT customer_id FROM Travel WHERE booking_id = ?");
                statement1.setInt(1, primKey);
                ResultSet res = statement1.executeQuery();

                int customerId = 0;

                while (res.next()) {
                    customerId = res.getInt(1);
                }

                PreparedStatement statement2 = con.prepareStatement("UPDATE travel SET travel_seatsavailable = travel_seatsavailable + (SELECT nbr_of_seats_booked FROM customertravel WHERE customer_id = ?) WHERE customertravel.travel_id = travel.travel_id AND customer." + pk + "= customertravel." + pk);
                statement1.setInt(1, customerId);
                statement2.execute();

                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + pk + " = ?");
                statement.setInt(1, primKey);
                statement.execute();

                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();

            } else if (pk == "booking_id") {
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();

                int primKey = Integer.parseInt(primaryKey);

                PreparedStatement statement1 = con.prepareStatement("SELECT customer_id FROM customertravel WHERE booking_id = ?");
                statement1.setInt(1, primKey);
                ResultSet res = statement1.executeQuery();

                int customerId = 0;

                while (res.next()) {
                    customerId = res.getInt(1);
                }

                PreparedStatement travelIdStatement = con.prepareStatement("SELECT travel_id FROM customertravel WHERE booking_id = ?");
                travelIdStatement.setInt(1, primKey);
                ResultSet travelIDres = travelIdStatement.executeQuery();

                int travelId = 0;

                while (travelIDres.next()) {
                    travelId = travelIDres.getInt(1);
                }

                PreparedStatement statement2 = con.prepareStatement("UPDATE travel SET travel_seatsavailable = travel_seatsavailable + (SELECT nbr_of_seats_booked FROM customertravel WHERE booking_id = ? ) WHERE travel.travel_id = ?");
                statement2.setInt(1, primKey);
                statement2.setInt(2, travelId);
                statement2.execute();

                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + pk + " = ?");
                statement.setInt(1, primKey);
                statement.execute();

                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();

            } else if (pk == "travel_id") {
                int primKey = Integer.parseInt(primaryKey);
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();
                PreparedStatement statement2 = con.prepareStatement("DELETE FROM customertravel WHERE " + pk + " = ?");
                statement2.setInt(1, primKey);
                statement2.execute();
                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + pk + " = ?");
                statement.setInt(1, primKey);
                statement.execute();
                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();


            } else if (pk == "driver_id") {

                int primKey = Integer.parseInt(primaryKey);
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();
                PreparedStatement statement2 = con.prepareStatement("UPDATE travel SET travel_driverid = ?");
                statement2.setInt(1, 0);
                statement2.execute();
                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + pk + " = ?");
                statement.setInt(1, primKey);
                statement.execute();
                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();

            } else if (pk == "customer_id") {

                int primKey = Integer.parseInt(primaryKey);
                PreparedStatement beginStatement = con.prepareStatement("BEGIN");
                beginStatement.execute();

                PreparedStatement selectStatement = con.prepareStatement("SELECT nbr_of_seats_booked,travel_id FROM customertravel WHERE customer_id = ?");
                selectStatement.setInt(1, primKey);
                ResultSet selectRes = selectStatement.executeQuery();
                while(selectRes.next()){
                    PreparedStatement updateStatement = con.prepareStatement("UPDATE Travel SET travel_seatsAvailable = travel_seatsavailable + ? WHERE travel_id = ?" );
                    updateStatement.setInt(1,selectRes.getInt(1));
                    updateStatement.setInt(2,selectRes.getInt(2));

                    PreparedStatement statement3 = con.prepareStatement("DELETE FROM customertravel WHERE " + pk + " = ?");
                    statement3.setInt(1, primKey);
                    statement3.execute();
                }

                PreparedStatement statement = con.prepareStatement("DELETE FROM " + table + " WHERE " + pk + " = ?");
                statement.setInt(1, primKey);
                statement.execute();

                PreparedStatement commitStatement = con.prepareStatement("COMMIT");
                commitStatement.execute();
            }



        } catch (SQLException e) {
            e.printStackTrace();
            disconnect();
            return false;
        }


        disconnect();
        return true;
    }

    public boolean insert(String table, String attr) {
        String[] attributes = attr.split(",");

        if (table.toLowerCase() == "customer") {
            return insertCustomer(attributes);
        } else if (table.toLowerCase() == "driver") {
            return insertDriver(attributes);
        } else if (table.toLowerCase() == "travel") {
            return insertTravel(attributes);
        } else if (table.toLowerCase() == "customertravel") {
            return insertCustomerTravel(attributes);
        } else if (table.toLowerCase() == "city") {
            return insertCity(attributes);
        }
        return false;
    }

    private boolean insertCustomer(String[] attributes) {
        connect();
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO Customer (customer_fname, customer_lname, customer_address, customer_email, customer_phoneNumber, customer_zipcode, customer_city) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, attributes[0]);
            statement.setString(2, attributes[1]);
            statement.setString(3, attributes[2]);
            statement.setString(4, attributes[3]);
            statement.setString(5, attributes[4]);
            statement.setInt(6, Integer.parseInt(attributes[5]));
            statement.setString(7, attributes[6]);
            statement.execute();
        } catch (Exception e) {
            disconnect();
            return false;
        }
        disconnect();
        return true;
    }

    private boolean insertDriver(String[] attributes) {
        connect();

        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO Driver (driver_personnumber, driver_fname, driver_lname, driver_address, driver_telephonenumber, driver_zipcode, driver_city) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, attributes[0]);
            statement.setString(2, attributes[1]);
            statement.setString(3, attributes[2]);
            statement.setString(4, attributes[3]);
            statement.setString(5, attributes[4]);
            statement.setInt(6, Integer.parseInt(attributes[5]));
            statement.setString(7, attributes[6]);
            statement.execute();
        } catch (Exception e) {
            disconnect();
            return false;
        }
        disconnect();
        return true;
    }

    private boolean insertTravel(String[] attributes) {
        connect();
        for (int i = 0; i < attributes.length; i++) {
            System.out.println(attributes[i]);
        }
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO Travel (travel_to, travel_from, travel_departure, travel_arrival, travel_price, travel_seatsavailable, travel_seatsamount, travel_driverid) " +
                    "VALUES(?, ?, " + "'" + attributes[2] + "'" + ", " + "'" + attributes[3] + "'" + ", ?, ?, ?, ?)");
            statement.setString(1, attributes[0]);
            statement.setString(2, attributes[1]);
            statement.setInt(3, Integer.parseInt(attributes[4]));
            statement.setInt(4, Integer.parseInt(attributes[5]));
            statement.setInt(5, Integer.parseInt(attributes[6]));
            statement.setInt(6, Integer.parseInt(attributes[7]));
            statement.execute();
        } catch (Exception e) {
            disconnect();
            e.printStackTrace();
            return false;
        }
        disconnect();
        return true;
    }

    private boolean insertCustomerTravel(String[] attributes) {
        connect();
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO Customertravel (customer_id, travel_id, nbr_of_seats_booked) " +
                    "VALUES(?, ?, ?)");
            statement.setInt(1, Integer.parseInt(attributes[0]));
            statement.setInt(2, Integer.parseInt(attributes[1]));
            statement.setInt(3, Integer.parseInt(attributes[2]));
            statement.execute();
        } catch (Exception e) {
            disconnect();
            return false;
        }
        disconnect();
        return true;
    }

    private boolean insertCity(String[] attributes) {
        connect();
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO City (city_name, city_countryname, city_streetaddress) " +
                    "VALUES(?, ?, ?)");
            statement.setString(1, attributes[0]);
            statement.setString(2, attributes[1]);
            statement.setString(3, attributes[2]);
            statement.execute();
        } catch (Exception e) {
            disconnect();
            return false;
        }
        disconnect();
        return true;
    }


    public void connect() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();

            con = DriverManager.getConnection("jdbc:postgresql://pgserver.mah.se/traveldata_grp1_nov6?user=aj0739&password=6lg2f7p2");
        } catch (Exception e) {
            try {
                con.close();
            } catch (SQLException ex) {
            }
        }
    }

    public void disconnect() {
        try {
            con.close();
        } catch (Exception e) {
            System.exit(0);
        }
    }

}
