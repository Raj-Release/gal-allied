(function($){
jQuery.fn.timerTabPanel = function (opt) {
  var init = {
    tabPanel: this,      // tab panel wrapper selector
    startTab: 1,         // start tab number
    timeInterval: 10000,  // time interval (ms)
    tabElm: "li",        // tab selector: jquery-ui default
    panelElm: "div",     // panel selector: jquery-ui default
    isStopped: false	// stopFlag
  };
  var d = $.extend({}, init, opt);
  var n = (d.startTab > 0 && $(d.tabPanel).find(d.tabElm).length >= d.startTab) ? d.startTab - 1 : 0;
  var startTimerTabPanel = function () {
    $(d.tabPanel).tabs();
    // tab panel timer settings
    var tabTimer = setInterval(tabChange, d.timeInterval);
   // $(d.tabPanel).on("mouseover", function () { clearInterval(tabTimer); });
//    $("#pinbutton").click(function(){
////    	var ids = {};
////    	$("#tabs>div").each(function(i){
////    		ids =$(this).prop('id');
////    	});
////	var tabz = $("#tabs");
////	var tabIndex = tabz.tabs("option", "active");
////	alert(tabIndex)
////	var tab = tabz.find("div[role=tab]").eq(tabIndex);
////	alert(tab);
////	var tabId = tab.prop('id');
//	alert(d.timeInterval);
//    clearInterval(d.timeInterval);
//    d.timeInterval = 500000;
//});
    $(d.tabPanel).on("mouseleave", function () { tabTimer = setInterval(tabChange, d.timeInterval); });
    // tab panel click action settings
   // $(d.tabPanel).find(d.tabElm).on("click", function () { n = $(this).index(); });
   // $(d.tabPanel).find(d.tabElm).eq(n).find("a").trigger("click");
  };
  
  $("#pinbutton").click(function(){
	  clearInterval(d.timeInterval);
	  var tabz = $("#tabs");
	  var tabIndex = tabz.tabs("option", "active");
	  alert(tabIndex)
	  var tab = tabz.find("div[role=tab]").eq(tabIndex);
	  tab.tabs('option', 'active', tabIndex);
	  d.timeInterval = 500000;
//	  if(d.isStopped){
//		  $(d.tabPanel).timer('pause');
//		  d.isStopped = true;
//	  }
//	  if(!d.isStopped){
//		  $(d.tabPanel).timer('resume');
//		  d.isStopped = false;
//	  }
});
  
  // tab panel auto change function
  var tabChange = function () {
    var tabsElm = $(d.tabPanel).find(d.tabElm);
    var tabs = tabsElm.length;
    n = n % tabs;
    tabsElm.eq(n).find("a").trigger("click");
    n++;
  };
  // Start Timer Tab Panel
  startTimerTabPanel();
};
})(jQuery);