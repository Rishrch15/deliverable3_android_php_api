<?php
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'user';

$conn = new mysqli($host, $user, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Set response header
header('Content-Type: application/json');

// Utility: Get value from either GET or POST
function get($key) {
    // Check if the parameter exists in either GET or POST
    return isset($_GET[$key]) ? $_GET[$key] : (isset($_POST[$key]) ? $_POST[$key] : null);
}

$action = get('action') ?? 'read_all';  // Get action from GET or POST

switch ($action) {
    case 'create':
        $name = get('name');
        $email = get('email');
        $password = get('password');

        if (empty($name)) {
            die(json_encode("Name is required"));
        }
        if (empty($email)) {
            die(json_encode("Email is required"));
        }
        if (empty($password)) {
            die(json_encode("Password is required"));
        }

        $stmt = $conn->prepare("INSERT INTO finalactivity (name, email, password) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $name, $email, $password);
        if ($stmt->execute()) {
            echo json_encode("User created successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    case 'read_all':
        $result = $conn->query("SELECT * FROM finalactivity");
        $users = [];

        while ($row = $result->fetch_assoc()) {
            $users[] = $row;
        }

        echo json_encode($users);
        break;

    case 'update':
        $id = get('id');
        $name = get('name');
        $email = get('email');
        $password = get('password');

        if (empty($id)) {
            die(json_encode("User ID is required for update"));
        }

        $stmt = $conn->prepare("UPDATE finalactivity SET name=?, email=?, password=? WHERE id=?");
        $stmt->bind_param("sssi", $name, $email, $password, $id);
        if ($stmt->execute()) {
            echo json_encode("User updated successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    case 'delete':
        $id = get('id');

        if (empty($id)) {
            die(json_encode("User ID is required for deletion"));
        }

        $stmt = $conn->prepare("DELETE FROM finalactivity WHERE id=?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            echo json_encode("User deleted successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    default:
        echo json_encode("Invalid action");
        break;
}

$conn->close();
?>
