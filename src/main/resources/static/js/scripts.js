/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project
document.getElementById('messageForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission
    const message = document.getElementById('messageInput').value;

    response = fetch('/send-message', { // The URL to your endpoint
        method: 'POST', // Using the POST method
        headers: {
            'Content-Type': 'application/json', // Specifying JSON content type
        },
        body: JSON.stringify({ message: message }), // Sending the message in the request body
    })
        .then(response => response.json()) // Parsing the JSON response
        .then(data => document.getElementById('messageOutput').value = data.reply) // Handling the response data
        .catch(error => console.error('Error:', error)); // Handling any errors

    document.getElementById('messageInput').value = ''; // Optionally clear the input field
});
