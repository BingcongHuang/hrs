function formatTime(date, symbol) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join(symbol) + '  ' + [hour, minute, second].map(formatNumber).join(':');

}
function nowDate() {
  var date = new Date();
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('-');
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

function formatTimeToString(datetime) {
  var date = new Date(datetime);
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatYM(_d) {
  var date = new Date(_d);
  var year = date.getFullYear()
  var month = date.getMonth() + 1

  return year + "年" + month + "月";
}
function formatYMD(_d) {
  var date = new Date(_d);
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  return year + "年" + month + "月" + day + "日";
}

function formatMD(_d) {
  var date = new Date(_d);
  var month = date.getMonth() + 1
  var day = date.getDate()
  if(month<10&&day<10){
    return"0"+ month + "/" +"0" +day;
  } else if  (month < 10){
    return"0"+ month + "/" + day;
  } else if (day<10){
    return month + "/" + "0" + day;
  }else{
    return month + "/"  + day;
  }
   
} 

function formatMDD(_d) {
  var date = new Date(_d);
  var month = date.getMonth() + 1
  var day = date.getDate()
  if (month < 10 && day < 10) {
    return month + "." + "0" + day;
  } else if (month < 10) {
    return month + "." + day;
  } else if (day < 10) {
    return month + "." + "0" + day;
  } else {
    return month + "." + day;
  }

} 
 

function formatYMD1() {
  var date = new Date();
  var year = date.getFullYear()+1
  var month = date.getMonth() + 1
  var day = date.getDate()

  return year + "年" + month + "月" + day + "日";
}

function formatYMDHm(_d) {
  var date = new Date(_d);
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()
  var hour = date.getHours()
  var minute = date.getMinutes();

  return year + "年" + month + "月" + day + "日" + [hour, minute].map(formatNumber).join(':');
}

function formatYMDHms(_d) {
  var date = new Date(_d);
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()
  var hour = date.getHours()
  var minute = date.getMinutes();
  var second =date.getSeconds();
  return year + "-" + month + "-" + day + "-" + [hour, minute].map(formatNumber).join(':');
}

function formatDateToStr(_d) {
  var date = new Date(_d);
  var Y = date.getFullYear() + '-';
  var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
  var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
  return (Y + M + D)
}

function formatDateToStr2(_d) {
  var date = new Date(_d);
  var Y = date.getFullYear() + '.';
  var M = date.getMonth() + 1 + '.';
  var D = date.getDate();
  return (Y + M + D)
}

function formatHMS(_d) {  /* 传入时间戳 */
  var dt = new Date(_d);
  var hour = dt.getHours();
  var minite = dt.getMinutes();
  var second = dt.getSeconds();
  return hour + ':' + minite + ':' + second
}

function formatHM(_d) {  /* 传入时间戳 */
  var dt = new Date(_d);
  var hour = dt.getHours();
  var minite = dt.getMinutes();
  return hour + ':' + minite 
}
function formatMinite(_d) {  /* 传入时间戳 */
  var hourStamp = 1000 * 60 * 60,
    miniteStamp = 1000 * 60,
    secondStamp = 1000;

  var hour = Math.floor(_d / hourStamp),
    minite = Math.floor((_d - hour * hourStamp) / miniteStamp);

  return minite
}


/**
 * a数组 是否包含 b 元素
 */
function contains(a, b) {
  for (var i in a) {
    if (a[i] == b) {
      return true;
    }
  }
  return false;
}
function isBlank(str) {
  if (Object.prototype.toString.call(str) === '[object Undefined]') {//空
    return true
  } else if (
    Object.prototype.toString.call(str) === '[object String]' ||
    Object.prototype.toString.call(str) === '[object Array]') { //字条串或数组
    return str.length == 0 ? true : false
  } else if (Object.prototype.toString.call(str) === '[object Object]') {
    return JSON.stringify(str) == '{}' ? true : false
  } else {
    return true
  }

}

