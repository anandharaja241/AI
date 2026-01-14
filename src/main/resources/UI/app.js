// $(function () {
//     $('.create-btn').on('click', submitToJavaInCreatePage);
// });


function submitToJavaInCreatePage() {
    alert("Submitting job to Java Backend...");
    // Collect data from the form
    // const jobData = {
    //     label: document.getElementById('jobLabel').value,
    //     role: document.getElementById('role').value,
    //     experience: document.getElementById('experience').value
    // };

    // // Basic validation
    // if (!jobData.label || !jobData.role || !jobData.experience) {
    //     alert("Please fill all fields");
    //     return;
    // }

    // Send to Java Backend
    // We pass the data as a JSON string for easy parsing in Java
    // javaBackend.saveJob(JSON.stringify(jobData));
    javaBackend.triggerAction("OKOK");
}