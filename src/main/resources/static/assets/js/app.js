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
            displayPrograms(data);  // Update the UI with the fetched programs
        } catch (error) {
            console.error('Error fetching programs:', error);
            showErrorMessage('Failed to load programs. Please try again later.');  // Show a user-friendly error message
        }
    }

    // Function to display the programs in the DOM
    function displayPrograms(programs) {
        const programsContainer = document.getElementById('programsContainer');  // Assuming there's an element with this ID
        programsContainer.innerHTML = '';  // Clear any existing content

        if (programs.length === 0) {
            programsContainer.innerHTML = '<p>No programs available.</p>';
            return;
        }

        programs.forEach(program => {
            const programElement = document.createElement('div');
            programElement.classList.add('program');

            const programName = document.createElement('h3');
            programName.textContent = program.name;

            const programDescription = document.createElement('p');
            programDescription.textContent = program.description || 'No description available';

            programElement.appendChild(programName);
            programElement.appendChild(programDescription);
            programsContainer.appendChild(programElement);
        });
    }

    // Function to display an error message
    function showErrorMessage(message) {
        const errorMessageElement = document.getElementById('errorMessage');  // Assuming there's an element with this ID
        errorMessageElement.textContent = message;
        errorMessageElement.style.display = 'block';  // Make the error message visible
    }

    // Fetch programs when the page loads
    fetchPrograms();
});
