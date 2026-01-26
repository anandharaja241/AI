$(document).ready(function () {
    $('#btnAnalyze').click(function () {
        const files = $('#resumeUpload')[0].files;
        const role = $("#jobRole option:selected").text();

        if (files.length === 0) {
            alert("Please select at least one resume.");
            return;
        }

        // Show loading state
        $('#resultsArea').addClass('d-none');
        $('#loading').removeClass('d-none');

        // Simulate AI Delay
        setTimeout(function () {
            $('#loading').addClass('d-none');
            $('#resultsArea').removeClass('d-none').html('');

            // Mock Result Generation
            for (let i = 0; i < files.length; i++) {
                const randomScore = Math.floor(Math.random() * (98 - 45 + 1)) + 45;
                const color = randomScore > 80 ? 'success' : (randomScore > 60 ? 'warning' : 'danger');

                const html = `
                        <div class="card resume-card shadow-sm mb-3">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <h5 class="mb-1 text-primary">${files[i].name}</h5>
                                    <p class="mb-0 text-muted"><i class="bi bi-briefcase me-1"></i> Match for: ${role}</p>
                                    <small class="text-secondary">Extracted Skills: Java, Spring Boot, Microservices</small>
                                </div>
                                <div class="text-center">
                                    <div class="text-${color} score-badge">${randomScore}%</div>
                                    <small class="text-uppercase fw-bold">Match</small>
                                </div>
                            </div>
                        </div>
                    `;
                $('#resultsArea').append(html);
            }

            // Add to history
            const now = new Date().toLocaleString();
            $('#historyList').prepend(`
                    <div class="history-item">
                        <small class="text-muted">${now}</small>
                        <div class="fw-bold">${role} Batch</div>
                        <small>${files.length} Resumes Analyzed</small>
                    </div>
                `);

        }, 1500);
    });

    $('#historyList').html(javaBackend.loadHistory());
    $('body').on('click', '.history-item', loadHistoryDetails);
});

function loadHistoryDetails(evt) {
    const jobId = $(evt.currentTarget).data('id');
    const details = javaBackend.getHistoryDetails(jobId);
    $('#resultsArea').html(details);
}