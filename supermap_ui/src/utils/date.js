// 获取时间
export function getNowFormatDate(time) {
  let date = "";
  if(time!==undefined){
    // 时间戳转为时间
    date = date = new Date(time);
  }else{
    date = new Date();
  }
  var month = date.getMonth() + 1;
  var curDate = date.getDate();
  var curHours = date.getHours();
  var curMinutes = date.getMinutes();
  var curSeconds = date.getSeconds();

  var day = date.getDay();
  var weeks = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");

  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (curDate >= 0 && curDate <= 9) {
    curDate = "0" + curDate;
  }
  if (curHours >= 0 && curHours <= 9) {
    curHours = "0" + curHours;
  }
  if (curMinutes >= 0 && curMinutes <= 9) {
    curMinutes = "0" + curMinutes;
  }
  if (curSeconds >= 0 && curSeconds <= 9) {
    curSeconds = "0" + curSeconds;
  }

  var dateTime = date.getFullYear() + "/" + month + "/" + curDate;
  var currentTime = curHours + ":" + curMinutes + ":" + curSeconds;
  var week = weeks[day];
  var currentDate = dateTime + "," + currentTime + "," + week;
  return currentDate;
}

// 获取某一时间段的时间(按小时计算)
export function getTimeSlot(timeSection){
  // 获取当前时间戳
  let date_timestamp = new Date().getTime();
  // 获取某段时间前的时间戳
  let startDate = date_timestamp - timeSection * 3600000;
  //格式转换
  return getNowFormatDate(startDate);
}

// 将时间格式转为数据库格式
export function conversionTime(string){
  let timeArr = string.split(",");
  let ymd = timeArr[0].split("/").join("-");
  return `${ymd} ${timeArr[1]}`;
}
