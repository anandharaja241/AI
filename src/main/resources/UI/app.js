var javaBackend = javaBackend || {};
var options = options || {};

$(function () {
    // Initial Events
    // setActiveNavButton(javaBackend.currentPage);
    $('#jobRole').html(javaBackend.getJobOptions());

    // Event handlers
    $('body').on('click', '.nav-btn', navigateTo);
    $('.create-btn').on('click', processCreateJob);
    $('body').on('click', '.edit-nav-btn', setJobId);
    $('.update-btn').on('click', processUpdateJob);
    $('body').on('click', '.delete-btn', processDeleteJob);


    if (!javaBackend['createJobs']) {
        $('body').append("Status: Backend createJobs function not found.");
    }
});

function navigateTo(evt) {
    javaBackend.navigateTo && javaBackend.navigateTo(evt.currentTarget.dataset.href);
}

function processCreateJob(evt) {
    // var action = evt.currentTarget.dataset.action;
    var role = $('#role').removeClass('is-invalid').val();
    var exp = $('#experience').removeClass('is-invalid').val();
    $('.status').html('');
    $('body').append("Status: 1 Processing...");

    if (!role) {
        $('.status').append('Please select role').addClass('text-danger');
        $('#role').addClass('is-invalid').focus();
        return;
    } else if (!exp) {
        $('.status').append('Please select experience').addClass('text-danger');
        $('#experience').addClass('is-invalid').focus();
        return;
    }

    $('body').append("Status: 2 Processing...");

    if (javaBackend['createJobs']) {
        $('body').append("Status: 3 Processing...");
        var status = javaBackend.createJobs(role, exp);
        $('body').append("Status: " + status);

        if (status) {
            $('.status').addClass('text-danger').append('The create job has successful');
        } else {
            $('.status').addClass('text-danger').append('The create job has failed');
        }
    }
}

function processUpdateJob(evt) {
    // var action = evt.currentTarget.dataset.action;
    var id = evt.currentTarget.dataset.id;
    var role = $('#role').removeClass('is-invalid').val();
    var exp = $('#experience').removeClass('is-invalid').val();
    $('.status').html('');
    $('body').append("Status: 1 Processing...");

    if (!role) {
        $('.status').append('Please select role').addClass('text-danger');
        $('#role').addClass('is-invalid').focus();
        return;
    } else if (!exp) {
        $('.status').append('Please select experience').addClass('text-danger');
        $('#experience').addClass('is-invalid').focus();
        return;
    }

    $('body').append("Status: 2 Processing...");

    if (javaBackend['updateJobs']) {
        $('body').append("Status: 3 Processing...");
        var status = javaBackend.updateJobs(""+id, role, exp);
        $('body').append("id: " + id);

        if (status) {
            $('.status').addClass('text-danger').append('The update job has successful');
        } else {
            $('.status').addClass('text-danger').append('The update job has failed');
        }
    }
}

function processDeleteJob(evt) {
    var $deleteBtn = $(evt.currentTarget);
    var id = evt.currentTarget.dataset.id;
    $('.status').html('');
    // $('body').append("Status: Processing...");

    if (javaBackend['deleteJobs']) {
        var status = javaBackend.deleteJobs(""+id);
        $('body').append("Status: " + status);
        if (status) {
            // $deleteBtn.closest('tr').remove();
            location.reload();
        } else {
            $('.status').addClass('text-danger').append('The delete job has failed');
        }
    }
}

function setJobId(evt) {
    var id = evt.currentTarget.dataset.id;
    localStorage.setItem('editJobId', id);
    // $('body').append("2. editJobId: " + localStorage.getItem('editJobId') + '@'+ id);
}