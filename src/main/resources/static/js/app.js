// Grab current user and CSRF info from <meta>
const currentUser = document.querySelector("meta[name='current-user']")?.content || "";
const csrfToken = document.querySelector("meta[name='_csrf']")?.content || "";
const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.content || "";

// Toggle dropdown
function toggleDropdown() {
    document.getElementById('profileDropdown').classList.toggle('hidden');
}

// Close dropdown when clicking outside
window.addEventListener('click', function(e) {
    if (!e.target.closest('#profileMenu')) {
        document.getElementById('profileDropdown').classList.add('hidden');
    }
});

// Load chat for selected friend or agent
function openChat(name) {
    document.getElementById('chatWith').innerText = name;
    document.getElementById('chatMessages').innerHTML = '';

    fetch(`/chat/history?recipient=${name}`)
        .then(res => res.json())
        .then(messages => {
            if (messages.length === 0) {
                let msg = document.createElement('p');
                msg.className = 'text-gray-500';
                msg.innerText = `No previous messages with ${name}.`;
                document.getElementById('chatMessages').appendChild(msg);
            } else {
                messages.forEach(m => {
                    let msg = document.createElement('div');
                    if (m.startsWith("You:")) {
                        msg.className = 'self-end bg-blue-500 text-white px-4 py-2 rounded-2xl max-w-xs text-sm mb-2';
                        msg.innerText = m.replace("You:", "").trim();
                    } else {
                        msg.className = 'self-start bg-gray-200 text-gray-800 px-4 py-2 rounded-2xl max-w-xs text-sm mb-2';
                        msg.innerText = m;
                    }
                    document.getElementById('chatMessages').appendChild(msg);
                });
            }
        });
}

// Send a message
function sendMessage() {
    const recipient = document.getElementById('chatWith').innerText;
    const input = document.querySelector('input[type=text]');
    const message = input.value.trim();

    if (!recipient || recipient === "...") {
        alert("Select someone to chat with first!");
        return;
    }
    if (!message) return;

    fetch(`/chat/send?recipient=${recipient}&message=${encodeURIComponent(message)}`, {
        method: 'POST'
    }).then(() => {
        const placeholder = document.querySelector('#chatMessages p.text-gray-500');
        if (placeholder) placeholder.remove();

        let msg = document.createElement('div');
        msg.className = 'self-end bg-blue-500 text-white px-4 py-2 rounded-2xl max-w-xs text-sm mb-2';
        msg.innerText = message;
        document.getElementById('chatMessages').appendChild(msg);
        input.value = "";
    });
}

// Debounce helper
function debounce(fn, d = 200) {
    let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), d); };
}
function esc(s){
    return s.replace(/[&<>"']/g, c => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", "\"": "&quot;", "'": "&#39;"
    }[c]));
}

// Autocomplete
window.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("friendSearch");
    const resultsBox = document.getElementById("searchResults");
    let activeIndex = -1;
    let suggestions = [];

    async function fetchNames(q){
        const resp = await fetch(`/users/suggest?q=${encodeURIComponent(q)}`, { credentials: "include" });
        if (!resp.ok) return [];
        return resp.json();
    }

    function render(items){
        suggestions = items; activeIndex = -1;
        if (!items.length){
            resultsBox.innerHTML = "<div class='px-2 py-1 text-gray-500'>No matches</div>";
            resultsBox.classList.remove("hidden");
            return;
        }
        resultsBox.innerHTML = items.map((u,i)=>`
            <div class="px-2 py-1 hover:bg-gray-100 cursor-pointer"
                role="option" id="sr-${i}"
                onclick="selectUser('${esc(u)}')">${esc(u)}</div>
        `).join("");
        resultsBox.classList.remove("hidden");
    }

    const doSearch = debounce(async (val)=>{
        const q = (val||"").trim();
        if (q.length < 2){ resultsBox.innerHTML = ""; resultsBox.classList.add("hidden"); return; }
        try { render(await fetchNames(q)); }
        catch { resultsBox.innerHTML = ""; resultsBox.classList.add("hidden"); }
    }, 200);

    window.searchUsers = (v)=>doSearch(v);

    window.selectUser = (u)=>{
        input.value = u;
        resultsBox.classList.add("hidden");
        resultsBox.innerHTML = "";
    };

    document.addEventListener("click",(e)=>{
        if (!resultsBox.contains(e.target) && e.target !== input){
            resultsBox.classList.add("hidden");
        }
    });

    input.addEventListener("keydown",(e)=>{
        if (resultsBox.classList.contains("hidden") || suggestions.length === 0) return;
        if (e.key==="ArrowDown"){ e.preventDefault(); activeIndex=(activeIndex+1)%suggestions.length; updateActive(); }
        else if (e.key==="ArrowUp"){ e.preventDefault(); activeIndex=(activeIndex-1+suggestions.length)%suggestions.length; updateActive(); }
        else if (e.key==="Enter" && activeIndex>=0){ e.preventDefault(); window.selectUser(suggestions[activeIndex]); }
        else if (e.key==="Escape"){ resultsBox.classList.add("hidden"); }
    });

    function updateActive(){
        [...resultsBox.children].forEach((el,i)=>el.classList.toggle("bg-gray-100", i===activeIndex));
    }
});

