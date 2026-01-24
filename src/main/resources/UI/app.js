var javaBackend = javaBackend || {};
var options = options || {};

$(function () {
    // Initial Events
    // setActiveNavButton(javaBackend.currentPage);

    // Event handlers
    $('.nav-btn').on('click', navigateTo);
});

function navigateTo(evt) {
    javaBackend.navigateTo && javaBackend.navigateTo(evt.currentTarget.dataset.href);
}