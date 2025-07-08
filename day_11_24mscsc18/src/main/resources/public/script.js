const apiBase = 'http://localhost:4567';

// Register
if (document.getElementById('registerForm')) {
    document.getElementById('registerForm').onsubmit = async (e) => {
        e.preventDefault();
        const email = document.getElementById('regEmail').value;
        const name = document.getElementById('regName').value;

        const res = await fetch(`${apiBase}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, name })
        });

        const text = await res.text();
        document.getElementById('registerMsg').innerText = text;
    };
}

// Login
if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').onsubmit = async (e) => {
        e.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        const res = await fetch(`${apiBase}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const text = await res.text();
        document.getElementById('loginMsg').innerText = text;
    };
}

// Events
if (document.getElementById('eventsList')) {
    fetch(`${apiBase}/events`)
        .then(res => res.json())
        .then(events => {
            const container = document.getElementById('eventsList');
            events.forEach(event => {
                const div = document.createElement('div');
                div.innerHTML = `
          <strong>${event.title}</strong><br/>
          Date: ${event.date}<br/>
          Tokens left: ${event.availableTokens}<br/>
          <button onclick="bookEvent('${event._id}')">Book</button>
          <hr/>
        `;
                container.appendChild(div);
            });
        });
}

function bookEvent(eventId) {
    const email = prompt('Enter your registered email to book:');
    if (!email) return;

    fetch(`${apiBase}/book`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, eventId })
    })
        .then(res => res.text())
        .then(alert);
}
