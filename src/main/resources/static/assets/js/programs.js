document.addEventListener('DOMContentLoaded', function() {
    // Load programs when the page loads
    loadPrograms();

    // Add event listeners
    document.getElementById('saveProgram').addEventListener('click', saveProgram);

    // Function to load programs from the API
    async function loadPrograms() {
        try {
            const response = await fetch('/api/programs');
            if (!response.ok) {
                throw new Error('Failed to fetch programs');
            }

            const programs = await response.json();
            displayPrograms(programs);
        } catch (error) {
            console.error('Error:', error);
            document.getElementById('programsTable').innerHTML =
                `<tr><td colspan="4" class="text-center text-danger">Error loading programs: ${error.message}</td></tr>`;
        }
    }

    // Display programs in the table
    function displayPrograms(programs) {
        const tableBody = document.getElementById('programsTable');

        // If no programs are found, show a message
        if (programs.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4" class="text-center">No programs found</td></tr>';
            return;
        }

        let html = '';
        programs.forEach(program => {
            html += `
                <tr>
                    <td>${program.id}</td>
                    <td>${program.name}</td>
                    <td>${program.description || '-'}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editProgram(${program.id})">Edit</button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteProgram(${program.id})">Delete</button>
                    </td>
                </tr>
            `;
        });

        tableBody.innerHTML = html;
    }

    // Save a new program
    async function saveProgram() {
        const name = document.getElementById('programName').value.trim();
        const description = document.getElementById('programDescription').value.trim();

        // Check for required fields
        if (!name) {
            alert('Program name is required');
            return;
        }

        try {
            const response = await fetch('/api/programs', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, description })
            });

            if (!response.ok) {
                throw new Error('Failed to create program');
            }

            // Close modal and reload programs
            const modal = bootstrap.Modal.getInstance(document.getElementById('addProgramModal'));
            modal.hide();

            // Clear form
            document.getElementById('addProgramForm').reset();

            // Reload programs
            loadPrograms();

        } catch (error) {
            console.error('Error:', error);
            alert(`Error creating program: ${error.message}`);
        }
    }

    // Making these functions globally available
    window.editProgram = function(id) {
        alert(`Edit program ${id} - Functionality to be implemented`);
    };

    window.deleteProgram = async function(id) {
        if (confirm(`Are you sure you want to delete program ${id}?`)) {
            try {
                const response = await fetch(`/api/programs/${id}`, {
                    method: 'DELETE'
                });

                if (!response.ok) {
                    throw new Error('Failed to delete program');
                }

                // Reload programs
                loadPrograms();

            } catch (error) {
                console.error('Error:', error);
                alert(`Error deleting program: ${error.message}`);
            }
        }
    };
});