// Add friend
function addFriendFromInput() {
    const input = document.getElementById("friendSearch");
    const username = (input?.value || "").trim();
    if (!username) return;

    const url = `/api/friend-requests/${encodeURIComponent(currentUser)}/${encodeURIComponent(username)}`;
    const headers = { "Accept": "text/plain" };
    if (csrfHeader && csrfToken) headers[csrfHeader] = csrfToken;

    fetch(url, {
        method: "POST",
        headers,
        credentials: "same-origin"
    })
    .then(async res => {
        const msg = await res.text().catch(() => "");
        if (res.ok) {
            alert(`Friend request sent to ${username}`);
        } else {
            alert(`Error: ${msg || res.status}`);
        }
    })
    .catch(err => alert(`Error: ${err.message}`))
    .finally(() => {
        if (input) input.value = "";
        const box = document.getElementById("searchResults");
        if (box) box.classList.add("hidden");
    });

}

// button to toggle friend search
function toggleFriendSearch() {
    const area = document.getElementById("friendSearchArea");
    const button = event.currentTarget;
    const isHidden = area.classList.toggle("hidden");
    button.textContent = isHidden ? "+" : "×";
}

// Profile dropdown toggle
function toggleDropdown() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown.classList.toggle("hidden");
}

// for opening chat with specific person
function openChat(name) {
    document.getElementById("chatWith").textContent = name;
    const chatMessages = document.getElementById("chatMessages");
    chatMessages.innerHTML = `<p class="text-gray-600">You are now
     chatting with <strong>${name}</strong>.</p>`;
}

document.addEventListener("DOMContentLoaded", () => {
    const metaTag = document.querySelector("meta[name='chatFriend']");
    const chatFriend = metaTag ? metaTag.content.trim() : "";

    if (chatFriend) {
        // Set header text
        document.getElementById("chatWith").textContent = chatFriend;

        // Load previous messages from backend
        fetch(`/chat/history?recipient=${encodeURIComponent(chatFriend)}`)
            .then(res => res.json())
            .then(messages => {
                const chatMessages = document.getElementById("chatMessages");
                chatMessages.innerHTML = "";

                if (messages.length === 0) {
                    const msg = document.createElement("p");
                    msg.className = "text-gray-500";
                    msg.textContent = `No previous messages with ${chatFriend}.`;
                    chatMessages.appendChild(msg);
                    return;
                }

                messages.forEach(m => {
                    const div = document.createElement("div");
                    if (m.startsWith("You:")) {
                        div.className = "self-end bg-blue-500 text-white px-4 py-2 rounded-2xl max-w-xs text-sm mb-2";
                        div.textContent = m.replace("You:", "").trim();
                    } else {
                        div.className = "self-start bg-gray-200 text-gray-800 px-4 py-2 rounded-2xl max-w-xs text-sm mb-2";
                        div.textContent = m;
                    }
                    chatMessages.appendChild(div);
                });
            })
            .catch(err => console.error("Failed to load chat history:", err));
    }
});