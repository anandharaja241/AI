var javaBackend = javaBackend || '';
var options = options || {};

$(function () {
    // Initial Events
    if (javaBackend == '') {
        $('body').addClass('error-page');
        setTimeout(function() {
            location.reload();
        }, 500);
    }

    // Event handlers
    $('body').on('click', '.nav-btn', navigateTo);
    $('body').on('click', '.create-btn', processCreateJob);
    $('body').on('click', '.edit-nav-btn', setJobId);
    $('body').on('click', '.update-btn', processUpdateJob);
    $('body').on('click', '.delete-btn', processDeleteJob);

    $('.toggle-input').on('click', function(evt) {
        evt.preventDefault();
        var parentClass = $(evt.currentTarget).data('parentclass');
        var childClass = $(evt.currentTarget).data('childclass');
        var $parent = $('.' + parentClass);
        $parent.find('.select2-container, select').toggleClass('d-none');
        $parent.find('input.' + childClass).toggleClass('d-none');
        if ($parent.find('.select2-container, select').hasClass('d-none')) {
            $parent.find('input.' + childClass).focus();
        } else {
            $parent.find('.select2-container, select').focus();
        }
    });

    if (javaBackend['getJobOptions'] && $('#jobRole').length > 0) {
        $('#jobRole').html(javaBackend.getJobOptions());
    }
    if (javaBackend['getDashboardStats'] && $('.total-resumes').length > 0) {
        var statsString = javaBackend.getDashboardStats();
        if (statsString && statsString.includes(";;")) {
            var parts = statsString.split(";;");
            $('.total-resumes').html(parts[0] || 0);
            $('.matched-resumes').html(parts[1] || 0);
            $('.unmatched-resumes').html(parts[2] || 0);
        }
    }

    if (javaBackend['getRecentResults']) {
        var recentResults = javaBackend.getRecentResults(5);
        if (recentResults && $('.recent-results').length > 0) {
            $('.recent-results').html(recentResults);
        }
    }

    if ($('select').length > 0) {
        $('select').select2({selectOnClose: true, closeOnSelect: true, theme: "classic"});
    }
});

function navigateTo(evt) {
    if (javaBackend == '') {
        location.reload();
    }
    javaBackend.navigateTo && javaBackend.navigateTo(evt.currentTarget.dataset.href);
}

function processCreateJob(evt) {
    var $roleInput = getCurrentField($('.role-input').removeClass('is-invalid'));
    var $expInput = getCurrentField($('.exp-input').removeClass('is-invalid'));
    var role = $roleInput.val();
    var exp = $expInput.val();

    $('.status').html('');

    if (!role) {
        $('.status').append('Please select role').addClass('text-danger');
        $('.role-input').addClass('is-invalid').focus();
        return;
    } else if (!exp) {
        $('.status').append('Please select experience').addClass('text-danger');
        $('.exp-input').addClass('is-invalid').focus();
        return;
    }

    if (javaBackend['createJobs']) {
        var status = javaBackend.createJobs(role, exp);

        if (status) {
            $('.status').addClass('text-success').html('The create job was successful');
            if ($roleInput.is('input')) {
                $('select.role-input').append("<option value='"+role+"'>"+role+"</option>");
            }
            if ($expInput.is('input')) {
                $('select.exp-input').append("<option value='"+exp+"'>"+exp+"</option>");
            }
            $roleInput.val('');
            $expInput.val('');
        } else {
            $('.status').addClass('text-danger').html('The create job has failed');
        }
    }
}

function processUpdateJob(evt) {
    var id = evt.currentTarget.dataset.id;
    var role = $('#role').removeClass('is-invalid').val();
    var exp = $('#experience').removeClass('is-invalid').val();
    $('.status').html('');

    if (!role) {
        $('.status').append('Please select role').addClass('text-danger');
        $('#role').addClass('is-invalid').focus();
        return;
    } else if (!exp) {
        $('.status').append('Please select experience').addClass('text-danger');
        $('#experience').addClass('is-invalid').focus();
        return;
    }

    if (javaBackend['updateJobs']) {
        var status = javaBackend.updateJobs(""+id, role, exp);

        if (status) {
            $('.status').addClass('text-success').append('The update job has successful');
        } else {
            $('.status').addClass('text-danger').append('The update job has failed');
        }
    }
}

function processDeleteJob(evt) {
    var $deleteBtn = $(evt.currentTarget);
    var id = evt.currentTarget.dataset.id;
    $('.status').html('');

    if (javaBackend['deleteJobs']) {
        var status = javaBackend.deleteJobs(""+id);

        if (status) {
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

function getCurrentField($selector) {
    if (!$selector || $selector.length === 0) return null;
    if ($selector.eq(1).hasClass('d-none')) {
        return $selector.eq(0);
    }
    return $selector.eq(1);
}