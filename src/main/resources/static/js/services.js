// src/main/resources/static/js/services.js

document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('serviceSearchInput');

    // 1. Live Search Filter for Cards (Customer) and Rows (Admin)
    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const query = this.value.toLowerCase().trim();

            // Filter Customer Service Cards
            const cards = document.querySelectorAll('.service-card');
            cards.forEach(card => {
                const title = card.querySelector('h3')?.innerText.toLowerCase() || '';
                const desc = card.querySelector('.desc')?.innerText.toLowerCase() || '';
                card.style.display = (title.includes(query) || desc.includes(query)) ? 'flex' : 'none';
            });

            // Filter Admin Table Rows
            const rows = document.querySelectorAll('.services-table tbody tr.service-row');
            rows.forEach(row => {
                const rowText = row.innerText.toLowerCase();
                row.style.display = rowText.includes(query) ? '' : 'none';
            });
        });
    }

    // 2. Delete Confirmation Handler
    const deleteButtons = document.querySelectorAll('.js-delete-service');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            const serviceName = this.getAttribute('data-name') || 'this service';
            const confirmed = confirm(`Are you sure you want to permanently delete "${serviceName}"?`);
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });

    // 3. Form Validation for Add/Edit Service
    const serviceForm = document.getElementById('serviceForm');
    if (serviceForm) {
        serviceForm.addEventListener('submit', function (e) {
            const price = parseFloat(document.getElementById('price')?.value || 0);
            const duration = parseInt(document.getElementById('durationMinutes')?.value || 0, 10);

            if (price <= 0) {
                alert('Please enter a valid price greater than 0 LKR.');
                e.preventDefault();
                return;
            }

            if (duration < 5 || duration > 480) {
                alert('Duration must be between 5 and 480 minutes.');
                e.preventDefault();
            }
        });
    }
});