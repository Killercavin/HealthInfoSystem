// src/main/resources/static/js/app.js
document.addEventListener('DOMContentLoaded', function() {
    console.log('Health Information System loaded');

    // Example of fetching programs from the API
    async function fetchPrograms() {
        try {
            const response = await fetch('/api/programs');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            console.log('Programs:', data);
            return data;
        } catch (error) {
            console.error('Error fetching programs:', error);
            return [];
        }
    }

    // You can add more client-side functionality here
});