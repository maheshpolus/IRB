
$(document).ready(function(){
    $("#mobileMenu").click(function(){
        $(".slideMenu").toggleClass("open");
    });
    
    $(".pullBack,.slideMenu>ul>li>a").click(function(){
        $(".slideMenu").removeClass("open");
    });
    
     $(".advancedTools").hide();
    $("#advcanceSearch").click(function(){
        $(".scheduleFilterBoxes").toggle();
    });
    
    $("#showNotify, .notificationBox>li>.showMore").click(function(){
        $(".notificationBox").toggle();
    });

    // toggle accordion
    $(".setHead").click(function(){
        $(this).next(".setBody").toggle();
        $(this).children("a").children("i").toggleClass("fa-chevron-circle-down");
    });  
    
    $("#roleAddingRow").hide();
    $("#expertiseAddingRow").hide();
    $("#rolesAddBtn").click(function(){
    	$("#roleAddingRow").toggle();
    });
    $("#expertiseAddBtn").click(function(){
    	$("#expertiseAddingRow").toggle();
    });
    
    $(".expertiseEditingActions").hide();
    $("#expertiseEditBtn").click(function() {
    	$(".expertiseEditingActions").toggle();
	});
    $(".roleEditingActions").hide();
    $("#roleEditBtn").click(function() {
    	 $(".roleEditingActions").toggle();
	});
    
    $("#scheduleCommentHeader").hide();
    $("#dellaCommentBox").hide();
    $("#dellaComment").click(function() {
		$("#scheduleCommentHeader").show();
		$("#dellaCommentBox").toggle();
	});
    
    $("#peterSamCommentBox").hide();
    $("#peterSamComment").click(function() {
		$("#scheduleCommentHeader").show();
		$("#peterSamCommentBox").toggle();
	});
    
    $("#InezChewCommentBox").hide();
    $("#InezChewComment").click(function() {
		$("#scheduleCommentHeader").show();
		$("#InezChewCommentBox").toggle();
	});
    $("#addAreaOfResearchRow").hide();
    $("#areaOfResearchAddBtn").click(function() {
		$("#addAreaOfResearchRow").toggle();
	});
    
    $("#addMinutesRow").hide();
    $("#minutesAddBtn").click(function() {
    	$("#addMinutesRow").toggle();
    })
});