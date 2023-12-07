<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tree Information Form</title>
    <script type="text/javascript">
        function updateTreeInputs() {
            var numberOfTrees = document.getElementById('treeCount').value;
            // Hide all tree inputs initially
            for (var i = 1; i <= 3; i++) {
                var treeSection = document.getElementById('tree' + i);
                treeSection.style.display = 'none';
            }
            // Show the selected number of tree inputs
            for (var i = 1; i <= numberOfTrees; i++) {
                var treeSection = document.getElementById('tree' + i);
                treeSection.style.display = 'block';
            }
        }
    </script>
</head>
<body onload="updateTreeInputs()">
    <div align="center">
        <h2>Tree Information Submission</h2>
        <form action="submitTreeInfo" method="POST">
            <!-- Dropdown for number of trees -->
            <select id="treeCount" name="treeCount" onchange="updateTreeInputs()">
                <option value="1">1 Tree</option>
                <option value="2">2 Trees</option>
                <option value="3">3 Trees</option>
            </select>
            
            <!-- Tree information sections -->
                <!-- Tree 1 information fields -->
<div id="tree1" style="display: none;">
    <table border="1" cellpadding="5">
        <tr>
            <th>Photo 1: </th>
            <td><input type="file" name="tree1_photo_1" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 2: </th>
            <td><input type="file" name="tree1_photo_2" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 3: </th>
            <td><input type="file" name="tree1_photo_3" accept="image/*"></td>
        </tr>
        <tr>
            <th>Size (inch): </th>
            <td><input type="number" name="tree1_size"  ></td>
        </tr>
        <tr>
            <th>Height (feet): </th>
            <td><input type="number" name="tree1_height" step="any"  ></td>
        </tr>
        <tr>
            <th>Location: </th>
            <td><input type="text" name="tree1_location"></td>
        </tr>
        <tr>
            <th>Note: </th>
            <td><textarea name="tree1_note" rows="4" cols="50"></textarea></td>
        </tr>
    </table>
</div>
                
                
                <!-- Tree 2 information fields -->
           
<div id="tree2" style="display: none;">
    <table border="1" cellpadding="5">
        <tr>
            <th>Photo 1: </th>
            <td><input type="file" name="tree2_photo_1" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 2: </th>
            <td><input type="file" name="tree2_photo_2" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 3: </th>
            <td><input type="file" name="tree2_photo_3" accept="image/*"></td>
        </tr>
        <tr>
            <th>Size (inch): </th>
            <td><input type="number" name="tree2_size"  ></td>
        </tr>
        <tr>
            <th>Height (feet): </th>
            <td><input type="number" name="tree2_height" step="any"  ></td>
        </tr>
        <tr>
            <th>Location: </th>
            <td><input type="text" name="tree2_location"></td>
        </tr>
        <tr>
            <th>Note: </th>
            <td><textarea name="tree2_note" rows="4" cols="50"></textarea></td>
        </tr>
    </table>
</div>
                
                
                <!-- Tree 3 information fields -->
<div id="tree3" style="display: none;">
    <table border="1" cellpadding="5">
        <tr>
            <th>Photo 1: </th>
            <td><input type="file" name="tree3_photo_1" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 2: </th>
            <td><input type="file" name="tree3_photo_2" accept="image/*"></td>
        </tr>
        <tr>
            <th>Photo 3: </th>
            <td><input type="file" name="tree3_photo_3" accept="image/*"></td>
        </tr>
        <tr>
            <th>Size (inch): </th>
            <td><input type="number" name="tree3_size"  ></td>
        </tr>
        <tr>
            <th>Height (feet): </th>
            <td><input type="number" name="tree3_height" step="any"  ></td>
        </tr>
        <tr>
            <th>Location: </th>
            <td><input type="text" name="tree3_location"></td>
        </tr>
        <tr>
            <th>Note: </th>
            <td><textarea name="tree3_note" rows="4" cols="50"></textarea></td>
        </tr>
    </table>
</div>
                
                

            <!-- Rest of the form and other inputs -->
            <!-- ... -->

            <!-- Submit Button -->
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Submit Tree Info"/>
                </td>
            </tr>
        </form>
    </div>
</body>
</html>
