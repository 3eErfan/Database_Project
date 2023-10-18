<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Information Form</title>
</head>
<body>
    <div align="center">
        <form action="register" method="POST">
            <table border="1" cellpadding="5">
                <!-- Email -->
                <tr>
                    <th>Email: </th>
                    <td><input type="email" name="email" required></td>
                </tr>
                <!-- Password -->
                <tr>
                    <th>Password: </th>
                    <td><input type="password" name="password" required></td>
                </tr>
                <!-- Password confirmation-->
                <tr>
                    <th>Password confirmation: </th>
                    <td><input type="password" name="confirmation" required></td>
                </tr>
                <!-- First Name -->
                <tr>
                    <th>First Name: </th>
                    <td><input type="text" name="firstName" required></td>
                </tr>
                <!-- Last Name -->
                <tr>
                    <th>Last Name: </th>
                    <td><input type="text" name="lastName" required></td>
                </tr>
                <!-- Address Street Number -->
                <tr>
                    <th>Street Number: </th>
                    <td><input type="text" name="adress_street_num" required></td>
                </tr>
                <!-- Address Street -->
                <tr>
                    <th>Street: </th>
                    <td><input type="text" name="adress_street" required></td>
                </tr>
                <!-- City -->
                <tr>
                    <th>City: </th>
                    <td><input type="text" name="adress_city" required></td>
                </tr>
                <!-- State -->
                <tr>
                    <th>State: </th>
                    <td><input type="text" name="adress_state" required></td>
                </tr>
                <!-- Zip Code -->
                <tr>
                    <th>Zip Code: </th>
                    <td><input type="text" name="adress_zip_code" required></td>
                </tr>
                <!-- Phone Number -->
                <tr>
                    <th>Phone Number: </th>
                    <td><input type="tel" name="phone_number" required></td>
                </tr>
                <!-- Credit Card Number -->
                <tr>
                    <th>Credit Card Number: </th>
                    <td><input type="text" name="credit_card_no" required></td>
                </tr>
                <!-- CVV -->
                <tr>
                    <th>CVV: </th>
                    <td><input type="text" name="credit_card_cvv" maxlength="3" required></td>
                </tr>
                <!-- Credit Card Expiry Year -->
                <tr>
                    <th>Expiry Year: </th>
                    <td><input type="number" name="credit_card_ex_year" required></td>
                </tr>
                <!-- Credit Card Expiry Month -->
                <tr>
                    <th>Expiry Month: </th>
                    <td><input type="number" name="credit_card_ex_month" required></td>
                </tr>
                <!-- Submit Button -->
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Register"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
