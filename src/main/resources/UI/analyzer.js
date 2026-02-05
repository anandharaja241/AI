$(document).ready(function () {
    $('body').on('click', '#btnAnalyze', analyzeResume);
    $('body').on('click', '.history-item', loadHistoryDetails);

    var initialHistory = javaBackend.loadHistory();
    if (initialHistory != "") {
        $('#historyList').html(initialHistory);
    }
});

// Inside your "Analyze" button click handler
function analyzeResume() {
    const fileInput = document.getElementById('resumeUpload');
    const role = document.getElementById('jobRole').value;
    const file = fileInput.files[0];

    $('body').append("Role:" + role, " File:" + file.name);

    if (fileInput.files.length < 1) {
        // FIXME: Use better UI feedback
        $('body').append("Please select at least one resume.");
        return;
    }

    // Show loading state
    $('#resultsArea').addClass('d-none');
    $('#loading').removeClass('d-none');

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const base64Data = e.target.result.split(',')[1]; // Get data after the comma
            const filePath = file.name;
            const filePathParts = filePath.split('/');
            const fileName = filePathParts.pop();

            // Send to Java via Bridge
            var resultHtml = javaBackend.processWithAI(fileName, base64Data, role);
            $('#loading').addClass('d-none');
            $('#resultsArea').removeClass('d-none').html(resultHtml);
            // Refresh history
            var history = javaBackend.loadHistory();
            if (history != "") {
                $('#historyList').html(history);
            }
        };
        reader.readAsDataURL(file);
    }
}

function loadHistoryDetails(evt) {
    $('.history-item').removeClass('active');
    var $parent = $(evt.currentTarget);
    const jobId = $(evt.currentTarget).addClass('active').data('id');
    const details = javaBackend.getHistoryDetails(jobId);
    $('#resultsArea').html(details);
}