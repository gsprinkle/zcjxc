/* 日期格式化方法 */
function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

/* 获取当前日期 */
function getCurDate() {
	var curDate = new Date();
	return myformatter(curDate);
}

/* 格式化日期为年月 */
function formatterForMonth(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	return y + '-' + (m < 10 ? ('0' + m) : m);
}

/* 获取当前年月 */
function getYearMonth() {
	var curDate = new Date();
	return formatterForMonth(curDate);
}

/* 关闭tabs*/
function closeTab(title){
	var tabs = parent.$("#wu-tabs");
	if(tabs.tabs('exists',title)){
		tabs.tabs('close',title);
	}
}