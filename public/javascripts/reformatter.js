$(function() {
    var $formatInput = $("#formatText");
    var pollHtmlCheck = "";

    // Handle any changes to the input form and send a request to server
    $formatInput.change(function(e) {
        $("#formatForm").submit();
    });

    $("#formatForm").submit(function(e) {
        e.preventDefault();

        if ($.trim($formatInput.val()) === "") {
            alert("Please enter some text to be formatted.");
            return;
        }

        $.post('/', { 'formatText':$formatInput.val() }, function(data) {
            $("#outputBox").text(data);
            $("#outputBox").html($("#outputBox").html().replace(/\n/g, "<br /><br />"));
        })
            .fail(function() {
                alert("Oops! There was an error with reformatting the text." +
                      " Please send an email at the bottom of the page.");
            });
    });
    // Example button
    $("#exampleButton").click(function() {
        $.ajax({
            url: "/assets/misc/example.txt",
            dataType: "text",
            success: function (data) {
                $formatInput.val(data);
                $("#formatForm").submit();
            },
            error: function() {
                alert("There was an error with the example file. Please send an email at the bottom of the page.");
            }
        });
    });

    // Enable clipboard functionality
    var flash = true;
    var clip = new ZeroClipboard($("#copyClipboardBtn"));
    clip.on('noflash', function() { flash = false; });
    $("#copyClipboardBtn").click(function(e) {
        // Check for flash
        if (!flash) {
            alert("It seems like Flash is not enabled in your browser. Unfortunately, for the copy-paste functionality to work, Flash must be enabled.");
            return;
        }
    });

 });